# Phone/watch connectivity preparation

This isolated M2.4 probe follows the
[pre-execution contract](../../../specifications/m2-watch-connectivity-contract.md).

## T04 host correlation cycle

The [red result](correlation-red.xml) fails the expected matching-acknowledgment
assertion against the [pre-fix source](PendingProbe-before-fix.kt.txt). The
[green result](correlation-green.xml) passes after the minimum peer/token and
single-completion check. The complete JVM suite passed fourteen tests: ten
storage, three readiness and one diagnostic correlation test. This is one new
observed T04 red/green cycle, not evidence of actual Data Layer delivery.

## Build/deployment and initial device state

The standalone Android project built successfully. The
[deployment receipt](deployment.json) records the identical APK installed and
opened on authorized Pixel 10 Pro XL and Pixel Watch 4 devices. Pixel 8 Pro was
not targeted. No release key, publishing endpoint or camera/audio path is used.

The [initial phone report](initial-pixel-10-pro-xl.json) shows `listener_ready`.
The watch process was running, but no report file was available at collection
time. Watch listener readiness, discovery and round trips are therefore pending,
not established by installation or process presence. The first report read
could not parse the absent watch output; a separate shell read confirmed the
file did not yet exist. No malformed JSON was retained as device evidence.

Next: owner opens/keeps both diagnostic screens awake and reports their status;
then sends a ping from each side. Do not report delivery before matched
acknowledgments. Direct pairing is owner-reported; Bluetooth-only routing,
application authorization, disconnect/reconnect, latency targets and the second
Android/uplink topology remain unverified. M2.4 and G2 remain open.

## Collected watch-initiated round trips

The owner reported round trips around 50–140 ms. The subsequently collected
[watch report](round-trips-pixel-watch-4.json) contains 21 matched acknowledgments,
21 discoveries of exactly one nearby peer, and no timeout or send-failure events.
Across all 21 retained samples, minimum is 58 ms, maximum 449 ms, arithmetic
mean approximately 147.57 ms and median 107 ms. Three higher samples of
398/429/449 ms are included, not discarded. The owner's observed range is not
the full measured range or a calculated average.

The [phone report](round-trips-pixel-10-pro-xl.json) contains 21 received pings
and 21 accepted acknowledgment sends. Together these support watch → phone →
watch diagnostic delivery. Both apps' listeners are now evidenced as ready.
There are no phone-initiated matched acknowledgments in this collected session;
the reverse initiation remains pending. Successful round trips are not evidence
of foreground/background reliability over time or the PRD remote-control target,
which also includes committed state and outgoing overlay updates.

Only sanitized JSON events were retained, without peer IDs or payload tokens.
The actual Bluetooth/network path remains unmeasured. Next: tap Send ping on
the Pro XL while both probe screens are awake, then collect the reverse-direction
result before a separately controlled disconnect/reconnect experiment.

## Bidirectional follow-up

The owner reports pinging on phone and watch. Pixel 8 Pro has no role in this
watch-only check and received no probe installation or interaction command.

The [phone follow-up report](bidirectional-pixel-10-pro-xl.json) records three
discoveries of one nearby peer, two matched acknowledgments (142 and 178 ms),
one ten-second acknowledgment timeout and no send-failure event. The timeout's
cause is unknown; successful sends do not prove the watch listener received the
request. Do not attribute this to screen sleep or a radio fault without evidence.

The [watch follow-up report](bidirectional-pixel-watch-4.json) records 26 matched
acknowledgments cumulatively, including the previous 21, not 26 new trials.
The complete retained watch-initiated set spans 58–449 ms, mean approximately
141.85 ms, median 107.5 ms, with no watch-side timeout or send-failure event.
The phone records 26 incoming pings, and the watch records two incoming pings.

These observations establish successful diagnostic round trips initiated in
both directions, with one unresolved phone-side timeout retained as a limitation.
They do not establish lossless delivery, background reliability or production
command handling. Next: keep both screens awake and collect several additional
phone-initiated trials before controlled disconnect/reconnect. Do not change
watch pairing or disable radios automatically; actual transport remains unmeasured.

## Awake retry follow-up

