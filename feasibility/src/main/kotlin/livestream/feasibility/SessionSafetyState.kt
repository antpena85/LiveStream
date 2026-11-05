package livestream.feasibility

/** Probe snapshot only; not the application's complete match or fit assessment. */
data class ProbeSnapshot(val points: Int, val revision: Long, val epoch: Long = 0)

/** Read-only seam for the isolated session probe, not a publishing authorization. */
interface SessionSafetyState {
    fun snapshot(): ProbeSnapshot
    fun pendingStopSessions(): Set<String>
}