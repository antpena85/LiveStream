package livestream.feasibility

/** Host-only external boundary; not an Android or network publisher. */
interface ProbePublisher {
    fun connect(sessionId: String, onConnected: () -> Unit)
    fun stop(sessionId: String)
}

/** Experimental T02 adapter; does not provide production concurrency guarantees. */
class PublisherSessionProbe(
    private val session: StreamSession,
    private val publisher: ProbePublisher,
) {
    private var activeSessionId: String? = null

    fun status(): StreamStatus = session.status()

    fun stop(confirmed: Boolean) {
        session.requestStop(confirmed)
        if (confirmed && session.status() == StreamStatus.Stopping) {
            activeSessionId?.let { publisher.stop(it) }
        }
    }

    fun start(sessionId: String, assessedSnapshot: ProbeSnapshot): StartResult {
        val result = session.requestStart(true, true, true, true, assessedSnapshot)
        if (result == StartResult.Accepted) {
            activeSessionId = sessionId
            publisher.connect(sessionId) {
                session.connectionSucceeded()
                if (session.status() == StreamStatus.Stopping) publisher.stop(sessionId)
            }
        }
        return result
    }
}