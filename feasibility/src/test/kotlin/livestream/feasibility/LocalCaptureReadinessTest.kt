package livestream.feasibility

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LocalCaptureReadinessTest {
    @Test
    fun unconfirmedLocalCaptureIsCancelledEvenWhenPermissionsAreGranted() {
        assertThat(LocalCaptureReadiness().request(false, true, true))
            .isEqualTo(LocalCaptureResult.Cancelled)
    }

    @Test
    fun confirmedLocalCaptureWithoutMicrophonePermissionReportsMicrophoneRequired() {
        assertThat(LocalCaptureReadiness().request(true, true, false))
            .isEqualTo(LocalCaptureResult.MicrophonePermissionRequired)
    }

    @Test
    fun confirmedLocalCaptureWithoutCameraPermissionReportsCameraRequired() {
        assertThat(LocalCaptureReadiness().request(true, false, true))
            .isEqualTo(LocalCaptureResult.CameraPermissionRequired)
    }

    @Test
    fun confirmedLocalCaptureWithPermissionsIsReady() {
        val result = LocalCaptureReadiness().request(true, true, true)

        assertThat(result).isEqualTo(LocalCaptureResult.Ready)
    }
}