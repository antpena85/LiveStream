# ST04: concurrent commands through one host store

Recorded: 2026-10-23 (America/New_York)  
Result: Local T01 shared-store experiment passed; broader concurrency/device evidence remains pending.  
Task: [M2.5](../../subplans/m2-5-lifecycle-and-storage-feasibility.md)  
Boundary: Owner-confirmed T01 public commands, receipts/snapshots and real-store recovery.  
Acceptance reference: ST04, supporting FR-05/FR-06 and future V06 command-order evidence.

## Scenario and observation

Eight worker threads wait at a common start gate, then each submits a distinct
point command through the same public store instance backed by a real temporary
SQLite file. The test allows any arrival order. It observes returned receipts
and public snapshots; it does not inspect database tables or require a particular
locking implementation.

| Step | Expected and observed result in the passing run |
| --- | --- |
| Start from the fixture baseline | Points 7, revision 0. |
| Release eight distinct point commands together | All eight complete successfully. |
| Inspect their receipts without imposing a caller order | Exactly 8/1, 9/2, 10/3, 11/4, 12/5, 13/6, 14/7 and 15/8; no repeated or skipped accepted revision. |
| Read the current public snapshot after completion | Points 15, revision 8. |
| Close and reopen the file through the public probe | Points 15, revision 8 remain durable. |

The chosen worker count and the 10-second coordination/result waits and
5-second cleanup wait are bounded harness settings, not product limits or
latency targets. Workers are joined before the store closes.

## Observed red, minimum change and green

The first run failed in the new concurrent test with an `ExecutionException`
caused by `SQLException: database in auto-commit mode`, reported during rollback.
The previous four tests passed. Concurrent command calls were sharing mutable
transaction state on the probe's single connection without command serialization.
This was a probe behavior failure, not a dependency or build failure.

The minimum change adds `@Synchronized` to `recordPoint`, keeping one caller's
receipt lookup, state/receipt writes and commit together before another command
can use that instance's connection. No schema, SQL, dependency or provider
configuration changed. This is a local feasibility choice, not selection of the
application's final threading or storage architecture.

- [Pre-fix store](tdd-cycle-05-red-source.txt) and
  [red SQLite report](tdd-cycle-05-red.xml): three SQLite tests, one failure;
  full run: five tests, one failure, exit code 1.
- [Green SQLite report](tdd-cycle-05-green.xml): three passing tests, and
  [green session report](tdd-cycle-05-green-session.xml): two passing tests.
  Complete run: five tests passed, zero failures/errors, exit code 0.
- [Public behavioral test](../../../feasibility/src/test/kotlin/livestream/feasibility/DurableCommandProbeTest.kt)
  and [current probe](../../../feasibility/src/main/kotlin/livestream/feasibility/DurableCommandProbe.kt).

Both runs used the existing offline command from the workspace root:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File tools/m2/run-feasibility.ps1 test --offline
```

Retained reports replace only the workstation hostname with `local-host`;
assertions, counts and timestamps are unchanged. Review found no additional
refactor needed for this slice. Existing permission, cancellation, reopen/retry
and interrupted-write checks remain green.

## Evidence limits and next work

This supplies the distinct-command, single-instance host part of ST04. It does
not demonstrate simultaneous duplicate-ID retries, independent store instances
or processes, snapshots during a write, close/write races, stale-revision
rejection, reset identities, full match/Undo, Android Room or remote transports.
The existing sequential duplicate test remains separate evidence. The fixture
does not yet accept expected revisions or implement the controller protocol.

M2.5 remains In progress; D04/D08/D11 and G2 remain open. The next independent
local check is simultaneous delivery of one command ID through this same T01
boundary. Physical lifecycle, media/network and provider checks retain their
previously recorded input gaps.

Subsequent [duplicate-delivery verification](st04-duplicate-commands.md) supplies
that next case with the existing implementation. Its complete suite has six
passing tests; this record retains the earlier five-test red/green observation.
