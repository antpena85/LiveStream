# M2 local media capture experiment

Specified: 2026-10-24 (America/New_York)  
Scope: Isolated M2.2 hardware experiment; not the release application or public broadcast.

## Observable contracts before execution

T02 is already owner-confirmed under Q47. For the audio-enabled experiment,
a confirmed start with camera permission but without microphone permission
must return `MicrophonePermissionRequired` and remain Stopped. Specify one
behavioral test, observe its failure, then add the minimum guard. Existing
camera/cancel and T01 tests remain regression coverage. The Android adapter
The original Android adapter compiled that tested source. The 2026-11-01
integration repair below replaces it with a separately tested local-only gate.
The physical experiment evaluates the already proposed RootEncoder 2.8.1
candidate with its Camera2 source, built-in microphone preference, GL bitmap
scoreboard, AVC encoder and AAC audio. Its operator initiates a ten-second
local evidence capture through the phone UI. It never calls a publishing
endpoint. The short clip is diagnostic evidence, not a v1 local-recording feature.

Initial trial values are landscape 1280×720, 30 fps, 2 Mbps requested video,
48 kHz stereo AAC at 128 kbps. These are experiment inputs, not final D07 bounds.
The constant asymmetric scoreboard includes codes, points 99/100, sets 2/1,
timeouts 2/1 and a serve mark. Its geometry is a candidate, not approved D10 fit.

After capture, inspect actual encoded tracks, sample counts/timestamps, audio
route metadata and a decoded frame; manual playback must verify the scoreboard
pixels and audible audio. Distinguish an accepted bitrate request from measured
encoder compliance. Do not infer court readability or destination reception
from a local clip. Retain only sanitized metadata in planning evidence; the
clip remains in the diagnostic app's private storage for owner review.

The first experiment uses one rear logical camera. Lens changes, prolonged
load, background/lock/calls, Wi-Fi/cellular routing, watch commands, provider
authorization and received broadcasts remain separate required checks. Q48
defers external microphone support/testing; it does not waive built-in audio.

## Front/rear and mute extension (specified before execution)

An additional operator-started 20-second local experiment retains the same
fixture/settings and confirmed T02/T05 physical observation boundaries. At four
seconds request microphone mute; at eight request unmute; at twelve request
front-camera switching; at sixteen request switching back. Stop at twenty.
Keep speaking/counting throughout to review live/muted/live audio intervals.
Record timed events, camera IDs and mute API state, including control failures.
API state is not proof of audible silence or the correct captured scene.

Review the entire clip for audible/muted/audible intervals, front/rear scene
changes, stable scoreboard, orientation and interruptions. Encoded timestamps
measure gaps but do not prove audiovisual synchronization. Physical rear-lens
selection, received output and sustained load remain separate experiments.
The next run replaces the private clip; previously collected metadata is kept.
This adapter-only exploratory extension adds no application behavior, host TDD
cycle or hardware compatibility claim before physical observation.

## Proposed isolated publishing experiment — 2026-11-01

This is an M2.2 experiment proposal, not a release application or permission to
publish automatically. The operator selects an approved test destination and
enters its details directly on the device; never place stream keys or complete
credential-bearing URLs in chat, source, retained reports, screenshots or logs.
Use explicit confirmation before each start and stop, visible stopped/connecting/
connected/failure states based on actual callbacks, a bounded trial, and safe
cleanup when the activity exits or the connection fails. No unattended retry,
automatic start or provider-resource creation is implied. Specify and test the
public session/media boundary (T02, confirmed under Q47) one behavior at a
time before implementing it. A cancelled start must not send media; a connection
attempt is not a successful broadcast. Callback messages must be sanitized
without recording URLs, keys, tokens or raw provider error strings.

For MP01, use the existing asymmetric overlay and built-in audio fixture; record
the selected device/build, transport, trial start/stop outcomes and sanitized
errors. The operator inspects the destination playback for the scoreboard,
audible audio and synchronization, reporting observations separately from API
callbacks. Measured synchronization, actual bitrate compliance, remote network
coexistence and sustained thermal operation require separately defined checks;
one received short trial cannot establish them or pass G2.

### Baseline integration blocker

On 2026-11-01, the existing Android media project's offline `assembleDebug`
failed in `compileDebugKotlin`: its generated copy of `StreamSession.kt` now
references `DurableCommandProbe` and `ProbeSnapshot`, which that Android project
does not provide. The shared session and its tests contain existing in-progress
changes, which must be preserved. The JVM `test --offline` task succeeded but
was up to date; the Android compilation error is not a behavioral red result.
Resolve the shared public readiness/storage boundary without copying JVM SQLite
code into Android, replacing pending-stop/numeric-fit safety with a permissive
stub, or implying that the diagnostic capture is a production stream. Rebuild
and rerun the affected tests before implementing publishing or deploying a new
APK. The previous installed APK is historical and does not validate current
source.

### Local-only integration repair

The media activity now uses the separate JVM-tested `LocalCaptureReadiness`
permission gate, copied into the Android project at build time. It governs only
the existing local-recording experiment and cannot authorize a network stream.
The unchanged `StreamSession` retains durable pending-stop and snapshot-fit
checks; no store stub or production persistence claim was added. At T02,
the local gate's ready, camera-denied and microphone-denied cases each have
observed red/green evidence: [ready red](../evidence/m2/local-capture-ready-red.xml)
and [green](../evidence/m2/local-capture-ready-green.xml),
[camera red](../evidence/m2/local-capture-camera-red.xml) and
[green](../evidence/m2/local-capture-camera-green.xml),
[microphone red](../evidence/m2/local-capture-mic-red.xml) and
[green](../evidence/m2/local-capture-mic-green.xml). The
[unconfirmed case](../evidence/m2/local-capture-cancel-first-run.xml) passed
on its first run without an implementation change; it is not a red/green cycle.
After review the full JVM suite ran fresh with 47 passing tests and the media
`assembleDebug --offline` build succeeded. The rebuilt APK was not installed
or physically tested. No publishing adapter or race-safe authorization to
publish is established by this local-only repair.
