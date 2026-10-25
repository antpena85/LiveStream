# M2 local media capture experiment

Specified: 2026-10-24 (America/New_York)  
Scope: Isolated M2.2 hardware experiment; not the release application or public broadcast.

## Observable contracts before execution

T02 is already owner-confirmed under Q47. For the audio-enabled experiment,
a confirmed start with camera permission but without microphone permission
must return `MicrophonePermissionRequired` and remain Stopped. Specify one
behavioral test, observe its failure, then add the minimum guard. Existing
camera/cancel and T01 tests remain regression coverage. The Android adapter
will compile this same tested source, rather than duplicate the policy.

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
