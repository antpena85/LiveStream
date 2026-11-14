package livestream.feasibility.mediaprobe

import org.json.JSONObject

/** T03 read-only provider boundary. Responses and credentials never leave this check. */
class YouTubeAccessCheck(private val get: (String) -> Pair<Int, String>) {
    fun check(): String = try {
        val channel = get("channels?part=id&mine=true&maxResults=1&fields=items(id)")
        if (channel.first != 200) "YouTube channel access check failed (HTTP ${channel.first}). Publishing remains disabled."
        else if (JSONObject(channel.second).getJSONArray("items").length() == 0)
            "No YouTube channel returned for this authorization. Live access remains unverified. Publishing remains disabled."
        else {
            val live = get("liveBroadcasts?part=id&mine=true&maxResults=1&fields=items(id)")
            if (live.first == 403 && JSONObject(live.second).optJSONObject("error")
                    ?.optJSONArray("errors")?.let { errors ->
                        (0 until errors.length()).any { errors.optJSONObject(it)?.optString("reason") == "liveStreamingNotEnabled" }
                    } == true)
                "YouTube channel access verified. YouTube reports live streaming is not enabled. Publishing remains disabled."
            else if (live.first != 200)
                "YouTube channel access verified. Live API check failed (HTTP ${live.first}); eligibility remains unverified. Publishing remains disabled."
            else {
                JSONObject(live.second).getJSONArray("items")
                "YouTube channel access verified. Live API access verified; broadcast eligibility and received output remain unverified. Publishing remains disabled."
            }
        }
    } catch (_: Exception) { "YouTube access check unavailable. Publishing remains disabled." }
}