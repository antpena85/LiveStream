# M2 consistent safety read: proposed host experiment

Recorded: 2026-11-05. Status: two narrow host T01 red/green slices completed;
combined T02 safety read remains proposed. This refines the owner-confirmed T01 match-command and T02 session
control boundaries, not the release application's storage API. See the
[ST06 contract](m2-st06-pending-stop-contract.md) and
[current safety seam](../evidence/m2/session-safety-seam-2026-11-05.md).

## Proposed observable contract

When T02 evaluates a confirmed Start or a connection callback, the match
snapshot and all pending-stop markers must come from one committed store view.
T02 must not accept an assessment for a different points/revision/epoch, nor
report Live while that view contains any pending stop. A failed read must deny
Start or require Stopping on a callback, as appropriate. These rules do not
replace the separately supplied numeric-fit assessment or establish that it
matches a rendered frame.

The existing `SessionSafetyState` exposes two reads. The host fixture writes a
point, receipt, and optional stop marker in one transaction, but its unsynchronized
snapshot read and separate pending-marker read do not establish a single
committed view for a concurrent session. This is a gap in demonstrated behavior,
not evidence that every interleaving fails.

## First experiment: committed view during a held write

First narrow slice: use the existing synchronous pre-commit fault checkpoint
to observe the public T01 `snapshot()` during a point transaction and then
deliberately abort that transaction. The read must return the previous committed
snapshot, not the point that will be rolled back. After the failure, verify the
same snapshot and absence of the pending-stop marker through T01. This uses a
real store and a deterministic fault boundary, not a simulated store or a timed
concurrent schedule. It covers uncommitted snapshot exposure only; it does not
establish a combined T02 read, cross-connection isolation, or the held-write
start interleaving described below.

Use a fresh real temporary SQLite store and a deterministic, bounded write
checkpoint before commit. Through T01, begin an accepted point with a simulated
fit failure and hold the transaction before commit. Concurrently request a
confirmed T02 Start using an assessment for the original committed snapshot.
Release the transaction and allow it to commit. The session may conservatively
deny or wait for a consistent view; it must not accept Start using a mixture of
uncommitted and committed data. The public outcome must be checked alongside the
T01 receipt, final snapshot and durable marker. A controlled rollback variant is
needed separately: no reader may use the rolled-back point or marker as committed
state. Test coordination must not depend on sleep or an unbounded worker; retain
actual red output and pre-fix source before changing the implementation.

The first concrete test must choose one explicit observable result and arrange an
interleaving that distinguishes the old implementation from the new behavior.
If the existing public seams cannot deterministically expose that interleaving,
document the limitation and design a suitable test fixture before implementation;
a compilation failure, scheduler-dependent timeout, or a first-run pass is not a
behavioral red. T01 covers durable command effects; T02 covers session decisions.
Neither alone tests a real Android publisher.

## Boundaries and follow-up

- A proposed combined read must have a defined committed-view guarantee in each
  implementation; merely returning both values in an object is insufficient.
  SQLite connection ownership, transactions, same-instance synchronization and
  independent writers need separate investigation. No Android store is selected.
- Even a genuinely consistent read can become stale immediately afterward. The
  [publisher race contract](m2-simulated-publisher-race-contract.md) and actual
  external-effect reconciliation remain required; never claim no unsafe frame or
  confirmed termination from a read, status assignment, or stop request.
- Session callbacks, writes after Live, process death, fit rendering, provider
  behavior, and received video remain outside this single host experiment.
- Retain fail-closed behavior when safety state is unavailable. Do not clear a
  durable stop marker without separately established session-specific termination.

## First host slice result — 2026-11-05

The pre-commit callback read the public snapshot and then deliberately threw to
roll back the point and pending marker. The [red test](../evidence/m2/snapshot-rollback-red.xml)
observed `8/1/0` rather than the last committed `7/0/0`; the
[pre-fix source](../evidence/m2/snapshot-rollback-red-source.txt) is retained.
The host fixture now reads public snapshots on a separate SQLite connection,
while transactional operations read their own writes on the writer connection.
The [targeted green](../evidence/m2/snapshot-rollback-green.xml) verifies the
committed snapshot and the final rolled-back state and empty marker. A forced
offline full JVM run passed 84 tests, with zero failures, errors, or skips.
The separate local-only media debug build succeeded with 35 tasks up to date;
it does not compile or use the host SQLite fixture.

This test is a synchronous reentrant checkpoint, not a concurrent T02 test. It
does not establish an atomic snapshot/marker pair, cross-instance scheduling,
Android persistence or safety after a read. SQLite locking and read failures
remain relevant; no network, device, destination or G2 evidence was added.

## Next narrow slice: rolled-back pending marker

