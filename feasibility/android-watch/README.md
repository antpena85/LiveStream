# Isolated M2 phone/watch link probe

Not the release application. Uses the same diagnostic APK/application ID and
project-local debug key on the Pixel 10 Pro XL and paired Pixel Watch 4. No
production UI framework, remote transport or release signing decision is made.
Google Play services Wearable 20.0.1 is a probe-only dependency.

From the workspace root:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File tools/m2/run-android-inventory.ps1 -Probe watch assembleDebug
powershell -NoProfile -ExecutionPolicy Bypass -File tools/m2/run-feasibility.ps1 test --offline
```

Open **M2 Watch Link** on both devices and keep both screens awake. Wait for
`listener_ready`, then tap **Send ping** on the phone. A `matched_ack` with a
round-trip duration is success; `send_accepted_not_ack` alone is not success.
Repeat initiating from the watch. Discovery requires exactly one connected peer.
If either screen reports a failure or never enables Send ping, report its text.
No radio/pairing reset is required. Pausing cancels pending requests; this probe
does not evaluate background delivery.

Sanitized metadata is in each app's private `files/link-report.json`, readable
using ADB `run-as livestream.feasibility.watchprobe`. It contains event kinds,
durations and counts, not peer IDs or diagnostic tokens. An activity restart
starts a new event list. Do not copy device serials into evidence.

See the [contract](../../plan/specifications/m2-watch-connectivity-contract.md)
and [execution evidence](../../plan/evidence/m2/watch-connectivity/README.md).

## Lifecycle diagnostic revision 2

The updated probe keeps lifecycle/message events in the separate atomic
`files/link-lifecycle-report.json`. The old `link-report.json` is preserved.
Revision 2 records activity instance, create/resume/pause/stop/destroy, listener
removal completion and incoming callback type, plus sampled interactive/keyguard/
device-lock flags. Interactive state is not an exact screen-on measurement.
Events survive activity recreation; diagnostic wall times help compare devices
but clock alignment is not assumed. No peer IDs or request tokens are retained.

The separate **Local request** text displays WAITING, MATCHED ACK, TIMEOUT or
CANCELLED. Incoming messages and listener events do not replace that result.
Only a newly matched acknowledgment counts for a new trial.

For controlled background observations, keep the watch app awake. First send
one baseline watch ping with the phone app open. Put the phone on Home, wait
five seconds, send one watch ping and wait at least ten seconds. Reopen the
phone probe, wait for listener readiness, and send one watch ping to check
recovery. Collect revision-2 metadata before repeating screen-off/lock as a
separate trial. Listener behavior is unchanged; no background service is added.

## Lifecycle diagnostic revision 3

Revision 3 writes `files/link-lifecycle-v3-report.json`; both older report files
are preserved. Events include `keepScreenOn`, the sampled activity window flag,
not proof of actual display state. An active pending local request sets
`FLAG_KEEP_SCREEN_ON`; matched acknowledgment, send failure, ten-second timeout
or pause clears it. No wake lock, background service or system sleep-setting
change is used. Listener registration/removal remains foreground-only.

For the next trial, open both probes and wait for `listener_ready`. Put only
the phone on Home, then open/keep the watch probe visible and send one ping.
Wait twelve seconds without leaving the watch app or pressing its power button.
Report the Local request result and leave the phone on Home for collection.
Do not combine this with a locked-phone trial. Whether the window flag prevents
the observed watch pause is still pending physical verification.

The layout-only safe-area update retains revision 3: phone content respects
system-bar/camera-cutout insets, and round displays use an inset, clipped
scrolling viewport. Open both probes to confirm text is readable before the
foreground recovery ping. No connectivity behavior changed.