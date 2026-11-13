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

### YouTube claim binding fixture — 2026-11-13

The Android diagnostic store can now atomically bind a supplied YouTube
broadcast ID to a local pending-session claim without storing a token or key.
The T01/T03 Pixel 10 Pro XL test first failed because the binding was absent,
then passed after closing and reopening the real SQLite store. A separate
version-1 fixture upgrade passed on its first run, preserving the score and an
unidentified legacy pending claim, which continued to block a new session.
Both Android APKs built; focused instrumentation passed; the JVM task was up
to date. No account or provider was contacted. The ID is supplied by a test,
not selected or authenticated in the UI, and this store API is not wired into
the real publisher. It does not prove the binding identifies the correct ingest
or permit clearing any claim. OAuth, selection, evidence freshness, actual stop
reconciliation, and the real Start gate remain open.

### YouTube reconciliation direction — 2026-11-13

The owner selected YouTube as the first destination for investigating
authenticated, session-specific stop reconciliation; manual RTMP remains
fail-closed. This is a scoped M2 investigation, not a final provider or stack
decision and not permission to transmit or create a broadcast automatically.

Official YouTube documentation distinguishes a broadcast from its bound ingest
stream. `liveBroadcasts.list` can query a specific broadcast ID with the `status`
part, but requires OAuth authorization; `status.lifeCycleStatus` describes the
broadcast, while `liveStreams.status.streamStatus` describes ingest. Neither a
generic RTMP URL nor the existing random local session ID establishes a mapping
to the exact broadcast or ingest session. A missing result, authorization error,
unknown status, or merely inactive local transport must not clear a claim.
Sources: [broadcast list](https://developers.google.com/youtube/data/live/reference/rest/v3/liveBroadcasts/list),
[broadcast resource](https://developers.google.com/youtube/data/live/reference/rest/v3/liveBroadcasts),
[stream resource](https://developers.google.com/youtube/data/live/reference/rest/v3/liveStreams),
and [authorization](https://developers.google.com/youtube/data/live/authentication).

The existing diagnostic has no YouTube OAuth integration, broadcast selection,
or durable binding between its local claim and a YouTube resource. Therefore it
cannot yet safely use a YouTube status to clear any existing marker. Next, design
and implement the authorized YouTube setup and durable binding before any
reconciliation, using the confirmed T03 setup and T02 session boundaries.
Do not assume a broadcast lifecycle status alone establishes ingest termination;
define and verify the precise evidence and freshness needed for the affected
session. Never persist credentials in the marker or retain raw errors. Existing
unidentified claims remain unresolved. Real Start stays blocked, and no account,
credentials, network publisher or broadcast was accessed in this investigation.

### Failed local release reporting — 2026-11-13

The adapter now records a successful return from local release separately from
the fact that release was attempted; the activity reports unconfirmed local
release when that operation throws. At T02 a controlled release failure on the
Pixel 10 Pro XL first failed because success was reported, then passed with the
publisher claim still pending. Both APKs built and focused instrumentation
passed; the JVM test task was up to date. This does not verify the activity's
failure message on-device, retry failed release, or establish real publisher
termination. The real Start gate remains closed and no broadcast was attempted.

### Stop/release coordination — 2026-11-13

The Android adapter now serializes its stop dispatch and local capture release;
once release begins, later Stop and callback cleanup cannot invoke the released
publisher object. The activity routes local stream release through that adapter,
keeps an unresolved claim and reports that local resources were released rather
than claiming a successful retry. The controlled T02 test on Pixel 10 Pro XL
first failed because Stop was dispatched again after release, then passed with
one stop, one release, Stopping status and the durable claim retained. Both
Android APKs built; the focused instrumentation passed and the JVM test task was
up to date. The complete instrumentation was blocked by the device's System UI
covering the app. This is not proof of real publisher termination, failed-release
recovery, GL/network behavior or process-restart reconciliation. No broadcast
was attempted and the real Start gate remains closed.

### RootEncoder stop/release review — 2026-11-13

The pinned RootEncoder 2.8.1 source was reviewed against the activity's owned
retry. `StreamBase.stopStream()` sets its local streaming flag false, invokes
the implementation stop, and may prepare encoders again. `GenericStream`'s
stop implementation selects a client by its current type and then sets that
type to NONE. `StreamBase.release()` stops and releases local sources. These
are local operations, not provider confirmation. After release, calling Stop
again through the retained adapter is not established to retry the original
network disconnect and may act on released resources. The controlled Stop test
therefore does not validate that real retry; do not expose real publishing or
describe this as reliable recovery. See the version-pinned RootEncoder sources:
`library/src/main/java/com/pedro/library/base/StreamBase.kt` and
`library/src/main/java/com/pedro/library/generic/GenericStream.kt` at tag 2.8.1.

Next integrated work must determine an evidence-backed session-specific
termination/reconciliation strategy and safely coordinate resource ownership;
do not rely on the return value, local streaming flag, or a delayed callback
alone. Until then, keep the durable claim and real Start gate closed. No code
fix or hardware/network validation is claimed by this source review.

### Activity exit after retry — 2026-11-13

The existing T06 instrumentation now checks that, after a confirmed retry of
an injected controlled publisher, finishing the activity does not dispatch an
additional stop and does not remove the durable claim. The full Pixel 10 Pro XL
instrumentation passed on the first run with unchanged application code; this is
verification, not a red/green implementation cycle. The test does not exercise
a real GenericStream, network shutdown, process death, or provider reconciliation.
The real publisher gate and the remaining next-session handoff below stay open.

### Next-session handoff — 2026-11-12

Continue the owner-authorized M2 broadcast-first vertical slice; do not spend
the next session on speculative probe-only permutations. The real GenericStream
Start remains deliberately blocked (`fitReady = false`): the diagnostic geometry
assessment is not evidence of encoded readability, and stop confirmation and
recovery are not yet established. No broadcast has been attempted.

The latest T06 test injects a controlled owned session after capture is absent;
it demonstrates a confirmed Stop retry and retained durable claim, not actual
GenericStream shutdown, releasing then retrying a real stream, process death,
or provider termination. In particular, review how the activity retains the
adapter while `stream.release()` runs, late callbacks are delivered, a store is
closed on activity destruction, and another preview might be blocked. Do not
clear a pending claim merely because `stopStream`, `release`, a disconnect
callback, or `isStreaming` reports a local result. Determine a reliable,
session-specific reconciliation before making the real Start path reachable.
Continue one TDD behavior at the owner-confirmed boundaries, prioritizing an
integrated stop/recovery step over additional artificial races.

The owner reported that a local 10-second clip recorded with audio and an ABC
score change, the reset button worked, and the cutout layout looked good; these
are owner observations, not proof of full encoded-score legibility or received
media. The installed diagnostic build for the latest slice is
`m2-dev-d9069d8e18bd`. The most recent Android APK build and full Pixel 10 Pro XL
instrumentation passed; the JVM test task was up to date. The phone must be
authorized and unlocked for the UI test: when locked, the accessibility root
can be System UI and a confirmation check can fail before exercising the app.
Do not overwrite the owner's private clip except through an operator-initiated
test. Before any later bounded network trial, satisfy the prototype gate above,
recheck ADB, and let the owner enter credentials on-device and initiate it.

### Owned stop retry — 2026-11-12

At T06/T02 on the Pixel 10 Pro XL, confirmed Stop initially failed to retry a
controlled unresolved session after capture was absent. After the change, the
full instrumentation passed: Stop retried the same supplied publisher operation
and retained its durable pending-stop claim. The activity keeps an unresolved
adapter instead of replacing it with a new preview, and retries Stop when there
is no capture. Both APKs built. This test uses a controlled publisher and
injected owned session; it does not establish real GenericStream idempotence,
termination, process-restart reconciliation or safe release/late-callback
ordering. The real publisher Start remains blocked; no broadcast was attempted.

### Stop-confirmation test blocker — 2026-11-12

The complete Android instrumentation previously failed while looking for the
Stop confirmation. Its accessibility root was System UI rather than the app;
after the owner unlocked the Pixel 10 Pro XL, the same full instrumentation
passed, including the confirmation check and publisher safety cases. The test
now includes active-window diagnostics on failure. This resolves the observed
test-environment blocker, not real publisher stop confirmation or recovery.
No capture or network broadcast was started by the instrumentation.

### Provisional diagnostic output geometry — 2026-11-12

The diagnostic projection now checks output dimensions against the same filter
position and scale used by the local GenericStream path. An illustrative 24px
minimum nominal text size and 5% bounds are provisional experiment parameters,
not established readability or approved product limits. The T05 Pixel 10 Pro XL
test first failed because an undersized 320×180 output accepted the projection;
it then passed with that output rejected and the normal 1280×720 fixture still
delivered. The focused run skipped the known UI failure. Both APKs built; JVM
tests were up to date. No encoded recording was inspected for this change.
The assessment assumes the intended filter transform and does not validate
encoded pixels, clipping, compression or human legibility; network Start stays
blocked until the outstanding real publisher and stop safeguards are resolved.

### Confirmed diagnostic score reset — 2026-11-12

The owner requested an explicit off-air reset after observing that the diagnostic
score persists across updates. The reset uses one transaction to zero both
fixture scores and advance the snapshot epoch, retaining pending-stop records.
At T01 the Pixel 10 Pro XL focused instrumentation first failed on reopen with
the old score, then passed cancellation, confirmed reset, reopen, generation,
and pending-stop checks. A confirmation button is now present and refuses reset
while a preview or recording is active. The complete T06 instrumentation run
fails at the layout visibility assertion on this run, before exercising the
button; confirmation and active-capture UI behavior remain unverified on-device.
No owner's score or private clip was reset by instrumentation, which uses a
separate test database. The actual publisher remains blocked.

### Local controls layout and short clip — 2026-11-12

The owner reported a roughly two-second Pixel 10 recording and controls moving
off-screen as status text grew. The sanitized Pixel 10 Pro XL metadata recorded
`operator_stopped`, 63 video samples and no reported setup, stop or inspection
error; it does not establish why Stop was invoked. No clip was read or replaced.
The activity now uses a bounded scrollable status area, a compact three-column
control grid and a separate preview pane. A physical T06 visibility check failed
before the change because the recording control was outside the display; after
the change the initial and expanded-status visibility checks passed on the Pixel
10 Pro XL. The existing later stop-confirmation UI check still failed, so the
full instrumentation run did not pass. The APKs built. A fresh operator-run
recording and manual ergonomic review are still needed; a ten-second result or
the cause of the earlier operator stop cannot be claimed from this check.

### Local encoded-frame review control — 2026-11-12

The media experiment now offers an on-device side-by-side review of two
approximately selected frames surrounding a single operator score change in
the existing local recording. Missing, multiple, or insufficiently separated
changes do not produce a comparison. At T06, the Pixel 10 Pro XL run first
failed because the review control was missing; after implementation it passed
that control and unavailable-state check, then failed at the pre-existing
stop-confirmation UI check. The complete instrumentation run is not green.
Both Android APKs built. No local recording was initiated in this checkpoint;
actual frame selection, encoded pixels and human legibility remain unverified.
An operator must initiate the local capture and review the full clip and frames;
this is neither broadcast authorization nor evidence of received output.

### Live diagnostic output failure — 2026-11-12

At the T02/T05 boundaries, a controlled live publisher's output consumer
throws while receiving an assessed projection. Pixel 10 Pro XL instrumentation
first failed because no stop was requested; after the change it passed with
Stopping displayed and the session claim retained. Projection read, render, or
submission failure now requests the owned lifecycle's stop and invalidates the
submitted assessment. Android APK builds succeeded and the JVM test task was
up to date. The focused run skips the previously failing UI confirmation check.
It uses a publisher double and a synchronous submission exception: asynchronous
GL failures, actual encoder output, real shutdown and provider reception are not
established. The actual publisher Start remains blocked.

### Diagnostic helper admission — 2026-11-12

The diagnostic helper now delegates to the same submitted-assessment Start
path rather than independently rendering a new assessment. The focused T02
instrumentation on the Pixel 10 Pro XL first failed because the helper started
without a submitted projection, then passed after the change, with no claim or
connection for that denied start. Both Android APKs built; the JVM test task
was up to date. The publisher-only instrumentation skips the unresolved UI
confirmation check and does not exercise real networking or encoded output.
The real publisher remains blocked; encoded fit and stop recovery remain open.

### Submitted assessment admission — 2026-11-12

The diagnostic manual Start now requires an assessment retained only after the
output consumer accepts the rendered bitmap, and checks its revision before
the existing transactional publisher claim. On a Pixel 10 Pro XL, a T02 test
first failed because Start accepted an assessment never submitted to output;
the focused publisher instrumentation then passed with the missing assessment
denied, a submitted current score accepted, and its durable claim retained. A
stale-revision check also passed on its first run; it is verification, not a
separate red/green cycle. The Android APKs built and the JVM test task was
up to date. The complete instrumentation run failed at the existing UI
stop-confirmation check on both tested phones; a publisher-only invocation
skipped that UI check, so this is not a full instrumentation-suite pass.

This controls the manual test-double path only. The older startDiagnostic test
helper independently assesses the fixture, and the real GenericStream Start
remains blocked. Consumer return does not prove GL application, encoded fit or
readability. Live updates, actual shutdown confirmation/recovery, and received
output still require integration and evidence before a trial.

### Assessed diagnostic projection to GL filter — 2026-11-12

The local preview/recording path now supplies its dynamic scoreboard bitmap to
the GenericStream image filter from the same committed projection that produces
its diagnostic assessment and revision. At T05, Android instrumentation on the
Pixel 10 Pro XL first failed because the projection was not delivered, then
passed for an asymmetric score and revision. The Android APKs built and the JVM
suite passed. One intermediate instrumentation run failed at the pre-existing
stop-confirmation UI check; a rerun after waking the device passed. No recording
or network stream was initiated by this test.

The output consumer currently does not retain the assessment for Start, and the
real GenericStream Start path remains blocked. The test checks the bitmap supplied
to a consumer, not GL application, encoded geometry/readability, real publishing,
live update safety, or received output. Those remain necessary before the gate.

### Connecting score change — 2026-11-12

At the confirmed T02 boundary, the Android instrumentation changed a score
after a controlled publisher start but before its connection callback. The
Pixel 10 Pro XL run first failed because no stop was dispatched; after the
change it passed with the score saved, stop dispatched, and the durable claim
retained. The activity now routes its score controls through this adapter when
one is present. Both Android APKs built offline and the forced JVM suite passed.
The publisher was a network double: this does not establish real stop, encoded
fit, live-score safety, or a received broadcast. The real start gate remains
closed; integrating and validating those safeguards remains necessary.

### Next-session handoff — 2026-11-11

The owner wants integrated broadcast progress, not further speculative
probe-only permutations or repeated status work. The manual adapter's controlled
publisher path joins a durable claim and lifecycle, but the `GenericStream`
constructor still routes Start through a separate path with `fitReady = false`.
The prepared preview uses a simplified diagnostic scoreboard; its bitmap fit is
not a reviewed encoded-output fit. A disconnect callback and `isStreaming` flag
have not established session termination, and a retained claim cannot yet be
recovered through the UI. No real broadcast has been run.

Continue by integrating the actual publisher, its current scoreboard projection,
confirmed stop, sanitized status and durable recovery as one useful slice. Keep
TDD at confirmed boundaries and the prototype gate above; do not flip the fit
Boolean or clear a pending claim without implementing the corresponding safety
behavior. Verify ADB authorization before phone-dependent work. The owner will
enter destination credentials on the phone and initiate a bounded trial after
the gate is met; do not request or retain credentials or personal media.

Owner direction on 2026-11-10: prioritize integrated implementation and defer
unobserved speculative scenarios for evaluation during actual implementation
and live testing. Continue required TDD and concrete safety safeguards; do not
expand probe-only permutations merely to anticipate hypothetical failures.

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

### Publisher status display — 2026-11-11

The activity now displays the diagnostic session state separately from other
messages, refreshes it after start, callbacks and cleanup, and labels a connected
callback as unverified received output. At T06, the unresolved-session status
was absent before the change and the instrumentation passed afterward on the
Pixel 8 Pro (build CP3A.261005.005). Both APKs built offline. The test establishes
the reopened unresolved status, not live network state or received output. Real
publisher start remains blocked pending fit and termination safeguards.

### Manual session path with controlled publisher — 2026-11-11

The adapter's manual `start()` now joins assessment, durable Android session
claim, connection callback and confirmed stop in the existing controlled-publisher
configuration. At T02, the integrated test failed before the change and passed
on the Pixel 8 Pro (build CP3A.261005.005); both APKs built offline. It uses
real Android SQLite and a network double. The `GenericStream` constructor remains
fail-closed: diagnostic bitmap fit is not broadcast-fit approval, and session
termination confirmation and the real network path remain unresolved.

### Unresolved-session operator notice — 2026-11-11

At the confirmed T06 activity boundary, a test reopening the diagnostic UI
with a durable publisher claim failed because no warning was displayed. After
the change, the Android instrumentation passed on the Pixel 8 Pro (Android 17,
build CP3A.261005.005), and both diagnostic APKs built offline. A separate
notice now reports an unresolved prior claim; a failed safety-store read also
shows a conservative warning. Ordinary status messages do not replace it.
The notice is assessed on activity creation and does not provide recovery or
prove shutdown. This is not a real publisher or received-broadcast test; the
complete fit and stop-confirmation safeguards and the real start gate remain open.

### Reopened pending-session status — 2026-11-10

At T02, an Android store reopened with an unresolved publisher claim was
incorrectly reported Stopped by a fresh adapter. The instrumented test failed
before the change and passed on a Pixel 8 Pro (build CP3A.261005.005) afterward.
The diagnostic lifecycle now reports Stopping for a retained claim or an
unavailable store, without clearing a claim or connecting. Both APKs built
offline. This is a conservative unresolved state, not proof that shutdown is
in progress or that a real publisher has terminated; recovery remains open.

### Activity stop confirmation — 2026-11-10

The diagnostic activity now asks for confirmation before an operator stop and
requests publisher-session stop before releasing capture; lifecycle and error
cleanup remain immediate. A T06 UI check failed before the dialog was added.
After adding a bounded wait for the dialog window, the instrumentation passed
on the Pixel 8 Pro (build CP3A.261005.005); both APKs built offline. The UI test
checks dialog visibility, not a live publisher stop or termination confirmation.
Real publisher start remains disabled.

### Off-air diagnostic scoring — 2026-11-10

The diagnostic score controls now accept and display a point when capture is
stopped, using the existing Android score store. A T06 activity test with a
dedicated test database failed before the change and passed on the Pixel 8 Pro
(build CP3A.261005.005) afterward; both APKs built offline. The test does not
modify the normal diagnostic database. This is simplified fixture scoring, not
full match logic or proof of a broadcast or fit-triggered live shutdown.

### Session-scoped disconnect routing — 2026-11-10

The capture-scoped callback now routes disconnect separately from connection
failure to its owning diagnostic lifecycle. At T02, a stop followed by a
disconnect previously dispatched stop again; the device test failed before this
change and passed afterward on the Pixel 8 Pro (build CP3A.261005.005). Both
Android APKs built offline. The event does not clear the durable claim or prove
termination; the controlled callback test is not a real RTMP shutdown test.
Full fit and the real publisher start path remain blocked.

### Candidate stop-state inspection — 2026-11-10

Inspection of the locally resolved RootEncoder 2.8.1 `StreamBase` bytecode shows
that `stopStream()` sets its `isStreaming` field false before invoking the
underlying stop operation. Therefore a false `isStreaming` value alone is not
evidence of completed shutdown and must not clear the durable session claim.
The resolved RTMP client's public `disconnect()` launches cleanup asynchronously,
so return from `stopStream()` also cannot establish completion. Its disconnect
path invokes its callback after a connection-close step, but subsequent retry/job
cleanup occurs after that callback; this ordering alone does not establish that
the session cannot resume or that the destination has stopped receiving media.
This is a source/bytecode observation, not a network shutdown test. Establish
an appropriate termination signal and its session provenance before enabling
the real publishing path; preserve unresolved markers in the meantime.

### Publisher preview preparation — 2026-11-10

The manual-start adapter was previously available only during a timed local
recording. The activity now has a separate untimed publisher-preview preparation
path using the existing camera, audio and overlay setup, without recording or
starting a network stream. Stop and activity exit release the prepared preview.
At T06, the missing preparation control failed on the Pixel 8 Pro before the
change and the instrumentation passed afterward; both Android APKs built offline.
The automated check confirms control presence only, not physical preview,
resource cleanup, real publishing or received output. The real start gate remains
closed pending complete fit, stop confirmation and other prototype safeguards.

### Activity manual-start control — 2026-11-10

The local media activity now exposes a confirmed manual-start dialog and passes
the destination and permission state to the existing publisher adapter. Without
an active local capture it explains that no broadcast was started; the adapter's
fit gate still denies a real start. At T06 the missing-control test failed before
implementation and the instrumentation passed on a Pixel 8 Pro (build
CP3A.261005.005). The debug and test APKs built offline. This connects the UI to
the existing blocked path, not a network publisher: complete fit, durable stop
confirmation, real status/cleanup and received output are still required.

### Diagnostic re-entrant stop guard — 2026-11-10

At the confirmed T02 boundary, the Android instrumentation first failed when a
controlled publisher delivered a disconnect synchronously during stop: a second
stop was dispatched before the first returned. The diagnostic lifecycle now
suppresses re-entrant stop dispatch while retaining the durable claim; a later
explicit retry remains possible. The updated test and existing instrumentation
passed on a Pixel 8 Pro (Android 17, build CP3A.261005.005), and the debug and
test APKs built offline. This uses a publisher double, not a network stream;
asynchronous callbacks, verified shutdown, full fit and trial readiness remain
open. The real publisher start remains disabled.

### Android network prerequisite — 2026-11-10

The diagnostic APK's merged manifest lacked Internet permission, a concrete
blocker to a future network publisher. An installed-package check failed before
the permission was added and passed afterward on the Pixel 8 Pro (build
CP3A.261005.005). Both diagnostic APKs built offline. This grants network access
only; it does not connect or establish destination reachability, fit, confirmed
shutdown, received output or trial readiness. Publisher start remains disabled.

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

Later the same day, a fresh installation retry on the Pixel 10 Pro XL again
timed out. The owner reported a Google security review prompt; the cause of the
timeout is not independently established. Both installed diagnostic APK hashes
matched the current built APKs, so instrumentation was run without reinstalling
and returned PASS with instrumentation code -1 on `CP3A.261005.005`. No device
security setting was changed. This supersedes the missing Pro XL result for the
diagnostic test only, not any real publisher or received-broadcast evidence.

### Diagnostic session callback bridge — 2026-11-09

At confirmed T02, a Pixel 8 Pro instrumentation test first failed when the
candidate callback did not transition its claimed diagnostic session on
connection success. The session-scoped callback bridge now sends success and
failure events to that lifecycle; the test passed, including cleanup request
and durable claim retention on failure. Android APKs built offline and the JVM
task was up to date. Callbacks were invoked directly with a publisher double;
the bridge is not wired into the activity or `GenericStream`, and real callback
threading, shutdown confirmation, complete fit and received output remain open.

### Candidate adapter lifecycle wiring — 2026-11-09

Adapter-level verification on a connected Pixel 8 Pro passed for unconfirmed,
missing-permission and invalid-destination starts, duplicate starts, and a new
adapter blocked by an unresolved earlier claim. The publisher was a test double;
this was a first-run verification, not a red/green implementation cycle. An
exploratory repeated-stop assertion failed and was removed: suppressing retries
could prevent cleanup after failure, so no stop behavior was changed. The APKs
built offline and the JVM test task was up to date. Complete fit and independent
remote-stop confirmation remain unresolved; the real start gate stays closed.

The adapter's diagnostic-only manual-start path now joins the fixture assessment,
Android SQLite claim, supplied publisher operation and lifecycle callback. At
T02 an instrumented Pixel 8 Pro test first failed to dispatch, then passed with
a fictional endpoint and test publisher. The actual `GenericStream` constructor
disables this diagnostic path; ordinary start remains hard-denied. Media APKs
built after a timed-out first build, and the JVM task was up to date. This does
not establish full fit, real network start, stop confirmation or received output.

An Android instrumentation test first failed when the publisher adapter did
not forward success to its owned diagnostic lifecycle. The adapter now forwards
connection and stop events to the capture-scoped lifecycle, and the local
activity constructs that lifecycle alongside its candidate `GenericStream`.
The test passed on a Pixel 8 Pro with supplied publisher operations. Both media
APKs built offline; the JVM task was up to date. The local activity does not
claim a session or expose a publisher start, and this is not a real network or
shutdown test. Full fit, lifecycle/start integration, stop confirmation and
received output remain open; the existing start gate is unchanged.

The adapter now exposes the status of its selected diagnostic lifecycle (or the
existing session control when no lifecycle is supplied). A Pixel 8 Pro test
first failed when an owned connecting session was reported stopped and then
passed. The Android APKs built after a concurrent build timeout and the JVM
suite passed. This is status plumbing only: manual start remains hard-denied,
and no real publisher or user-facing live status was exercised.

The adapter's confirmed start request now checks the selected diagnostic
lifecycle before consulting the still-closed fit gate: an existing connecting,
live or stopping session is reported accordingly. A Pixel 8 Pro test first
failed for an already-live session and then passed. Android APKs built offline;
the JVM test task was up to date. This resolves conflicting status reporting,
but does not connect manual start to the durable claim or authorize publishing.

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