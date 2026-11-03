package livestream.feasibility

enum class StartResult { Accepted, CameraPermissionRequired, MicrophonePermissionRequired, Cancelled, PendingStop, NumericFitUnresolved, AlreadyConnecting, AlreadyLive }
enum class StreamStatus { Stopped, Connecting, Live, Stopping }

/** Isolated M2 T02 probe; this does not control Android capture or publishing. */
class StreamSession(private val store: DurableCommandProbe? = null) {
    private var currentStatus = StreamStatus.Stopped
    private var connectingAssessment: ProbeSnapshot? = null

    fun status(): StreamStatus = currentStatus

    fun connectionSucceeded() {
        if (currentStatus == StreamStatus.Connecting) {
            currentStatus = if (store == null || store.pendingStopSessions().isNotEmpty() || store.snapshot() != connectingAssessment) {
                StreamStatus.Stopping
            } else StreamStatus.Live
        }
    }

    fun requestStop(confirmed: Boolean) {
        if (confirmed && (currentStatus == StreamStatus.Connecting || currentStatus == StreamStatus.Live)) {
            currentStatus = StreamStatus.Stopping
        }
    }

    fun requestStart(confirmed: Boolean, cameraPermission: Boolean, microphonePermission: Boolean = true, numericFitReady: Boolean = false, assessedSnapshot: ProbeSnapshot? = null): StartResult {
        if (!confirmed) return StartResult.Cancelled
        if (currentStatus == StreamStatus.Connecting) return StartResult.AlreadyConnecting
        if (currentStatus == StreamStatus.Live) return StartResult.AlreadyLive
        if (currentStatus == StreamStatus.Stopping) return StartResult.PendingStop
        if (!cameraPermission) return StartResult.CameraPermissionRequired
        if (!microphonePermission) return StartResult.MicrophonePermissionRequired
        if (store != null && store.pendingStopSessions().isNotEmpty()) return StartResult.PendingStop
        if (!numericFitReady || store == null || assessedSnapshot == null || assessedSnapshot != store.snapshot()) {
            return StartResult.NumericFitUnresolved
        }
        connectingAssessment = assessedSnapshot
        currentStatus = StreamStatus.Connecting
        return StartResult.Accepted
    }
}
