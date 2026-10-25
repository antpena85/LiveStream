package livestream.feasibility

/** T04 diagnostic correlation only, not production command authorization. */
class PendingProbe(val peer: String, val token: String) {
    private var completed = false

    fun isPending(): Boolean = !completed

    fun complete() { completed = true }

    fun acknowledge(source: String, receivedToken: String): Boolean {
        if (completed || source != peer || receivedToken != token) return false
        completed = true
        return true
    }
}