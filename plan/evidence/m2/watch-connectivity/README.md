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