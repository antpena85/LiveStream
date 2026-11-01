# M2 ST06: proposed durable numeric-fit stop experiment

Recorded: 2026-10-30

Status: Host T01 reopen/retry and before/after-commit interruptions pass;
publisher-dependent checkpoints and observations remain pending.

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

## First host slice — 2026-10-30

In a real temporary SQLite store, one point with a simulated fit failure commits
8 points/revision 1/epoch 0 and the marker `session-a`. After a normal close and
reopen, the public snapshot and marker remain; retry returns the original receipt
without applying another point. The [red result](../evidence/m2/st06-pending-stop-red.xml)
expected `session-a` but observed null with the [pre-fix probe](../evidence/m2/st06-pending-stop-red-source.txt).
The [green store suite](../evidence/m2/st06-pending-stop-green.xml) contains
11 passing tests. A forced full offline JVM run passed 33 tests. The earlier
compilation error caused by an editing error was not a behavioral red.

This is a normal close/reopen check, not an interruption or proof of atomicity
under failure. At this checkpoint the fixture permitted replacing its single marker and had
no publisher, stop confirmation, fit re-evaluation, notification or start gate;
it must not be used as a complete session-safety design.

## Before-commit interruption — 2026-10-30

The child executes a fit-failure point in the real temporary SQLite store and
prints a checkpoint immediately before the deliberate `halt(23)`, after the
candidate point, receipt and stop marker writes but before commit. The parent
observes the checkpoint and exit 23 without an acknowledgment. On reopening,
the previous accepted 8/1/0 and its receipt remain, with no stop marker. A
retry accepts 9/2/0 with marker `session-a`; a duplicate returns the same
receipt, and a second reopen retains the new state and marker.

This [targeted test](../evidence/m2/st06-before-commit-first-run.xml) passed on
its first run using unchanged store logic; the child fault fixture was extended
for this scenario. It is a verification, not a new red/green cycle. A forced
full offline run passed 34 tests, including the [12 store cases](../evidence/m2/st06-before-commit-full-store.xml).
Reports redact the host name only. This covers one abrupt host-process checkpoint,
not all timings, power loss, Android, publishing, or received output. Next,
investigate the post-commit lost-acknowledgment checkpoint and session replacement
separately; stop dispatch and recovery remain unimplemented.

## Post-commit lost acknowledgment — 2026-10-30

After an earlier accepted 8/1/0, a child submits a fit-failure point and emits
only a checkpoint after the command returns from commit, then halts with exit 23
before sending its receipt or closing the store. Reopening exposes 9/2/0 and
marker `session-a`. Retrying returns the original 9/2/0 receipt. A subsequent
point reaches 10/3/0; retries still return 9/2/0 without replacing the newer
snapshot or marker, including after another reopen.

The [targeted test](../evidence/m2/st06-after-commit-first-run.xml) passed on
its first run with unchanged store logic; only the child fault mode and test
were extended. This is verification, not a new red/green cycle. A forced offline
full run passed 35 tests, including [13 store cases](../evidence/m2/st06-after-commit-full-store.xml).
Host names in retained reports are redacted. The checkpoint is not a receipt or
publisher acknowledgment, and this host experiment does not establish Android
recovery, power-loss durability, stop dispatch or actual broadcast termination.
Next, investigate session replacement and stale completion safety before
attempting publisher-dependent stop/reconciliation checkpoints.

## Session-matched confirmation — 2026-10-30

The isolated store now accepts an externally supplied stop confirmation for a
session and removes a pending marker only if its session matches. The test
confirms A, creates B, reopens, and verifies a delayed A confirmation leaves B
intact; a matching B confirmation clears it and a duplicate does nothing.
Points and revision remain 9/2/0. The [red test](../evidence/m2/st06-stale-stop-red.xml)
failed because the [pre-fix probe](../evidence/m2/st06-stale-stop-red-source.txt)
could not clear even A. The [green store suite](../evidence/m2/st06-stale-stop-green.xml)
passed 14 cases; the forced full offline JVM run passed 36 tests.

This tests a supplied confirmation, not whether a publisher really stopped.
It assumes distinct session identifiers and does not test identifier reuse,
independent writers, a crash during marker clearance, or callback authentication.
At this checkpoint the single-marker fixture still permitted an unconfirmed A
marker to be replaced by B; it did not enforce the proposed Start gate.

## Distinct unresolved stop markers — 2026-10-30

