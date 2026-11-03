package livestream.feasibility

import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertThrows
import org.junit.rules.TemporaryFolder

class PublisherSessionProbeTest {
    @get:Rule val temporaryFolder = TemporaryFolder()

    @Test
    fun confirmedRetryAfterStopRequestFailureTargetsSameSessionAndRemainsStopping() {
        val database = temporaryFolder.root.toPath().resolve("retry-failed-stop.sqlite")
        DurableCommandProbe(database).use { store ->
            val publisher = HeldPublisher(failStop = true)
            val session = PublisherSessionProbe(StreamSession(store), publisher)
            val assessed = store.snapshot()
            assertThat(session.start("session-a", assessed)).isEqualTo(StartResult.Accepted)
            assertThrows(IllegalStateException::class.java) { session.stop(confirmed = true) }
            publisher.failStop = false

            session.stop(confirmed = true)

            assertThat(publisher.stopRequests).containsExactly("session-a", "session-a").inOrder()
            assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
            assertThat(session.start("session-b", assessed)).isEqualTo(StartResult.PendingStop)
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
            assertThat(session.start("session-a", assessed)).isEqualTo(StartResult.Accepted)

            assertThrows(IllegalStateException::class.java) { session.stop(confirmed = true) }

            assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
            assertThat(publisher.stopRequests).containsExactly("session-a")
            assertThat(session.start("session-b", assessed)).isEqualTo(StartResult.PendingStop)
            assertThat(publisher.connectRequests).containsExactly("session-a")
        }
    }

    @Test
    fun connectionAfterConfirmedStopRequestsAnotherStopForSameSession() {
        val database = temporaryFolder.root.toPath().resolve("late-after-stop.sqlite")
        DurableCommandProbe(database).use { store ->
            val publisher = HeldPublisher()
            val session = PublisherSessionProbe(StreamSession(store), publisher)
            assertThat(session.start("session-a", store.snapshot())).isEqualTo(StartResult.Accepted)
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
            assertThat(session.start("session-a", store.snapshot())).isEqualTo(StartResult.Accepted)

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
            assertThat(session.start("session-a", assessed)).isEqualTo(StartResult.Accepted)
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

    private class HeldPublisher(var failStop: Boolean = false) : ProbePublisher {
        val connectRequests = mutableListOf<String>()
        val stopRequests = mutableListOf<String>()
        private var callback: (() -> Unit)? = null

        override fun connect(sessionId: String, onConnected: () -> Unit) {
            connectRequests.add(sessionId)
            callback = onConnected
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