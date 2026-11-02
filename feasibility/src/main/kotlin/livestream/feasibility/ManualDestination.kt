package livestream.feasibility

import java.net.URI

enum class DestinationResult { Ready, InvalidServer, MissingKey }

/** Validation only; does not retain credentials or authorize publishing. */
object ManualDestination {
    fun withEndpoint(server: String, key: String, consume: (String) -> Unit): DestinationResult {
        val result = validate(server, key)
        if (result == DestinationResult.Ready) consume(server.trimEnd('/') + "/" + key)
        return result
    }

    fun validate(server: String, key: String): DestinationResult {
        if (!server.startsWith("rtmp://") && !server.startsWith("rtmps://")) return DestinationResult.InvalidServer
        val uri = try { URI(server) } catch (_: IllegalArgumentException) { return DestinationResult.InvalidServer }
        catch (_: java.net.URISyntaxException) { return DestinationResult.InvalidServer }
        if (uri.host.isNullOrBlank()) return DestinationResult.InvalidServer
        if (uri.userInfo != null) return DestinationResult.InvalidServer
        if (uri.rawQuery != null) return DestinationResult.InvalidServer
        if (uri.rawFragment != null) return DestinationResult.InvalidServer
        if (key.isBlank()) return DestinationResult.MissingKey
        return DestinationResult.Ready
    }
}