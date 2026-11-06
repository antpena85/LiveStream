package livestream.feasibility

import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertThrows
import org.junit.rules.TemporaryFolder

class PublisherSessionProbeTest {
    @get:Rule val temporaryFolder = TemporaryFolder()

    @Test
    fun committedFitFailureWhileLiveRequestsStopForAffectedSession() {
        val database = temporaryFolder.root.toPath().resolve("live-fit-failure.sqlite")
        DurableCommandProbe(database).use { store ->
            val publisher = HeldPublisher()
            val session = PublisherSessionProbe(StreamSession(store), publisher)
            assertThat(session.start("session-a", store.snapshot(), confirmed = true)).isEqualTo(StartResult.Accepted)
            publisher.releaseConnection()
            assertThat(session.status()).isEqualTo(StreamStatus.Live)

            assertThat(store.recordPoint("fit-point", 0, "session-a")).isEqualTo(ProbeSnapshot(8, 1, 0))
            session.safetyChanged()

            assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
            assertThat(publisher.stopRequests).containsExactly("session-a")
            assertThat(store.pendingStopSessions()).containsExactly("session-a")
            assertThat(store.recordPoint("fit-point", 0)).isEqualTo(ProbeSnapshot(8, 1, 0))
        }
        DurableCommandProbe(database).use { reopened ->
            assertThat(reopened.snapshot()).isEqualTo(ProbeSnapshot(8, 1, 0))
            assertThat(reopened.pendingStopSessions()).containsExactly("session-a")
        }
    }

    @Test
    fun startWithoutExplicitConfirmationDoesNotConnect() {
        val database = temporaryFolder.root.toPath().resolve("publisher-unconfirmed-start.sqlite")
        DurableCommandProbe(database).use { store ->
            val publisher = HeldPublisher()
            val session = PublisherSessionProbe(StreamSession(store), publisher)

            val result = session.start("session-a", store.snapshot())

            assertThat(result).isEqualTo(StartResult.Cancelled)
            assertThat(session.status()).isEqualTo(StreamStatus.Stopped)
            assertThat(publisher.connectRequests).isEmpty()
        }
    }

    @Test
    fun canceledStopWhileConnectingDoesNotStopThePublisher() {
        val database = temporaryFolder.root.toPath().resolve("publisher-canceled-stop.sqlite")
        DurableCommandProbe(database).use { store ->
            val publisher = HeldPublisher()
            val session = PublisherSessionProbe(StreamSession(store), publisher)
            assertThat(session.start("session-a", store.snapshot(), confirmed = true)).isEqualTo(StartResult.Accepted)

            session.stop(confirmed = false)

            assertThat(session.status()).isEqualTo(StreamStatus.Connecting)
            assertThat(publisher.connectRequests).containsExactly("session-a")
            assertThat(publisher.stopRequests).isEmpty()
            publisher.releaseConnection()
            assertThat(session.status()).isEqualTo(StreamStatus.Live)
            assertThat(publisher.stopRequests).isEmpty()
        }
    }

    @Test
    fun canceledStartDoesNotRequestConnection() {
        val database = temporaryFolder.root.toPath().resolve("publisher-canceled-start.sqlite")
        DurableCommandProbe(database).use { store ->
            val publisher = HeldPublisher()
            val session = PublisherSessionProbe(StreamSession(store), publisher)

            val result = session.start("session-a", store.snapshot(), confirmed = false)

            assertThat(result).isEqualTo(StartResult.Cancelled)
            assertThat(session.status()).isEqualTo(StreamStatus.Stopped)
            assertThat(publisher.connectRequests).isEmpty()
            assertThat(publisher.stopRequests).isEmpty()
        }
    }

    @Test
    fun unavailableSafetyStoreAtConnectionRequestsStopAndBlocksNewStart() {
        val database = temporaryFolder.root.toPath().resolve("publisher-safety-store-unavailable.sqlite")
        val store = DurableCommandProbe(database)
        val publisher = HeldPublisher()
        val session = PublisherSessionProbe(StreamSession(store), publisher)
        val assessed = store.snapshot()
        assertThat(session.start("session-a", assessed, confirmed = true)).isEqualTo(StartResult.Accepted)
        store.close()

        publisher.releaseConnection()

        assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
        assertThat(publisher.stopRequests).containsExactly("session-a")
        assertThat(session.start("session-b", assessed, confirmed = true)).isEqualTo(StartResult.PendingStop)
        assertThat(publisher.connectRequests).containsExactly("session-a")
    }