The host fixture now retains distinct unresolved session markers together while
accepting the second point. After reopen, both A and B remain, retrying B returns
its 9/2/0 receipt without another point, and confirming A leaves B intact across
another reopen until B is confirmed. This is a defensive recovery experiment,
not permission for multiple simultaneous broadcasts. The [red result](../evidence/m2/st06-multiple-stops-red.xml)
observed B but lost A with the [pre-fix probe](../evidence/m2/st06-multiple-stops-red-source.txt).
The [green store suite](../evidence/m2/st06-multiple-stops-green.xml) passed
15 cases; the forced full offline JVM run passed 37 tests.

The probe uses a new table and fresh temporary files; **no migration from the
prior single-marker probe schema is provided or tested**. The singular accessor
fails rather than reporting no pending stop when multiple markers exist; callers
must use the set accessor for that case. This host result does not implement
start gating, authenticate confirmations, prove provider termination, test
identifier reuse or independent writers, or resolve how many old sessions a
real recovery protocol must retain. The next section investigates the pending
stop portion of a T02 start gate; unresolved fit remains separate.

## T02 pending-stop start gate — 2026-10-30

With a real temporary store reopened after a fit-failure point, a newly created
session denies a confirmed, permission-ready Start while a durable stop marker
exists, remains stopped, and leaves the point and marker unchanged. The
[red result](../evidence/m2/st06-start-pending-red.xml) returned Accepted instead
of PendingStop with the [pre-fix session](../evidence/m2/st06-start-pending-red-source.txt).
The [green session suite](../evidence/m2/st06-start-pending-green.xml) passed four
tests; a forced full offline run passed 38 tests. The session queries the real
reopened probe store; it does not replace it with a mock.

This is one modeled initial Start request, not a complete concurrent session
protocol. The optional store preserves older standalone fixture behavior, and
therefore does not enforce recovery for callers constructed without a store.
Storage read failures, concurrent marker creation, later requests while already
connecting, actual publisher state, and operator notice are untested. Most
importantly, clearing a pending stop does not establish that numeric fit is
resolved: at this checkpoint the probe had no separate fit-readiness gate.

## T02 explicitly supplied numeric-fit readiness — 2026-10-30

A separate T02 test records a fit-failure point, supplies a stop confirmation,
reopens the real store, and requests confirmed Start with numeric fit explicitly
reported unresolved. The store has no pending marker, yet Start is denied,
status remains stopped and the accepted point survives. The
[red result](../evidence/m2/st06-start-fit-red.xml) returned Accepted rather than
NumericFitUnresolved with the [pre-fix session](../evidence/m2/st06-start-fit-red-source.txt).
The [green session suite](../evidence/m2/st06-start-fit-green.xml) passes five
tests; a forced full offline run passes 39 tests.

At this checkpoint readiness was supplied by the caller and defaulted to ready.
The fixture did not determine fit, bind it to a match revision, persist it or
validate rendered output.
Stop confirmation is simulated, not proof of publisher termination. Successful
restart after a fresh fit assessment, unknown readiness, concurrent changes,
real capture, notices and recovery remain untested. Next investigate a fail-closed,
revision-bound readiness contract before treating this gate as recovery evidence.

## T02 missing assessment — 2026-10-30

After a fit-failure point and simulated stop confirmation, reopening the real
store and requesting Start without a fit assessment now returns
NumericFitUnresolved, stays stopped, and preserves the 8/1/0 match. The
[red result](../evidence/m2/st06-start-unknown-red.xml) returned Accepted with
the [pre-fix session](../evidence/m2/st06-start-unknown-red-source.txt). The
[green session suite](../evidence/m2/st06-start-unknown-green.xml) passes six
tests; a forced offline full run passes 40 tests. The probe now defaults the
modeled readiness input to false rather than assuming readiness.

This only fails closed for an omitted boolean. An explicitly supplied true
value can still be stale or unrelated to the current match, and the optional
store can still be omitted. Actual fit detection, revision/epoch binding,
concurrent changes, and publisher recovery remain open. Next test a stale
assessment against a changed real-store revision before implementing any
revision-bound mechanism.

## T02 stale snapshot assessment — 2026-10-30

In a fresh real store, a modeled ready assessment references 8/1/0; a second
point advances the match to 9/2/0. After reopening, Start with the earlier
snapshot is denied and the session stays stopped. The [red result](../evidence/m2/st06-start-stale-red.xml)
returned Accepted with the [pre-fix session](../evidence/m2/st06-start-stale-red-source.txt).
The [green session suite](../evidence/m2/st06-start-stale-green.xml) passes seven
tests; a forced offline full run passes 41 tests. The probe now requires an
explicit ready flag, a store and a supplied snapshot equal to the current
points/revision/epoch snapshot. Missing values fail closed.

