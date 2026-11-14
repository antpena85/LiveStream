package livestream.feasibility.mediaprobe

import org.json.JSONObject

data class YouTubeTestBinding(val broadcastId: String, val streamId: String)

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
            if (stream.getString("id") != streamId || stream.getJSONObject("snippet").getString("channelId") != channel ||
                stream.getJSONObject("status").getString("streamStatus") != "inactive") return fail("ingest ownership, identity or inactive-state check failed")
            YouTubeTestBinding(id, streamId)
        } catch (_: Exception) { fail("$stage request or response unavailable") }
    }
}