    @Test
    fun confirmedStopOfLiveSessionRequestsStopAndBlocksNewStart() {
        val database = temporaryFolder.root.toPath().resolve("stop-live.sqlite")
        DurableCommandProbe(database).use { store ->
            val publisher = HeldPublisher()
            val session = PublisherSessionProbe(StreamSession(store), publisher)
            val assessed = store.snapshot()
            assertThat(session.start("session-a", assessed, confirmed = true)).isEqualTo(StartResult.Accepted)
            publisher.releaseConnection()
            assertThat(session.status()).isEqualTo(StreamStatus.Live)

            session.stop(confirmed = true)

            assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
            assertThat(publisher.stopRequests).containsExactly("session-a")
            assertThat(session.start("session-b", assessed, confirmed = true)).isEqualTo(StartResult.PendingStop)
            assertThat(publisher.connectRequests).containsExactly("session-a")
        }
    }

    @Test
    fun ordinaryRevisionChangeWhileConnectingStopsTheStaleAttempt() {
        val database = temporaryFolder.root.toPath().resolve("stale-assessment.sqlite")
        DurableCommandProbe(database).use { store ->
            val publisher = HeldPublisher()
            val session = PublisherSessionProbe(StreamSession(store), publisher)
            assertThat(session.start("session-a", store.snapshot(), confirmed = true)).isEqualTo(StartResult.Accepted)

            val changed = store.recordPoint("point", expectedEpoch = 0)
            publisher.releaseConnection()

            assertThat(changed).isEqualTo(ProbeSnapshot(8, 1, 0))
            assertThat(store.snapshot()).isEqualTo(changed)
            assertThat(store.pendingStopSessions()).isEmpty()
            assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
            assertThat(publisher.stopRequests).containsExactly("session-a")
        }
    }

    @Test
    fun connectAndCleanupFailurePreserveOriginalFailureAndBlockNewStart() {
        val database = temporaryFolder.root.toPath().resolve("connect-and-stop-failed.sqlite")
        DurableCommandProbe(database).use { store ->
            val publisher = HeldPublisher(failConnect = true, failStop = true)
            val session = PublisherSessionProbe(StreamSession(store), publisher)
            val assessed = store.snapshot()

            val failure = assertThrows(IllegalStateException::class.java) { session.start("session-a", assessed, confirmed = true) }

            assertThat(failure).hasMessageThat().isEqualTo("simulated connect failure")
            assertThat(failure.suppressed.map { it.message }).containsExactly("simulated stop failure")
            assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
            assertThat(publisher.stopRequests).containsExactly("session-a")
            assertThat(session.start("session-b", assessed, confirmed = true)).isEqualTo(StartResult.PendingStop)
            assertThat(publisher.connectRequests).containsExactly("session-a")
        }
    }

    @Test
    fun connectionRequestFailureRequiresStopAndBlocksNewStart() {
        val database = temporaryFolder.root.toPath().resolve("connect-failed.sqlite")
        DurableCommandProbe(database).use { store ->
            val publisher = HeldPublisher(failConnect = true)
            val session = PublisherSessionProbe(StreamSession(store), publisher)
            val assessed = store.snapshot()

            assertThrows(IllegalStateException::class.java) { session.start("session-a", assessed, confirmed = true) }

            assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
            assertThat(publisher.stopRequests).containsExactly("session-a")
            assertThat(session.start("session-b", assessed, confirmed = true)).isEqualTo(StartResult.PendingStop)
            assertThat(publisher.connectRequests).containsExactly("session-a")
        }
    }

    @Test
    fun confirmedRetryAfterStopRequestFailureTargetsSameSessionAndRemainsStopping() {
        val database = temporaryFolder.root.toPath().resolve("retry-failed-stop.sqlite")
        DurableCommandProbe(database).use { store ->
            val publisher = HeldPublisher(failStop = true)
            val session = PublisherSessionProbe(StreamSession(store), publisher)
            val assessed = store.snapshot()
            assertThat(session.start("session-a", assessed, confirmed = true)).isEqualTo(StartResult.Accepted)
            assertThrows(IllegalStateException::class.java) { session.stop(confirmed = true) }
            publisher.failStop = false

            session.stop(confirmed = true)

            assertThat(publisher.stopRequests).containsExactly("session-a", "session-a").inOrder()
            assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
            assertThat(session.start("session-b", assessed, confirmed = true)).isEqualTo(StartResult.PendingStop)
            assertThat(publisher.connectRequests).containsExactly("session-a")
        }
    }

