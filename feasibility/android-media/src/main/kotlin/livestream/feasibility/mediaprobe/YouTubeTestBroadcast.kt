package livestream.feasibility.mediaprobe

import org.json.JSONObject

enum class YouTubeIngestObservation { INACTIVE, CONFIGURATION_READY }

data class YouTubeTestBinding(
    val broadcastId: String,
    val streamId: String,
    val ingestObservation: YouTubeIngestObservation
) {
    val selectionMessage: String
        get() = when (ingestObservation) {
            YouTubeIngestObservation.INACTIVE -> "Private test broadcast selected; owned ready broadcast and bound inactive ingest verified. Selection is temporary; publishing remains disabled."
            YouTubeIngestObservation.CONFIGURATION_READY -> "Private test broadcast selected; owned ready broadcast and bound ingest configuration ready. Ingest inactivity is unverified. Selection is temporary; publishing remains disabled."
        }
}

/** Read-only selection, not YouTube's mutating bind operation or publishing authorization. */
class YouTubeTestBroadcast(private val get: (String) -> Pair<Int, String>) {
    var failureMessage: String = "Private test broadcast could not be verified. Publishing remains disabled."
        private set
    fun select(id: String): YouTubeTestBinding? {
        fun fail(reason: String): YouTubeTestBinding? {
            failureMessage = "Private test broadcast check: $reason. Publishing remains disabled."
            return null
        }
        if (!id.matches(Regex("[A-Za-z0-9_-]{11}"))) return fail("invalid video ID format")
        var stage = "channel"
        return try {
            fun item(path: String): JSONObject? {
                val response = get(path)
                if (response.first != 200) {
                    fail("$stage request failed (HTTP ${response.first})")
                    return null
                }
                val items = JSONObject(response.second).getJSONArray("items")
                return if (items.length() == 1) items.getJSONObject(0) else {
                    fail("$stage result missing or ambiguous")
                    null
                }
            }
            val channel = item("channels?part=id&mine=true&maxResults=1&fields=items(id)")?.getString("id") ?: return null
            stage = "broadcast"
            val broadcast = item("liveBroadcasts?part=id,snippet,status,contentDetails&id=$id&fields=items(id,snippet/channelId,status(privacyStatus,lifeCycleStatus),contentDetails/boundStreamId)") ?: return null
            if (broadcast.getString("id") != id || broadcast.getJSONObject("snippet").getString("channelId") != channel) return fail("broadcast ownership or identity mismatch")
            val status = broadcast.getJSONObject("status")
            if (status.getString("privacyStatus") != "private") return fail("broadcast is not private")
            if (status.getString("lifeCycleStatus") != "ready") return fail("broadcast is not ready")
            val streamId = broadcast.getJSONObject("contentDetails").optString("boundStreamId")
            if (!streamId.matches(Regex("[A-Za-z0-9_-]{1,128}"))) return fail("bound ingest missing or unsupported")
            stage = "ingest"
            val stream = item("liveStreams?part=id,snippet,status&id=$streamId&fields=items(id,snippet/channelId,status/streamStatus)") ?: return null
            if (stream.getString("id") != streamId) return fail("ingest identity mismatch")
            if (stream.getJSONObject("snippet").getString("channelId") != channel) return fail("ingest ownership mismatch")
            val ingestState = stream.getJSONObject("status").getString("streamStatus")
            if (ingestState != "inactive" && ingestState != "ready") {
                val safeState = when (ingestState) {
                    "created" -> "created"
                    "active" -> "active"
                    "error" -> "error"
                    else -> "unrecognized"
                }
                return fail("ingest is not inactive (state: $safeState)")
            }
            YouTubeTestBinding(id, streamId, if (ingestState == "inactive")
                YouTubeIngestObservation.INACTIVE else YouTubeIngestObservation.CONFIGURATION_READY)
        } catch (_: Exception) { fail("$stage request or response unavailable") }
    }
}