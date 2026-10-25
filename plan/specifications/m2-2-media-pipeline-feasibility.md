# M2.2 media pipeline investigation

Recorded: 2026-10-23 (America/New_York)  
Status: Candidate comparison and probe protocol prepared; no media path demonstrated.  
Task: [M2.2](../subplans/m2-2-media-pipeline-feasibility.md)

## Candidate comparison

| Candidate | Reason to evaluate | Remaining cost or risk |
| --- | --- | --- |
| RootEncoder 2.8.1 with its camera/audio sources, GL composition, encoder and RTMP publisher | One integrated path; upstream documents overlays and live bitrate changes, with CameraX as an extra source option. | Device lens/audio control, cellular socket selection, timing, dependency/license inventory, live changes and thermal behavior need verification. |
| CameraX capture/effects with a separate encoder and publisher | Higher-level lifecycle integration and official camera abstractions. | CameraX Recorder is a recording pipeline, not evidence of a complete RTMP publishing path; verify surfaces, encoded buffers and publisher ownership before selecting it. |
| Camera2 + AudioRecord + EGL/MediaCodec + publisher | Direct control over capture, composition and network routing. | Greatest integration/maintenance effort; every lifecycle and timestamp boundary is ours to validate. |

Recommend evaluating the integrated option first, comparing CameraXSource and
Camera2Source against the actual exposed lenses. This is an engineering
recommendation, not a selected application dependency. The upstream
[RootEncoder README](https://github.com/pedroSG94/RootEncoder#readme),
[API](https://pedrosg94.github.io/RootEncoder/library/com.pedro.library.base/-stream-base/index.html),
and [Apache-2.0 license](https://github.com/pedroSG94/RootEncoder/blob/master/LICENSE.txt)
support that candidate. Its transitive dependencies and notices still need a
resolved-artifact review. The official
[CameraX capture architecture](https://developer.android.com/media/camera/camerax/video-capture)
describes Recorder's encoders/muxer/file outputs; this comparison does not assert
that all custom streaming integrations are impossible.

## Probe sequence and evidence

All cases below are unrun. Use [M2.1](m2-1-capability-validation.md) observations
and confirmed T02/T05 boundaries. Start with built-in audio, landscape
1280×720/30, one camera, no account automation, and an operator-approved test
destination. Never put credentials into retained fixtures or logs.

| ID | Experiment | Required observation |
| --- | --- | --- |
| MP01 | Camera → one GL scoreboard → AVC surface encoder; audio → AAC; publish | Decode actual received output containing required fields and synchronized audible audio. Preview alone is insufficient. |
| MP02 | Change match projection and move through all five anchors | Coherent accepted values, stable team ownership and readable received scoreboard. Include Q44/Q45 large-number cases. |
| MP03 | Switch supported cameras/lenses | Record interruption and timestamps; retain match, overlay and intended audio route. |
| MP04 | Exercise A00 built-in audio for version 1 | Actual captured/received audio, levels, mute, synchronization and visible failures. A01–A04 accessory support/testing follows v1 under Q48. |
| MP05 | Request live video bitrate changes | Record requested/observed output, send queue and receiver continuity; an API return is not measured encoder compliance. |
| MP06 | Run representative sustained capture | Retain temperature/thermal status, battery, frames, audio drift, crashes and interruptions. PRD full-duration V01 sessions remain separate acceptance. |
| MP07 | LAN controller plus cellular publishing | Demonstrate actual per-connection routing, DNS and TLS; do not assume process-wide network binding is safe. |

Reject a candidate if any required path lacks a supported integration route;
document the failure rather than building multiple competing pipelines. No
pipeline, audio accessory, live switch, or thermal claim is established here.
D03/D07/D08 and M2.2 remain open.
