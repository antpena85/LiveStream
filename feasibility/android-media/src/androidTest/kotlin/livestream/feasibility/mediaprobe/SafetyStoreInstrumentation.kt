package livestream.feasibility.mediaprobe

import android.app.Activity
import android.app.Instrumentation
import android.os.Bundle
import livestream.feasibility.ProbeSnapshot
import livestream.feasibility.StartResult
import livestream.feasibility.StreamSession

class SafetyStoreInstrumentation : Instrumentation() {
    override fun newActivity(cl: ClassLoader, className: String, intent: android.content.Intent): Activity {
        val activity = super.newActivity(cl, className, intent)
        if (activity is MediaActivity) activity.diagnosticDatabaseName = "off-air-ui-instrumentation.db"
        return activity
    }
    override fun onCreate(arguments: Bundle?) { super.onCreate(arguments); start() }

    override fun onStart() {
        val name = "safety-instrumentation-test.db"
        targetContext.deleteDatabase(name)
        targetContext.deleteDatabase("off-air-ui-instrumentation.db")
        val result = Bundle()
        try {
            AndroidSafetyStore(targetContext, "off-air-ui-instrumentation.db").use { store ->
                check(store.claimPublisherSession("previous-session", store.snapshot()))
            }
            val activity = startActivitySync(android.content.Intent(targetContext, MediaActivity::class.java).addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK))
            try {
                var uiFailure: Throwable? = null
                runOnMainSync {
                    try {
                    fun findText(view: android.view.View, text: String): android.widget.TextView? {
                        if (view is android.widget.TextView && view.text.toString() == text) return view
                        if (view is android.view.ViewGroup) for (index in 0 until view.childCount) {
                            findText(view.getChildAt(index), text)?.let { return it }
                        }
                        return null
                    }
                    val root = activity.window.decorView
                    check(findText(root, "Previous publisher session is unresolved. Publishing remains blocked; shutdown is not confirmed.") != null) {
                        "Unresolved publisher session was not shown on reopening"
                    }
                    check(findText(root, "Publisher: stop unconfirmed") != null) { "Publisher state was not visible" }
                    check(findText(root, "Prepare publisher preview") != null) { "Publisher preview preparation is missing" }
                    val start = findText(root, "Manual publisher start")
                    check(start != null) { "Manual publisher control is missing" }
                    start.performClick()
                    check(findText(root, "Start a local camera/audio test before checking publisher start. No broadcast was started.") != null) {
                        "Publisher control did not explain why start is unavailable"
                    }
                    (findText(root, "+ ABC") ?: error("Score control missing")).performClick()
                    check(findText(root, "Off-air diagnostic score: > ABC 1 S0 T/O2 | XYZ 0 S0 T/O2") != null) {
                        "Off-air scoring did not show the accepted score"
                    }
                    (findText(root, "Stop") ?: error("Stop control missing")).performClick()
                    } catch (failure: Throwable) { uiFailure = failure }
                }
                uiFailure?.let { throw it }
                waitForIdleSync()
                val deadline = android.os.SystemClock.uptimeMillis() + 3000
                var confirmationVisible = false
                while (android.os.SystemClock.uptimeMillis() < deadline && !confirmationVisible) {
                    confirmationVisible = uiAutomation.rootInActiveWindow?.findAccessibilityNodeInfosByText("Confirm stop")?.isNotEmpty() == true
                    if (!confirmationVisible) android.os.SystemClock.sleep(100)
                }
                check(confirmationVisible) { "Stop did not ask for confirmation" }
            } finally { activity.finish() }
            val packageInfo = targetContext.packageManager.getPackageInfo(targetContext.packageName, android.content.pm.PackageManager.GET_PERMISSIONS)
            check(packageInfo.requestedPermissions?.contains(android.Manifest.permission.INTERNET) == true) { "Publisher APK cannot access the network" }
            AndroidSafetyStore(targetContext, name).use { store ->
                val lifecycle = DiagnosticPublisherLifecycle(store)
                check(lifecycle.claim("disconnect-event", store.snapshot()))
                var stops = 0
                val callbacks = lifecycle.callbacksFor("disconnect-event") { stops++ }
                lifecycle.requestStop("disconnect-event", true) { stops++ }
                callbacks.onDisconnect()
                check(stops == 1 && lifecycle.status() == DiagnosticPublisherStatus.Stopping) { "Disconnect incorrectly dispatched another stop" }
                check(store.pendingStopSessions() == setOf("disconnect-event")) { "Unverified disconnect cleared the durable claim" }
            }
            targetContext.deleteDatabase(name)
            AndroidSafetyStore(targetContext, name).use { store ->
                val lifecycle = DiagnosticPublisherLifecycle(store)
                check(lifecycle.claim("reentrant-stop", store.snapshot()))
                var stops = 0
                lateinit var callbacks: PublisherCallbacks
                val stop = {
                    stops++
                    if (stops == 1) callbacks.onDisconnect()
                }
                callbacks = lifecycle.callbacksFor("reentrant-stop", stop)
                lifecycle.requestStop("reentrant-stop", true, stop)
                check(stops == 1 && lifecycle.status() == DiagnosticPublisherStatus.Stopping) { "Disconnect during stop dispatched a reentrant stop" }
                check(store.pendingStopSessions() == setOf("reentrant-stop")) { "Disconnect cleared unresolved stop" }
            }
            targetContext.deleteDatabase(name)
            AndroidSafetyStore(targetContext, name).use { store ->
                check(store.claimPublisherSession("prior-session", store.snapshot()))
            }
            AndroidSafetyStore(targetContext, name).use { store ->
                val adapter = AndroidPublisherAdapter(store, connect = {}, stop = {}, lifecycle = DiagnosticPublisherLifecycle(store), sessionId = "new-session")
                check(adapter.diagnosticStatus() == DiagnosticPublisherStatus.Stopping) { "Unresolved prior session was reported stopped" }
            }
            targetContext.deleteDatabase(name)
            AndroidSafetyStore(targetContext, name).use { store ->
                val lifecycle = DiagnosticPublisherLifecycle(store)
                var stops = 0
                val adapter = AndroidPublisherAdapter(store, connect = {}, stop = { stops++ }, lifecycle = lifecycle, sessionId = "score-change")
                check(adapter.start("rtmps://example.test/live", "fictional-key", true, true, true) == StartResult.Accepted)
                adapter.scorePoint(true)
                check(store.scoreDisplay() == "> ABC 1 S0 T/O2 | XYZ 0 S0 T/O2") { "Score change was not saved" }
                check(stops == 1 && adapter.diagnosticStatus() == DiagnosticPublisherStatus.Stopping) { "Score change while connecting did not stop the stale publisher" }
                check(store.pendingStopSessions() == setOf("score-change")) { "Score change lost the unresolved publisher claim" }
            }
            targetContext.deleteDatabase(name)
            AndroidSafetyStore(targetContext, name).use { store ->
                val lifecycle = DiagnosticPublisherLifecycle(store)
                var endpoint = ""
                var stops = 0
                val adapter = AndroidPublisherAdapter(store, connect = { endpoint = it }, stop = { stops++ }, lifecycle = lifecycle, sessionId = "manual-unified")
                check(adapter.start("rtmps://example.test/live", "fictional-key", true, true, true) == StartResult.Accepted) { "Manual start did not use the durable session" }
                check(endpoint == "rtmps://example.test/live/fictional-key" && store.pendingStopSessions() == setOf("manual-unified")) { "Manual start did not dispatch with a durable claim" }
                adapter.connectionSucceeded()
                check(adapter.diagnosticStatus() == DiagnosticPublisherStatus.Live) { "Manual session did not report live" }
                adapter.requestStop(true)
                check(stops == 1 && adapter.diagnosticStatus() == DiagnosticPublisherStatus.Stopping && store.pendingStopSessions() == setOf("manual-unified")) { "Manual stop lost the unresolved claim" }
            }
            targetContext.deleteDatabase(name)
            AndroidSafetyStore(targetContext, name).use { store ->
                val lifecycle = DiagnosticPublisherLifecycle(store)
                var endpoint = ""
                val adapter = AndroidPublisherAdapter(store, connect = { endpoint = it }, stop = {}, lifecycle = lifecycle, sessionId = "integrated-start")
                val started = adapter.startDiagnostic("rtmps://example.test/live", "fictional-key", true, true, true)
                check(started && endpoint == "rtmps://example.test/live/fictional-key") { "Diagnostic adapter did not dispatch manual start" }
                check(store.pendingStopSessions() == setOf("integrated-start")) { "Diagnostic adapter did not claim session" }
                adapter.connectionSucceeded()
                check(adapter.diagnosticStatus() == DiagnosticPublisherStatus.Live) { "Started session did not become live" }
            }
            targetContext.deleteDatabase(name)
            AndroidSafetyStore(targetContext, name).use { store ->
                val lifecycle = DiagnosticPublisherLifecycle(store)
                var connects = 0
                val adapter = AndroidPublisherAdapter(store, connect = { connects++ }, stop = {}, lifecycle = lifecycle, sessionId = "guarded-adapter")
                check(!adapter.startDiagnostic("rtmps://example.test/live", "fictional-key", false, true, true))
                check(!adapter.startDiagnostic("rtmps://example.test/live", "fictional-key", true, false, true))
                check(!adapter.startDiagnostic("rtmps://example.test/live", "fictional-key", true, true, false))
                check(!adapter.startDiagnostic("invalid", "fictional-key", true, true, true))
                check(connects == 0 && store.pendingStopSessions().isEmpty() && adapter.diagnosticStatus() == DiagnosticPublisherStatus.Stopped) { "Denied start changed publisher state" }
                check(adapter.startDiagnostic("rtmps://example.test/live", "fictional-key", true, true, true))
                check(!adapter.startDiagnostic("rtmps://example.test/live", "fictional-key", true, true, true))
                check(connects == 1 && store.pendingStopSessions() == setOf("guarded-adapter")) { "Duplicate start dispatched or changed claim" }
                adapter.requestStop(true)
                val replacement = AndroidPublisherAdapter(store, connect = { connects++ }, stop = {}, lifecycle = DiagnosticPublisherLifecycle(store), sessionId = "replacement")
                check(!replacement.startDiagnostic("rtmps://example.test/live", "fictional-key", true, true, true))
                check(connects == 1 && store.pendingStopSessions() == setOf("guarded-adapter")) { "Unresolved stop allowed another publisher" }
            }
            targetContext.deleteDatabase(name)
            AndroidSafetyStore(targetContext, name).use { store ->
                val lifecycle = DiagnosticPublisherLifecycle(store)
                var stops = 0
                val adapter = AndroidPublisherAdapter(store, connect = {}, stop = { stops++ }, lifecycle = lifecycle, sessionId = "adapter-session")
                check(lifecycle.claim("adapter-session", store.snapshot()))
                check(adapter.diagnosticStatus() == DiagnosticPublisherStatus.Connecting) { "Adapter did not expose connecting status" }
                adapter.connectionSucceeded()
                check(lifecycle.status() == DiagnosticPublisherStatus.Live) { "Adapter did not forward success to owned session" }
                check(adapter.start("rtmps://example.test/live", "fictional-key", true, true, true) == StartResult.AlreadyLive) { "Live session was not reported by manual start" }
                adapter.requestStop(true)
                check(stops == 1 && lifecycle.status() == DiagnosticPublisherStatus.Stopping) { "Adapter did not stop owned session" }
                check(store.pendingStopSessions() == setOf("adapter-session")) { "Adapter cleared unresolved session" }
            }
            targetContext.deleteDatabase(name)
            AndroidSafetyStore(targetContext, name).use { store ->
                val lifecycle = DiagnosticPublisherLifecycle(store)
                var stops = 0
                check(lifecycle.claim("callback-session", store.snapshot()))
                val callbacks = lifecycle.callbacksFor("callback-session") { stops++ }
                callbacks.onConnectionSuccess()
                check(lifecycle.status() == DiagnosticPublisherStatus.Live) { "Publisher callback did not mark matching session live" }
                callbacks.onConnectionFailed("fictional error")
                check(stops == 1 && lifecycle.status() == DiagnosticPublisherStatus.Stopping) { "Publisher failure did not request session cleanup" }
                check(store.pendingStopSessions() == setOf("callback-session")) { "Publisher failure cleared claim" }
            }
            targetContext.deleteDatabase(name)
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
                check(!accepted && !connected && lifecycle.status() == DiagnosticPublisherStatus.Stopping) { "Unavailable safety store did not block start or reported a verified stop" }
            }
            targetContext.deleteDatabase(name)
            AndroidSafetyStore(targetContext, name).use { store ->
                check(store.claimPublisherSession("prior-session", store.snapshot()))
                var connected = false
                val lifecycle = DiagnosticPublisherLifecycle(store)
                val accepted = lifecycle.startDiagnostic("next-session", "rtmps://example.test/live", "fictional-key", true, true, true, true, store.snapshot(), stop = {}) { connected = true }
                check(!accepted && !connected && lifecycle.status() == DiagnosticPublisherStatus.Stopping) { "Unresolved prior session allowed connection or reported stopped" }
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
            targetContext.deleteDatabase("off-air-ui-instrumentation.db")
        }
    }
}