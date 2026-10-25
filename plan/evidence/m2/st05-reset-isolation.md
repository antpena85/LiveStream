# ST05: reset generation and late-command isolation

Recorded: 2026-10-23 (America/New_York)  
Result: Host reset/reopen, old-command rejection, scoped receipts, cancellation and before-commit reset interruption passed.  
Task: [M2.5](../../subplans/m2-5-lifecycle-and-storage-feasibility.md)  
Boundary: Owner-confirmed T01 commands, returned/current snapshots and real-store reopen.  
Acceptance references: ST05 and M1.5 E33/E34/E35; supporting FR-05/FR-06 and future V06.

## Probe contract specified before execution

Use the existing points-only SQLite fixture. Add a durable nonnegative match
generation (`epoch`) to its public snapshot. Point commands carry the epoch
observed when they were created; they must not silently adopt the current one.
A confirmed reset targeting the current epoch atomically changes points and
revision to zero and advances the epoch. The initial fixture remains 7/0/0.
Cancellation preserves the entire snapshot. A mismatched epoch returns an
explicit stale-match failure carrying the current snapshot, without mutation.

Old point commands, including already accepted IDs and never-seen IDs, must
remain invalid after reopening. A command ID can be used independently in a
new epoch; its duplicate must still return the original receipt exactly once.
Revisions may coincide across resets, so revision alone cannot isolate matches.

This is a local experiment of the proposed D11 identity mechanism, not a final
application API, numeric limit, schema or protocol choice. Reset permissions,
UI confirmations, retained team/settings fields, Undo, receipt expiry, retrying
reset itself and migration of earlier probe databases are outside this fixture.
Use fresh temporary databases; do not claim ST07 migration support.

## Planned vertical slices

1. Observe a missing-behavior failure for confirmed reset and durable epoch;
   implement the minimum and rerun the existing suite.
2. Observe a missing-behavior failure for old point commands after reset/reopen;
   reject them while preserving the new match.
3. Observe a missing-behavior failure for independent command IDs across epochs;
   scope durable receipts to the target epoch and verify retry after reopen.

Retain pre-fix sources and actual red/green JUnit results for each slice.
Cancellation will be verified separately; existing behavior gets no invented
red cycle. Physical lifecycle, independent writers and power loss remain pending.

## Observed red and green

| Cycle | Missing behavior observed before its fix | Minimum change and passing result |
| --- | --- | --- |
| 06 | Confirmed reset returned the old 8/1/0 snapshot instead of 0/0/1. [Red report](tdd-cycle-06-red.xml), [store source](tdd-cycle-06-red-source.txt), [test source](tdd-cycle-06-red-test-source.txt). | Store the epoch and reset points/revision/epoch in one transaction. Reopening preserves 0/0/1 and new scoring reaches 1/1/1. [Five SQLite tests](tdd-cycle-06-green.xml) and [two session tests](tdd-cycle-06-green-session.xml) pass; exit 0. |
| 07 | Retrying an accepted old-epoch ID after reset returned its old receipt instead of a stale-match failure. [Red report](tdd-cycle-07-red.xml), [store source](tdd-cycle-07-red-source.txt), [test source](tdd-cycle-07-red-test-source.txt). | Check the target epoch inside the command transaction before receipt lookup. Both an accepted old ID and a never-delivered old ID are rejected with current state 1/1/1. Fresh scoring remains usable. [Six SQLite tests](tdd-cycle-07-green.xml) and [two session tests](tdd-cycle-07-green-session.xml) pass; exit 0. |
| 08 | Using the same ID in epoch 1 returned the epoch-0 8/1/0 receipt instead of accepting 1/1/1. [Red report](tdd-cycle-08-red.xml), [store source](tdd-cycle-08-red-source.txt), [test source](tdd-cycle-08-red-test-source.txt). | Scope receipt uniqueness and lookup to epoch plus command ID. Reopening/retrying returns the original 1/1/1 receipt while retaining the later 2/2/1 snapshot. [Seven SQLite tests](tdd-cycle-08-green.xml) and [two session tests](tdd-cycle-08-green-session.xml) pass; exit 0. |

Each red run has exactly one new behavioral failure; the earlier tests pass.
These are assertion failures, not compiler/dependency failures. Snapshots use
points/revision/epoch notation above. No test queries database tables, private
fields or implementation locks. The receipt-scoping test also checks that a
retry returns its original outcome without replacing the newer current state.

The command signature now requires an explicit `expectedEpoch`; existing host
tests and the original child point fixture target their known initial epoch 0.
Old receipts remain stored in this experiment. Rejecting their old target epoch
prevents replay; no production pruning or expiry policy is selected.

Commands from the workspace root:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File tools/m2/run-feasibility.ps1 test --offline
```

Reports redact only the workstation hostname. Dates and result counts remain
unchanged. Review found no necessary storage refactor after the three slices.

## Additional verification of existing storage behavior

Cancellation passed on its first run without changing the store: canceled
reset preserves 8/1/0, the next point reaches 9/2/0, and reopening/retrying the
original ID returns its 8/1/0 receipt while retaining current state 9/2/0.
That run passes [eight SQLite tests](st05-canceled-reset-green.xml) and
[two session tests](st05-canceled-reset-green-session.xml), exit 0.

The child-process fixture was then given a reset operation using the existing
before-commit checkpoint. It abruptly halts with exit 23 after candidate reset
writes but before commit. Reopening preserves the old 8/1/0 state and receipt.
A deliberate reset retry reaches 0/0/1, new scoring reaches 1/1/1, and another
reopen retains 1/1/1. This also passed on its first run with the unchanged store.
The shared child-launch helper was extracted during green review to keep the
two interruption scenarios on the same bounded launcher and cleanup path.

Final complete suite: [nine SQLite tests](st05-reset-isolation-green.xml) and
[two session tests](st05-reset-isolation-green-session.xml), eleven passed,
zero failures/errors/skips, exit 0. The store SHA-256 before and after the two
additional verifications is
`0BCFAB08B1069988A7959B8EEABFF42AEA4841CFEEE69E1CA341EC5139619C59`.
There are eight observed red/green cycles in total; these two first-run checks
do not add cycles. The earlier duplicate-delivery verification is also separate.

## Limits and next work

This supplies host evidence for the specified ST05 cases through real SQLite
and public snapshots. It does not deliver the application's full reset,
team/settings retention, Undo, phone/watch authorization, revision review,
receipt expiry, duplicate reset acknowledgment or Android/Room integration.
An epoch is local to this file; a new/replaced file begins at epoch 0. A final
identity scheme must also address store replacement, restore and controller
sessions. The fixture does not make epochs globally unique.

Reset interruption covers one exact before-commit timing, alongside the earlier
point-write interruption. Other timings, independent processes writing together,
power loss, schema upgrades and actual device recovery remain untested. Earlier
probe schemas are not migrated: use fresh temporary databases, then reopen
within that schema. Do not count this as ST07 migration evidence.

M2.5/M2.8 remain In progress; D04/D08/D11/D13 and G2 stay open. Recommended
next independent host work is ST03's explicit commit-then-lost-acknowledgment
process interruption; its existing reopen/retry evidence remains partial.
Physical media/lifecycle, provider and network work retains the known input gaps.
