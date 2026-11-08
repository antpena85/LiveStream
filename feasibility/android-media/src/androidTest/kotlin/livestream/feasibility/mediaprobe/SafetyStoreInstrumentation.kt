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
            AndroidSafetyStore(targetContext, name).use { it.recordPointWithPendingStop("session-a") }
            AndroidSafetyStore(targetContext, name).use { reopened ->
                val view = reopened.safetyView()
                check(view.snapshot == ProbeSnapshot(1, 1, 0)) { "Incorrect reopened snapshot" }
                check(view.pendingStops == setOf("session-a")) { "Missing reopened pending stop" }
                val start = StreamSession(reopened).requestStart(true, true, true, true, view.snapshot)
                check(start == StartResult.PendingStop) { "Pending stop did not block Start" }
            }
            result.putString("result", "PASS: point and pending stop survive reopening and block Start")
            finish(Activity.RESULT_OK, result)
        } catch (failure: Throwable) {
            result.putString("result", "FAIL: ${failure.javaClass.simpleName}: ${failure.message}")
            finish(Activity.RESULT_CANCELED, result)
        } finally {
            targetContext.deleteDatabase(name)
        }
    }
}