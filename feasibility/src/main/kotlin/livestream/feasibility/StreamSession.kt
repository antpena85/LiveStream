package livestream.feasibility

enum class StartResult { Accepted, CameraPermissionRequired, MicrophonePermissionRequired, Cancelled, PendingStop, NumericFitUnresolved, AlreadyConnecting }
enum class StreamStatus { Stopped, Connecting, Stopping }

/** Isolated M2 T02 probe; this does not control Android capture or publishing. */
class StreamSession(private val store: DurableCommandProbe? = null) {
    private var currentStatus = StreamStatus.Stopped

    fun status(): StreamStatus = currentStatus

    fun requestStop(confirmed: Boolean) {
        if (confirmed && currentStatus == StreamStatus.Connecting) currentStatus = StreamStatus.Stopping
    }

    fun requestStart(confirmed: Boolean, cameraPermission: Boolean, microphonePermission: Boolean = true, numericFitReady: Boolean = false, assessedSnapshot: ProbeSnapshot? = null): StartResult {
        if (!confirmed) return StartResult.Cancelled
        if (currentStatus == StreamStatus.Connecting) return StartResult.AlreadyConnecting
        if (currentStatus == StreamStatus.Stopping) return StartResult.PendingStop
        if (!cameraPermission) return StartResult.CameraPermissionRequired
        if (!microphonePermission) return StartResult.MicrophonePermissionRequired
        if (store != null && store.pendingStopSessions().isNotEmpty()) return StartResult.PendingStop
        if (!numericFitReady || store == null || assessedSnapshot == null || assessedSnapshot != store.snapshot()) {
            return StartResult.NumericFitUnresolved
        }
        currentStatus = StreamStatus.Connecting
        return StartResult.Accepted
    }
}
