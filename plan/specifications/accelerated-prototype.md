# Accelerated broadcast prototype

Date: 2026-11-01  
Status: Prototype direction authorized; implementation and physical broadcast pending.

## Objective and scope

Demonstrate one manually configured destination receiving video, built-in audio,
and a locally controlled scoreboard from one phone. Use a provisional media
candidate and the existing physical capture evidence as input, not as a final
stack decision. No sign-in, remote controller, adaptive quality, full scoring
rules, or sustained-match validation is claimed in this initial slice.

## Prototype gate and evidence

Before a trial, establish a buildable Android publisher with explicit confirmed
start and stop, actual connection/failure status, credential-safe handling,
cleanup on exit or failure, and the applicable scoreboard fit and stop safety
contracts. Test repeatable behavior at the previously confirmed T01–T06
boundaries. Never substitute the local-only capture permission gate for stream
authorization. The operator provides an approved test destination on-device;
no key, credential-bearing URL, raw provider error, or personal media is retained.

Check ADB visibility and authorization for the selected streaming phone before
deployment. The owner initiates a bounded trial and separately inspects the
received picture, updated score and audio. Record sanitized device/build and
results; API callbacks alone do not prove received output. A build or local
recording alone does not pass this gate.

## Deferred, not waived

Provider sign-in, direct watch pairing, phone remote, complete match behavior,
adaptation, recovery, sustained operation and the full device/provider matrix
remain required for the approved release. Existing G2 and release acceptance
remain open; this narrower prototype is not a G2 pass, compatibility claim, or
match-ready application. Review the initial dependency license risk before
adoption and complete the release dependency/license review before release.

## Immediate sequence

Owner-confirmed priority on 2026-11-06: **broadcast-first, code-first and
document-on-green**. Keep TDD: write a focused behavioral test at an already
confirmed boundary, observe red, implement the minimum and run green. Record
concise evidence and remaining risks after each coherent green slice; group
related cases and commits rather than creating a document for each test.
Pause speculative host-only race permutations unless they unblock integration
or address a concrete safety defect. First-run passes are verification, not
red/green cycles. Keep existing probe tests and evidence.

1. Reuse the isolated Android media experiment and its provisional candidate.
   Build a manual-destination publisher path with explicit confirmation,
   observable connection/failure state, safe credential handling and cleanup.
   Implement in small tested vertical slices; do not expose a real trial path
   before the safety requirements below are in place.
2. Integrate a real Android-compatible safety state and fit/stop safeguards:
   preserve values, fail closed on unresolved fit or pending stop, and do not
   claim stopped without appropriate confirmation. Do not substitute permissive
   stubs or infer Android guarantees from the host SQLite fixture.
3. Connect the local scoreboard to the publishing/encoded path. Build, run the
   relevant automated tests and document results after green. Continue host and
   build work while a physical device is unavailable.
4. Once the prototype gate is met, recheck ADB for the chosen streaming phone.
   The owner chooses destination and duration and initiates the bounded trial;
   separately inspect received video, changed score and built-in audio. Record
   sanitized observations and failures without retaining keys or personal media.

Provider sign-in, phone/watch controls, adaptation tuning, recovery stress and
the full device/provider matrix can proceed after this first demonstration or
in parallel when useful. They are deferred from this prototype, not removed
from release. M2 completion, G2 and M3–M8 authorization are unchanged.

No fixed time-box duration or test provider has been selected. Choose these with
the owner when needed rather than treating a proposed value as confirmed.

## Manual publisher control slice — 2026-11-08

An isolated host control now passes a validated manual endpoint to a supplied
publisher boundary only after the existing session accepts a confirmed start.
The new T02/T03 behavioral test first failed with `Cancelled` instead of
`Accepted`, then passed in the full offline JVM suite. This is an initial host
slice, not Android integration: it has no real publisher, connection callback,
stop dispatch, durable Android safety adapter, or credential-lifecycle proof.
The existing session and fit concurrency limitations still apply. No broadcast
or device validation is claimed; do not use this control for a trial.

The subsequent publisher-failure test initially failed because an exception
escaped; it then passed with a sanitized `PublisherFailed` result and the
session left `Stopping`, blocking another start. The full offline JVM suite
passed 106 tests. The media APK compiled the shared control successfully
(35 tasks: five executed), with the existing listener-parameter warning. This
only prevents this boundary from propagating the caught exception: it does not
prove library logging or credential safety, dispatch a stop, or integrate the
control with the activity. The prototype gate remains open.

