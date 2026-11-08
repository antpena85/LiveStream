package livestream.feasibility

enum class StartResult { Accepted, CameraPermissionRequired, MicrophonePermissionRequired, Cancelled, InvalidDestination, PendingStop, NumericFitUnresolved, SafetyStateUnavailable, AlreadyConnecting, AlreadyLive }
enum class StreamStatus { Stopped, Connecting, Live, Stopping }

/** Isolated M2 T02 probe; this does not control Android capture or publishing. */
class StreamSession(private val store: SessionSafetyState? = null) {
    private var currentStatus = StreamStatus.Stopped
    private var connectingAssessment: ProbeSnapshot? = null
    private var safetyGeneration = 0L

    fun status(): StreamStatus = currentStatus

    fun safetyChanged() {
        synchronized(this) { safetyGeneration++ }
        if (currentStatus == StreamStatus.Live || currentStatus == StreamStatus.Connecting) {
            val requiresStop = try {
                store == null || store.pendingStopSessions().isNotEmpty()
            } catch (_: Exception) {
                true
            }
            if (requiresStop) currentStatus = StreamStatus.Stopping
        }
    }

    fun connectionSucceeded() {
        if (currentStatus == StreamStatus.Connecting) {
            val result = try {
                if (store == null || store.pendingStopSessions().isNotEmpty() || store.snapshot() != connectingAssessment) {
                    StreamStatus.Stopping
                } else StreamStatus.Live
            } catch (_: Exception) {
                StreamStatus.Stopping
            }
            if (currentStatus == StreamStatus.Connecting) currentStatus = result
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
        val startingGeneration = synchronized(this) { safetyGeneration }
        try {
            val view = store?.safetyView()
            if (view != null && view.pendingStops.isNotEmpty()) return StartResult.PendingStop
            if (!numericFitReady || view == null || assessedSnapshot == null || assessedSnapshot != view.snapshot) {
                return StartResult.NumericFitUnresolved
            }
        } catch (_: Exception) {
            return StartResult.SafetyStateUnavailable
        }
        return synchronized(this) {
            when (currentStatus) {
                StreamStatus.Connecting -> StartResult.AlreadyConnecting
                StreamStatus.Live -> StartResult.AlreadyLive
                StreamStatus.Stopping -> StartResult.PendingStop
                StreamStatus.Stopped -> {
                    if (safetyGeneration != startingGeneration) return@synchronized StartResult.SafetyStateUnavailable
                    connectingAssessment = assessedSnapshot
                    currentStatus = StreamStatus.Connecting
                    StartResult.Accepted
                }
            }
        }
    }
}
