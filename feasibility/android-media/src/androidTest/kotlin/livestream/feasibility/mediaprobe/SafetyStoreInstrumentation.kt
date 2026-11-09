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
                val lifecycle = DiagnosticPublisherLifecycle(store)
                check(lifecycle.claim("retry-stop", store.snapshot()))
                var attempts = 0
                lifecycle.requestStop("retry-stop", true) { attempts++; throw IllegalStateException("stop failed") }
                lifecycle.requestStop("retry-stop", true) { attempts++ }
                check(attempts == 2 && lifecycle.status() == DiagnosticPublisherStatus.Stopping) { "Failed publisher stop could not be retried" }
                check(store.pendingStopSessions() == setOf("retry-stop")) { "Retry cleared unresolved stop" }
            }
            targetContext.deleteDatabase(name)
            run {
                val snapshot = AndroidSafetyStore(targetContext, name).use { it.snapshot() }
                val store = AndroidSafetyStore(targetContext, "/dev/null/unavailable.db")
                val lifecycle = DiagnosticPublisherLifecycle(store)
                var connected = false
                val accepted = lifecycle.startDiagnostic("closed-store", "rtmps://example.test/live", "fictional-key", true, true, true, true, snapshot, stop = {}) { connected = true }
                check(!accepted && !connected && lifecycle.status() == DiagnosticPublisherStatus.Stopped) { "Unavailable safety store allowed start" }
            }
            targetContext.deleteDatabase(name)
            AndroidSafetyStore(targetContext, name).use { store ->
                check(store.claimPublisherSession("prior-session", store.snapshot()))
                var connected = false
                val lifecycle = DiagnosticPublisherLifecycle(store)
                val accepted = lifecycle.startDiagnostic("next-session", "rtmps://example.test/live", "fictional-key", true, true, true, true, store.snapshot(), stop = {}) { connected = true }
                check(!accepted && !connected && lifecycle.status() == DiagnosticPublisherStatus.Stopped) { "Unresolved prior session allowed connection" }
                check(store.pendingStopSessions() == setOf("prior-session")) { "Prior session claim changed" }
            }
            targetContext.deleteDatabase(name)
            AndroidSafetyStore(targetContext, name).use { store ->
                val lifecycle = DiagnosticPublisherLifecycle(store)
                var endpoint = ""
                var claimedBeforeConnect = false
                val accepted = lifecycle.startDiagnostic("manual-session", "rtmps://example.test/live", "fictional-key", true, true, true, true, store.snapshot(), stop = {}) {
                    endpoint = it
                    claimedBeforeConnect = store.pendingStopSessions() == setOf("manual-session")
                }
                check(accepted && claimedBeforeConnect && endpoint == "rtmps://example.test/live/fictional-key") { "Manual start did not durably claim before connecting" }
                check(lifecycle.status() == DiagnosticPublisherStatus.Connecting) { "Manual start did not enter Connecting" }
            }
            targetContext.deleteDatabase(name)
            AndroidSafetyStore(targetContext, name).use { store ->
                val lifecycle = DiagnosticPublisherLifecycle(store)
                var stops = 0
                check(lifecycle.claim("late-connect", store.snapshot()))
                lifecycle.requestStop("late-connect", true) { stops++ }
                lifecycle.connected("late-connect") { stops++ }
                check(stops == 2 && lifecycle.status() == DiagnosticPublisherStatus.Stopping) { "Late connection was not stopped again" }
                check(store.pendingStopSessions() == setOf("late-connect")) { "Late connection cleared the claim" }
            }
            targetContext.deleteDatabase(name)
            AndroidSafetyStore(targetContext, name).use { store ->
                val lifecycle = DiagnosticPublisherLifecycle(store)
                var stopped = false
                check(lifecycle.claim("async-failure", store.snapshot()))
                lifecycle.failed("async-failure") { stopped = true }
                check(stopped && lifecycle.status() == DiagnosticPublisherStatus.Stopping) { "Async failure did not request cleanup" }
                check(store.pendingStopSessions() == setOf("async-failure")) { "Async failure cleared the claim" }
            }
            targetContext.deleteDatabase(name)
            AndroidSafetyStore(targetContext, name).use { store ->
                val lifecycle = DiagnosticPublisherLifecycle(store)
                var stopped = false
                check(lifecycle.claim("stale-callback", store.snapshot()))
                store.scorePoint(left = true)
                lifecycle.connected("stale-callback") { stopped = true }
                check(stopped && lifecycle.status() == DiagnosticPublisherStatus.Stopping) { "Unsafe connection did not request cleanup" }
                check(store.pendingStopSessions() == setOf("stale-callback")) { "Unsafe callback cleared the claim" }
            }
            targetContext.deleteDatabase(name)
            var oldEvents = 0
            var newEvents = 0
            val oldCallbacks = PublisherCallbacks(onSuccess = { oldEvents++ }, onFailure = {})
            val newCallbacks = PublisherCallbacks(onSuccess = { newEvents++ }, onFailure = {})
            oldCallbacks.onConnectionSuccess()
            check(oldEvents == 1 && newEvents == 0) { "Old callback reached a newer publisher" }
            newCallbacks.onConnectionSuccess()
            check(oldEvents == 1 && newEvents == 1) { "New publisher callback not delivered" }
            AndroidSafetyStore(targetContext, name).use { store ->
                var stopped = false
                val lifecycle = DiagnosticPublisherLifecycle(store)
                val accepted = lifecycle.claimAndConnect("partial-failure", store.snapshot(), stop = { stopped = true }) {
                    throw IllegalStateException("fictional sensitive endpoint")
                }
                check(!accepted && stopped && lifecycle.status() == DiagnosticPublisherStatus.Stopping) { "Partial connection was not stopped" }
                check(store.pendingStopSessions() == setOf("partial-failure")) { "Unconfirmed stop lost its marker" }
            }
            AndroidSafetyStore(targetContext, name).use { it.confirmStopped("partial-failure") }
            AndroidSafetyStore(targetContext, name).use { store ->
                var connected = false
                val lifecycle = DiagnosticPublisherLifecycle(store)
                check(lifecycle.claimAndConnect("session-integrated", store.snapshot(), stop = {}) {
                    check(store.pendingStopSessions() == setOf("session-integrated")) { "Publisher called before durable claim" }
                    connected = true
                }) { "Integrated claim was rejected" }
                check(connected) { "Publisher was not called" }
                var unexpectedStop = false
                lifecycle.connected("session-integrated") { unexpectedStop = true }
                check(!unexpectedStop && lifecycle.status() == DiagnosticPublisherStatus.Live) { "Matching callback did not transition to live" }
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
                var unexpectedStop = false
                lifecycle.connected("old-session") { unexpectedStop = true }
                check(!unexpectedStop && lifecycle.status() == DiagnosticPublisherStatus.Connecting) { "Old callback changed the current session" }
                lifecycle.connected("owned-session") { unexpectedStop = true }
                check(!unexpectedStop && lifecycle.status() == DiagnosticPublisherStatus.Live) { "Own claim was treated as an old stop" }
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
                check(it.scoreDisplay() == "> ABC 1 S0 T/O2 | XYZ 2 S0 T/O2") { "Diagnostic scoreboard did not survive reopening" }
                val assessment = AndroidPublisherAdapter(it, connect = {}, stop = {}).diagnosticAssessment()
                check(assessment.display == "> ABC 1 S0 T/O2 | XYZ 2 S0 T/O2" && assessment.snapshot == ProbeSnapshot(3, 3, 0) && assessment.fits) {
                    "Fit assessment did not identify the score and revision assessed"
                }
                check(assessment.display.contains("T/O") && assessment.display.contains("S0") && assessment.display.contains(">")) {
                    "Diagnostic projection omitted timeouts, sets or serving indicator"
                }
            }
            AndroidSafetyStore(targetContext, name).use { it.scorePointWithPendingStop(left = false, sessionId = "session-a") }
            AndroidSafetyStore(targetContext, name).use { reopened ->
                check(reopened.scoreDisplay() == "> ABC 1 S0 T/O2 | XYZ 3 S0 T/O2") { "Updated score missing after pending stop" }
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
                check(reopened.scoreDisplay() == "> ABC 1 S0 T/O2 | XYZ 3 S0 T/O2") { "Stop confirmation changed score" }
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