## Android safety-store foundation — 2026-11-08

An isolated Android SQLite store now implements the shared probe read interface.
On an ADB-authorized Pixel 8 Pro (Android 17, API 37), an instrumented test first
reported `UnsupportedOperationException` for the unimplemented point/stop write;
after implementation it passed after closing and reopening the store. The point,
revision and session-specific pending-stop marker were present together. A
subsequent first-run integration assertion verified that `StreamSession` rejects
Start while that reopened marker exists. The debug and test APKs built offline;
the JVM suite was up to date and successful. The initial test-harness compilation
and wrong APK-path attempts were setup errors, not behavioral red results.

This is an isolated points-only store, not the activity's scoreboard or a live
publisher. It does not yet provide full match values, fit measurement, command
receipts, verified stop reconciliation, interruption testing or production
concurrency guarantees. Neither device storage result proves received output or
safe broadcasting. The next integration needs to connect real scoreboard state,
fit decisions and publisher stop behavior without bypassing these gaps.

## Local durable scoreboard integration — 2026-11-08

The local media activity now updates and renders two diagnostic scores through
an Android SQLite store, rather than resetting an in-memory fixture at each
capture. At the confirmed T01 boundary, an instrumented reopen test first
failed with the intentionally unimplemented score operation, then passed on
an authorized Pixel 8 Pro: left 1 and right 2 survived reopening, and the
existing pending-stop check also passed. The offline Android APK and test APK
built; the JVM suite was up to date. An initial installation attempt used an
incorrect APK filename and was corrected before the behavioral red run.

The activity wiring compiled but was not separately exercised on-device, and
no fresh encoded recording or broadcast was inspected. This points-only
diagnostic is not full match state, numeric fit measurement, stop dispatch or
verified stop reconciliation. Publishing remains disabled; the prototype gate
and G2 remain open.

## Atomic diagnostic score and pending stop — 2026-11-08

At the confirmed T01 Android real-store boundary, the combined operation now
commits the changed team score, probe revision and session-specific pending-stop
marker in one SQLite transaction. An instrumented test on an authorized Pixel
8 Pro first failed on the unimplemented operation, then passed after reopening:
the right score changed from 2 to 3, the marker survived, and Start was blocked.
Both Android APKs built offline; the JVM test task was successful and up to date.

This operation is not yet called by the activity or connected to fit assessment
or publisher stop dispatch. The test does not simulate process interruption or
verify a broadcast. Continue the integrated fit and stop path before enabling a
trial; this checkpoint does not complete M2 or pass G2.

## Diagnostic overlay measurement — 2026-11-08

The local-only Android overlay measures its diagnostic score text and expands
its bitmap to a provisional maximum rather than silently clipping; an unfittable
value causes local capture to stop with a notice after the score is saved. At
the confirmed T05 rendering boundary, instrumented checks on a Pixel 8 Pro
observed an unimplemented failure first, then passed for ordinary, wider and
unfittable strings. Both APKs built offline. This checks renderer behavior,
not actual readable encoded/received output or the activity's stop path.

The score fixture omits required real-match fields. The chosen dimensions are
experimental, not approved D10 safe bounds or a production fit assessment.
The combined score/pending-stop operation remains disconnected from the local
activity, there is no publisher stop dispatch, and publishing stays disabled.

## Manual publisher failure cleanup — 2026-11-08

At confirmed T02, the manual publisher control now requires a cleanup callback
and invokes it when a connection attempt throws, while keeping the session in
Stopping and returning a sanitized failure. A focused test first failed because
cleanup was not requested, then passed; an earlier compilation error while
updating the test constructor was a harness error, not the behavioral red.
The complete host suite was run after the behavioral change. The Android project
compiled the shared control; it is not yet wired to a network publisher.
Cleanup dispatch alone is not stop confirmation and does not authorize a trial.

On the next T02 cycle, the manual control's connection-success callback now
requests publisher stop if the session fails closed because its safety store is
unavailable. The focused test first failed because no stop was requested and
then passed in the full host suite. A first forced-suite command timed out;
a subsequent suite run succeeded, and both Android APKs built offline. This
callback is not yet wired to Android's publisher and does not establish stop
confirmation, real network behavior, or an integrated broadcast path.

