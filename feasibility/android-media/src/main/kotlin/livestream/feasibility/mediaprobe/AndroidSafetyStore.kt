package livestream.feasibility.mediaprobe

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import livestream.feasibility.ProbeSafetyView
import livestream.feasibility.ProbeSnapshot
import livestream.feasibility.SessionSafetyState

/** Isolated diagnostic store; not a complete match or publishing authorization. */
class AndroidSafetyStore(context: Context, name: String) : SQLiteOpenHelper(context, name, null, 1), SessionSafetyState {
    fun claimPublisherSession(sessionId: String, assessed: ProbeSnapshot): Boolean {
        require(sessionId.isNotBlank())
        val db = writableDatabase
        db.beginTransaction()
        try {
            val view = safetyView()
            if (view.snapshot != assessed || view.pendingStops.isNotEmpty()) return false
            db.execSQL("INSERT INTO pending_stops (session) VALUES (?)", arrayOf(sessionId))
            db.setTransactionSuccessful()
            return true
        } finally { db.endTransaction() }
    }
    fun diagnosticProjection(): Pair<String, ProbeSnapshot> = readableDatabase.rawQuery(
        "SELECT diagnostic_score.left_points, diagnostic_score.right_points, state.points, state.revision, state.epoch FROM diagnostic_score JOIN state ON state.id=diagnostic_score.id WHERE state.id=1", null
    ).use { rows ->
        check(rows.moveToFirst())
        fixtureDisplay(rows.getInt(0), rows.getInt(1)) to ProbeSnapshot(rows.getInt(2), rows.getLong(3), rows.getLong(4))
    }
    /** Caller must independently establish that this exact session has stopped. */
    fun confirmStopped(sessionId: String) {
        require(sessionId.isNotBlank())
        writableDatabase.delete("pending_stops", "session = ?", arrayOf(sessionId))
    }
    fun scorePointWithPendingStop(left: Boolean, sessionId: String) {
        require(sessionId.isNotBlank())
        recordScore(left, sessionId)
    }

    fun scorePoint(left: Boolean) = recordScore(left, null)

    private fun recordScore(left: Boolean, sessionId: String?) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            db.execSQL(if (left) "UPDATE diagnostic_score SET left_points=left_points+1 WHERE id=1" else "UPDATE diagnostic_score SET right_points=right_points+1 WHERE id=1")
            db.execSQL("UPDATE state SET points=points+1, revision=revision+1 WHERE id=1")
            if (sessionId != null) db.execSQL("INSERT OR IGNORE INTO pending_stops (session) VALUES (?)", arrayOf(sessionId))
            db.setTransactionSuccessful()
        } finally { db.endTransaction() }
    }

    fun scoreDisplay(): String = readableDatabase.rawQuery(
        "SELECT left_points, right_points FROM diagnostic_score WHERE id=1", null
    ).use { rows ->
        check(rows.moveToFirst())
        fixtureDisplay(rows.getInt(0), rows.getInt(1))
    }
    private fun fixtureDisplay(left: Int, right: Int) = "> ABC $left S0 T/O2 | XYZ $right S0 T/O2"
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE state (id INTEGER PRIMARY KEY CHECK(id=1), points INTEGER NOT NULL, revision INTEGER NOT NULL, epoch INTEGER NOT NULL)")
        db.execSQL("INSERT INTO state VALUES (1, 0, 0, 0)")
        db.execSQL("CREATE TABLE diagnostic_score (id INTEGER PRIMARY KEY CHECK(id=1), left_points INTEGER NOT NULL, right_points INTEGER NOT NULL)")
        db.execSQL("INSERT INTO diagnostic_score VALUES (1, 0, 0)")
        db.execSQL("CREATE TABLE pending_stops (session TEXT PRIMARY KEY NOT NULL)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        throw IllegalStateException("Unsupported diagnostic database upgrade")
    }

    fun recordPointWithPendingStop(sessionId: String) {
        require(sessionId.isNotBlank())
        val db = writableDatabase
        db.beginTransaction()
        try {
            db.execSQL("UPDATE state SET points=points+1, revision=revision+1 WHERE id=1")
            db.execSQL("INSERT OR IGNORE INTO pending_stops (session) VALUES (?)", arrayOf(sessionId))
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    override fun safetyView(): ProbeSafetyView = readableDatabase.rawQuery(
        "SELECT state.points, state.revision, state.epoch, pending_stops.session FROM state LEFT JOIN pending_stops ON 1=1 WHERE state.id=1", null
    ).use { rows ->
        check(rows.moveToFirst())
        val snapshot = ProbeSnapshot(rows.getInt(0), rows.getLong(1), rows.getLong(2))
        val stops = mutableSetOf<String>()
        do { if (!rows.isNull(3)) stops.add(rows.getString(3)) } while (rows.moveToNext())
        ProbeSafetyView(snapshot, stops)
    }

    override fun snapshot(): ProbeSnapshot = safetyView().snapshot
    override fun pendingStopSessions(): Set<String> = safetyView().pendingStops
}