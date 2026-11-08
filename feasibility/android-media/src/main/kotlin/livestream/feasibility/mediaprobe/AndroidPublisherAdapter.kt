package livestream.feasibility.mediaprobe

import livestream.feasibility.StartResult
import livestream.feasibility.ManualPublisherControl
import livestream.feasibility.StreamSession
import com.pedro.library.generic.GenericStream

/** Android integration under construction; no real trial is authorized yet. */
class AndroidPublisherAdapter(private val store: AndroidSafetyStore, connect: (String) -> Unit, stop: () -> Unit) {
    fun diagnosticAssessment(): DiagnosticAssessment {
        val (display, snapshot) = store.diagnosticProjection()
        val bitmap = DiagnosticOverlay.render(display)
        bitmap?.recycle()
        return DiagnosticAssessment(display, snapshot, bitmap != null)
    }
    fun diagnosticFit(): Boolean = diagnosticAssessment().fits
    private val control = ManualPublisherControl(StreamSession(store), stop = stop, connect = connect)

    constructor(store: AndroidSafetyStore, stream: GenericStream) : this(
        store, connect = { stream.startStream(it) }, stop = { stream.stopStream() }
    )

    fun start(server: String, key: String, confirmed: Boolean, camera: Boolean, microphone: Boolean): StartResult =
        control.start(server, key, confirmed, camera, microphone, fitReady = false, assessed = null)

    fun connectionSucceeded() = control.connectionSucceeded()
    fun requestStop(confirmed: Boolean) = control.requestStop(confirmed)
}