## Android publisher adapter checkpoint — 2026-11-08

The isolated Android project now contains a `GenericStream` adapter for the
manual control. At T02, a confirmed operator stop first failed to dispatch
cleanup, then passed after dispatch was added. An Android instrumented gate
check first failed on an unimplemented adapter, then passed with the incomplete
fit assessment returning `NumericFitUnresolved`. This deliberately blocks
network start. The adapter is not connected to the activity, callbacks, encoded
score updates, durable stop confirmation, or a real network session; it is not
an integrated publisher and does not pass the prototype gate or G2.

## Android pending-stop reconciliation — 2026-11-08

At T01, a Pixel 8 Pro instrumentation test observed the unimplemented failure,
then passed with session-scoped marker deletion: confirming session A leaves
session B's marker and the diagnostic score intact across reopening. The Android
APKs built; the JVM suite was up to date. The operation requires independent
confirmation and is not connected to any callback. Inspection of the candidate
library's `stopStream()` bytecode shows its Boolean is encoder preparation,
not provider termination; neither that result nor `isStreaming` is sufficient
to clear a marker. Actual stop confirmation, process recovery, complete fit,
activity wiring and real publishing remain unresolved. The trial gate is closed.

## Local adapter wiring and diagnostic preflight — 2026-11-08

The local recording activity now constructs the guarded Android adapter, forwards
connection-success and failure/disconnect/auth-error events without retaining
raw provider text, and offers a no-network diagnostic fit preflight. T05/T02
instrumentation on an authorized Pixel 8 Pro first failed for an unimplemented
diagnostic assessment, then passed against the renderer and the still-denied
start gate. The Android APKs built after an initial build-command timeout; the
JVM test task was up to date. No network callback or activity UI was exercised.

The provisional bitmap measurement does not assess the complete scoreboard,
reviewed output bounds or received readability. The callback does not confirm
remote stop or clear a durable marker. Real start remains blocked and no
publisher UI is exposed. Provider-scoped confirmation/reconciliation, complete
fit enforcement and encoded broadcast validation remain open.

## Capture-scoped callback routing — 2026-11-08

The local Android activity now binds each candidate stream's callback object to
that capture's adapter, rather than dereferencing the activity's current adapter
when a delayed callback arrives. At the confirmed T02 boundary, an instrumented
Pixel 8 Pro test first failed for the unimplemented callback and then passed
with separate old/new callback targets. Both APKs built offline; the JVM task was
up to date. This tests routing with direct callback calls, not actual asynchronous
network events or provider termination. The adapter still denies publishing;
its durable diagnostic lifecycle remains separate, so the prototype gate and
M2 remain open.

## Diagnostic callback cleanup — 2026-11-09

At T02, an Android instrumentation test on a Pixel 8 Pro first failed because
a stale score revision at connection success entered `Stopping` without
requesting cleanup. The diagnostic lifecycle now requests its supplied stop
operation and retains the durable claim. The lifecycle also accepts a
session-scoped asynchronous failure and requests cleanup without clearing the
claim; its new test first failed to compile because the entry point did not
exist, so that case is not an observed behavioral red/green cycle. Both cases
passed with test doubles on Pixel 8 Pro builds `DP11.261016.003` and
`CP3A.261005.005`, and Pixel 10 Pro XL build `CP3A.261005.005`. The media,
inventory and watch APKs built offline; the JVM test task was up to date.
These are diagnostic lifecycle checks, not real provider callbacks, confirmed
network shutdown, complete fit, or integration with the activity. The guarded
adapter still denies publishing; the prototype gate and G2 remain open.

### Late success after stop — 2026-11-09

At T02, a physical Pixel 8 Pro test first failed because a connection-success
event arriving after a confirmed stop did not request cleanup again. The
diagnostic lifecycle now dispatches another stop for its own session, retaining
the pending claim. The revised instrumentation passed on both Pixel 8 Pro
builds and the Pixel 10 Pro XL listed above. Both media APKs built offline and
the forced offline JVM suite passed 109 tests with zero failures, errors or
skips. These callbacks use doubles; real callback provenance, actual network
shutdown, activity integration and the broadcast gate remain unverified.

