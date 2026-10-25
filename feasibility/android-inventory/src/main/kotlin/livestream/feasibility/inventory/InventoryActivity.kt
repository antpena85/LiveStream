package livestream.feasibility.inventory

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.media.MediaCodecInfo
import android.media.MediaCodecList
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONObject

/** Exploratory capability measurement only; no capture, publishing or app features. */
class InventoryActivity : Activity() {
    private lateinit var reportView: TextView
    private val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 48, 24, 24)
        }
        content.addView(TextView(this).apply {
            text = "M2 Hardware Inventory\nLists advertised capabilities. Camera/audio capture and streaming are not started."
            textSize = 18f
        })
        content.addView(Button(this).apply {
            text = "Read capabilities"
            setOnClickListener {
                if (permissions.any { checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED }) {
                    requestPermissions(permissions, 1)
                } else {
                    collectReport()
                }
            }
        })
        reportView = TextView(this).apply { setTextIsSelectable(true) }
        content.addView(ScrollView(this).apply { addView(reportView) })
        setContentView(content)
        collectReport()
    }

    override fun onRequestPermissionsResult(requestCode: Int, requested: Array<out String>, results: IntArray) {
        super.onRequestPermissionsResult(requestCode, requested, results)
        collectReport()
    }

    private fun collectReport() {
        val report = JSONObject()
            .put("probeVersion", "m2-inventory-1")
            .put("model", Build.MODEL)
            .put("androidRelease", Build.VERSION.RELEASE)
            .put("api", Build.VERSION.SDK_INT)
            .put("buildId", Build.ID)
            .put("securityPatch", Build.VERSION.SECURITY_PATCH)
            .put("cameraPermission", checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
            .put("audioPermission", checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
            .put("cameraCapture", "not_run")
            .put("audioCapture", "not_run")
            .put("encoding", "not_run")
            .put("publishing", "not_run")
        try {
            val cameras = JSONArray()
            val manager = getSystemService(CameraManager::class.java)
            for (id in manager.cameraIdList) {
                val camera = manager.getCameraCharacteristics(id)
                val sizes = camera.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                    ?.getOutputSizes(SurfaceTexture::class.java).orEmpty()
                cameras.put(JSONObject()
                    .put("cameraId", id)
                    .put("facing", camera.get(CameraCharacteristics.LENS_FACING))
                    .put("focalLengths", JSONArray(camera.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)?.toList().orEmpty()))
                    .put("physicalCameraIds", JSONArray(camera.physicalCameraIds.toList().sorted()))
                    .put("advertises720pSurfaceTexture", sizes.any { it.width == 1280 && it.height == 720 }))
            }
            report.put("cameras", cameras)
        } catch (failure: Exception) {
            report.put("cameraInventoryError", failure.javaClass.simpleName)
        }
        try {
            val encoders = JSONArray()
            for (codec in MediaCodecList(MediaCodecList.ALL_CODECS).codecInfos.filter { it.isEncoder }) {
                for (type in codec.supportedTypes.filter { it == "video/avc" || it == "audio/mp4a-latm" }) {
                    val capabilities = codec.getCapabilitiesForType(type)
                    val item = JSONObject().put("name", codec.name).put("mime", type)
                        .put("hardwareAccelerated", codec.isHardwareAccelerated)
                    if (type == "video/avc") {
                        item.put("advertises720p30", capabilities.videoCapabilities?.areSizeAndRateSupported(1280, 720, 30.0) ?: JSONObject.NULL)
                            .put("advertisesSurfaceInput", capabilities.colorFormats.contains(MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface))
                    }
                    encoders.put(item)
                }
            }
            report.put("encoders", encoders)
        } catch (failure: Exception) {
            report.put("encoderInventoryError", failure.javaClass.simpleName)
        }
        try {
            val inputs = JSONArray()
            for (device in getSystemService(AudioManager::class.java).getDevices(AudioManager.GET_DEVICES_INPUTS)) {
                inputs.put(JSONObject().put("type", device.type)
                    .put("sampleRates", JSONArray(device.sampleRates.toList()))
                    .put("channelCounts", JSONArray(device.channelCounts.toList())))
            }
            report.put("advertisedAudioInputs", inputs)
        } catch (failure: Exception) {
            report.put("audioInventoryError", failure.javaClass.simpleName)
        }
        val text = report.toString(2)
        filesDir.resolve("capability-report.json").writeText(text)
        reportView.text = text
    }
}
