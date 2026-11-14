package livestream.feasibility

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class YouTubeAuthorizationTest {
    @Test
    fun cancelledAuthorizationDoesNotProceedToDestinationSetup() {
        var proceeded = false
        val result = YouTubeAuthorization.complete(AuthorizationResponse.Cancelled) { proceeded = true }

        assertThat(result).isEqualTo(YouTubeAuthorizationResult.NotAuthorized)
        assertThat(proceeded).isFalse()
    }
}