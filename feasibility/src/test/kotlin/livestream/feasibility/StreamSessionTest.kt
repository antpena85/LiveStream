package livestream.feasibility

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.Rule
import org.junit.Assert.assertThrows
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class StreamSessionTest {
    @get:Rule val temporaryFolder = TemporaryFolder()

    @Test
    fun callbackStopsWhenIndependentStoreCommitsFitFailureDuringConnection() {
        val database = temporaryFolder.root.toPath().resolve("callback-independent-fit-failure.sqlite")
        DurableCommandProbe(database).use { reader ->
            DurableCommandProbe(database).use { writer ->
                val session = StreamSession(reader)
                assertThat(session.requestStart(true, true, true, true, ProbeSnapshot(7, 0, 0)))
                    .isEqualTo(StartResult.Accepted)
                assertThat(writer.recordPoint("fit-point", 0, "session-a"))
                    .isEqualTo(ProbeSnapshot(8, 1, 0))
                session.connectionSucceeded()
                assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
            }
        }
        DurableCommandProbe(database).use { reopened ->
            assertThat(reopened.safetyView())
                .isEqualTo(ProbeSafetyView(ProbeSnapshot(8, 1, 0), setOf("session-a")))
        }
    }

    @Test
    fun callbackRejectsOldEpochAfterFitFailureAndResetBetweenReads() {
        val database = temporaryFolder.root.toPath().resolve("callback-reset-between-reads.sqlite")
        DurableCommandProbe(database).use { store ->
            assertThat(store.reset(true, 0)).isEqualTo(ProbeSnapshot(0, 0, 1))
            var checkpointReached = false
            val checkpoint = object : SessionSafetyState by store {
                override fun pendingStopSessions(): Set<String> {
                    val before = store.pendingStopSessions()
                    checkpointReached = true
                    assertThat(store.recordPoint("fit-point", 1, "session-a"))
                        .isEqualTo(ProbeSnapshot(1, 1, 1))
                    assertThat(store.reset(true, 1)).isEqualTo(ProbeSnapshot(0, 0, 2))
                    return before
                }
            }
            val session = StreamSession(checkpoint)
            assertThat(session.requestStart(true, true, true, true, ProbeSnapshot(0, 0, 1)))
                .isEqualTo(StartResult.Accepted)
            session.connectionSucceeded()
            assertThat(checkpointReached).isTrue()
            assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
        }
        DurableCommandProbe(database).use { reopened ->
            assertThat(reopened.snapshot()).isEqualTo(ProbeSnapshot(0, 0, 2))
            assertThat(reopened.pendingStopSessions()).containsExactly("session-a")
        }
    }

    @Test
    fun startDoesNotAcceptSnapshotAndMarkersFromDifferentCommittedStates() {
        val database = temporaryFolder.root.toPath().resolve("mixed-safety-view.sqlite")
        DurableCommandProbe(database).use { store ->
            var checkpointReached = false
            fun commitFitFailure() {
                checkpointReached = true
                assertThat(store.recordPoint("fit-point", 0, "session-a"))
                    .isEqualTo(ProbeSnapshot(8, 1, 0))
            }
            val checkpoint = object : SessionSafetyState by store {
                override fun pendingStopSessions(): Set<String> {
                    val before = store.pendingStopSessions()
                    commitFitFailure()
                    return before
                }
                override fun safetyView(): ProbeSafetyView {
                    commitFitFailure()
                    return store.safetyView()
                }
            }
            val session = StreamSession(checkpoint)
            val result = session.requestStart(true, true, true, true, ProbeSnapshot(8, 1, 0))
            assertThat(checkpointReached).isTrue()
            assertThat(result).isNotEqualTo(StartResult.Accepted)
            assertThat(session.status()).isEqualTo(StreamStatus.Stopped)
            assertThat(store.snapshot()).isEqualTo(ProbeSnapshot(8, 1, 0))
            assertThat(store.pendingStopSessions()).containsExactly("session-a")
        }
        DurableCommandProbe(database).use { reopened ->
            assertThat(reopened.recordPoint("fit-point", 0)).isEqualTo(ProbeSnapshot(8, 1, 0))
            assertThat(reopened.pendingStopSessions()).containsExactly("session-a")
        }
    }

    @Test
    fun callbackDuringAbortedFitWriteUsesPreviouslyCommittedState() {
        val database = temporaryFolder.root.toPath().resolve("callback-during-rollback.sqlite")
        lateinit var session: StreamSession
        var observed: StreamStatus? = null
        DurableCommandProbe(database, beforeCommit = {
            session.connectionSucceeded()
            observed = session.status()
            throw IllegalStateException("simulated interruption before commit")
        }).use { store ->
            session = StreamSession(store)
            assertThat(session.requestStart(true, true, true, true, ProbeSnapshot(7, 0, 0)))
                .isEqualTo(StartResult.Accepted)
            assertThrows(IllegalStateException::class.java) {
                store.recordPoint("aborted", 0, "session-a")
            }
            assertThat(observed).isEqualTo(StreamStatus.Live)
        }
        DurableCommandProbe(database).use { reopened ->
            assertThat(reopened.snapshot()).isEqualTo(ProbeSnapshot(7, 0, 0))
            assertThat(reopened.pendingStopSessions()).isEmpty()
        }
    }

    @Test
    fun startDuringUncommittedFitFailureCannotBecomeLiveAfterCommit() {
        val database = temporaryFolder.root.toPath().resolve("start-during-fit-write.sqlite")
        lateinit var session: StreamSession
        var startResult: StartResult? = null
        DurableCommandProbe(database, beforeCommit = {
            startResult = session.requestStart(true, true, true, true, ProbeSnapshot(7, 0, 0))
        }).use { store ->
            session = StreamSession(store)
            assertThat(store.recordPoint("fit-point", 0, "session-a")).isEqualTo(ProbeSnapshot(8, 1, 0))
            assertThat(startResult).isEqualTo(StartResult.Accepted)
            session.connectionSucceeded()
            assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
        }
        DurableCommandProbe(database).use { reopened ->
            assertThat(reopened.snapshot()).isEqualTo(ProbeSnapshot(8, 1, 0))
            assertThat(reopened.pendingStopSessions()).containsExactly("session-a")
            assertThat(reopened.recordPoint("fit-point", 0)).isEqualTo(ProbeSnapshot(8, 1, 0))
        }
    }

    @Test
    fun resetDuringConnectionPreventsOldMatchAssessmentFromBecomingLive() {
        val database = temporaryFolder.root.toPath().resolve("reset-during-connect.sqlite")
        DurableCommandProbe(database).use { store ->
            val session = StreamSession(store)
            val assessed = store.snapshot()
            assertThat(session.requestStart(true, true, true, true, assessed)).isEqualTo(StartResult.Accepted)

            assertThat(store.reset(confirmed = true, expectedEpoch = assessed.epoch))
                .isEqualTo(ProbeSnapshot(0, 0, 1))
            session.connectionSucceeded()

            assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
        }
    }

    @Test
    fun confirmedStartWithUnavailableSafetyStoreIsDeniedAndRemainsStopped() {
        val database = temporaryFolder.root.toPath().resolve("unavailable-at-start.sqlite")
        val store = DurableCommandProbe(database)
        val assessed = store.snapshot()
        val session = StreamSession(store)
        store.close()

        val result = session.requestStart(true, true, true, true, assessed)

        assertThat(result).isEqualTo(StartResult.SafetyStateUnavailable)
        assertThat(session.status()).isEqualTo(StreamStatus.Stopped)
    }

    @Test
    fun connectionWithUnavailableSafetyStoreRequiresStoppingInsteadOfLive() {
        val database = temporaryFolder.root.toPath().resolve("unavailable-during-connect.sqlite")
        val store = DurableCommandProbe(database)
        val session = StreamSession(store)
        val assessed = store.snapshot()
        assertThat(session.requestStart(true, true, true, true, assessed)).isEqualTo(StartResult.Accepted)
        store.close()

        session.connectionSucceeded()

        assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
    }

    @Test
    fun changedMatchDuringConnectionRequiresStoppingInsteadOfLive() {
        val database = temporaryFolder.root.toPath().resolve("changed-during-connect.sqlite")
        DurableCommandProbe(database).use { store ->
            val session = StreamSession(store)
            val assessed = store.snapshot()
            assertThat(session.requestStart(true, true, true, true, assessed)).isEqualTo(StartResult.Accepted)
            assertThat(store.recordPoint("new-point", expectedEpoch = assessed.epoch)).isEqualTo(ProbeSnapshot(8, 1, 0))
            session.connectionSucceeded()
            assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
        }
    }

    @Test
    fun connectionAfterFitFailureIsNotReportedLive() {
        val database = temporaryFolder.root.toPath().resolve("fit-failure-during-connect.sqlite")
        DurableCommandProbe(database).use { store ->
            val session = StreamSession(store)
            val assessed = store.snapshot()
            assertThat(session.requestStart(true, true, true, true, assessed)).isEqualTo(StartResult.Accepted)
            val updated = store.recordPoint("fit-failure", expectedEpoch = assessed.epoch, fitFailureSession = "session-a")
            assertThat(updated).isEqualTo(ProbeSnapshot(8, 1, 0))
            session.connectionSucceeded()
            assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
            assertThat(store.pendingStopSessions()).containsExactly("session-a")
        }
    }

    @Test
    fun anotherStartWhileLiveIsRejectedWithoutChangingStatus() {
        val database = temporaryFolder.root.toPath().resolve("start-live.sqlite")
        DurableCommandProbe(database).use { store ->
            val session = StreamSession(store)
            val snapshot = store.snapshot()
            assertThat(session.requestStart(true, true, true, true, snapshot)).isEqualTo(StartResult.Accepted)
            session.connectionSucceeded()
            assertThat(session.requestStart(true, true, true, true, snapshot)).isNotEqualTo(StartResult.Accepted)
            assertThat(session.status()).isEqualTo(StreamStatus.Live)
        }
    }

    @Test
    fun confirmedStopOfLiveSessionRemainsStoppingUntilConfirmed() {
        val database = temporaryFolder.root.toPath().resolve("stop-live.sqlite")
        DurableCommandProbe(database).use { store ->
            val session = StreamSession(store)
            assertThat(session.requestStart(true, true, true, true, store.snapshot())).isEqualTo(StartResult.Accepted)
            session.connectionSucceeded()
            assertThat(session.status()).isEqualTo(StreamStatus.Live)
            session.requestStop(confirmed = true)
            assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
        }
    }

    @Test
    fun lateConnectionAfterStopKeepsSessionStopping() {
        val database = temporaryFolder.root.toPath().resolve("late-connection.sqlite")
        DurableCommandProbe(database).use { store ->
            val session = StreamSession(store)
            assertThat(session.requestStart(true, true, true, true, store.snapshot())).isEqualTo(StartResult.Accepted)
            session.requestStop(confirmed = true)
            session.connectionSucceeded()
            assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
        }
    }

    @Test
    fun canceledStopLeavesConnectingSessionUnchanged() {
        val database = temporaryFolder.root.toPath().resolve("canceled-stop.sqlite")
        DurableCommandProbe(database).use { store ->
            val session = StreamSession(store)
            assertThat(session.requestStart(true, true, true, true, store.snapshot())).isEqualTo(StartResult.Accepted)
            session.requestStop(confirmed = false)
            assertThat(session.status()).isEqualTo(StreamStatus.Connecting)
        }
    }

    @Test
    fun startWhileStopIsUnconfirmedCannotBeginConnecting() {
        val database = temporaryFolder.root.toPath().resolve("start-during-stop.sqlite")
        DurableCommandProbe(database).use { store ->
            val session = StreamSession(store)
            val snapshot = store.snapshot()
            assertThat(session.requestStart(true, true, true, true, snapshot)).isEqualTo(StartResult.Accepted)
            session.requestStop(confirmed = true)
            assertThat(session.requestStart(true, true, true, true, snapshot)).isNotEqualTo(StartResult.Accepted)
            assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
        }
    }

    @Test
    fun confirmedStopWhileConnectingDoesNotClaimStoppedBeforePublisherConfirmation() {
        val database = temporaryFolder.root.toPath().resolve("stop-connecting.sqlite")
        DurableCommandProbe(database).use { store ->
            val session = StreamSession(store)
            assertThat(session.requestStart(true, true, true, true, store.snapshot())).isEqualTo(StartResult.Accepted)
            session.requestStop(confirmed = true)
            assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
        }
    }

    @Test
    fun repeatedStartWhileConnectingIsNotAccepted() {
        val database = temporaryFolder.root.toPath().resolve("duplicate-start.sqlite")
        DurableCommandProbe(database).use { store ->
            val session = StreamSession(store)
            val snapshot = store.snapshot()
            assertThat(session.requestStart(true, true, true, true, snapshot)).isEqualTo(StartResult.Accepted)
            assertThat(session.requestStart(true, true, true, true, snapshot)).isNotEqualTo(StartResult.Accepted)
            assertThat(session.status()).isEqualTo(StreamStatus.Connecting)
        }
    }

    @Test
    fun priorEpochFitAssessmentCannotAuthorizeStartWhenValuesAndRevisionCoincide() {
        val database = temporaryFolder.root.toPath().resolve("stale-epoch-fit.sqlite")
        val assessment = DurableCommandProbe(database).use { store ->
            val old = store.reset(confirmed = true, expectedEpoch = 0)
            assertThat(old).isEqualTo(ProbeSnapshot(0, 0, 1))
            assertThat(store.reset(confirmed = true, expectedEpoch = 1))
                .isEqualTo(ProbeSnapshot(0, 0, 2))
            old
        }
        DurableCommandProbe(database).use { store ->
            val session = StreamSession(store)
            assertThat(session.requestStart(confirmed = true, cameraPermission = true,
                numericFitReady = true, assessedSnapshot = assessment))
                .isEqualTo(StartResult.NumericFitUnresolved)
            assertThat(session.status()).isEqualTo(StreamStatus.Stopped)
            assertThat(store.snapshot()).isEqualTo(ProbeSnapshot(0, 0, 2))
        }
    }

    @Test
    fun confirmedStartWithMatchingFitAssessmentAfterReopenBeginsConnecting() {
        val database = temporaryFolder.root.toPath().resolve("ready-fit.sqlite")
        val assessment = DurableCommandProbe(database).use { store ->
            store.recordPoint("fit-point", expectedEpoch = 0, fitFailureSession = "session-a")
            assertThat(store.confirmStopped("session-a")).isTrue()
            assertThat(store.snapshot()).isEqualTo(ProbeSnapshot(8, 1, 0))
            store.snapshot()
        }
        DurableCommandProbe(database).use { store ->
            val session = StreamSession(store)
            assertThat(session.requestStart(confirmed = true, cameraPermission = true,
                microphonePermission = true, numericFitReady = true, assessedSnapshot = assessment))
                .isEqualTo(StartResult.Accepted)
            assertThat(session.status()).isEqualTo(StreamStatus.Connecting)
            assertThat(store.snapshot()).isEqualTo(ProbeSnapshot(8, 1, 0))
            assertThat(store.pendingStopSessions()).isEmpty()
        }
    }

    @Test
    fun staleFitAssessmentCannotAuthorizeStartAfterMatchRevisionChanges() {
        val database = temporaryFolder.root.toPath().resolve("stale-fit.sqlite")
        val assessment = DurableCommandProbe(database).use { store ->
            val assessed = store.recordPoint("first", expectedEpoch = 0)
            assertThat(assessed).isEqualTo(ProbeSnapshot(8, 1, 0))
            assertThat(store.recordPoint("second", expectedEpoch = 0)).isEqualTo(ProbeSnapshot(9, 2, 0))
            assessed
        }
        DurableCommandProbe(database).use { store ->
            val session = StreamSession(store)
            assertThat(session.requestStart(confirmed = true, cameraPermission = true,
                numericFitReady = true, assessedSnapshot = assessment))
                .isEqualTo(StartResult.NumericFitUnresolved)
            assertThat(session.status()).isEqualTo(StreamStatus.Stopped)
            assertThat(store.snapshot()).isEqualTo(ProbeSnapshot(9, 2, 0))
        }
    }

    @Test
    fun missingFitAssessmentAfterStopConfirmationCannotStart() {
        val database = temporaryFolder.root.toPath().resolve("unknown-fit.sqlite")
        DurableCommandProbe(database).use { store ->
            store.recordPoint("fit-point", expectedEpoch = 0, fitFailureSession = "session-a")
            assertThat(store.confirmStopped("session-a")).isTrue()
        }
        DurableCommandProbe(database).use { store ->
            val session = StreamSession(store)
            assertThat(session.requestStart(confirmed = true, cameraPermission = true))
                .isEqualTo(StartResult.NumericFitUnresolved)
            assertThat(session.status()).isEqualTo(StreamStatus.Stopped)
            assertThat(store.snapshot()).isEqualTo(ProbeSnapshot(8, 1, 0))
        }
    }

    @Test
    fun stopConfirmationDoesNotClearUnresolvedNumericFitForStart() {
        val database = temporaryFolder.root.toPath().resolve("unresolved-fit.sqlite")
        DurableCommandProbe(database).use { store ->
            store.recordPoint("fit-point", expectedEpoch = 0, fitFailureSession = "session-a")
            assertThat(store.confirmStopped("session-a")).isTrue()
        }
        DurableCommandProbe(database).use { store ->
            val session = StreamSession(store)
            assertThat(store.pendingStopSessions()).isEmpty()
            assertThat(session.requestStart(confirmed = true, cameraPermission = true, numericFitReady = false))
                .isEqualTo(StartResult.NumericFitUnresolved)
            assertThat(session.status()).isEqualTo(StreamStatus.Stopped)
            assertThat(store.snapshot()).isEqualTo(ProbeSnapshot(8, 1, 0))
        }
    }

    @Test
    fun confirmedStartWithDurablePendingStopAfterReopenIsDenied() {
        val database = temporaryFolder.root.toPath().resolve("pending-start.sqlite")
        DurableCommandProbe(database).use { store ->
            store.recordPoint("fit-point", expectedEpoch = 0, fitFailureSession = "session-a")
        }
        DurableCommandProbe(database).use { store ->
            val session = StreamSession(store)
            assertThat(session.requestStart(confirmed = true, cameraPermission = true))
                .isEqualTo(StartResult.PendingStop)
            assertThat(session.status()).isEqualTo(StreamStatus.Stopped)
            assertThat(store.pendingStopSessions()).containsExactly("session-a")
            assertThat(store.snapshot()).isEqualTo(ProbeSnapshot(8, 1, 0))
        }
    }

    @Test
    fun confirmedStartWithoutMicrophonePermissionIsDeniedAndRemainsStopped() {
        val session = StreamSession()

        val result = session.requestStart(
            confirmed = true,
            cameraPermission = true,
            microphonePermission = false,
        )

        assertThat(result).isEqualTo(StartResult.MicrophonePermissionRequired)
        assertThat(session.status()).isEqualTo(StreamStatus.Stopped)
    }

    @Test
    fun confirmedStartWithoutCameraPermissionIsDeniedAndRemainsStopped() {
        val session = StreamSession()

        val result = session.requestStart(confirmed = true, cameraPermission = false)

        assertThat(result).isEqualTo(StartResult.CameraPermissionRequired)
        assertThat(session.status()).isEqualTo(StreamStatus.Stopped)
    }

    @Test
    fun unconfirmedStartDoesNotBeginConnectingEvenWithCameraPermission() {
        val session = StreamSession()

        val result = session.requestStart(confirmed = false, cameraPermission = true)

        assertThat(result).isEqualTo(StartResult.Cancelled)
        assertThat(session.status()).isEqualTo(StreamStatus.Stopped)
    }
}
