package livestream.feasibility

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ManualDestinationTest {
    @Test
    fun serverPortAboveTcpRangeIsRejectedBeforeEndpointCanBeUsed() {
        var called = false
        val result = ManualDestination.withEndpoint("rtmps://example.test:65536/live", "fixture-key") { called = true }
        assertThat(result).isEqualTo(DestinationResult.InvalidServer)
        assertThat(called).isFalse()
    }

    @Test
    fun serverFragmentIsRejectedBeforeEndpointCanBeUsed() {
        var called = false
        val result = ManualDestination.withEndpoint("rtmps://example.test/live#fragment", "fixture-key") { called = true }
        assertThat(result).isEqualTo(DestinationResult.InvalidServer)
        assertThat(called).isFalse()
    }

    @Test
    fun serverQueryIsRejectedBeforeEndpointCanBeUsed() {
        var called = false
        val result = ManualDestination.withEndpoint("rtmps://example.test/live?token=fixture", "fixture-key") { called = true }
        assertThat(result).isEqualTo(DestinationResult.InvalidServer)
        assertThat(called).isFalse()
    }

    @Test
    fun endpointCombinesServerAndKeyForPublisherWithoutDoubleSlash() {
        var observed: String? = null
        val result = ManualDestination.withEndpoint("rtmps://example.test/live/", "fixture-key") { observed = it }
        assertThat(result).isEqualTo(DestinationResult.Ready)
        assertThat(observed).isEqualTo("rtmps://example.test/live/fixture-key")
    }

    @Test
    fun embeddedCredentialsInServerAreRejected() {
        assertThat(ManualDestination.validate("rtmps://user:secret@example.test/live", "test-key"))
            .isEqualTo(DestinationResult.InvalidServer)
    }

    @Test
    fun wellFormedRtmpsServerAndNonblankKeyPassBasicValidation() {
        assertThat(ManualDestination.validate("rtmps://example.test/live", "test-key"))
            .isEqualTo(DestinationResult.Ready)
    }

    @Test
    fun serverWithoutHostIsNotReady() {
        assertThat(ManualDestination.validate("rtmp:///live", "test-key"))
            .isEqualTo(DestinationResult.InvalidServer)
    }

    @Test
    fun nonRtmpServerIsNotReady() {
        assertThat(ManualDestination.validate("https://example.test/live", "test-key"))
            .isEqualTo(DestinationResult.InvalidServer)
    }

    @Test
    fun missingStreamKeyIsNotReady() {
        assertThat(ManualDestination.validate("rtmps://example.test/live", " "))
            .isEqualTo(DestinationResult.MissingKey)
    }
}