The [phone retry report](awake-retry-pixel-10-pro-xl.json) starts a new activity
event list rather than extending the previous phone snapshot. It contains ten
matched acknowledgments, 66–279 ms, mean 119.4 ms, median 105.5 ms, eleven
incoming pings and no timeout or send-failure event. These ten successes do not
erase the earlier separately retained phone-side timeout.

The [watch retry report](awake-retry-pixel-watch-4.json) extends its previous
snapshot with fifteen further matched acknowledgments and no new timeout or
send-failure event. These new samples span 57–330 ms, mean approximately
110.33 ms. Its cumulative report has 41 successes. Activity report lifetimes
differ, so incoming counts must not be presented as one synchronized trial set.

Pixel 8 Pro remains outside this check. Successful awake trials support the
foreground diagnostic path but do not explain the earlier timeout. Next conduct
a foreground-listener interruption/recovery check: leave the watch probe screen,
send one phone ping and record its outcome, reopen the watch probe, then retry
from both sides. This is not radio disconnection or network handoff evidence;
physical transport loss and production snapshot recovery remain separate work.

## Owner-reported foreground interruption/recovery

The owner reports that a phone ping timed out with the watch on its watch face,
and pings succeeded after both devices returned to the probe. This agrees with
the diagnostic's intended foreground-only listener scope: `onPause` marks it
inactive and removes its listener; `onResume` registers it again. It is not a
demonstration of radio loss or a selected production background design.

The [latest phone snapshot](lifecycle-pixel-10-pro-xl.json) has a fresh activity
event list containing one matched acknowledgment at 109 ms; it does not retain
the owner-reported watch-face timeout. The [watch snapshot](lifecycle-pixel-watch-4.json)
retains an additional matched acknowledgment at 247 ms and an incoming ping
with acknowledgment-send acceptance. Thus successful delivery after reopening
has device evidence, while this interruption's timeout is owner-reported only.

The owner clarified that successful watch pings were observed both with the
Pro XL screen off/locked and with the phone showing the home screen or another
app. The phone was not reported powered off. These are owner observations,
not controlled state-correlated device evidence. The probe removes its listener
on pause, so the apparent background success needs reproduction with explicit
lifecycle logging and matching acknowledgment evidence before claiming support.
Do not infer that an accepted send alone confirms the remote listener ran.
Production background delivery, radio disconnect/reconnect and G2 remain open.

## Lifecycle diagnostic revision 2 deployed

