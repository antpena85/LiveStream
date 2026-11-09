package livestream.feasibility.mediaprobe

import livestream.feasibility.ProbeSnapshot
import livestream.feasibility.DestinationResult
import livestream.feasibility.ManualDestination

enum class DiagnosticPublisherStatus { Stopped, Connecting, Live, Stopping }

/** Testable session ownership experiment; not an authorization to publish. */
class DiagnosticPublisherLifecycle(private val store: AndroidSafetyStore) {
    fun startDiagnostic(sessionId: String, server: String, key: String, confirmed: Boolean, camera: Boolean, microphone: Boolean, fit: Boolean, snapshot: ProbeSnapshot, stop: () -> Unit, connect: (String) -> Unit): Boolean {
        if (!confirmed || !camera || !microphone || !fit || ManualDestination.validate(server, key) != DestinationResult.Ready) return false
        return claimAndConnect(sessionId, snapshot, stop) {
            ManualDestination.withEndpoint(server, key, connect)
        }
    }
    @Synchronized fun failed(sessionId: String, stop: () -> Unit) {
        requestStop(sessionId, true, stop)
    }
    @Synchronized fun requestStop(sessionId: String, confirmed: Boolean, stop: () -> Unit) {
        if (!confirmed || owner != sessionId || current == DiagnosticPublisherStatus.Stopped || current == DiagnosticPublisherStatus.Stopping) return
        current = DiagnosticPublisherStatus.Stopping
        try { stop() } catch (_: Exception) { /* Keep the durable claim and stopping state. */ }
    }
    @Synchronized fun claimAndConnect(sessionId: String, snapshot: ProbeSnapshot, stop: () -> Unit, connect: () -> Unit): Boolean {
        if (!claim(sessionId, snapshot)) return false
        try {
            connect()
        } catch (_: Exception) {
            current = DiagnosticPublisherStatus.Stopping
            try { stop() } catch (_: Exception) { /* Preserve the unresolved claim. */ }
            return false
        }
        return true
    }
    private var owner: String? = null
    private var assessed: ProbeSnapshot? = null
    private var current = DiagnosticPublisherStatus.Stopped

    @Synchronized fun claim(sessionId: String, snapshot: ProbeSnapshot): Boolean {
        if (current != DiagnosticPublisherStatus.Stopped) return false
        if (!store.claimPublisherSession(sessionId, snapshot)) return false
        owner = sessionId
        assessed = snapshot
        current = DiagnosticPublisherStatus.Connecting
        return true
    }

    @Synchronized fun connected(sessionId: String, stop: () -> Unit) {
        if (owner != sessionId) return
        if (current == DiagnosticPublisherStatus.Stopping) {
            try { stop() } catch (_: Exception) { /* Preserve the unresolved claim. */ }
            return
        }
        if (current != DiagnosticPublisherStatus.Connecting) return
        current = try {
            val view = store.safetyView()
            if (view.pendingStops == setOf(sessionId) && view.snapshot == assessed) DiagnosticPublisherStatus.Live
            else DiagnosticPublisherStatus.Stopping
        } catch (_: Exception) { DiagnosticPublisherStatus.Stopping }
        if (current == DiagnosticPublisherStatus.Stopping) {
            try { stop() } catch (_: Exception) { /* Preserve the unresolved claim. */ }
        }
    }

    @Synchronized fun status(): DiagnosticPublisherStatus = current
}