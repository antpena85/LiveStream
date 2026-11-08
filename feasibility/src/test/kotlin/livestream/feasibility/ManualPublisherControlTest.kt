package livestream.feasibility

import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class ManualPublisherControlTest {
    @get:Rule val temporaryFolder = TemporaryFolder()

    @Test fun confirmedOperatorStopRequestsPublisherCleanup() {
        DurableCommandProbe(temporaryFolder.root.toPath().resolve("operator-stop.sqlite")).use { store ->
            var stops = 0
            val session = StreamSession(store)
            val control = ManualPublisherControl(session, stop = { stops++ }, connect = {})
            assertThat(control.start("rtmps://example.test/live", "fictional-key", true, true, true, true, store.snapshot()))
                .isEqualTo(StartResult.Accepted)
            control.requestStop(false)
            assertThat(stops).isEqualTo(0)
            control.requestStop(true)
            assertThat(stops).isEqualTo(1)
            assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
        }
    }

    @Test fun connectionCallbackWithUnavailableSafetyStoreRequestsPublisherStop() {
        DurableCommandProbe(temporaryFolder.root.toPath().resolve("changed-score.sqlite")).use { store ->
            var stops = 0
            val session = StreamSession(store)
            val control = ManualPublisherControl(session, stop = { stops++ }, connect = {})
            val assessed = store.snapshot()
            assertThat(control.start("rtmps://example.test/live", "fictional-key", true, true, true, true, assessed))
                .isEqualTo(StartResult.Accepted)
            store.close()
            control.connectionSucceeded()
            assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
            assertThat(stops).isEqualTo(1)
        }
    }

    @Test fun failedConnectionRequestsPublisherCleanupWithoutExposingCredentials() {
        DurableCommandProbe(temporaryFolder.root.toPath().resolve("cleanup.sqlite")).use { store ->
            var stopped = false
            val session = StreamSession(store)
            val control = ManualPublisherControl(session, connect = { throw IllegalStateException(it) }, stop = { stopped = true })
            val result = control.start("rtmps://example.test/live", "fictional-secret", true, true, true, true, store.snapshot())
            assertThat(result).isEqualTo(StartResult.PublisherFailed)
            assertThat(stopped).isTrue()
            assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
        }
    }

    @Test fun publisherFailureReturnsSafeStatusAndBlocksAnotherStart() {
        DurableCommandProbe(temporaryFolder.root.toPath().resolve("publisher-failure.sqlite")).use { store ->
            val session = StreamSession(store)
            val control = ManualPublisherControl(session, stop = {}) { endpoint -> throw IllegalStateException(endpoint) }

            val result = try {
                control.start("rtmps://example.test/live", "fictional-secret", true, true, true, true, store.snapshot()).toString()
            } catch (_: Exception) {
                "Exception escaped"
            }

            assertThat(result).isEqualTo("PublisherFailed")
            assertThat(session.status()).isEqualTo(StreamStatus.Stopping)
            assertThat(control.start("rtmps://example.test/live", "fictional-secret", true, true, true, true, store.snapshot()))
                .isEqualTo(StartResult.PendingStop)
        }
    }

    @Test fun confirmedSafeStartConnectsValidatedManualDestination() {
        DurableCommandProbe(temporaryFolder.root.toPath().resolve("safety.sqlite")).use { store ->
            val endpoints = mutableListOf<String>()
            val control = ManualPublisherControl(StreamSession(store), stop = {}, connect = endpoints::add)
            val result = control.start("rtmps://example.test/live", "fictional-key", true, true, true, true, store.snapshot())
            assertThat(result).isEqualTo(StartResult.Accepted)
            assertThat(endpoints).containsExactly("rtmps://example.test/live/fictional-key")
        }
    }
}