The [observation contract](../../../specifications/m2-watch-connectivity-contract.md#lifecycle-observation-extension)
was extended before instrumentation changes. The new diagnostic adds persistent
atomic lifecycle/message events and sampled interactive/lock flags, without a
background service or changed intended listener lifetime. A separate local-request
result distinguishes WAITING/MATCHED ACK/TIMEOUT/CANCELLED from incoming pings.
Old `link-report.json` files remain unchanged; new events use
`link-lifecycle-report.json` and `diagnosticRevision=2`.

The APK rebuilt successfully offline. The JVM regression task passed with cached
results; no new test or red/green cycle is claimed for instrumentation. The
[revision-2 receipt](revision2-deployment.json) confirms identical APK installation
and accepted launches on Pro XL and watch only. An initial synchronous deployment
call timed out without a receipt; a subsequent monitored deployment completed.

The [initial phone report](revision2-initial-pixel-10-pro-xl.json) records create,
resume and listener readiness. The [initial watch report](revision2-initial-pixel-watch-4.json)
records create/resume followed by pause/stop and successful listener removal,
so the watch must be reopened and kept awake for the controlled trial. These
events are not a new ping result. The first trial is phone Home/other-app with
baseline and recovery pings, collected before a separate screen-off/lock trial.

## Revision-2 Home trial: initiating watch paused

The owner reported `Local request: CANCELLED on pause`, a first phone request
timeout after reopening, then successful retries from phone and watch.
The [watch lifecycle report](home-trial-pixel-watch-4.json) records request start
at local elapsed 44,582,786 ms and pause at 44,592,733 ms, approximately
9.947 seconds later, with `interactive=false`, pending cancellation and listener
removal. This trial is confounded by the initiating watch pausing before the
ten-second timeout; it does not settle phone background delivery. No screen
timeout setting, wake lock or listener behavior was changed.

The [phone lifecycle report](home-trial-pixel-10-pro-xl.json) records a request
timeout while its activity was active, followed by a matched acknowledgment
at 580 ms. The watch report later records a matched acknowledgment at 121 ms
and receipt/acknowledgment of a phone ping. Watch pause/resume transitions are
present, but the timeout's exact cause is not established. Do not equate active
phone state with readiness of the remote watch listener at that instant.

Collection initially timed out querying one ADB transport; a subsequent read
collected the watch report without resetting pairing or radios. Pixel 8 Pro
received no probe interaction. Next repeat the phone Home trial with the watch
probe kept awake throughout the wait. Use one request, wait for a new matched
acknowledgment or timeout, and collect before changing the phone state again.
If cancellation recurs, retain it as cancellation, not remote failure. Screen-off/
lock, production background delivery, radio recovery and G2 remain unverified.

## Home repeat: watch pause reproduced

The owner again reported cancellation. The
[watch repeat report](home-repeat-pixel-watch-4.json) records request start at
49,324,247 ms, pause at 49,334,172 ms (9.925 seconds later), and pending
cancellation at 49,334,227 ms. The pause samples `interactive=false`. This is
another local cancellation, not an observed acknowledgment timeout.

The [phone repeat report](home-repeat-pixel-10-pro-xl.json) records activity
pause/stop and successful listener removal; the latest sampled state is inactive
and interactive. No new incoming callback follows that final pause in this
snapshot. Phone Home-state response remains unproven because the initiating
watch did not stay active through its timeout window.

Do not ask the owner to repeat the unchanged procedure again. Next prepare a
bounded diagnostic keep-awake option for the visible initiating watch during
an outstanding request, with cleanup on acknowledgment, failure, timeout or
pause. Preserve listener scope, do not add a background service or change
system sleep settings, and label the intervention in evidence. No such change
has been implemented in this follow-up.

## Revision 3: bounded pending-request keep-awake deployed

The subsequent owner `proceed` authorized the diagnostic adjustment.
Two T04 red/green cycles verify pending eligibility through matched acknowledgment
and explicit completion rejecting late acknowledgment. Retained outputs:
[first red](keep-awake-red.log), [first green](keep-awake-green.log),
[terminal red](keep-awake-terminal-red.log),
[terminal green](keep-awake-terminal-green.log), and
[final request-state XML](keep-awake-terminal-green.xml). The full JVM suite
has 16 passing tests; these are host request-state checks, not Android power
or listener timing tests.

The Android adapter sets its keep-screen-on window flag for an active pending
request and clears it on matched acknowledgment, send failure, timeout or pause.
Each revision-3 event samples the flag. No background service, wake lock,
pairing reset or system sleep-setting change was added. A stale send-failure
callback is labeled separately rather than replacing a newer request result.

[Build output](revision3-build.log) and [deployment receipt](revision3-deployment.json)
retain the offline build and APK digest. Installation succeeded on Pro XL and
Watch 4; [phone initial report](revision3-initial-pixel-10-pro-xl.json) and
[watch initial report](revision3-initial-pixel-watch-4.json) confirm revision 3.
Both initial reports include subsequent pause events, so they do not establish
stable foreground readiness or keep-awake success. Revision 3 uses the separate
`files/link-lifecycle-v3-report.json`; revision-2 reports are preserved.
Pixel 8 Pro received no deployment or probe interaction.

Next: owner opens both probes, waits for readiness, puts phone on Home, then
sends one watch ping while leaving the watch probe visible for twelve seconds.
Collect before recovery or screen-off trials. Physical keep-awake efficacy,
Home-state response, M2.2, M2.4 and G2 remain open.

## Revision-3 Home trial: timeout without initiating-watch pause

After the owner reported `timeout`, collected new snapshots without reopening
the phone probe or altering device settings:
[phone](revision3-home-timeout-20261025T191120Z-pixel-10-pro-xl.json) and
[watch](revision3-home-timeout-20261025T191120Z-pixel-watch-4.json).
The watch request started at local elapsed 53,252,455 ms with `keepScreenOn=true`;
send acceptance followed. `ack_timeout` occurred at 53,262,476 ms, 10.021 seconds
later, still active and interactive, with `keepScreenOn=false` after cleanup.
No intervening watch pause is logged. Its next pause was 35.223 seconds after
the timeout. This run verifies the bounded adjustment avoided the previously
observed initiating-watch cancellation; it does not establish all terminal
Android cleanup paths or sustained power behavior.

The phone's final sequence is listener readiness, pause/stop and listener removal
with `removed=true`, inactive, interactive and unlocked. There is no later
incoming callback in the collected phone report. Approximate wall-time ordering
puts removal before the watch request, consistent with the owner's Home trial;
cross-device clocks are not independently synchronized and monotonic clocks
must not be compared across devices.

Result: the foreground-only diagnostic produced no matching acknowledgment
with the receiving phone app on Home. This is consistent with deliberate
listener removal; it is not evidence of radio disconnection, a registration
race, or platform inability to support a production background receiver.
Next reopen the phone probe, wait for `listener_ready`, and send one watch ping
to verify foreground recovery. Collect before a separate screen-off trial.
Do not repeat the unchanged Home case. M2.2, M2.4 and G2 remain open.

## Layout-only safe-area correction

Owner reported first-line obstruction by the phone camera cutout and watch
round edge before attempting recovery. Replaced raw-pixel padding with dp
spacing and system-bar/display-cutout insets. Round screens use a centered
70%-width/height clipped scrolling viewport, keeping all rows away from the
curved corners even when scrolled. No dependency, transport, listener, timeout,
keep-awake policy or diagnostic schema change was made.

[Offline build](safe-area-build.log) passed; the
[JVM regression task](safe-area-regression.log) passed using up-to-date results,
not a fresh test execution or UI test. [Deployment receipt](safe-area-deployment.json)
records successful installation of the same APK digest on Pro XL and Watch 4.
No pings were sent. An attempted UI capture showed the phone lock screen,
so visual confirmation requires the owner to unlock/open both probes; do not
claim verified clipping correction from the build or installation alone.
Pixel 8 Pro received no deployment or probe interaction. Recovery trial remains
pending until the owner can read both displays.

Pre-commit validation: [fresh JVM regression run](precommit-regression.log)
passed with all tasks rerun (16 tests), distinct from the cached layout check.
The incidental phone lock-screen screenshot was removed before staging; it
provided no probe UI evidence. Retain the deployment receipt and owner's
subsequent readability confirmation instead.

## Recovery after requested lock trial: matched acknowledgment

Owner reported `MATCHED`. The
[phone snapshot](revision3-lock-recovery-20261025T195500Z-pixel-10-pro-xl.json)
records a new foreground activity, listener readiness, incoming ping and
accepted acknowledgment send while interactive and unlocked. The
[watch retry snapshot](revision3-lock-recovery-20261025T195500Z-pixel-watch-4-retry.json)
records request start at local elapsed 55,907,349 ms and matched acknowledgment
at 55,907,910 ms (`roundTripMs=561`), active/interactive with no intervening
pause. The keep-screen-on flag changed from true at request/send to false at
matched acknowledgment. Exact packet-level cross-device correlation cannot be
reconstructed without the intentionally omitted tokens; the watch's matched
acknowledgment establishes local request success.

The first watch ADB read timed out; retry collected the report without a
pairing/radio reset, activity launch or settings change. An ADB collection
timeout is not a probe acknowledgment timeout. Pixel 8 Pro received no probe
interaction.

This completes the requested Home and lock lifecycle observation sequence:
watch cancellation was corrected by bounded foreground keep-awake; watch
requests timed out with the phone receiver paused/listener removed, and
foreground recovery succeeded after each condition (187 ms after Home,
561 ms after the requested lock trial). The lock trial sampled locked state,
not a proven sustained non-interactive interval. Owner readability acceptance
is recorded separately. No further identical manual ping trial is needed.
Production background handling, broader lifecycle/race reliability, other
pending M2 checks and G2 are not established by this sequence; M2.2, M2.4 and
G2 remain open.

## Foreground recovery and owner layout review

Owner reported `local request MATCHED` and confirmed the updated text looks
cleaner and is readable on the phone and watch. This is owner visual acceptance
for the current device layouts, not coverage of every screen size/font setting.

New [watch snapshot](revision3-recovery-20261025T192225Z-pixel-watch-4.json)
records a request at local elapsed 53,918,607 ms with `keepScreenOn=true`, followed
by an incoming acknowledgment and `matched_ack` with `roundTripMs=187`, still
active/interactive and with `keepScreenOn=false` after cleanup. There was no
intervening pause. The [phone snapshot](revision3-recovery-20261025T192225Z-pixel-10-pro-xl.json)
records foreground listener readiness, incoming ping and accepted acknowledgment
send. Neither request tokens nor peer IDs were retained; exact cross-device
packet matching cannot be independently reconstructed from these sanitized logs.
The initiating watch's matched acknowledgment is the success criterion.

Foreground recovery after the retained Home-state timeout is verified for this
attempt, with no pairing/radio reset. Together these trials observe keep-awake
release on timeout and matched acknowledgment; other cleanup paths are not
physically established. Do not infer background support or all lifecycle-race
coverage. No code or device settings changed during collection; Pixel 8 Pro
received no probe interaction. Next separately check phone screen-off/locked
state with the watch probe visible, then collect before reopening the phone.
M2.2, M2.4 and G2 remain open.

## Revision-3 requested screen-off trial: watch timeout, phone lock sampled

After the owner reported `timeout`, collected
[phone snapshot](revision3-screen-off-20261025T192743Z-pixel-10-pro-xl.json) and
[watch snapshot](revision3-screen-off-20261025T192743Z-pixel-watch-4.json)
without launching either activity or changing device settings.
The latest watch request started at local elapsed 54,265,677 ms and timed out
at 54,275,708 ms (10.031 seconds), active and interactive throughout the logged
request endpoints, with no intervening pause. The keep-screen-on flag was true
at start/send acceptance and false at timeout. No matched acknowledgment was
recorded for that request.

The phone's latest activity became ready, then paused, removed its listener
(`removed=true`) and stopped. At stop, `keyguardLocked=true` and
`deviceLocked=true`, but `interactive=true`. No incoming ping callback followed
in the snapshot. Thus a locked state is sampled; non-interactive/display-off
state during the watch request is not directly measured by these activity-only
logs. Approximate wall-time ordering places phone stop before the watch request;
cross-device clocks remain unsynchronized. Do not label this a proven sustained
screen-off interval or infer radio disconnection/platform background limits.

An additional earlier phone-initiated request timed out in this snapshot while
the phone was active. It predates the final phone activity's pause/stop and the
latest watch request; its remote listener state and cause are unresolved. Do
not conflate it with the requested watch-initiated trial or assume the owner
performed only the prescribed sequence.

The current result is consistent with the foreground-only receiver's deliberate
listener removal. Next unlock/reopen the phone, wait for `listener_ready`, then
send one watch ping with both probes visible to check recovery after this trial.
No repeated unchanged lock trial is needed. Pixel 8 Pro received no probe
interaction. M2.2, M2.4 and G2 remain open.

## Lock-trial recovery evidence reviewed on 2026-10-29

The already-retained October 25 [phone recovery snapshot](revision3-lock-recovery-20261025T195500Z-pixel-10-pro-xl.json)
and [watch recovery snapshot](revision3-lock-recovery-20261025T195500Z-pixel-watch-4-retry.json)
complete the preceding foreground-recovery handoff. The phone records an unlocked,
active activity with `listener_ready`, followed by a received ping and accepted
acknowledgment send. The watch discovers one nearby peer, starts a request and
records `matched_ack` with `roundTripMs=561`. Its keep-screen-on flag is true
while pending and false at completion; no pause intervenes before that result.
This is one successful foreground recovery attempt, not background delivery or
proof of the underlying radio route. Sanitized reports do not allow independent
cross-device packet correlation, and device clocks are not assumed synchronized.
These are historical observations, not a new October 29 physical test.

On October 29, ADB lists one authorized Pixel 10 Pro XL and no watch. Read-only
inspection confirms the phone probe remains installed and its revision-3 report
is readable. No activity was launched or device setting changed. Reconnect and
authorize the paired Pixel Watch 4 before further physical watch validation;
the next remaining topology work includes controlled disconnect/reconnect and
second-Android-controller coexistence with the internet uplink. Do not repeat
the completed foreground-recovery trial merely because its narrative was absent.

Host regression was explicitly rerun using
`powershell -NoProfile -ExecutionPolicy Bypass -File tools/m2/run-feasibility.ps1 test --offline --rerun-tasks`:
all sixteen tests passed (ten storage, three session-readiness and three pending
request cases), with four Gradle tasks executed and build exit 0. The initial
up-to-date invocation was not counted as fresh execution. The planning-package
checker also passed with 99 documents, 1,565 local links, 302 anchor links,
53 tasks and 159 dependency edges before this documentation update. No probe
code, application-stack decision, task completion or G2 disposition changed.

## Fresh foreground baseline on 2026-10-29 (America/New_York)

Both Pro XL and paired Pixel Watch 4 were authorized in ADB. The existing
diagnostic activities were brought to the foreground; the owner reported a
matched acknowledgment after initiating one watch ping. The retained
[watch report](foreground-baseline-20261030T002904Z-pixel-watch-4.json)
records one nearby peer and `matched_ack` with `roundTripMs=541`, active and
interactive at request/completion, with no intervening pause. The pending
keep-screen-on flag clears on completion. The [phone report](foreground-baseline-20261030T002904Z-pixel-10-pro-xl.json)
records listener readiness, received ping and accepted acknowledgment send.
UTC filenames fall on October 30; the local observation date is October 29.
This is foreground diagnostic delivery only; transport route, application
authorization and disconnect/reconnect remain unverified. No radio setting
was changed by the agent. Next perform one operator-controlled Bluetooth
interruption, collecting its result before restoring Bluetooth and testing
fresh discovery/recovery. A successful interruption-phase ping does not prove
a full disconnection occurred or identify the fallback route.

## Operator-controlled Bluetooth interruption on 2026-10-29

Following the foreground baseline, the operator was asked to disable Bluetooth
on the Pro XL only, leave Wi-Fi/mobile data unchanged, reopen both probes and
send one watch ping. The owner selected the timeout/no-peer response; collected
[watch metadata](bluetooth-interruption-20261030T003721Z-pixel-watch-4.json)
resolves that ambiguity: discovery records `count=1;nearby=0`, followed by send
acceptance and `ack_timeout`. Request elapsed time is 10,044 ms, from
418,426,448 to 418,436,492 ms. The activity remains active/interactive without
an intervening pause; keep-screen-on clears at timeout. No matched reply is
recorded for this request. This is not a zero-peer observation.

The [phone metadata](bluetooth-interruption-20261030T003721Z-pixel-10-pro-xl.json)
contains a foreground ping callback and accepted acknowledgment send in the
trial's approximate wall-time interval. Without retained request tokens or
synchronized clocks, exact cross-device correlation cannot be independently
established. Send acceptance is not watch receipt, and the failure cause is
unresolved. A subsequent read-only `settings get global bluetooth_on` returned
`0`; this samples the collection-time setting, not continuous radio state.
The underlying network route and complete transport disconnection are not
established by this Bluetooth interruption. No radio was changed by the agent.

Next restore phone Bluetooth, reopen both probes, wait for listener readiness
and fresh discovery, then initiate one watch ping to assess recovery. Recovery
is pending; do not count this trial as a completed disconnect/reconnect case
or as evidence of production background support. M2.4 and G2 remain open.

## Bluetooth-restored foreground recovery on 2026-10-29

The owner restored phone Bluetooth and reported a matched acknowledgment after
reopening both probes and initiating one watch ping. The retained
[watch recovery report](bluetooth-recovery-20261030T003951Z-pixel-watch-4.json)
records fresh discovery `count=1;nearby=1`, request start at 418,585,459 ms
and `matched_ack` at 418,585,729 ms with the probe-reported
`roundTripMs=271`. No pause intervenes; the activity is active/interactive
and keep-screen-on clears on completion. Subtracting the recorded event
timestamps gives 270 ms; the retained probe round-trip field is 271 ms and is
reported as recorded rather than replaced by that subtraction.
The [phone recovery report](bluetooth-recovery-20261030T003951Z-pixel-10-pro-xl.json)
records a foreground ping and accepted acknowledgment send. A read-only phone
setting query returned `bluetooth_on=1` at collection. The agent did not change
radios, pairing or network settings. The owner-controlled setting is restored.

This establishes one foreground recovery after a Bluetooth-off trial with a
watch acknowledgment timeout; it does not establish a total Data Layer
disconnection, Bluetooth-only routing, a timeout cause, sustained reliability,
fresh authoritative match snapshots or production authorization. The broader
RT04 protocol case and Android-controller/uplink coexistence remain unverified.
No probe code, technology decision, task completion or gate disposition changed.