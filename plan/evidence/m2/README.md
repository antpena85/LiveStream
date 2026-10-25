# M2 local TDD evidence

Recorded: 2026-10-23 (America/New_York). These are JUnit outputs from the
isolated JVM T02 and host SQLite T01 probes, not Android or broadcast results.
Latest verification: ST03 on 2026-10-24 (America/New_York).
The workstation hostname was redacted; counts, assertions and timestamps are
unchanged.

| Cycle | Retained pre-fix source | Observed red | Green |
| --- | --- | --- | --- |
| 01 — Camera permission | [Pre-fix source](tdd-cycle-01-red-source.txt) | [One test, one failed](tdd-cycle-01-red.xml): expected permission requirement, got Accepted. | [One test, zero failed](tdd-cycle-01-green.xml). |
| 02 — Start confirmation | [Pre-fix source](tdd-cycle-02-red-source.txt) | [Two tests, one failed](tdd-cycle-02-red.xml): expected Cancelled, got Accepted; camera case passed. | [Two tests, zero failed](tdd-cycle-02-green.xml), exit code 0. |
| 03 — Real-store reopen/retry | [Pre-fix source](tdd-cycle-03-red-source.txt) | [One failed T01 test](tdd-cycle-03-red.xml): after reopen expected points 8/revision 1, got 7/0. Both T02 cases stayed green in that run. | [Passing T01 result](tdd-cycle-03-green.xml); final full run passed all three tests, exit code 0. |
| 04 — Interrupted child write | [Pre-fix child](tdd-cycle-04-red-source.txt) and [store](tdd-cycle-04-red-store-source.txt) | [Before-commit case failed](tdd-cycle-04-red.xml): the initial fixture committed before halting, leaving 9/2 rather than 8/1. | [Both SQLite cases pass](tdd-cycle-04-green.xml); final complete suite: four tests passed, exit code 0. |
| 05 — Concurrent distinct commands | [Pre-fix store](tdd-cycle-05-red-source.txt) | [Concurrent case failed](tdd-cycle-05-red.xml): shared connection transaction state caused an auto-commit error; existing four cases passed. | [Three SQLite cases](tdd-cycle-05-green.xml) and [two session cases](tdd-cycle-05-green-session.xml) pass; complete suite: five tests passed, exit code 0. |
| 06 — Durable reset generation | [Pre-fix store](tdd-cycle-06-red-source.txt) and [test](tdd-cycle-06-red-test-source.txt) | [Reset failed](tdd-cycle-06-red.xml): expected 0/0/1, got old 8/1/0 (points/revision/epoch). | [Five SQLite tests](tdd-cycle-06-green.xml) and [two session tests](tdd-cycle-06-green-session.xml) pass; seven total, exit 0. |
| 07 — Late old-match commands | [Pre-fix store](tdd-cycle-07-red-source.txt) and [test](tdd-cycle-07-red-test-source.txt) | [Old-command check failed](tdd-cycle-07-red.xml): the old receipt was returned without the required stale-match failure. | [Six SQLite tests](tdd-cycle-07-green.xml) and [two session tests](tdd-cycle-07-green-session.xml) pass; eight total, exit 0. |
| 08 — Receipts scoped to generation | [Pre-fix store](tdd-cycle-08-red-source.txt) and [test](tdd-cycle-08-red-test-source.txt) | [New-generation ID failed](tdd-cycle-08-red.xml): expected new 1/1/1, got old 8/1/0. | [Seven SQLite tests](tdd-cycle-08-green.xml) and [two session tests](tdd-cycle-08-green-session.xml) pass; nine total, exit 0. |

The tests call the public session request/result/status interface and assert
literal required outcomes. They do not inspect private fields or substitute
internal collaborators. Each failure was observed before the minimal change
for that cycle. The [tooling record](../../specifications/m2-8-tdd-tooling-and-boundaries.md)
contains versions, commands, owner boundary confirmation and scope limits.
The SQLite case uses a real temporary file through the public command/snapshot
interface, closes/reopens its connection, then retries the accepted ID. Cycle 04
adds a separate child JVM abruptly halted before commit; [ST02 details](st02-interrupted-write.md)
explain the checkpoint, red/green distinction and recovery/retry observations.
Cycle 05 adds [ST04 shared-store concurrency](st04-concurrent-commands.md): eight
distinct commands receive separate durable revisions. Simultaneous duplicates,
including retry after reopening, are covered by the additional verification
below. Independent writers, power loss, full match/Undo and Android Room remain untested.

## Additional verification of existing behavior

[ST04 duplicate delivery](st04-duplicate-commands.md) passed on its first run
with the existing durable receipts and command serialization. Eight calls for
one ID all return 8/1; reopening and retrying leave state at 8/1. No probe
implementation changed, and that verification adds no red/green cycle. At that
checkpoint the suite passed six tests: [four SQLite cases](st04-duplicate-commands-green.xml)
and [two session cases](st04-duplicate-commands-session-green.xml).

[ST05 reset evidence](st05-reset-isolation.md) retains the contract, three new
red/green slices and two further first-run verifications. Cancellation preserves
state/epoch/receipts: [eight SQLite tests](st05-canceled-reset-green.xml) and
[two session tests](st05-canceled-reset-green-session.xml) pass. A child JVM
interrupted during reset before commit preserves the old epoch and accepted
state, then permits a deliberate reset retry. The store is unchanged for those
two checks; the child launcher was shared during green review.

[ST03 lost-acknowledgment verification](st03-lost-acknowledgment.md) then halts
a child JVM after committing a point but before sending its receipt. Reopening
preserves 9/2/0; retries return that original receipt without applying another
point or replacing later 10/3/0 state. It passed on its first run with the
unchanged store implementation and adds no red/green cycle.

Current complete suite: [ten SQLite tests](st03-lost-acknowledgment-green.xml) and
[two session tests](st03-lost-acknowledgment-green-session.xml), twelve passed,
zero failures/errors/skips, exit 0. There are eight observed red/green cycles
and four separate first-run verifications. Full match/Undo, other fault timings,
store-replacement identity, migration, independent writers, Android and device
recovery remain outside this result.
