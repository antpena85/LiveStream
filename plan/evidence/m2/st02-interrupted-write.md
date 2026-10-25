# ST02: host process interruption before commit

Recorded: 2026-10-23 (America/New_York)  
Result: Local T01 experiment passed; Android/power-loss validation remains pending.  
Task: [M2.5](../../subplans/m2-5-lifecycle-and-storage-feasibility.md)  
Boundary: Existing owner-confirmed T01 command/snapshot and real-store recovery.

## Scenario and observation

The test uses a real temporary SQLite file and a separate JVM. It observes only
public command receipts/snapshots and the child process result, without reading
private database tables. The child's deliberate halt is a probe-only external
process fault; it does not alter the application's scoring or controller policy.

| Step | Expected and observed result in the passing run |
| --- | --- |
| Accept an earlier point from the fixture's 7/0 baseline | Points 8, revision 1 are committed. |
| Execute a different point in the child JVM | Its candidate state and receipt are written within the existing transaction. |
| Halt that child immediately before commit | Exit code 23; no normal connection close, shutdown hook or finally-block cleanup. |
| Reopen through the public probe | Points 8, revision 1: the interrupted candidate did not replace the accepted state. |
| Retry the interrupted command ID | Points 9, revision 2 are accepted. |
| Repeat the same command ID | Its receipt remains 9/2 and the current state stays 9/2; no second point. |

## Observed red, minimum change and green

The first fixture halted **after** `recordPoint` committed. The new test failed
with expected 8/1 but observed 9/2 after reopening; the child still exited with
the expected code. This exposed the missing before-commit interruption support
in the fault fixture, not a newly discovered SQLite transaction defect.

The minimum change adds an optional before-commit checkpoint to the isolated
store and passes the abrupt halt from the child fixture through it. Existing
transaction/receipt SQL is unchanged. The checkpoint defaults to no action;
ordinary probe commands retain their prior path. The crash fixture supplies
the process fault after both candidate writes and before the transaction commit.

- [Red result](tdd-cycle-04-red.xml): the SQLite suite had two tests, one failed;
  the other three tests in the complete run passed.
- [Pre-fix child fixture](tdd-cycle-04-red-source.txt) and
  [pre-fix store](tdd-cycle-04-red-store-source.txt).
- [Green result](tdd-cycle-04-green.xml): both SQLite tests passed; the final
  complete suite passed four tests, zero failures, exit code 0.
- [Child fixture](../../../feasibility/src/main/kotlin/livestream/feasibility/InterruptedWriteProcess.kt)
  and [public behavioral test](../../../feasibility/src/test/kotlin/livestream/feasibility/DurableCommandProbeTest.kt).

Ran the existing command from the workspace root:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File tools/m2/run-feasibility.ps1 test --offline
```

The child wait is bounded to 20 seconds with cleanup of that child on timeout.
These are test-harness limits, not product performance requirements. Retained
JUnit reports redact the workstation hostname; assertions/counts/timestamps
are unchanged. No new dependency, device access, credential or provider action
was needed.

## Evidence limits and next work

This supplies the host before-commit termination case in ST02 and extends
ST01/ST03 recovery/retry evidence. It covers one deterministic checkpoint in a
points-only fixture. It does not establish every interruption timing, power
failure, Android process death/Room behavior, full match/Undo recovery,
concurrent writers, migrations or Q45 publisher shutdown.

M2.5 remains In progress and D04/D08/D11/G2 remain open. The next independent
local investigation is ST04's concurrent-command storage behavior; physical
lifecycle and provider checks retain their previously recorded external inputs.
