# ST04: simultaneous duplicate delivery through one host store

Recorded: 2026-10-23 (America/New_York)  
Result: Local T01 verification passed on its first run; existing implementation unchanged.  
Task: [M2.5](../../subplans/m2-5-lifecycle-and-storage-feasibility.md)  
Boundary: Owner-confirmed T01 public commands, receipts/snapshots and real-store recovery.  
Acceptance reference: ST04 with ST01/ST03 retry recovery; FR-05/FR-06 and future V06 evidence.

## Scenario and observation

Eight worker threads wait at one start gate, then each submits `shared-point`
through the same public store instance backed by a real temporary SQLite file.
All expected snapshots are literal outcomes from the fixture's 7/0 baseline.
The test observes only public command results and snapshots, including after
reopening; it neither queries private tables nor mocks the store.

| Step | Expected and observed result |
| --- | --- |
| Start from points 7, revision 0 | No command has yet been accepted. |
| Submit the same point command ID eight times together | All eight calls return points 8, revision 1. |
| Read the current public snapshot after all calls finish | Points 8, revision 1: only one point was applied. |
| Close and reopen the real file | Points 8, revision 1 remain durable. |
| Retry `shared-point` after reopening | Return 8/1 again and leave current state at 8/1. |

The harness uses eight workers, 10-second coordination/result waits and a
5-second cleanup wait. These bound the experiment; they are not product
capacity or latency promises. Workers terminate before the store closes.

## First-run verification and retained results

The new test passed against the existing implementation: cycle 03 already
provided durable receipts and cycle 05 serialized commands through the shared
instance. No implementation or dependency change was needed. There was no
observed missing-behavior red result for this check; it is additional
verification of existing behavior, not a sixth red/green cycle. Future missing
behavior or bug fixes continue to require their own observed red before a fix.

The complete offline run passed six tests, zero failures/errors, exit code 0:

- [SQLite report](st04-duplicate-commands-green.xml): four tests, including
  `simultaneousRetriesOfOnePointApplyOnceAndSurviveReopen`.
- [Session report](st04-duplicate-commands-session-green.xml): two tests.
- [Public behavioral test](../../../feasibility/src/test/kotlin/livestream/feasibility/DurableCommandProbeTest.kt)
  and [unchanged probe](../../../feasibility/src/main/kotlin/livestream/feasibility/DurableCommandProbe.kt).

Ran the existing command from the workspace root:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File tools/m2/run-feasibility.ps1 test --offline
```

The probe's SHA-256 matched its pre-check baseline:
`c28ddba24366727b390baea190d9a1f38d7e3b30b4337b882c9010c68e859df6`.
Retained reports replace only the workstation hostname with `local-host`;
assertions, counts and timestamps are unchanged. Review found no necessary
refactor for this bounded check. No account, device or provider action occurred.

## Evidence limits and next work

Together with the [distinct-command experiment](st04-concurrent-commands.md),
this covers the shared-instance host cases for concurrent distinct commands
and identical-ID delivery. It does not establish independent writers/processes,
mixed-command stress, snapshot/close races, stale-revision policy, reset/late
commands, full match/Undo, Android Room or remote transport behavior.

M2.5 remains In progress; D04/D08/D11 and G2 remain open. Next independent
local work is ST05 reset/late-command isolation, beginning with the probe's
identity/reset contract. Final application storage/protocol choices and
physical/provider measurements retain their existing unresolved status.

The subsequent [ST05 experiment](st05-reset-isolation.md) supplies the specified
host reset/late-command cases. The six-test result and unchanged-store hash
above are historical observations of this duplicate-delivery checkpoint.
