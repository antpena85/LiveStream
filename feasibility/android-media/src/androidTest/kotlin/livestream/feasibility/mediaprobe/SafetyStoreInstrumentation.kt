package livestream.feasibility.mediaprobe

import android.app.Activity
import android.app.Instrumentation
import android.os.Bundle
import livestream.feasibility.ProbeSnapshot
import livestream.feasibility.StartResult
import livestream.feasibility.StreamSession

class SafetyStoreInstrumentation : Instrumentation() {
    override fun onCreate(arguments: Bundle?) { super.onCreate(arguments); start() }

    override fun onStart() {
        val name = "safety-instrumentation-test.db"
        targetContext.deleteDatabase(name)
        val result = Bundle()
        try {
            AndroidSafetyStore(targetContext, name).use { store ->
                var connected = false
                val lifecycle = DiagnosticPublisherLifecycle(store)
                check(lifecycle.claimAndConnect("session-integrated", store.snapshot()) {
                    check(store.pendingStopSessions() == setOf("session-integrated")) { "Publisher called before durable claim" }
                    connected = true
                }) { "Integrated claim was rejected" }
                check(connected) { "Publisher was not called" }
                lifecycle.connected("session-integrated")
                check(lifecycle.status() == DiagnosticPublisherStatus.Live) { "Matching callback did not transition to live" }
                var stopped = false
                lifecycle.requestStop("session-integrated", true) { stopped = true }
                check(stopped && lifecycle.status() == DiagnosticPublisherStatus.Stopping) { "Confirmed stop did not dispatch" }
                check(store.pendingStopSessions() == setOf("session-integrated")) { "Stop request prematurely cleared marker" }
            }
            AndroidSafetyStore(targetContext, name).use { reopened ->
                check(!DiagnosticPublisherLifecycle(reopened).claim("new-session", reopened.snapshot())) { "Prior publisher not reconciled" }
                reopened.confirmStopped("session-integrated")
            }
            AndroidSafetyStore(targetContext, name).use { store ->
                val lifecycle = DiagnosticPublisherLifecycle(store)
                check(lifecycle.claim("owned-session", store.snapshot())) { "Could not claim diagnostic session" }
                lifecycle.connected("old-session")
                check(lifecycle.status() == DiagnosticPublisherStatus.Connecting) { "Old callback changed the current session" }
                lifecycle.connected("owned-session")
                check(lifecycle.status() == DiagnosticPublisherStatus.Live) { "Own claim was treated as an old stop" }
            }
            AndroidSafetyStore(targetContext, name).use { reopened ->
                check(!DiagnosticPublisherLifecycle(reopened).claim("next-session", reopened.snapshot())) { "Unresolved prior claim did not block" }
                reopened.confirmStopped("owned-session")
            }
            AndroidSafetyStore(targetContext, name).use { store ->
                var connected = false
                val adapter = AndroidPublisherAdapter(store, connect = { connected = true }, stop = {})
                check(adapter.diagnosticFit() == true) { "Normal diagnostic score should fit its provisional bitmap" }
                val outcome = adapter.start("rtmps://example.test/live", "fictional-key", true, true, true)
                check(outcome == StartResult.NumericFitUnresolved) { "Incomplete safety gate must block network start" }
                check(!connected) { "Incomplete safety gate invoked publisher" }
                check(store.claimPublisherSession("session-claim", store.snapshot())) { "Safe session claim failed" }
            }
            AndroidSafetyStore(targetContext, name).use { reopened ->
                check(reopened.pendingStopSessions() == setOf("session-claim")) { "Publisher claim did not survive reopen" }
                check(!reopened.claimPublisherSession("session-new", reopened.snapshot())) { "Unresolved session permitted another claim" }
                reopened.confirmStopped("session-claim")
            }
            val compact = DiagnosticOverlay.render("ABC  1  |  2  XYZ") ?: error("Normal score did not fit")
            val wider = DiagnosticOverlay.render("ABC  12345678901234567890  |  2  XYZ") ?: error("Wider score did not fit")
            check(wider.width > compact.width) { "Scoreboard did not widen" }
            check(DiagnosticOverlay.render("ABC  ${"9".repeat(100)}  |  2  XYZ") == null) { "Unfittable score was accepted" }
            compact.recycle()
            wider.recycle()
            AndroidSafetyStore(targetContext, name).use {
                it.scorePoint(left = true)
                it.scorePoint(left = false)
                it.scorePoint(left = false)
            }
            AndroidSafetyStore(targetContext, name).use {
                check(it.scoreDisplay() == "ABC  1  |  2  XYZ") { "Diagnostic scoreboard did not survive reopening" }
                val assessment = AndroidPublisherAdapter(it, connect = {}, stop = {}).diagnosticAssessment()
                check(assessment.display == "ABC  1  |  2  XYZ" && assessment.snapshot == ProbeSnapshot(3, 3, 0) && assessment.fits) {
                    "Fit assessment did not identify the score and revision assessed"
                }
            }
            AndroidSafetyStore(targetContext, name).use { it.scorePointWithPendingStop(left = false, sessionId = "session-a") }
            AndroidSafetyStore(targetContext, name).use { reopened ->
                check(reopened.scoreDisplay() == "ABC  1  |  3  XYZ") { "Updated score missing after pending stop" }
                val view = reopened.safetyView()
                check(view.snapshot == ProbeSnapshot(4, 4, 0)) { "Incorrect reopened snapshot" }
                check(view.pendingStops == setOf("session-a")) { "Missing reopened pending stop" }
                val start = StreamSession(reopened).requestStart(true, true, true, true, view.snapshot)
                check(start == StartResult.PendingStop) { "Pending stop did not block Start" }
                reopened.recordPointWithPendingStop("session-b")
                reopened.confirmStopped("session-a")
            }
            AndroidSafetyStore(targetContext, name).use { reopened ->
                check(reopened.pendingStopSessions() == setOf("session-b")) { "Stop confirmation cleared the wrong session" }
                check(reopened.scoreDisplay() == "ABC  1  |  3  XYZ") { "Stop confirmation changed score" }
            }
            result.putString("result", "PASS: publisher gate, diagnostic overlay and durable safety state")
            finish(Activity.RESULT_OK, result)
        } catch (failure: Throwable) {
            result.putString("result", "FAIL: ${failure.javaClass.simpleName}: ${failure.message}")
            finish(Activity.RESULT_CANCELED, result)
        } finally {
            targetContext.deleteDatabase(name)
        }
    }
}