### Verification and integration gap — 2026-11-09

After the late-success change was committed, the Android safety instrumentation
passed again on both Pixel 8 Pro builds and the Pixel 10 Pro XL. The media debug
and test APK builds were up to date; the watch diagnostic APK also built offline.
The forced JVM suite ran 109 tests with no failures, errors or skips, and the
diagnostic media runtime dependency configuration resolved offline. These are
regressions and build checks, not new TDD cycles or completed M2 tasks.

The actual activity adapter uses `ManualPublisherControl`, which still always
denies start because complete fit is unavailable. The durable diagnostic
publisher lifecycle is separate; its session claim cannot simply be inserted
into that control because `StreamSession` treats any pending marker as a blocker.
The marker also cannot be cleared merely from `stopStream()` or a callback
without independently established session-scoped termination. Joining these
paths, complete scoreboard assessment and real stop confirmation remain required
before a trial. No live destination or owner-selected trial duration was used.

### Diagnostic manual-start integration — 2026-11-09

At confirmed T02, an instrumented Pixel 8 Pro test first failed when manual
start did not durably claim a session before invoking a supplied publisher. The
diagnostic lifecycle now validates the manual destination, checks supplied
readiness and invokes its Android SQLite claim before the supplied connect
operation. The test passed on both Pixel 8 Pro builds and Pixel 10 Pro XL. A
separate prior-pending-stop check passed first-run on Pixel 8 Pro; it is not a
red/green cycle. Android APK builds and a forced 109-test JVM regression passed.
The publisher was a test double and the endpoint fictional. This diagnostic is
not connected to the activity or actual provider; the supplied fit Boolean is
not a complete fit assessment, and no real stream is enabled. Full fit/stop,
credential-safe activity integration and received output remain open.

An unavailable Android SQLite database at diagnostic start initially raised
`SQLiteCantOpenDatabaseException` on Pixel 8 Pro rather than returning a safe
denial. The diagnostic entry point now denies the start without invoking the
publisher; the instrumentation passed on both Pixel 8 Pro builds and Pixel 10
Pro XL. The media APKs built offline and the forced JVM regression passed on a
separate retry after the concurrent command timed out. This checks a real
unopenable Android database and a publisher double; it does not test arbitrary
storage failures, connect the activity or authorize broadcasting.

### Diagnostic projection fields — 2026-11-09

At T05, the Android instrumentation first failed because the dynamic diagnostic
projection omitted sets, timeouts and a serving cue. Its local projection and
fit preflight now use the same fixture text: fixed S0, T/O2 and left-serve values
alongside the stored point totals. The check passed on both Pixel 8 Pro builds
and the Pixel 10 Pro XL; the APKs built offline and the JVM test task was up to
date. These fields are static fixtures, not match state; no local recording was
reviewed for the changed fixture and no received output was tested. This is not
full scoreboard fit, match logic, or authorization to publish.

### Diagnostic stop retry — 2026-11-09

At T02, an instrumented Pixel 8 Pro test failed when a confirmed stop whose
publisher operation threw could not be retried. A later confirmed request for
the same owned session now dispatches stop again while retaining `Stopping` and
the durable claim. The test passed on both Pixel 8 Pro builds. A multi-device
command and subsequent Pixel 10 Pro XL retry timed out, so no Pro XL result is
claimed for this revision. Media APKs built offline and the JVM task was up to
date. These use publisher doubles and do not demonstrate real stop completion,
provider reconciliation, activity integration or a safe broadcast.

## Coherent diagnostic fit input — 2026-11-08

The diagnostic assessment now reads both displayed scores and their probe
revision in one SQLite query before measuring the resulting fixture. At the
confirmed T05/T01 boundary, a Pixel 8 Pro instrumented test first observed the
unimplemented failure and then passed for the 1–2 score and revision 3. Both
Android APKs built; an earlier build command timed out and was rerun. The JVM
test task was up to date. This is a diagnostic projection, not a complete match
or an assessment of pending stops, encoded readability or safe publishing;
the adapter still denies all starts and stop reconciliation remains unresolved.

## Durable publisher session claim — 2026-11-08

