package livestream.feasibility.mediaprobe

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import livestream.feasibility.ProbeSafetyView
import livestream.feasibility.ProbeSnapshot
import livestream.feasibility.SessionSafetyState

/** Isolated diagnostic store; not a complete match or publishing authorization. */
class AndroidSafetyStore(context: Context, name: String) : SQLiteOpenHelper(context, name, null, 1), SessionSafetyState {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE state (id INTEGER PRIMARY KEY CHECK(id=1), points INTEGER NOT NULL, revision INTEGER NOT NULL, epoch INTEGER NOT NULL)")
        db.execSQL("INSERT INTO state VALUES (1, 0, 0, 0)")
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