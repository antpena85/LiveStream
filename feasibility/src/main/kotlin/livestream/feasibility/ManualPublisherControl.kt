package livestream.feasibility

/** Prototype boundary only; no network publisher or Android safety adapter is supplied. */
class ManualPublisherControl(private val session: StreamSession, private val stop: () -> Unit, private val connect: (String) -> Unit) {
    fun requestStop(confirmed: Boolean) {
        val previous = session.status()
        session.requestStop(confirmed)
        if (confirmed && previous != StreamStatus.Stopping && session.status() == StreamStatus.Stopping) {
            try { stop() } catch (_: Exception) { /* Remain Stopping. */ }
        }
    }
    fun connectionSucceeded() {
        session.connectionSucceeded()
        if (session.status() == StreamStatus.Stopping) {
            try { stop() } catch (_: Exception) { /* Remain Stopping. */ }
        }
    }
    fun start(server: String, key: String, confirmed: Boolean, cameraPermission: Boolean,
              microphonePermission: Boolean, fitReady: Boolean, assessed: ProbeSnapshot?): StartResult {
        if (!confirmed) return StartResult.Cancelled
        if (ManualDestination.validate(server, key) != DestinationResult.Ready) return StartResult.InvalidDestination
        val result = session.requestStart(true, cameraPermission, microphonePermission, fitReady, assessed)
        if (result == StartResult.Accepted) {
            try {
                ManualDestination.withEndpoint(server, key, connect)
            } catch (_: Exception) {
                session.requestStop(true)
                try { stop() } catch (_: Exception) { /* Remain Stopping; do not expose publisher errors. */ }
                return StartResult.PublisherFailed
            }
        }
        return result
    }
}