At T01 an Android SQLite transaction now checks the assessed snapshot and all
pending markers, then persists a session marker before an external connection
could be initiated. On an authorized Pixel 8 Pro, the instrumented test first
failed for the unimplemented operation and then passed: the marker survived
reopening and blocked a second claim. The APKs built offline; the JVM test task
was up to date. The claim is not wired into the adapter or a real publisher;
an active-session marker must be distinguished from a stop requirement by the
eventual lifecycle integration. No actual shutdown confirmation, process-kill
test or broadcast was performed, and the trial gate remains closed.

## Diagnostic session ownership — 2026-11-08

At T01/T02, an Android lifecycle experiment now owns a durable claim in memory,
accepts the matching connection event while ignoring an unrelated session event,
and treats a surviving marker as unresolved in a new lifecycle. On an authorized
Pixel 8 Pro, instrumentation observed the unimplemented failure then passed;
both APKs built after retrying a timed-out command. The JVM suite was up to date.
This is not wired to the activity or publisher adapter and does not dispatch a
connection or stop, verify a callback's provenance, or confirm termination.
Broadcast fit and the real trial gate remain unresolved.

## Diagnostic claim and publisher dispatch — 2026-11-08

The Android diagnostic lifecycle now dispatches a supplied connect operation
only after a successful durable session claim and dispatches a confirmed stop
without clearing that claim. At T01/T02, two Pixel 8 Pro instrumented cases
first failed on unimplemented operations and then passed: the marker existed
before the connection operation, and stop dispatch retained it. A fresh
lifecycle remained blocked until a simulated independent confirmation. The
Android APKs built offline; the JVM suite was up to date.

The supplied operations were test doubles, not `GenericStream` or a network
provider. Connection failure currently retains the marker but does not dispatch
cleanup, and stop confirmation and callback provenance remain unverified. This
experiment is not connected to the activity or the real adapter; full fit,
safe actual integration, received output, the prototype gate and G2 remain open.

### Partial-connect cleanup follow-up — 2026-11-08

The diagnostic lifecycle now requires a stop operation and invokes it when the
connect operation throws after a durable claim. At T02, a Pixel 8 Pro test first
failed because cleanup was not called, then passed while the marker and Stopping
state remained. Both Android APKs built; the JVM task was up to date. The test
uses a double and a fictional sensitive exception; it does not verify network
shutdown. The earlier statement that this path does not dispatch cleanup is
superseded only for synchronous connect exceptions; async failure handling,
session-scoped callbacks, real adapter wiring and confirmation remain open.

## Host build and device availability — 2026-11-06

The offline `media assembleDebug` runner built the isolated diagnostic APK with
the current shared session source (35 tasks: five executed, 30 up to date).
The Kotlin compiler reported an existing listener-parameter-name warning. This
is a source/build compatibility check only: the activity does not use the shared
session as an Android publisher and has no Android durable safety-state adapter.

The ADB inventory runner returned an empty device list. Before any physical
deployment or validation, connect and authorize the intended streaming phone and
repeat the visibility check. No device, capture, network publisher, or received
broadcast was exercised in this checkpoint; the prototype gate remains open.

## Local scoreboard checkpoint — 2026-11-01

The owner reviewed a local Pixel 10 Pro XL recording and reported that both
score buttons worked and audio/video were good. [Sanitized evidence](../evidence/m2/local-score-controls-2026-11-01.md)
distinguishes this observation from metadata. This completes neither publishing
nor the prototype gate: the local diagnostic does not implement numeric-fit
safety, real match scoring, or an Android publisher.

## Host safety checkpoint — 2026-11-02

The [T02 store-unavailable review](../evidence/m2/session-store-unavailable-review-2026-11-02.md)
records an observed red/green host slice: if safety state cannot be read after a
connection callback, the session enters `Stopping`. This does not dispatch a
publisher stop or make the JVM store Android-compatible. The Android-compatible
boundary, durable state, fit enforcement and publisher integration remain open.

## Reset during connection — host verification

At the confirmed T02 boundary, a real temporary store is reset to a new epoch
after Start is accepted but before the connection callback. The existing session
returns `Stopping` on that callback, not `Live`. The [first-run result](../evidence/m2/session-reset-during-connect-first-run.xml)
passed without an implementation change; this is verification, not a red/green
cycle. The full forced offline JVM suite subsequently passed 70 tests with no
failures, errors or skips. This sequential check does not establish concurrency
atomicity, Android integration, real stop dispatch, or provider termination.