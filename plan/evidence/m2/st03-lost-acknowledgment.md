# ST03: commit followed by lost acknowledgment

Recorded: 2026-10-24 (America/New_York)  
Result: Host post-commit interruption/retry verification passed on its first run.  
Task: [M2.5](../../subplans/m2-5-lifecycle-and-storage-feasibility.md)  
Boundary: Existing owner-confirmed T01 command receipts, current snapshots and real-store reopen.

## Contract specified before execution

Extend the existing points-only SQLite probe's child-process fixture. Begin
with an accepted point at 8/1/0 (points/revision/epoch). In a separate JVM,
accept a different command, then halt abruptly after `recordPoint` returns
from its committed transaction and before sending its receipt to the parent.
Halt inside the open-store scope, without normal connection close or shutdown
cleanup. The parent must receive the deliberate fault exit, with no receipt.

After reopening through T01, the current snapshot must be 9/2/0. Retrying
the unacknowledged command ID must return its original 9/2/0 receipt without
applying another point. Accepting a later command must reach 10/3/0; further
retries of the unacknowledged command must still return 9/2/0 while preserving
current state 10/3/0, including after another reopen.

Observe public receipts/snapshots and the child process result only. Do not
query database tables or mock storage. This extends ST03's earlier graceful
reopen/retry evidence with one abrupt post-commit process interruption.

The existing transaction/receipt implementation is expected to satisfy this
contract. A first-run pass is additional verification, not a red/green cycle;
do not remove existing correct behavior to manufacture a failure. No new
application API, policy, dependency or storage mechanism is proposed.

## Observed result

The child fixture now supports `interrupt-after-commit`. It leaves the existing
before-commit checkpoint inactive for this mode, calls the unchanged store's
public point command, then uses `Runtime.halt(23)` before printing the receipt
or leaving the open-store scope. The parent observes exit 23 and empty output.
The earlier point/reset before-commit modes still pass their existing tests.

| Observation through T01 | Expected and observed points/revision/epoch |
| --- | --- |
| Accepted point before launching the child | 8/1/0 |
| Reopen after the child committed but sent no receipt | 9/2/0 |
| Retry the unacknowledged ID; read current state | Receipt 9/2/0; current 9/2/0 |
| Accept a later command | 10/3/0 |
| Retry the unacknowledged ID again | Original receipt 9/2/0; current remains 10/3/0 |
| Reopen again and retry | Original receipt 9/2/0; current remains 10/3/0 |

The first complete offline run passed [ten SQLite tests](st03-lost-acknowledgment-green.xml)
and [two session tests](st03-lost-acknowledgment-green-session.xml): twelve
passed, zero failures/errors/skips, exit 0. Reports redact only the workstation
hostname; result counts, assertions and timestamps are unchanged.

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File tools/m2/run-feasibility.ps1 test --offline
```

Only the [child fault fixture](../../../feasibility/src/main/kotlin/livestream/feasibility/InterruptedWriteProcess.kt)
and [public behavioral test/launcher](../../../feasibility/src/test/kotlin/livestream/feasibility/DurableCommandProbeTest.kt)
changed. The launch helper was renamed to describe both interruption timings;
it returns the captured output and retains its 20-second wait/timeout cleanup.
These are harness bounds, not product response targets. Review found no
necessary storage refactor or additional dependency.

The store SHA-256 before and after verification is
`0BCFAB08B1069988A7959B8EEABFF42AEA4841CFEEE69E1CA341EC5139619C59`.
This is the fourth first-run verification; the observed red/green cycle count
remains eight. No new storage defect or fix is claimed.

## Limits and next work

This covers one post-commit process interruption in the points-only host
fixture. It models a lost reply at the child/parent process boundary, without
exercising an Android controller transport, packet loss, publisher or device.
It does not establish all crash timings, power-loss recovery, full match/Undo,
reset-request deduplication, receipt expiry, migration, file replacement or
independent writers. ST06's Q45 stop/recovery sequence and ST07's migration
remain untested. The final application schema and D04/D08/D11/D13 remain open.

The next independent host investigation can specify ST06's durable pending-stop
contract and fault checkpoints at T01/T02 before an isolated TDD experiment.
Keep proposed ordering separate from the confirmed Q45 policy and final D10
decisions. Physical lifecycle/media, provider, topology and upload checks retain
their already documented external inputs. M2.5/M2.8 stay In progress and G2 has
not passed.
