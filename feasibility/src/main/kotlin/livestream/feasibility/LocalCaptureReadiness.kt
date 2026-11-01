package livestream.feasibility

enum class LocalCaptureResult { Ready, Cancelled, CameraPermissionRequired, MicrophonePermissionRequired }

/** Permission gate for local-only diagnostic capture; never authorizes publishing. */
class LocalCaptureReadiness {
    fun request(confirmed: Boolean, cameraPermission: Boolean, microphonePermission: Boolean): LocalCaptureResult =
        when {
            !confirmed -> LocalCaptureResult.Cancelled
            !cameraPermission -> LocalCaptureResult.CameraPermissionRequired
            !microphonePermission -> LocalCaptureResult.MicrophonePermissionRequired
            else -> LocalCaptureResult.Ready
        }
}