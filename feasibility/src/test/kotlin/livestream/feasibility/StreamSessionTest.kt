package livestream.feasibility

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class StreamSessionTest {
    @get:Rule val temporaryFolder = TemporaryFolder()

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
