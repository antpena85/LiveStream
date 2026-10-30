package livestream.feasibility

import java.nio.file.Path
import java.sql.DriverManager

data class ProbeSnapshot(val points: Int, val revision: Long, val epoch: Long = 0)

class StaleProbeMatch(val current: ProbeSnapshot) : IllegalStateException("Command targets an old match epoch")

/** Isolated SQLite feasibility fixture, not the application's match schema. */
class DurableCommandProbe(
    database: Path,
    private val beforeCommit: () -> Unit = {},
) : AutoCloseable {
    private val connection = DriverManager.getConnection("jdbc:sqlite:${database.toAbsolutePath()}")

    init {
        connection.createStatement().use { statement ->
            statement.execute("PRAGMA synchronous=FULL")
            statement.execute("CREATE TABLE IF NOT EXISTS state (id INTEGER PRIMARY KEY CHECK(id=1), points INTEGER NOT NULL, revision INTEGER NOT NULL, epoch INTEGER NOT NULL)")
            statement.execute("CREATE TABLE IF NOT EXISTS receipts (command_id TEXT NOT NULL, points INTEGER NOT NULL, revision INTEGER NOT NULL, epoch INTEGER NOT NULL, PRIMARY KEY (epoch, command_id))")
            statement.execute("CREATE TABLE IF NOT EXISTS pending_stops (session TEXT PRIMARY KEY NOT NULL)")
            statement.execute("INSERT OR IGNORE INTO state VALUES (1, 7, 0, 0)")
        }
    }

    fun snapshot(): ProbeSnapshot = connection.createStatement().use { statement ->
        statement.executeQuery("SELECT points, revision, epoch FROM state WHERE id=1").use { rows ->
            check(rows.next())
            ProbeSnapshot(rows.getInt(1), rows.getLong(2), rows.getLong(3))
        }
    }

    @Synchronized
    fun reset(confirmed: Boolean, expectedEpoch: Long): ProbeSnapshot {
        if (!confirmed) return snapshot()
        connection.autoCommit = false
        try {
            val current = snapshot()
            if (current.epoch != expectedEpoch) throw StaleProbeMatch(current)
            connection.prepareStatement("UPDATE state SET points=0, revision=0, epoch=? WHERE id=1").use { statement ->
                statement.setLong(1, Math.incrementExact(current.epoch))
                statement.executeUpdate()
            }
            val reset = snapshot()
            beforeCommit()
            connection.commit()
            return reset
        } catch (failure: Exception) {
            connection.rollback()
            throw failure
        } finally {
            connection.autoCommit = true
        }
    }

    @Synchronized
    fun pendingStopSession(): String? {
        val sessions = pendingStopSessions()
        check(sessions.size <= 1) { "Multiple pending stops require pendingStopSessions" }
        return sessions.singleOrNull()
    }

    @Synchronized
    fun pendingStopSessions(): Set<String> = connection.createStatement().use { statement ->
        statement.executeQuery("SELECT session FROM pending_stops").use { rows ->
            buildSet { while (rows.next()) add(rows.getString(1)) }
        }
    }

    @Synchronized
    fun confirmStopped(session: String): Boolean = connection.prepareStatement(
        "DELETE FROM pending_stops WHERE session=?"
    ).use { statement ->
        statement.setString(1, session)
        statement.executeUpdate() == 1
    }

    @Synchronized
    fun recordPoint(commandId: String, expectedEpoch: Long, fitFailureSession: String? = null): ProbeSnapshot {
        connection.autoCommit = false
        try {
            val current = snapshot()
            if (current.epoch != expectedEpoch) throw StaleProbeMatch(current)
            val existing = connection.prepareStatement("SELECT points, revision, epoch FROM receipts WHERE command_id=? AND epoch=?").use { statement ->
                statement.setString(1, commandId)
                statement.setLong(2, expectedEpoch)
                statement.executeQuery().use { rows ->
                    if (rows.next()) ProbeSnapshot(rows.getInt(1), rows.getLong(2), rows.getLong(3)) else null
                }
            }
            if (existing != null) {
                connection.commit()
                return existing
            }
            connection.createStatement().use { statement ->
                statement.executeUpdate("UPDATE state SET points=points+1, revision=revision+1 WHERE id=1")
            }
            val accepted = snapshot()
            connection.prepareStatement("INSERT INTO receipts VALUES (?, ?, ?, ?)").use { statement ->
                statement.setString(1, commandId)
                statement.setInt(2, accepted.points)
                statement.setLong(3, accepted.revision)
                statement.setLong(4, accepted.epoch)
                statement.executeUpdate()
            }
            if (fitFailureSession != null) {
                connection.prepareStatement("INSERT OR IGNORE INTO pending_stops VALUES (?)").use { statement ->
                    statement.setString(1, fitFailureSession)
                    statement.executeUpdate()
                }
            }
            beforeCommit()
            connection.commit()
            return accepted
        } catch (failure: Exception) {
            connection.rollback()
            throw failure
        } finally {
            connection.autoCommit = true
        }
    }

    override fun close() = connection.close()
}
