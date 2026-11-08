package livestream.feasibility

import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class ManualPublisherControlTest {
    @get:Rule val temporaryFolder = TemporaryFolder()

    @Test fun confirmedSafeStartConnectsValidatedManualDestination() {
        DurableCommandProbe(temporaryFolder.root.toPath().resolve("safety.sqlite")).use { store ->
            val endpoints = mutableListOf<String>()
            val control = ManualPublisherControl(StreamSession(store), endpoints::add)
            val result = control.start("rtmps://example.test/live", "fictional-key", true, true, true, true, store.snapshot())
            assertThat(result).isEqualTo(StartResult.Accepted)
            assertThat(endpoints).containsExactly("rtmps://example.test/live/fictional-key")
        }
    }
}