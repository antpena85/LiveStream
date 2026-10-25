package livestream.feasibility.watchprobe

import android.app.Activity
import android.app.KeyguardManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.os.SystemClock
import android.util.AtomicFile
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import livestream.feasibility.PendingProbe
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

/** Foreground-only transport diagnostic; no production command authorization. */
class LinkActivity : Activity(), MessageClient.OnMessageReceivedListener {
    private val handler = Handler(Looper.getMainLooper())
    private var events = JSONArray()
    private val activityInstance = UUID.randomUUID().toString()
    private var lastRequestResult = "No local request in this activity"
    private lateinit var status: TextView
    private lateinit var requestResult: TextView
    private lateinit var send: Button
    private var active = false
    private var ready = false
    private var pending: PendingProbe? = null
    private var started = 0L
    private val messages by lazy { Wearable.getMessageClient(this) }
    private val nodes by lazy { Wearable.getNodeClient(this) }
    private val timeout = Runnable {
        if (pending != null) { pending = null; note("ack_timeout"); send.isEnabled = ready }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 32, 28, 32)
        }
        status = TextView(this).apply { text = "M2 link check. Open this app on phone and watch. No match/media controls." }
        content.addView(status)
        requestResult = TextView(this).apply { text = lastRequestResult }
        content.addView(requestResult)
        send = Button(this).apply {
            text = "Send ping"
            isEnabled = false
            setOnClickListener { discoverAndSend() }
        }
        content.addView(send)
        setContentView(ScrollView(this).apply { addView(content) })
        val saved = lifecycleReport()
        try {
            if (saved.baseFile.exists() || filesDir.resolve("link-lifecycle-report.json.bak").exists()) {
                val previous = JSONObject(saved.readFully().toString(Charsets.UTF_8))
                if (previous.optInt("diagnosticRevision") == 2) events = previous.getJSONArray("events")
            }
        } catch (failure: Exception) {
            note("history_read_failed", failure.javaClass.simpleName)
        }
        note("activity_created")
    }

    override fun onResume() {
        super.onResume()
        active = true
        note("activity_resumed")
        GoogleApiAvailability.getInstance().checkApiAvailability(messages, nodes)
            .addOnSuccessListener {
                if (active) {
                    messages.addListener(this).addOnSuccessListener {
                        if (active) { ready = true; send.isEnabled = true; note("listener_ready") }
                        else messages.removeListener(this)
                    }.addOnFailureListener { note("listener_failed", it.javaClass.simpleName) }
                }
            }.addOnFailureListener { note("api_unavailable", it.javaClass.simpleName) }
    }

    private fun discoverAndSend() {
        send.isEnabled = false
        nodes.connectedNodes.addOnSuccessListener { peers ->
            if (!active) return@addOnSuccessListener
            note("discovery", "count=${peers.size};nearby=${peers.count { it.isNearby }}")
            if (peers.size != 1) { send.isEnabled = ready; return@addOnSuccessListener }
            val peer = peers.single()
            val token = UUID.randomUUID().toString()
            val request = PendingProbe(peer.id, token)
            pending = request
            started = SystemClock.elapsedRealtime()
            note("request_started")
            handler.postDelayed(timeout, 10000)
            messages.sendMessage(peer.id, "/m2/ping", token.toByteArray(Charsets.UTF_8))
                .addOnSuccessListener { note("send_accepted_not_ack") }
                .addOnFailureListener {
                    note("send_failed", it.javaClass.simpleName)
                    if (pending === request) {
                        pending = null; handler.removeCallbacks(timeout); send.isEnabled = ready
                    }
                }
        }.addOnFailureListener { note("discovery_failed", it.javaClass.simpleName); send.isEnabled = ready }
    }

    override fun onMessageReceived(event: MessageEvent) {
        if (event.path == "/m2/ping" || event.path == "/m2/ack") {
            note("message_callback", if (event.path == "/m2/ping") "ping" else "ack")
        }
        if (!active || event.data.size > 128) return
        when (event.path) {
            "/m2/ping" -> {
                note("ping_received")
                messages.sendMessage(event.sourceNodeId, "/m2/ack", event.data)
                    .addOnSuccessListener { note("ack_send_accepted") }
                    .addOnFailureListener { note("ack_send_failed", it.javaClass.simpleName) }
            }
            "/m2/ack" -> {
                if (pending?.acknowledge(event.sourceNodeId, event.data.toString(Charsets.UTF_8)) == true) {
                    note("matched_ack", "roundTripMs=${SystemClock.elapsedRealtime() - started}")
                    pending = null; handler.removeCallbacks(timeout); send.isEnabled = ready
                } else note("unmatched_ack")
            }
        }
    }

    private fun note(kind: String, detail: String = "") {
        val power = getSystemService(PowerManager::class.java)
        val keyguard = getSystemService(KeyguardManager::class.java)
        events.put(JSONObject().put("event", kind).put("detail", detail)
            .put("elapsedMs", SystemClock.elapsedRealtime())
            .put("wallTimeMs", System.currentTimeMillis())
            .put("activityInstance", activityInstance).put("activityActive", active)
            .put("interactive", power.isInteractive)
            .put("keyguardLocked", keyguard.isKeyguardLocked)
            .put("deviceLocked", keyguard.isDeviceLocked))
        status.text = "$kind $detail"
        when (kind) {
            "request_started" -> lastRequestResult = "Local request: WAITING for matching acknowledgment"
            "matched_ack" -> lastRequestResult = "Local request: MATCHED ACK $detail"
            "ack_timeout" -> lastRequestResult = "Local request: TIMEOUT (no matching acknowledgment)"
            "send_failed" -> lastRequestResult = "Local request: SEND FAILED"
            "pending_cancelled_on_pause" -> lastRequestResult = "Local request: CANCELLED on pause"
        }
        requestResult.text = lastRequestResult
        val report = JSONObject().put("probeVersion", "m2-watch-1")
            .put("diagnosticRevision", 2)
            .put("model", Build.MODEL).put("buildId", Build.ID)
            .put("transportPath", "not_measured").put("events", events)
        val file = lifecycleReport()
        val stream = file.startWrite()
        try {
            stream.write(report.toString(2).toByteArray(Charsets.UTF_8))
            file.finishWrite(stream)
        } catch (failure: Exception) {
            file.failWrite(stream)
            status.text = "Diagnostic write failed: ${failure.javaClass.simpleName}"
        }
    }

    private fun lifecycleReport() = AtomicFile(filesDir.resolve("link-lifecycle-report.json"))

    override fun onPause() {
        active = false; ready = false
        note("activity_paused")
        messages.removeListener(this)
            .addOnSuccessListener { note("listener_remove_completed", "removed=$it") }
            .addOnFailureListener { note("listener_remove_failed", it.javaClass.simpleName) }
        handler.removeCallbacks(timeout)
        if (pending != null) note("pending_cancelled_on_pause")
        pending = null; send.isEnabled = false
        super.onPause()
    }

    override fun onStop() {
        note("activity_stopped")
        super.onStop()
    }

    override fun onDestroy() {
        note("activity_destroyed")
        super.onDestroy()
    }
}