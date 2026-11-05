# Session safety-state seam — 2026-11-05

The isolated T02 `StreamSession` now depends on a read-only `SessionSafetyState`
interface rather than directly on the JVM SQLite fixture. The fixture implements
the same two existing reads; `ProbeSnapshot` moved to the shared source file.
No safety decision or runtime behavior was intentionally changed. The media
project also compiles the session and interface without the JVM SQLite fixture;
the activity does not instantiate or use them.

The forced offline JVM suite passed after the extraction, including the existing
real-store pending-stop, stale-snapshot and read-failure cases. This is a
behavior-preserving refactor verified by existing tests, **not** an observed
red/green cycle. The media offline `assembleDebug` subsequently succeeded with
all tasks up to date; an initial concurrent build command timed out, so its
outcome was not used. The generated session and interface sources match the
shared sources byte-for-byte; the Android Kotlin class and dex outputs contain
the session, interface and snapshot. This checks source/build compatibility,
not Android execution.

The interface has two separate reads and provides no atomic snapshot, durable
Android implementation, fit rendering, synchronization or real publisher
control. It is not a production authorization contract. No physical device,
credential or destination was used. M2.2 and G2 remain open.