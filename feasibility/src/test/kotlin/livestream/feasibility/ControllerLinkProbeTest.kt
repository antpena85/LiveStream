package livestream.feasibility

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ControllerLinkProbeTest {
    @Test
    fun authorizingOneControllerDoesNotAuthorizeAnotherController() {
        val link = ControllerLinkProbe()
        link.authorize("controller-a")

        assertThat(link.ping("controller-b", "request-1")).isEqualTo(
            ControllerLinkProbe.Result(ControllerLinkProbe.Status.DENIED)
        )
    }

    @Test
    fun revokedControllerCannotObtainAcknowledgmentForANewRequest() {
        val link = ControllerLinkProbe()
        link.authorize("controller-a")
        assertThat(link.ping("controller-a", "request-1")).isEqualTo(
            ControllerLinkProbe.Result(ControllerLinkProbe.Status.ACKNOWLEDGED, "request-1")
        )

        link.revoke("controller-a")

        assertThat(link.ping("controller-a", "request-2")).isEqualTo(
            ControllerLinkProbe.Result(ControllerLinkProbe.Status.DENIED)
        )
    }
}