    @Test
    fun failedStopRequestRemainsStoppingAndBlocksNewStart() {
        val database = temporaryFolder.root.toPath().resolve("failed-stop.sqlite")
        DurableCommandProbe(database).use { store ->
            val publisher = HeldPublisher(failStop = true)
            val session = PublisherSessionProbe(StreamSession(store), publisher)
            val assessed = store.snapshot()
            assertThat(session.start("session-a", assessed, confirmed = true)).isEqualTo(StartResult.Accepted)

            assertThrows(IllegalStateException::class.java) { session.stop(confirmed = true) }

            assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
            assertThat(publisher.stopRequests).containsExactly("session-a")
            assertThat(session.start("session-b", assessed, confirmed = true)).isEqualTo(StartResult.PendingStop)
            assertThat(publisher.connectRequests).containsExactly("session-a")
        }
    }

    @Test
    fun connectionAfterConfirmedStopRequestsAnotherStopForSameSession() {
        val database = temporaryFolder.root.toPath().resolve("late-after-stop.sqlite")
        DurableCommandProbe(database).use { store ->
            val publisher = HeldPublisher()
            val session = PublisherSessionProbe(StreamSession(store), publisher)
            assertThat(session.start("session-a", store.snapshot(), confirmed = true)).isEqualTo(StartResult.Accepted)
            session.stop(confirmed = true)
            assertThat(publisher.stopRequests).containsExactly("session-a")

            publisher.releaseConnection()

            assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
            assertThat(publisher.stopRequests).containsExactly("session-a", "session-a").inOrder()
        }
    }

    @Test
    fun confirmedStopWhileConnectingRequestsStopWithoutClaimingStopped() {
        val database = temporaryFolder.root.toPath().resolve("stop-connecting.sqlite")
        DurableCommandProbe(database).use { store ->
            val publisher = HeldPublisher()
            val session = PublisherSessionProbe(StreamSession(store), publisher)
            assertThat(session.start("session-a", store.snapshot(), confirmed = true)).isEqualTo(StartResult.Accepted)

            session.stop(confirmed = true)

            assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
            assertThat(publisher.stopRequests).containsExactly("session-a")
        }
    }

    @Test
    fun lateConnectionAfterFitFailureRequestsStopForAffectedSession() {
        val database = temporaryFolder.root.toPath().resolve("publisher-race.sqlite")
        DurableCommandProbe(database).use { store ->
            val publisher = HeldPublisher()
            val session = PublisherSessionProbe(StreamSession(store), publisher)
            val assessed = store.recordPoint("first", expectedEpoch = 0)
            assertThat(assessed).isEqualTo(ProbeSnapshot(8, 1, 0))
            assertThat(session.start("session-a", assessed, confirmed = true)).isEqualTo(StartResult.Accepted)
            assertThat(publisher.connectRequests).containsExactly("session-a")

            val changed = store.recordPoint("second", expectedEpoch = 0, fitFailureSession = "session-a")
            assertThat(changed).isEqualTo(ProbeSnapshot(9, 2, 0))
            assertThat(store.recordPoint("second", expectedEpoch = 0)).isEqualTo(changed)
            assertThat(store.pendingStopSessions()).containsExactly("session-a")

            publisher.releaseConnection()

            assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
            assertThat(publisher.stopRequests).containsExactly("session-a")
            assertThat(store.snapshot()).isEqualTo(changed)
            assertThat(store.pendingStopSessions()).containsExactly("session-a")
        }
    }

    private class HeldPublisher(var failStop: Boolean = false, private val failConnect: Boolean = false) : ProbePublisher {
        val connectRequests = mutableListOf<String>()
        val stopRequests = mutableListOf<String>()
        private var callback: (() -> Unit)? = null

        override fun connect(sessionId: String, onConnected: () -> Unit) {
            connectRequests.add(sessionId)
            callback = onConnected
            if (failConnect) throw IllegalStateException("simulated connect failure")
        }

        override fun stop(sessionId: String) {
            stopRequests.add(sessionId)
            if (failStop) throw IllegalStateException("simulated stop failure")
        }

        fun releaseConnection() {
            checkNotNull(callback).invoke()
        }
    }
}