This is a host-only snapshot comparison, not verified rendering or a complete
revision-bound authorization protocol. The test covers a changed revision, not
epoch reset, file replacement, a matching-assessment success path at that checkpoint, or changes
between checking and connecting. It does not establish atomicity with publishing,
assessment provenance, other layout inputs, real stop confirmation or Android
recovery. No application identity, concurrency or media decisions are settled.
Next verify deliberate Start with a matching assessment and all prerequisites,
then separately examine stale epoch and concurrent-change safety.

## T02 matching snapshot assessment — 2026-10-30

After a fit-failure point and simulated stop confirmation, the test models a
fresh ready assessment of the 8/1/0 snapshot, reopens the real store, and
requests Start with confirmation and both permissions. The session returns
Accepted and enters Connecting; the snapshot and empty pending-stop set remain
unchanged. The [targeted result](../evidence/m2/st06-start-matching-first-run.xml)
passed on its first run with unchanged session logic, so it is verification,
not a new red/green cycle. The [full session suite](../evidence/m2/st06-start-matching-full-session.xml)
passed eight cases; the forced full offline run passed 42 tests.

This verifies the modeled transition only: Connecting is not a live broadcast,
and the test supplies rather than measures readiness or stop confirmation. It
does not cover stale epochs, snapshot reuse after file replacement, other layout
inputs, changes between the check and connection, or real publisher behavior.
Next verify rejection of a prior epoch assessment even when the revision and
point values coincide; concurrency and publisher reconciliation remain separate.

## T02 prior-epoch assessment — 2026-10-30

Two confirmed resets in a fresh real store produce 0 points/revision 0 in both
epoch 1 and epoch 2. After reopening in epoch 2, the modeled session rejects a
ready assessment for epoch 1 and stays stopped. The [targeted test](../evidence/m2/st06-start-old-epoch-first-run.xml)
passed on its first run with unchanged session and store logic; this is
verification, not a new red/green cycle. The [full session suite](../evidence/m2/st06-start-old-epoch-full-session.xml)
passed nine cases and a forced full offline run passed 43 tests.

The epoch is local to this probe file, not a selected application identity.
This result does not cover file replacement or rollback, concurrent state
changes between assessment and connection, rendered fit, actual publishing or
Android recovery. Next investigate that check-to-connect race with a deterministic
host experiment; no hardware or provider result is implied.

## Proposed check-to-connect race experiment — 2026-10-30

Status: design only; no race test or fix has run. Existing T01 match commands
and T02 session control remain the owner-confirmed public boundaries. Model an
external publisher as a controllable boundary, not a mock match/store. The
current T02 probe only changes an in-memory status and has no publisher adapter,
so it cannot yet observe or prevent a real connection attempt.

The safety question is not whether a snapshot matched at some earlier instant:
a point, reset, fit failure, or pending stop may be committed after that check
and before an external publisher begins accepting output. A host-only lock
around a status assignment would not by itself protect asynchronous callbacks,
another store instance or a publisher already connecting. SQLite cannot roll
back the external effect. The exact serialization and reconciliation mechanism
remains proposed and must not be treated as a settled D04/D08/D10 decision.

Proposed first deterministic scenario:

1. Use a fresh real store and a supplied ready assessment for snapshot 8/1/0,
   with no pending stop. Request confirmed Start through T02. Arrange the
   controllable external publisher to pause before its connection takes effect;
   observe that the request has reached that boundary without using sleeps.
2. While paused, accept a different point through T01, yielding 9/2/0 with a
   simulated fit failure and a durable pending stop for the affected session.
   Verify the accepted receipt and marker through the public store interface.
3. Release the publisher boundary. The proposed invariant is that an obsolete
   Start must not result in unsafe publication or an authoritative live state.
   It must either be canceled before the effect or, if an effect cannot be
   canceled, enter an explicit stopping/unknown state and reconcile that exact
   session while retaining the match and stop marker. Do not report stopped
   merely because a stop was requested.
4. Inspect only T01/T02 results and externally observed publisher requests;
   bound waits and clean up the worker on failure. Record the actual timeline,
   failure mode and red result before the smallest corrective change. An
   infrastructure timeout is not a behavioral red.

The scenario needs an explicit simulated publisher contract: session identity,
when a connection or cancellation takes effect, callbacks after cancellation,
and what evidence counts as stop confirmation. A fake must permit late or
failed effects rather than making cancellation automatically succeed. A second
case should cover an ordinary revision change without a fit failure and specify
whether fresh assessment cancels or restarts the attempt. Also investigate
independent writers and changes after the eventual connection; neither is covered
by the first interleaving. The host probe must not claim that production can
guarantee no unreadable frame or that a provider has stopped. Actual publisher,
Android lifecycle, received-video and operator-notice evidence remain required.