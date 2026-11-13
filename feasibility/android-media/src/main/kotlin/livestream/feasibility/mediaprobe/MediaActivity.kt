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
import android.text.InputType
import android.view.SurfaceView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.VideoView
import com.pedro.encoder.input.gl.render.filters.`object`.ImageFilterRender
import com.pedro.encoder.input.sources.audio.MicrophoneSource
import com.pedro.encoder.input.sources.video.Camera2Source
import com.pedro.encoder.utils.CodecUtil
import com.pedro.library.base.recording.RecordController
import com.pedro.library.generic.GenericStream
import livestream.feasibility.LocalCaptureReadiness
import livestream.feasibility.LocalCaptureResult
import livestream.feasibility.ManualDestination
import livestream.feasibility.DestinationResult
import org.json.JSONArray
import org.json.JSONObject

/** Operator-initiated local M2 experiment. This activity never starts a network stream. */
class MediaActivity : Activity() {
    internal var diagnosticDatabaseName = "local-diagnostic-score.db"
    private val handler = Handler(Looper.getMainLooper())
    private val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    private var pendingCapture = false
    private var transitionTest = false
    private var publisherPreview = false
    private var capture: GenericStream? = null
    private var publisher: AndroidPublisherAdapter? = null
    private var scoreFilter: ImageFilterRender? = null
    private lateinit var diagnosticScore: AndroidSafetyStore
    private var report = JSONObject()
    private var decodedFrame: Bitmap? = null
    private var firstScoreChangeMs: Long? = null
    private var multipleScoreChanges = false
    private var scoreReviewDurationMs: Long? = null
    private lateinit var preview: SurfaceView
    private lateinit var status: TextView
    private lateinit var publisherStatus: TextView
    private lateinit var runButton: Button
    private lateinit var transitionButton: Button
    private val clip get() = filesDir.resolve("m2-capture.mp4")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        diagnosticScore = AndroidSafetyStore(this, diagnosticDatabaseName)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(8, 8, 8, 8)
        }
        val sidebar = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        val info = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        val safetyNotice = try {
            if (diagnosticScore.pendingStopSessions().isNotEmpty())
                "Previous publisher session is unresolved. Publishing remains blocked; shutdown is not confirmed."
            else null
        } catch (_: Exception) {
            "Publisher safety state is unavailable. Publishing remains blocked; shutdown is not confirmed."
        }
        if (safetyNotice != null) info.addView(TextView(this).apply { text = safetyNotice })
        publisherStatus = TextView(this)
        info.addView(publisherStatus)
        updatePublisherStatus()
        status = TextView(this).apply {
            text = "Local M2 test; no broadcast. Sets, timeouts and serve are fixed fixture values, not match state. Transition test: speak/count throughout. Mute at 4s, unmute at 8s, front at 12s, rear at 16s; stop at 20s. Each run replaces the private clip."
        }
        info.addView(status)
        sidebar.addView(ScrollView(this).apply { addView(info) }, LinearLayout.LayoutParams(-1, (56 * resources.displayMetrics.density).toInt()))
        val controls = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        controls.addView(Button(this).apply {
            text = "Prepare publisher preview"
            setOnClickListener {
                if (capture == null) {
                    publisherPreview = true
                    transitionTest = false
                    if (permissions.any { checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED }) {
                        pendingCapture = true
                        requestPermissions(permissions, 1)
                    } else startCapture()
                }
            }
        })
        controls.addView(Button(this).apply {
            text = "Manual publisher start"
            setOnClickListener {
                if (publisher == null) status.text = "Start a local camera/audio test before checking publisher start. No broadcast was started."
                else showDestinationCheck(publisherStart = true)
            }
        })
        runButton = Button(this).apply {
            text = "Run 10-second camera/audio test"
            setOnClickListener {
                publisherPreview = false
                transitionTest = false
                if (permissions.any { checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED }) {
                    pendingCapture = true
                    requestPermissions(permissions, 1)
                } else startCapture()
            }
        }
        controls.addView(runButton)
        controls.addView(Button(this).apply {
            text = "Check test destination (no broadcast)"
            setOnClickListener { showDestinationCheck() }
        })
        controls.addView(Button(this).apply {
            text = "Check diagnostic publisher readiness (no broadcast)"
            setOnClickListener {
                status.text = try {
                    val adapter = publisher
                    if (adapter == null) "Run the local camera/audio test first; publishing remains disabled."
                    else if (adapter.diagnosticFit()) "Diagnostic text fits its provisional bitmap; broadcast fit and stop confirmation remain unverified. Publishing disabled."
                    else "Diagnostic text does not fit; publishing disabled."
                } catch (_: Exception) { "Diagnostic safety state unavailable; publishing disabled." }
            }
        })
        val scoreControls = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        scoreControls.addView(Button(this).apply {
            text = "+ ABC"
            setOnClickListener { scorePoint(left = true) }
        })
        scoreControls.addView(Button(this).apply {
            text = "+ XYZ"
            setOnClickListener { scorePoint(left = false) }
        })
        controls.addView(scoreControls)
        controls.addView(Button(this).apply {
            text = "Reset diagnostic score"
            setOnClickListener {
                if (capture != null) {
                    status.text = "Stop the preview or recording before resetting the diagnostic score."
                } else {
                    AlertDialog.Builder(this@MediaActivity).setTitle("Reset diagnostic score?")
                        .setMessage("Reset ABC and XYZ to zero? Unresolved publisher stops and the recorded clip will be retained.")
                        .setPositiveButton("Reset") { _, _ ->
                            if (capture != null) status.text = "Stop the preview or recording before resetting the diagnostic score."
                            else try {
                                diagnosticScore.resetDiagnosticScore(true)
                                status.text = "Off-air diagnostic score: ${diagnosticScore.scoreDisplay()}"
                                updatePublisherStatus()
                            } catch (_: Exception) { status.text = "Diagnostic score reset failed; score state is unavailable." }
                        }.setNegativeButton("Cancel", null).show()
                }
            }
        })
        controls.addView(Button(this).apply {
            text = "Stop"
            setOnClickListener {
                AlertDialog.Builder(this@MediaActivity).setTitle("Confirm stop")
                    .setMessage("Stop the current preview or capture?")
                    .setPositiveButton("Stop") { _, _ -> stopCapture("operator_stopped") }
                    .setNegativeButton("Cancel", null).show()
            }
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
            text = "Review score change frames"
            setOnClickListener { reviewScoreChangeFrames() }
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
        transitionButton = Button(this).apply {
            text = "Run 20-second switch/mute test"
            setOnClickListener {
                publisherPreview = false
                transitionTest = true
                if (permissions.any { checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED }) {
                    pendingCapture = true
                    requestPermissions(permissions, 1)
                } else startCapture()
            }
        }
        controls.addView(transitionButton)
        val grid = android.widget.GridLayout(this).apply { columnCount = 3 }
        val ordered = listOf("Run 10-second camera/audio test", "+ ABC", "+ XYZ", "Stop", "Review clip", "Reset diagnostic score", "Review score change frames", "Prepare publisher preview", "Manual publisher start", "Check test destination (no broadcast)", "Check diagnostic publisher readiness (no broadcast)", "Decoded frame", "Run 20-second switch/mute test")
        val buttons = mutableListOf<Button>()
        fun collectButtons(group: android.view.ViewGroup) {
            for (index in 0 until group.childCount) {
                val child = group.getChildAt(index)
                if (child is Button) buttons.add(child) else if (child is android.view.ViewGroup) collectButtons(child)
            }
        }
        collectButtons(controls)
        ordered.forEachIndexed { index, label ->
            val button = buttons.first { it.text.toString() == label }
            (button.parent as android.view.ViewGroup).removeView(button)
            button.setPadding(4, 0, 4, 0)
            button.textSize = 11f
            button.isAllCaps = false
            val params = android.widget.GridLayout.LayoutParams().apply {
                width = 0
                height = (48 * resources.displayMetrics.density).toInt()
                columnSpec = android.widget.GridLayout.spec(index % 3, 1f)
                rowSpec = android.widget.GridLayout.spec(index / 3)
            }
            grid.addView(button, params)
        }
        sidebar.addView(ScrollView(this).apply { addView(grid) }, LinearLayout.LayoutParams(-1, 0, 1f))
        content.addView(sidebar, LinearLayout.LayoutParams(0, -1, 3f))
        preview = SurfaceView(this)
        content.addView(preview, LinearLayout.LayoutParams(0, -1, 2f))
        setContentView(content)
    }

    private fun showDestinationCheck(publisherStart: Boolean = false) {
        val fields = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        val server = EditText(this).apply {
            hint = "RTMP(S) server address"
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_URI
            isSaveEnabled = false
            importantForAutofill = android.view.View.IMPORTANT_FOR_AUTOFILL_NO
        }
        val key = EditText(this).apply {
            hint = "Stream key"
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            isSaveEnabled = false
            importantForAutofill = android.view.View.IMPORTANT_FOR_AUTOFILL_NO
        }
        fields.addView(server)
        fields.addView(key)
        val dialog = AlertDialog.Builder(this).setTitle(if (publisherStart) "Confirm publisher start" else "Local format check only")
            .setMessage(if (publisherStart) "Publisher start remains blocked until broadcast fit and stop safeguards are integrated." else "This does not connect, verify the destination, or start a stream.")
            .setView(fields).setPositiveButton(if (publisherStart) "Confirm start" else "Check") { _, _ ->
                status.text = if (publisherStart) {
                    val adapter = publisher
                    if (adapter == null) "Publisher is unavailable. No broadcast was started."
                    else "Publisher start result: ${adapter.start(server.text.toString(), key.text.toString(), true, checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED, checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)}. Broadcast safeguards remain incomplete."
                } else when (ManualDestination.validate(server.text.toString(), key.text.toString())) {
                    DestinationResult.Ready -> "Basic format accepted; destination not verified and publishing disabled."
                    DestinationResult.InvalidServer -> "Invalid server address; use an RTMP(S) address with a host."
                    DestinationResult.MissingKey -> "Enter a stream key."
                }
                updatePublisherStatus()
            }.setNegativeButton("Cancel", null).create()
        dialog.window?.setFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE, android.view.WindowManager.LayoutParams.FLAG_SECURE)
        dialog.setOnDismissListener { server.text.clear(); key.text.clear() }
        dialog.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, requested: Array<out String>, results: IntArray) {
        super.onRequestPermissionsResult(requestCode, requested, results)
        if (pendingCapture) { pendingCapture = false; startCapture() }
    }

    private fun startCapture() {
        if (capture != null) return
        val gate = LocalCaptureReadiness().request(true,
            checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED,
            checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
        if (gate != LocalCaptureResult.Ready) { status.text = "Capture did not start: $gate"; return }
        if (!preview.holder.surface.isValid) { status.text = "Preview surface is not ready; try again."; return }
        runButton.isEnabled = false
        transitionButton.isEnabled = false
        decodedFrame?.recycle()
        decodedFrame = null
        firstScoreChangeMs = null
        multipleScoreChanges = false
        scoreReviewDurationMs = null
        report = JSONObject().put("probeVersion", "m2-media-1").put("model", Build.MODEL)
            .put("buildId", Build.ID).put("targetWidth", 1280).put("targetHeight", 720)
            .put("targetFps", 30).put("requestedVideoBitrate", 2_000_000)
            .put("requestedAudioSampleRate", 48000).put("requestedAudioBitrate", 128000)
            .put("requestedVideoCodecMode", "hardware").put("publishing", "not_run")
            .put("scoreboardFixture", if (transitionTest) "ABC 99 S2 T/O2 serve; XYZ 100 S1 T/O1" else "local score controls; ABC and XYZ")
            .put("experiment", if (transitionTest) "front_rear_mute_20s" else "baseline_10s")
            .put("controlEvents", JSONArray())
        try {
            if (!publisherPreview && clip.exists()) check(clip.delete())
            val camera = Camera2Source(this)
            val microphone = MicrophoneSource(MediaRecorder.AudioSource.MIC)
            var capturePublisher: AndroidPublisherAdapter? = null
            val callbacks = PublisherCallbacks(
                onSuccess = { runOnUiThread { capturePublisher?.connectionSucceeded(); if (publisher === capturePublisher) updatePublisherStatus() } },
                onFailure = { runOnUiThread { capturePublisher?.requestStop(true); if (publisher === capturePublisher) updatePublisherStatus() } },
                onDisconnected = { runOnUiThread { capturePublisher?.disconnected(); if (publisher === capturePublisher) updatePublisherStatus() } }
            )
            val stream = GenericStream(this, callbacks, camera, microphone)
            capture = stream
            capturePublisher = AndroidPublisherAdapter(diagnosticScore, stream, DiagnosticPublisherLifecycle(diagnosticScore), java.util.UUID.randomUUID().toString())
            publisher = capturePublisher
            updatePublisherStatus()
            stream.forceCodecType(CodecUtil.CodecType.HARDWARE, CodecUtil.CodecType.SOFTWARE)
            check(stream.prepareVideo(1280, 720, 2_000_000, fps = 30, rotation = 0)) { "Video preparation failed" }
            check(stream.prepareAudio(48000, true, 128000)) { "Audio preparation failed" }
            val audio = getSystemService(AudioManager::class.java)
            val builtin = audio.getDevices(AudioManager.GET_DEVICES_INPUTS).firstOrNull { it.type == AudioDeviceInfo.TYPE_BUILTIN_MIC }
            report.put("builtinPreferenceAccepted", builtin != null && microphone.setPreferredDevice(builtin))
            val filter = ImageFilterRender().apply { setPosition(5f, 5f) }
            scoreFilter = filter
            if (transitionTest) {
                val image = scoreboard()
                filter.setImage(image)
                filter.setScale(image.width / 12f, 18f)
            } else updateScoreFilter()
            stream.getGlInterface().addFilter(filter)
            stream.startPreview(preview)
            if (publisherPreview) {
                status.text = "Publisher preview prepared; no recording or broadcast. Publisher start remains blocked pending safeguards."
                return
            }
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
            if (capture != null) stopCapture(if (failure is DiagnosticOverlayDoesNotFit) "score_does_not_fit" else "setup_error")
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
        publisher?.requestStop(true)
        val wasPublisherPreview = publisherPreview
        if (!wasPublisherPreview && !transitionTest && report.has("startedElapsedMs")) {
            scoreReviewDurationMs = SystemClock.elapsedRealtime() - report.getLong("startedElapsedMs")
        }
        publisherPreview = false
        capture = null
        publisher = null
        updatePublisherStatus()
        scoreFilter = null
        handler.removeCallbacksAndMessages(null)
        report.put("stopReason", reason).put("stoppedElapsedMs", SystemClock.elapsedRealtime())
        try { if (stream.isRecording) stream.stopRecord() }
        catch (failure: Exception) { report.put("stopError", failure.javaClass.simpleName) }
        try { stream.release() }
        catch (failure: Exception) { report.put("releaseError", failure.javaClass.simpleName) }
        if (wasPublisherPreview) {
            runButton.isEnabled = true
            transitionButton.isEnabled = true
            status.text = if (report.has("releaseError")) "Publisher preview cleanup failed." else "Publisher preview stopped; no broadcast was started."
            return
        }
        status.text = "Inspecting the encoded clip…"
        Thread {
            try { inspectClip() }
            catch (failure: Exception) { report.put("inspectionError", failure.javaClass.simpleName) }
            saveReport()
            runOnUiThread {
                runButton.isEnabled = true
                transitionButton.isEnabled = true
                val failed = listOf("setupError", "recordError", "stopError", "releaseError", "inspectionError", "controlError").any { report.has(it) }
                status.text = if (reason == "score_does_not_fit") "Local capture stopped: full diagnostic score does not fit. Score was saved."
                    else if (failed) "Test did not complete successfully ($reason). Diagnostic metadata was saved."
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

    private fun updatePublisherStatus() {
        val state = publisher?.diagnosticStatus() ?: try {
            if (diagnosticScore.pendingStopSessions().isEmpty()) DiagnosticPublisherStatus.Stopped else DiagnosticPublisherStatus.Stopping
        } catch (_: Exception) { DiagnosticPublisherStatus.Stopping }
        publisherStatus.text = when (state) {
            DiagnosticPublisherStatus.Stopped -> "Publisher: stopped"
            DiagnosticPublisherStatus.Connecting -> "Publisher: connecting"
            DiagnosticPublisherStatus.Live -> "Publisher: connected (received output not verified)"
            DiagnosticPublisherStatus.Stopping -> "Publisher: stop unconfirmed"
        }
    }

    private fun scorePoint(left: Boolean) {
        if (capture != null && transitionTest) return
        try {
            val activePublisher = publisher
            if (activePublisher != null) activePublisher.scorePoint(left) else diagnosticScore.scorePoint(left)
            updatePublisherStatus()
            if (capture == null) status.text = "Off-air diagnostic score: ${diagnosticScore.scoreDisplay()}"
            else {
                updateScoreFilter()
                if (!publisherPreview && report.has("startedElapsedMs")) {
                    if (firstScoreChangeMs == null) firstScoreChangeMs = SystemClock.elapsedRealtime() - report.getLong("startedElapsedMs")
                    else multipleScoreChanges = true
                }
            }
        } catch (_: DiagnosticOverlayDoesNotFit) {
            stopCapture("score_does_not_fit")
            status.text = "Local capture stopped: full diagnostic score does not fit. Score was saved."
        } catch (_: Exception) {
            stopCapture("score_state_unavailable")
            status.text = "Local capture stopped: diagnostic score state unavailable."
        }
    }

    private fun updateScoreFilter() {
        val filter = checkNotNull(scoreFilter)
        val adapter = checkNotNull(publisher)
        if (!adapter.renderDiagnosticProjection { image, _ ->
            filter.setImage(image)
            filter.setScale(image.width / 12f, 18f)
        }) throw DiagnosticOverlayDoesNotFit()
    }

    private class DiagnosticOverlayDoesNotFit : Exception()

    private fun reviewScoreChangeFrames() {
        val change = firstScoreChangeMs
        val duration = scoreReviewDurationMs
        if (capture != null || change == null || duration == null || multipleScoreChanges || change < 1500 || duration - change < 1500 || !clip.exists()) {
            status.text = "No score-change frames available. Run a local recording and change a score."
            return
        }
        val retriever = MediaMetadataRetriever()
        var before: Bitmap? = null
        var after: Bitmap? = null
        try {
            retriever.setDataSource(clip.absolutePath)
            before = retriever.getFrameAtTime((change - 1000) * 1000, MediaMetadataRetriever.OPTION_CLOSEST)
            after = retriever.getFrameAtTime((change + 1000) * 1000, MediaMetadataRetriever.OPTION_CLOSEST)
        } catch (_: Exception) {
            // Report unavailable rather than displaying a partial comparison.
        } finally { retriever.release() }
        if (before == null || after == null) {
            before?.recycle()
            after?.recycle()
            status.text = "No score-change frames available. Run a local recording and change a score."
            return
        }
        val frames = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            addView(ImageView(this@MediaActivity).apply { setImageBitmap(before); adjustViewBounds = true }, LinearLayout.LayoutParams(0, -2, 1f))
            addView(ImageView(this@MediaActivity).apply { setImageBitmap(after); adjustViewBounds = true }, LinearLayout.LayoutParams(0, -2, 1f))
        }
        AlertDialog.Builder(this).setTitle("Approximate frames: before / after")
            .setMessage("Verify both scores and full scoreboard yourself. Timing is approximate; this does not verify a broadcast.")
            .setView(frames).setPositiveButton("Close", null).create().apply {
                setOnDismissListener { before.recycle(); after.recycle() }
                show()
            }
    }
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

    override fun onDestroy() {
        diagnosticScore.close()
        super.onDestroy()
    }
}
