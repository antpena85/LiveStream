# M2 consistent safety read: proposed host experiment

Recorded: 2026-11-05. Status: host T01 slices and a combined-read T02 Start
slice completed; callback and independent-writer consistency remain open. This refines the owner-confirmed T01 match-command and T02 session
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

## Post-Live notification: stop-dispatch failure and retry

At confirmed T01/T02, start a simulated publisher and commit a point with a
pending-stop marker for that session in the real temporary store. When explicit
safety notification dispatches a stop that throws, T02 must stay Stopping and
retain the accepted point and marker. After the simulated stop boundary recovers,
a deliberate second notification must request stop for the same session; neither
attempt proves termination or clears the marker. Verify state again after reopen.
This covers a sequential failure/retry only, not automatic notification delivery,
concurrent notifications, process death or provider termination.

The [targeted first-run report](../evidence/m2/live-fit-stop-failure-first-run.xml)
passed with unchanged implementation, so this is verification, not a red/green
cycle. The forced offline JVM suite passed 89 tests with no failures, errors or
skips; the local-only media build succeeded with all 35 tasks up to date and does
not exercise the host publisher adapter or store. A stop request remains distinct
from a verified stop, and the test does not prove safe delivery of notifications.

## Safety notification while connecting

At confirmed T01/T02, begin a confirmed simulated publisher connection and hold
its callback. Commit a real-store point with simulated fit failure and a durable
stop marker for the active session. On explicit safety-change notification, T02
must enter Stopping and request stop immediately, without waiting for a connection
callback that might never arrive. If the callback arrives late, remain Stopping
and request stop again for that same session. Reopening must retain the accepted
point and marker; neither request confirms termination. This sequential controlled
host case does not establish automatic notification delivery, atomic combined
reads, concurrency or real publisher behavior.

The [red result](../evidence/m2/connecting-fit-notification-red.xml) observed
`Connecting` rather than `Stopping`; the [pre-fix session](../evidence/m2/connecting-fit-notification-red-source.txt)
is retained. The [green result](../evidence/m2/connecting-fit-notification-green.xml)
passes after applying the same pending-stop recheck to Connecting. The forced
offline JVM suite passed 90 tests, zero failures, errors or skips. The local-only
Android media build succeeded with five tasks executed; its activity does not
use this notification and the host publisher is not included. This does not
prove notification delivery, stop success, or safe real broadcasting.

## Safety notification with unavailable store while connecting

At T02, begin a confirmed connection using a real temporary store, then close
the store and explicitly deliver a safety-change notification before the held
connection callback. The session must enter Stopping and request stop for its
active session; a late callback must not report Live, and another Start must not
be accepted. This verifies fail-closed handling of this explicit signal only;
it does not detect store failure without a signal, establish automatic delivery,
or prove the external publisher stopped.

The [first-run result](../evidence/m2/connecting-notification-store-unavailable-first-run.xml)
passed with unchanged implementation; it is verification rather than a red/green
cycle. The forced offline JVM suite passed 91 tests with no failures, errors or
skips. The local-only Android media build succeeded with all 35 tasks up to date;
it does not use the host store or publisher adapter. No Android device or external
publisher was exercised.

## Proposed mixed-view Start experiment

At T01/T02, arrange a deterministic checkpoint after the old implementation has
read an empty pending-stop set but before it reads the snapshot. At the checkpoint,
commit a point and simulated fit-failure stop marker through the real store. Supply
the independently specified post-commit `8/1/0` assessment to Start. The old
separate reads can combine the pre-commit empty marker set with the post-commit
snapshot and incorrectly accept Start. A single committed-view read must instead
either see the old view and reject the assessment or see the new view and reject
the pending stop; it must never accept that mixed view. The test checkpoint must
not replace the actual SQLite store. A single SQL query is one candidate for a
host-only combined read, not a choice of Android storage or an external-publisher
atomicity guarantee. Test marker/point durability and receipt after reopening.

The [red report](../evidence/m2/mixed-safety-start-red.xml) shows Start accepted
the mixed view; the [pre-fix session](../evidence/m2/mixed-safety-start-red-source.txt)
is retained. The session now uses a mandatory combined safety view at Start;
the SQLite fixture supplies it through one SELECT. The [green report](../evidence/m2/mixed-safety-start-green.xml)
passes with the checkpoint asserted as reached and the real store checked after
reopen. The checkpoint was adapted for the new combined interface: the red run
commits between the old reads; the green run commits immediately before the new
combined read. Consequently this establishes rejection of that new committed view,
not an adversarial commit during the SQL statement. The forced offline JVM suite
passed 92 tests without failures, errors or skips. The local-only media APK
compiled the changed shared interface and session; it has no Android implementation
of the safety view and did not exercise the host SQLite store.

Connection callbacks still use the separate reads; this slice changes Start only.
Independent writers during a combined query, concurrent access to the shared
reader, error handling under contention, Android persistence, automatic safety
notifications and external publisher race/termination remain unverified. A
consistent view is not a guarantee that the view stays current after the read.

## Combined view across separate store instances

Use two real probe instances on the same fresh SQLite file. Through the reader's
public safety view, observe the original `7/0/0` and no stops. Commit a point
with a simulated fit failure through the other instance. A subsequent reader
view must contain `8/1/0` and the marker together; retry the point and verify
the same view after reopening. This is a sequential cross-connection check, not a
controlled commit during a SELECT or proof of concurrent reader thread safety.
It uses the existing T01 public state/result boundary and the proposed shared
safety-view seam. A first-run pass is verification, not a behavioral red/green.

The [first-run result](../evidence/m2/safety-view-independent-store-first-run.xml)
passed with unchanged store logic. A forced offline JVM run passed 93 tests with
no failures, errors or skips. The local-only media build succeeded with 35 tasks
up to date; it does not exercise the host store. No concurrent interleaving,
Android persistence, or callback behavior is established by this test.