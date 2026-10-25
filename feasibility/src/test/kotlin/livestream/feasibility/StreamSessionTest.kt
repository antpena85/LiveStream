package livestream.feasibility

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class StreamSessionTest {
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