At T01, use the existing synchronous pre-commit fault checkpoint to read pending
stop markers while a point and its marker are uncommitted, then abort the write.
The observable marker set must be the last committed empty set, and the reopened
store must retain the original snapshot and no marker. This tests committed marker
visibility with a real store, not a T02 concurrency interleaving or an atomic pair.

The [red test](../evidence/m2/marker-rollback-red.xml) observed the uncommitted
`session-a` marker instead of an empty committed set; the
[pre-fix source](../evidence/m2/marker-rollback-red-source.txt) is retained.
The host fixture now reads public pending markers from its separate committed-read
connection. The [green test](../evidence/m2/marker-rollback-green.xml) also checks
the snapshot and marker after reopening. A forced full offline JVM run passed
85 tests with no failures, errors or skips. The local-only Android media build
again succeeded with 35 tasks up to date; it does not use the host SQLite fixture.

This is a synchronous reentrant fault checkpoint, not a concurrent test. It does
not establish an atomic pair: separate reads can straddle a commit, and access to
the shared read connection during concurrent callers is not established as safe.
The next experiment must address those conditions without mocking the real store
or counting an infrastructure timeout as a behavioral failure. No device,
destination, real publisher or G2 evidence was added.

## In-flight write and later callback: next T01/T02 verification

Use the real store's existing pre-commit fault checkpoint synchronously: begin a
point with simulated fit failure, request a confirmed session Start using the
previously committed fit assessment from within the checkpoint, then commit and
deliver a simulated connection callback. Start may use the old committed view;
after the commit, the callback must not report Live and the accepted point and
pending marker must remain durable after reopen. This deliberately does not
claim the two reads are atomic or that a real publisher cannot transmit before
the callback. A first-run pass is verification, not a new red/green cycle.

The [first-run report](../evidence/m2/safety-start-inflight-first-run.xml) passed
with unchanged session and store code. The point and marker survived reopening,
and a repeated command returned its original receipt. The forced offline full JVM
suite passed 86 tests with no failures, errors or skips. The local-only media APK
build succeeded with all 35 tasks up to date; it does not use the host store.
This is synchronous reentrant observation, not thread-safety, an atomic pair, a
publisher effect, or Android execution. The combined-read problem remains open.

## Callback during a write that subsequently rolls back

At confirmed T01/T02, start with a valid assessment of the real store's committed
`7/0/0` state. During a point transaction that has inserted a simulated pending
stop, invoke the connection callback at the existing pre-commit fault checkpoint,
then throw to roll back the transaction. The callback should observe the prior
committed state, not a marker or score that never committed; it may report Live
in this host model. Verify the rollback after reopening. This synchronous
interleaving does not show what happens when the write commits afterward and
does not establish a combined read or safe real publication.

The [targeted first run](../evidence/m2/safety-callback-rollback-first-run.xml)
passed with unchanged session and store logic, so it is verification rather
than a red/green cycle. The forced offline JVM suite passed 87 tests without
failures, errors or skips; the local-only Android media build succeeded with all
35 tasks up to date and does not use the host SQLite fixture. If the same write
commits after this callback, the session may already be Live and the current
probe has no notification path to request a stop: that important scenario remains
unverified and requires a separate safety contract and test before publishing.

## Proposed post-Live safety recheck slice

At confirmed T01/T02 boundaries use a real temporary store and controllable
publisher. Start session A and deliver its connection callback, then commit a
point with simulated fit failure and durable pending stop for A. Deliver an
explicit safety-change notification to T02 after the commit. T02 must read the
actual store, enter Stopping, and request stop for A while preserving the point,
receipt and pending marker. A stop request is not confirmation of termination.
The fixture callback is explicitly delivered by the test; no automatic event
delivery or recovery from lost notifications is implied. Rollback, unrelated
markers, read failure, duplicate delivery, concurrency and publisher-stop failure
are separate cases. This single slice does not settle the notification mechanism,
prove timely delivery, or authorize Android or real publishing.

The [behavioral red](../evidence/m2/live-fit-notification-red.xml) observed
`Live` instead of `Stopping` with an explicitly delivered notification; the
[pre-fix adapter](../evidence/m2/live-fit-notification-red-source.txt) and
[session](../evidence/m2/live-fit-notification-red-session-source.txt) are retained.
The [green test](../evidence/m2/live-fit-notification-green.xml) observes the
requested stop and durable marker. The forced offline JVM suite passed 88 tests
without failures, errors or skips; the local-only Android media APK rebuilt with
five tasks executed. The Android activity does not call this safety notification
and the host publisher adapter is not included there. No device run occurred.

The adapter does not automatically observe commits; callers must explicitly
deliver notifications. Lost or late notifications, missed pending stops during
connection, unrelated markers, repeated notifications, stop failures, real
termination and synchronized callbacks remain unverified. A safety recheck may
request a stop but must not clear the durable marker or assert that output stopped.