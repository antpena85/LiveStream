# M2.5 lifecycle and storage investigation

Recorded: 2026-10-23 (America/New_York)  
Updated: 2026-10-24 (America/New_York)  
Status: Host reopen/retry, before/post-commit interruption, shared-store concurrency and reset-generation isolation passed; full storage/device evidence pending.  
Task: [M2.5](../subplans/m2-5-lifecycle-and-storage-feasibility.md)

## Capture lifecycle

Android camera/microphone foreground services require their service types,
permissions and applicable runtime capture grants. While-in-use restrictions
constrain starting capture from the background; a remote request cannot bypass
them. An already-started foreground capture service and a new background start
are different cases. [Foreground service types](https://developer.android.com/develop/background-work/services/fgs/service-types).

| Event | Proposed visible behavior | Evidence needed |
| --- | --- | --- |
| Confirmed Start while capture is unready | Deny with actionable reason, retain match, never claim live. | T02 covers modeled policy; Android permission/lifecycle observation still required. |
| Screen dim/background/lock | Preserve match; continue only if allowed by the selected capture lifecycle, otherwise show interruption/recovery. | Actual supported OS/build behavior; no screen-off continuity guarantee. |
| Call, audio loss or accessory disconnect | Show actual route/state; preserve intentional mute; recover only through permitted inputs. | Built-in and available accessory capture/received audio. |
| Thermal/resource/permission loss | Show a truthful degraded/error state and safe recovery; never silently reset match. | Physical event traces and selected D07/D08 limits. |
| Process recreation or reboot | Restore the durable match; remain stopped pending a fresh deliberate Start. | Real-store/process-recreation results, not emulator-only camera claims. |
| Q45 numeric-fit failure | Retain the complete accepted match, stop publishing with notice, keep off-air scoring. | Durable acceptance/stop ordering, callbacks and restart recovery. |

## Persistence comparison and proposed invariant

Recommend evaluating a transactional SQLite/Room store for match, current Undo,
revision, command receipt and any durable pending-stop intent. Accept only after
commit; expose state and receipt together after reopening. Store stream secrets
separately using platform-backed key protection and appropriate backup rules.
This is a design recommendation, not a library guarantee or selected schema.
[Room transactions](https://developer.android.com/reference/androidx/room/Transaction).

An atomic serialized document can also store the same complete state, but
history, concurrent commands and bounded receipts make growth/migration and
reconciliation our responsibility. AtomicFile does not provide its own locking;
an atomic write is not automatically a complete multi-writer protocol.
[AtomicFile](https://developer.android.com/reference/android/util/AtomicFile).

Propose one accepted command order and a transaction containing the entire new
match/Undo/revision/receipt. A confirmed reset starts a fresh identity/epoch;
late old commands stay invalid. Undo creates a new revision and consumes its
opportunity under Q26. Placement remains separate under Q40. Publisher shutdown
is an external effect, not part of a SQLite transaction; recover a durable stop
intent idempotently and never report stopped before publishing stops.

Validate ST01 reopen after acceptance; ST02 fail before commit; ST03 commit then
lose acknowledgment; ST04 simultaneous commands; ST05 reset/old-command retry;
ST06 interruption during Q45 shutdown; ST07 schema migration without loss.
Use a real temporary store through T01, not direct database assertions. Room
documents device tests and host testing with Room KMP; choose the matching
runner after storage selection. [Database testing](https://developer.android.com/training/data-storage/room/testing-db).

An isolated T01 host probe now supplies partial ST01/ST03 evidence: a real
SQLite file reopens with points 8/revision 1 after starting at 7/0, and retrying
the accepted command ID does not apply a second point. Its observed failure
preceded the transaction/receipt implementation; the final three-test suite
passed. [TDD evidence](../evidence/m2/README.md). The local driver is pinned to
the publisher's [SQLite JDBC 3.53.4.0](https://github.com/xerial/sqlite-jdbc/releases/tag/3.53.4.0).

The later [ST02 child-process experiment](../evidence/m2/st02-interrupted-write.md)
abruptly terminates a separate JVM after its candidate state/receipt writes
but before commit. Reopening preserves the prior accepted 8/1 state; retrying
the interrupted command accepts 9/2 once. The initial fixture failed because
it halted after commit; adding the before-commit fault checkpoint made the
test pass. That run passed four tests.

The subsequent [ST04 shared-store experiment](../evidence/m2/st04-concurrent-commands.md)
releases eight distinct commands through one instance. The initial test failed
with an auto-commit transaction error. Serializing command processing on that
instance gives all eight commands separate committed revisions, retaining
points 15/revision 8 after reopening from the 7/0 fixture baseline. That run
passed five tests. This does not select a production
threading architecture or test independent connections/processes.

The [ST04 duplicate-delivery verification](../evidence/m2/st04-duplicate-commands.md)
then submitted one command ID eight times together. All calls return 8/1;
reopening and retrying preserve 8/1. It passed on its first run with the existing
receipts/serialization, so that verification adds no red/green cycle. At that
checkpoint the suite passed six tests.

The [ST05 generation/reset experiment](../evidence/m2/st05-reset-isolation.md)
adds three observed red/green slices: a durable confirmed reset from 8/1/0 to
0/0/1 (points/revision/epoch), rejection of both accepted and unseen old-epoch
point commands after reopen, and receipts scoped to epoch plus command ID.
The old commands are rejected even when revisions coincide. New-epoch scoring
and duplicate retries preserve the correct current snapshot. Cancellation and
a child JVM halted during reset before commit then pass on their first runs
without changing storage logic. Reopening after interruption preserves 8/1/0;
a deliberate retry starts epoch 1. That checkpoint passed eleven tests,
with eight observed red/green cycles and three first-run verifications.

The epoch is a probe-only mechanism local to one file. File replacement/restore,
the final application identity/expiry policy and upgrades from earlier probe
schemas are not covered.

On 2026-10-24, [ST03 post-commit interruption](../evidence/m2/st03-lost-acknowledgment.md)
passed on its first run with unchanged storage logic. The child commits a point,
then halts before sending its receipt or closing the store. Reopening preserves
9/2/0; retries return that receipt without another point or replacing later
10/3/0 state, including after a second reopen. The complete offline suite now
passes twelve tests: ten SQLite and two session cases. Eight observed red/green
cycles and four first-run verifications are retained.

Recommended next independent host work is to specify ST06's durable pending-stop
contract/checkpoints at T01/T02 before an isolated TDD experiment. Q45's policy
is confirmed; final stop/recovery mechanisms remain D10 proposals.

This remains a points-only fixture, not the full match/Undo schema or Room
integration. These controlled host process interruptions do not establish every
crash timing, power failure, independent writers or Android storage. Physical
lifecycle and remaining ST failure/migration cases remain unrun. SQLite's
transaction guarantees inform the design but do not substitute for the
remaining application/device failure evidence.
[SQLite transactions](https://sqlite.org/transactional.html).
D04/D08/D11 remain open.
