# Isolated phone/watch connectivity contract

Scope: authorized M2.4 feasibility only; T04 is already owner-confirmed.

Use one diagnostic APK with matching application ID/signature on Pixel 10 Pro XL
and its paired Pixel Watch 4, with a simple native diagnostic screen rather than
selecting the production UI stack. Do not deploy this probe to Pixel 8 Pro.
Use Google Play services MessageClient for ping and explicit acknowledgment.
No match mutation, camera control, credentials or pairing secrets are transferred.

At T04, a pending request can be acknowledged only by its expected peer with
its exact diagnostic token. Wrong-peer or wrong-token acknowledgments leave the
request pending; a matching acknowledgment completes it once. Observe a failing
host test before implementing this behavior. Copy the tested source into the
Android build, as the media probe does for readiness.

The operator opens both probe screens and taps Send ping on either device.
Discover reachable nodes, require exactly one peer for this isolated setup,
and record counts/nearby flags, send outcomes, received pings, matched replies,
timeouts and round-trip durations. Do not retain node IDs or payload tokens.
An accepted send is not an acknowledgment. Report failures explicitly and time
out after ten seconds. Background service behavior is not evaluated; listeners
are active only while the screen is resumed. Pausing cancels a pending request.

First physical result: verify round trips initiated from each device. Later
disconnect/reconnect needs an operator-controlled disruption and fresh discovery.
Do not unpair/reset the watch or alter radios automatically. Data Layer may use
Bluetooth or a network path, so success is not proof of Bluetooth-only transport.
This probe does not establish application authorization, durable command results,
latency targets, reconnect snapshots or G2 completion.

References: [Data Layer overview](https://developer.android.com/training/wearables/data/overview)
and [MessageClient API](https://developers.google.com/android/reference/com/google/android/gms/wearable/MessageClient).

## Lifecycle observation extension

Before repeating phone screen-off/lock and home/other-app states, add diagnostic
events for activity create/resume/pause/stop/destroy, listener removal completion,
and received callbacks (including inactive callbacks). At every event sample
activity-active, interactive, keyguard and device-lock state. Interactive state
is not an exact screen-on signal; these samples are local observations, not
continuous power-state monitoring. Do not alter listener lifetime or add a
background service as part of this observation change.

Persist sanitized events across activity recreation using a separate atomic
`link-lifecycle-report.json` file, with an activity-instance marker and wall-clock
time in addition to local monotonic time. Keep the old report unchanged. Mark
the new diagnostic revision explicitly. Keep a separate visible local-request
result so an incoming ping or listener event cannot masquerade as a matching
reply. Each new request clears that result to waiting; record request start,
matching reply, timeout or cancellation. No tokens or node IDs are retained.

Perform one state at a time: foreground baseline, home/other app, foreground
recovery; collect; then screen-off/locked, foreground recovery; collect. Keep
the watch probe awake during each phone-state trial and wait at least ten
seconds after sending. Do not interpret foreground-only diagnostic timeout as
a production background-support decision. These are instrumentation changes,
not new command-protocol behavior or a claimed host red/green cycle.