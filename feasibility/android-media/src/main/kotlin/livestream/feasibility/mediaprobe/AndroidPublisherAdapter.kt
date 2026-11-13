package livestream.feasibility.mediaprobe

import livestream.feasibility.StartResult
import livestream.feasibility.ManualPublisherControl
import livestream.feasibility.StreamSession
import livestream.feasibility.ManualDestination
import livestream.feasibility.DestinationResult
import com.pedro.library.generic.GenericStream

/** Android integration under construction; no real trial is authorized yet. */
class AndroidPublisherAdapter(private val store: AndroidSafetyStore, private val connect: (String) -> Unit, private val stop: () -> Unit,
    private val lifecycle: DiagnosticPublisherLifecycle? = null, private val sessionId: String = "", private val diagnosticOnly: Boolean = true) {
    private var submittedAssessment: DiagnosticAssessment? = null
    private var captureReleased = false
    @Synchronized fun releaseCapture(release: () -> Unit) {
        if (captureReleased) return
        captureReleased = true
        release()
    }
    @Synchronized private fun stopIfCaptureAvailable() { if (!captureReleased) stop() }
    /** Test-double path only; never use the fixture as real broadcast authorization. */
    fun startDiagnostic(server: String, key: String, confirmed: Boolean, camera: Boolean, microphone: Boolean): Boolean {
        if (!diagnosticOnly || lifecycle == null) return false
        return start(server, key, confirmed, camera, microphone) == StartResult.Accepted
    }
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
    /** Supplies the bitmap and assessment from the same committed projection; the consumer owns the bitmap. */
    @Synchronized fun renderDiagnosticProjection(outputWidth: Int = 1280, outputHeight: Int = 720, consumer: (android.graphics.Bitmap, DiagnosticAssessment) -> Unit): Boolean {
        submittedAssessment = null
        try {
            val (display, snapshot) = store.diagnosticProjection()
            val bitmap = DiagnosticOverlay.render(display)
            if (bitmap == null) {
                stopForOutputFailure()
                return false
            }
            if (!DiagnosticOverlay.fitsOutput(bitmap, outputWidth, outputHeight)) {
                bitmap.recycle()
                stopForOutputFailure()
                return false
            }
            val assessment = DiagnosticAssessment(display, snapshot, true)
            consumer(bitmap, assessment)
            submittedAssessment = assessment
            return true
        } catch (failure: Exception) {
            stopForOutputFailure()
            throw failure
        }
    }
    private fun stopForOutputFailure() {
        lifecycle?.requestStop(sessionId, true, ::stopIfCaptureAvailable)
    }
    @Synchronized fun scorePoint(left: Boolean) {
        try {
            store.scorePoint(left)
        } finally {
            if (lifecycle?.status() == DiagnosticPublisherStatus.Connecting) lifecycle.requestStop(sessionId, true, ::stopIfCaptureAvailable)
        }
    }
    private val control = ManualPublisherControl(StreamSession(store), stop = ::stopIfCaptureAvailable, connect = connect)

    constructor(store: AndroidSafetyStore, stream: GenericStream, lifecycle: DiagnosticPublisherLifecycle, sessionId: String) : this(
        store, connect = { stream.startStream(it) }, stop = { stream.stopStream() }, lifecycle = lifecycle, sessionId = sessionId, diagnosticOnly = false
    )

    @Synchronized fun start(server: String, key: String, confirmed: Boolean, camera: Boolean, microphone: Boolean): StartResult {
        if (!confirmed) return StartResult.Cancelled
        if (captureReleased) return StartResult.SafetyStateUnavailable
        if (confirmed && lifecycle != null) when (lifecycle.status()) {
            DiagnosticPublisherStatus.Connecting -> return StartResult.AlreadyConnecting
            DiagnosticPublisherStatus.Live -> return StartResult.AlreadyLive
            DiagnosticPublisherStatus.Stopping -> return StartResult.PendingStop
            DiagnosticPublisherStatus.Stopped -> Unit
        }
        if (lifecycle != null && diagnosticOnly) {
            if (ManualDestination.validate(server, key) != DestinationResult.Ready) return StartResult.InvalidDestination
            if (!camera) return StartResult.CameraPermissionRequired
            if (!microphone) return StartResult.MicrophonePermissionRequired
            return try {
                val assessment = submittedAssessment
                if (assessment == null || !assessment.fits || assessment.snapshot != store.snapshot()) StartResult.NumericFitUnresolved
                else if (lifecycle.startDiagnostic(sessionId, server, key, true, true, true, true, assessment.snapshot, ::stopIfCaptureAvailable, connect)) StartResult.Accepted
                else if (lifecycle.status() == DiagnosticPublisherStatus.Stopping) StartResult.PublisherFailed
                else StartResult.SafetyStateUnavailable
            } catch (_: Exception) { StartResult.SafetyStateUnavailable }
        }
        return control.start(server, key, confirmed, camera, microphone, fitReady = false, assessed = null)
    }

    @Synchronized fun connectionSucceeded() {
        if (lifecycle != null) lifecycle.connected(sessionId, ::stopIfCaptureAvailable) else control.connectionSucceeded()
    }
    @Synchronized fun disconnected() {
        if (lifecycle != null) lifecycle.disconnected(sessionId, ::stopIfCaptureAvailable) else control.requestStop(true)
    }
    @Synchronized fun requestStop(confirmed: Boolean) {
        if (lifecycle != null) lifecycle.requestStop(sessionId, confirmed, ::stopIfCaptureAvailable) else if (!captureReleased) control.requestStop(confirmed)
    }
}