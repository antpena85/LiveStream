# M2 ST06: proposed durable numeric-fit stop experiment

Recorded: 2026-10-30

Status: Specification only; no ST06 test or publisher observation has run.

Task: [M2.5](../subplans/m2-5-lifecycle-and-storage-feasibility.md)

Authority: [PRD Q45](../../PRD.md) and
[lifecycle/storage investigation](m2-5-lifecycle-and-storage-feasibility.md).

## Confirmed policy and proposed mechanism

Q45 requires an otherwise valid numeric edit to be preserved in full when it
cannot fit legibly after layout adjustment. Broadcasting stops with a clear
notice, scoring remains available off-air, and a known unresolved fit failure
blocks Start. A later Start still requires normal confirmation and readiness.
Exact shutdown sequencing, fit detection and persistence are open technical
decisions; the following is an experiment, not an approved application design.

For a controlled host fixture, propose committing the accepted value, revision,
command receipt and a stop-required marker in one real-store transaction before
acknowledging that command. The marker identifies the affected publishing
session and the reason, not credentials. A failed transaction must not expose a
partial accepted command. Publication is an external effect and cannot be
rolled back with that transaction. Until a confirmed stop, expose stopping or
an error/unknown state, not a false stopped state. Block further unsafe output
and Start while fit failure or shutdown is unresolved; how to prevent an
unreadable frame at this boundary requires separate media investigation.

On recovery, restore committed match and receipt; do not automatically start a
broadcast. If a pending stop exists, surface the notice and reconcile the old
session conservatively. A repeated stop request must be safe for the same
session; an old completion must not clear a newer session's marker. Clear a
marker only after the relevant stop is confirmed or a documented, reliable
reconciliation establishes that session is no longer publishing. Unknown or
failed confirmation remains visible and blocks Start. A marker alone is not
proof of remote provider termination. Re-evaluate fit separately before any
subsequent confirmed Start; clearing a stop marker does not resolve fit.

## Proposed observable checkpoints

Use T01 commands/receipts/snapshots with a real temporary store and T02 public
session status against a controllable external publisher boundary. The fit
failure is a supplied test condition, not a claim about rendered readability.
The current points-only probe and in-memory session do not yet implement this
contract. A separate child process may provide abrupt termination checkpoints;
assert only observable results and retain red/green evidence for each new
behavior. Do not simulate a storage rollback by replacing the real store.

| Checkpoint | Expected observation after reopen/recovery |
| --- | --- |
| Before transaction commit | Previous accepted value and receipt remain; no new marker or acknowledgment. Retry can accept the edit once. An externally initiated precautionary stop, if any, cannot be rolled back. |
| After commit, before acknowledgment or stop dispatch | Full new value, revision, original receipt and session-specific pending stop survive together. Retry returns that receipt without another edit. Recovery does not auto-start; stop remains required. |
| After stop dispatch, before confirmation | Full value and marker survive; status is not falsely stopped. Recovery may safely retry/reconcile the same stop, without applying the edit again. |
| After publisher confirmation, before durable marker clear | Recovery conservatively retries or reconciles; repeated stop is safe and cannot affect a different session. No match value is lost. |
| After durable marker clear | Full value and receipt survive; recovery remains off-air, and unresolved fit still blocks Start. Subsequent off-air scoring remains available. |

Add explicit cases for failed/unknown stop, late callbacks for a different
session, repeated delivery, and later valid fit with deliberate Start. Test a
new case only after the preceding behavior is green; retain a first-run pass as
verification, not an invented red cycle. Check failure injection actually hits
the intended checkpoint and child termination is distinct from power loss.

## Boundaries and unresolved work

Host tests can investigate atomic storage and simulated session effects; they
cannot demonstrate actual camera/encoder shutdown, provider reception, Android
process death, durability under power loss, numeric rendering or human-visible
notice. Session identity, idempotence and reconciliation with a real provider,
bounded recovery, storage schema and safe handling of frames in flight remain
open under D04/D08/D10/D11. Do not infer an application stack or close G2 from
this proposal. Physical and received-broadcast evidence remains necessary.