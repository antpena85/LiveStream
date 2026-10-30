package livestream.feasibility

enum class StartResult { Accepted, CameraPermissionRequired, MicrophonePermissionRequired, Cancelled, PendingStop, NumericFitUnresolved }
enum class StreamStatus { Stopped, Connecting }

/** Isolated M2 T02 probe; this does not control Android capture or publishing. */
class StreamSession(private val store: DurableCommandProbe? = null) {
    private var currentStatus = StreamStatus.Stopped

    fun status(): StreamStatus = currentStatus

    fun requestStart(confirmed: Boolean, cameraPermission: Boolean, microphonePermission: Boolean = true, numericFitReady: Boolean = false): StartResult {
        if (!confirmed) return StartResult.Cancelled
        if (!cameraPermission) return StartResult.CameraPermissionRequired
        if (!microphonePermission) return StartResult.MicrophonePermissionRequired
        if (store != null && store.pendingStopSessions().isNotEmpty()) return StartResult.PendingStop
        if (!numericFitReady) return StartResult.NumericFitUnresolved
        currentStatus = StreamStatus.Connecting
        return StartResult.Accepted
    }
}
