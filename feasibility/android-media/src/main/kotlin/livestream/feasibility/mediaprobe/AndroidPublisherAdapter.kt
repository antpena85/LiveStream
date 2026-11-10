package livestream.feasibility.mediaprobe

import livestream.feasibility.StartResult
import livestream.feasibility.ManualPublisherControl
import livestream.feasibility.StreamSession
import com.pedro.library.generic.GenericStream

/** Android integration under construction; no real trial is authorized yet. */
class AndroidPublisherAdapter(private val store: AndroidSafetyStore, connect: (String) -> Unit, private val stop: () -> Unit,
    private val lifecycle: DiagnosticPublisherLifecycle? = null, private val sessionId: String = "") {
    fun diagnosticStatus(): DiagnosticPublisherStatus = lifecycle?.status() ?: when (control.status()) {
        livestream.feasibility.StreamStatus.Stopped -> DiagnosticPublisherStatus.Stopped
        livestream.feasibility.StreamStatus.Connecting -> DiagnosticPublisherStatus.Connecting
        livestream.feasibility.StreamStatus.Live -> DiagnosticPublisherStatus.Live
        livestream.feasibility.StreamStatus.Stopping -> DiagnosticPublisherStatus.Stopping
    }
    fun diagnosticAssessment(): DiagnosticAssessment {
        val (display, snapshot) = store.diagnosticProjection()
        val bitmap = DiagnosticOverlay.render(display)
        bitmap?.recycle()
        return DiagnosticAssessment(display, snapshot, bitmap != null)
    }
    fun diagnosticFit(): Boolean = diagnosticAssessment().fits
    private val control = ManualPublisherControl(StreamSession(store), stop = stop, connect = connect)

    constructor(store: AndroidSafetyStore, stream: GenericStream, lifecycle: DiagnosticPublisherLifecycle, sessionId: String) : this(
        store, connect = { stream.startStream(it) }, stop = { stream.stopStream() }, lifecycle = lifecycle, sessionId = sessionId
    )

    fun start(server: String, key: String, confirmed: Boolean, camera: Boolean, microphone: Boolean): StartResult {
        if (confirmed && lifecycle != null) when (lifecycle.status()) {
            DiagnosticPublisherStatus.Connecting -> return StartResult.AlreadyConnecting
            DiagnosticPublisherStatus.Live -> return StartResult.AlreadyLive
            DiagnosticPublisherStatus.Stopping -> return StartResult.PendingStop
            DiagnosticPublisherStatus.Stopped -> Unit
        }
        return control.start(server, key, confirmed, camera, microphone, fitReady = false, assessed = null)
    }

    fun connectionSucceeded() {
        if (lifecycle != null) lifecycle.connected(sessionId, stop) else control.connectionSucceeded()
    }
    fun requestStop(confirmed: Boolean) {
        if (lifecycle != null) lifecycle.requestStop(sessionId, confirmed, stop) else control.requestStop(confirmed)
    }
}