package livestream.feasibility.mediaprobe

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMetadataRetriever
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.SurfaceView
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.VideoView
import com.pedro.common.ConnectChecker
import com.pedro.encoder.input.gl.render.filters.`object`.ImageFilterRender
import com.pedro.encoder.input.sources.audio.MicrophoneSource
import com.pedro.encoder.input.sources.video.Camera2Source
import com.pedro.encoder.utils.CodecUtil
import com.pedro.library.base.recording.RecordController
import com.pedro.library.generic.GenericStream
import livestream.feasibility.StartResult
import livestream.feasibility.StreamSession
import org.json.JSONArray
import org.json.JSONObject

/** Operator-initiated local M2 experiment. This activity never starts a network stream. */
class MediaActivity : Activity() {
    private val handler = Handler(Looper.getMainLooper())
    private val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    private var pendingCapture = false
    private var transitionTest = false
    private var capture: GenericStream? = null
    private var report = JSONObject()
    private var decodedFrame: Bitmap? = null
    private lateinit var preview: SurfaceView
    private lateinit var status: TextView
    private lateinit var runButton: Button
    private lateinit var transitionButton: Button
    private val clip get() = filesDir.resolve("m2-capture.mp4")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 48, 32, 24)
        }
        status = TextView(this).apply {
            text = "Local M2 test; no broadcast. Transition test: speak/count throughout. Mute at 4s, unmute at 8s, front at 12s, rear at 16s; stop at 20s. Each run replaces the private clip."
        }
        content.addView(status)
        val controls = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        runButton = Button(this).apply {
            text = "Run 10-second camera/audio test"
            setOnClickListener {
                transitionTest = false
                if (permissions.any { checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED }) {
                    pendingCapture = true
                    requestPermissions(permissions, 1)
                } else startCapture()
            }
        }
        controls.addView(runButton)
        controls.addView(Button(this).apply {
            text = "Stop"
            setOnClickListener { stopCapture("operator_stopped") }
        })
        controls.addView(Button(this).apply {
            text = "Review clip"
            setOnClickListener {
                if (capture == null && clip.exists()) {
                    val player = VideoView(this@MediaActivity)
                    val dialog = AlertDialog.Builder(this@MediaActivity).setView(player)
                        .setPositiveButton("Close", null).create()
                    dialog.setOnDismissListener { player.stopPlayback() }
                    dialog.show()
                    player.setVideoPath(clip.absolutePath)
                    player.setOnPreparedListener { player.start() }
                }
            }
        })
        controls.addView(Button(this).apply {
            text = "Decoded frame"
            setOnClickListener {
                decodedFrame?.let { frame ->
                    val image = ImageView(this@MediaActivity).apply { setImageBitmap(frame); adjustViewBounds = true }
                    AlertDialog.Builder(this@MediaActivity).setTitle("Decoded encoded frame")
                        .setView(image).setPositiveButton("Close", null).show()
                }
            }
        })
        content.addView(controls)
        transitionButton = Button(this).apply {
            text = "Run 20-second switch/mute test"
            setOnClickListener {
                transitionTest = true
                if (permissions.any { checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED }) {
                    pendingCapture = true
                    requestPermissions(permissions, 1)
                } else startCapture()
            }
        }
        content.addView(transitionButton)
        preview = SurfaceView(this)
        content.addView(preview, LinearLayout.LayoutParams(-1, 0, 1f))
        setContentView(content)
    }

    override fun onRequestPermissionsResult(requestCode: Int, requested: Array<out String>, results: IntArray) {
        super.onRequestPermissionsResult(requestCode, requested, results)
        if (pendingCapture) { pendingCapture = false; startCapture() }
    }

    private fun startCapture() {
        if (capture != null) return
        val gate = StreamSession().requestStart(true,
            checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED,
            checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
        if (gate != StartResult.Accepted) { status.text = "Capture did not start: $gate"; return }
        if (!preview.holder.surface.isValid) { status.text = "Preview surface is not ready; try again."; return }
        runButton.isEnabled = false
        transitionButton.isEnabled = false
        decodedFrame?.recycle()
        decodedFrame = null
        report = JSONObject().put("probeVersion", "m2-media-1").put("model", Build.MODEL)
            .put("buildId", Build.ID).put("targetWidth", 1280).put("targetHeight", 720)
            .put("targetFps", 30).put("requestedVideoBitrate", 2_000_000)
            .put("requestedAudioSampleRate", 48000).put("requestedAudioBitrate", 128000)
            .put("requestedVideoCodecMode", "hardware").put("publishing", "not_run")
            .put("scoreboardFixture", "ABC 99 S2 T/O2 serve; XYZ 100 S1 T/O1")
            .put("experiment", if (transitionTest) "front_rear_mute_20s" else "baseline_10s")
            .put("controlEvents", JSONArray())
        try {
            if (clip.exists()) check(clip.delete())
            val camera = Camera2Source(this)
            val microphone = MicrophoneSource(MediaRecorder.AudioSource.MIC)
            val stream = GenericStream(this, object : ConnectChecker {
                override fun onConnectionStarted(url: String) {}
                override fun onConnectionSuccess() {}
                override fun onConnectionFailed(reason: String) {}
                override fun onDisconnect() {}
                override fun onAuthError() {}
                override fun onAuthSuccess() {}
            }, camera, microphone)
            capture = stream
            stream.forceCodecType(CodecUtil.CodecType.HARDWARE, CodecUtil.CodecType.SOFTWARE)
            check(stream.prepareVideo(1280, 720, 2_000_000, fps = 30, rotation = 0)) { "Video preparation failed" }
            check(stream.prepareAudio(48000, true, 128000)) { "Audio preparation failed" }
            val audio = getSystemService(AudioManager::class.java)
            val builtin = audio.getDevices(AudioManager.GET_DEVICES_INPUTS).firstOrNull { it.type == AudioDeviceInfo.TYPE_BUILTIN_MIC }
            report.put("builtinPreferenceAccepted", builtin != null && microphone.setPreferredDevice(builtin))
            val filter = ImageFilterRender().apply {
                setImage(scoreboard())
                setScale(40f, 18f)
                setPosition(5f, 5f)
            }
            stream.getGlInterface().addFilter(filter)
            stream.startPreview(preview)
            report.put("startedElapsedMs", SystemClock.elapsedRealtime())
            stream.startRecord(clip.absolutePath, listener = object : RecordController.Listener {
                override fun onStatusChange(recordStatus: RecordController.Status) {
                    runOnUiThread { status.text = "Capture: $recordStatus. Speak; the test stops automatically." }
                }
                override fun onError(e: Exception?) {
                    runOnUiThread { report.put("recordError", e?.javaClass?.simpleName ?: "unknown"); stopCapture("record_error") }
                }
            })
            if (transitionTest) {
                scheduleControl(4000, "mute") { microphone.mute(); "muted=${microphone.isMuted()}" }
                scheduleControl(8000, "unmute") { microphone.unMute(); "muted=${microphone.isMuted()}" }
                scheduleControl(12000, "switch_front") { camera.switchCamera(); "cameraId=${camera.getCurrentCameraId()}" }
                scheduleControl(16000, "switch_rear") { camera.switchCamera(); "cameraId=${camera.getCurrentCameraId()}" }
            }
            handler.postDelayed({
                if (capture != null) {
                    report.put("logicalCameraId", camera.getCurrentCameraId())
                    try { report.put("observedAudioInputTypes", JSONArray(audio.activeRecordingConfigurations.mapNotNull { it.audioDevice?.type })) }
                    catch (failure: Exception) { report.put("audioRouteError", failure.javaClass.simpleName) }
                }
            }, 2500)
            val duration = if (transitionTest) 20000L else 10000L
            handler.postDelayed({ stopCapture(if (transitionTest) "twenty_second_timer" else "ten_second_timer") }, duration)
        } catch (failure: Exception) {
            report.put("setupError", failure.javaClass.simpleName)
            if (capture != null) stopCapture("setup_error")
            else { runButton.isEnabled = true; transitionButton.isEnabled = true; saveReport(); status.text = "Test setup failed: ${failure.javaClass.simpleName}" }
        }
    }

    private fun scheduleControl(delayMs: Long, action: String, operation: () -> String) {
        handler.postDelayed({
            if (capture != null) {
                val event = JSONObject().put("action", action)
                    .put("elapsedMs", SystemClock.elapsedRealtime() - report.getLong("startedElapsedMs"))
                try {
                    event.put("apiState", operation())
                    status.text = "$action requested. Keep speaking/counting; review captured output afterward."
                } catch (failure: Exception) {
                    event.put("error", failure.javaClass.simpleName)
                    report.put("controlError", true)
                }
                report.getJSONArray("controlEvents").put(event)
            }
        }, delayMs)
    }

    private fun stopCapture(reason: String) {
        val stream = capture ?: return
        capture = null
        handler.removeCallbacksAndMessages(null)
        report.put("stopReason", reason).put("stoppedElapsedMs", SystemClock.elapsedRealtime())
        try { if (stream.isRecording) stream.stopRecord() }
        catch (failure: Exception) { report.put("stopError", failure.javaClass.simpleName) }
        try { stream.release() }
        catch (failure: Exception) { report.put("releaseError", failure.javaClass.simpleName) }
        status.text = "Inspecting the encoded clip…"
        Thread {
            try { inspectClip() }
            catch (failure: Exception) { report.put("inspectionError", failure.javaClass.simpleName) }
            saveReport()
            runOnUiThread {
                runButton.isEnabled = true
                transitionButton.isEnabled = true
                val failed = listOf("setupError", "recordError", "stopError", "releaseError", "inspectionError", "controlError").any { report.has(it) }
                status.text = if (failed) "Test did not complete successfully ($reason). Diagnostic metadata was saved."
                    else "Test finished ($reason). Review the clip and decoded frame for complete scoreboard values and audible audio."
            }
        }.start()
    }

    private fun inspectClip() {
        report.put("fileBytes", clip.length())
        val tracks = JSONArray()
        val extractor = MediaExtractor()
        try {
            extractor.setDataSource(clip.absolutePath)
            for (index in 0 until extractor.trackCount) {
                val format = extractor.getTrackFormat(index)
                val track = JSONObject().put("mime", format.getString(MediaFormat.KEY_MIME))
                for (key in listOf(MediaFormat.KEY_WIDTH, MediaFormat.KEY_HEIGHT, MediaFormat.KEY_SAMPLE_RATE, MediaFormat.KEY_CHANNEL_COUNT)) {
                    if (format.containsKey(key)) track.put(key, format.getInteger(key))
                }
                extractor.selectTrack(index)
                extractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC)
                var samples = 0
                var bytes = 0L
                var first = -1L
                var last = -1L
                var maximumGap = 0L
                while (extractor.sampleTime >= 0) {
                    val time = extractor.sampleTime
                    if (first < 0) first = time
                    if (last >= 0) maximumGap = maxOf(maximumGap, time - last)
                    last = time
                    bytes += extractor.sampleSize
                    samples++
                    if (!extractor.advance()) break
                }
                track.put("samples", samples).put("encodedBytes", bytes).put("firstTimeUs", first)
                    .put("lastTimeUs", last).put("maximumSampleGapUs", maximumGap)
                if (last > first) track.put("sampleRatePerSecond", (samples - 1) * 1_000_000.0 / (last - first))
                tracks.put(track)
                extractor.unselectTrack(index)
            }
        } finally { extractor.release() }
        report.put("tracks", tracks)
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(clip.absolutePath)
            decodedFrame = retriever.getFrameAtTime(5_000_000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            report.put("decodedFrameAvailable", decodedFrame != null)
        } finally { retriever.release() }
    }

    private fun saveReport() { filesDir.resolve("media-report.json").writeText(report.toString(2)) }

    private fun scoreboard(): Bitmap = Bitmap.createBitmap(480, 128, Bitmap.Config.ARGB_8888).apply {
        val canvas = Canvas(this)
        canvas.drawColor(Color.argb(235, 0, 0, 0))
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.WHITE; textSize = 32f; typeface = Typeface.MONOSPACE }
        canvas.drawText("> ABC  99  S2  T/O2", 16f, 48f, paint)
        canvas.drawText("  XYZ 100  S1  T/O1", 16f, 104f, paint)
    }

    override fun onStop() {
        pendingCapture = false
        stopCapture("activity_stopped")
        super.onStop()
    }
}
