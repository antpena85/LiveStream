# Post-update local front/rear and mute experiment

Recorded: 2026-10-30. Scope: isolated M2.2 local diagnostic capture on the
owner-identified, previously updating Pixel 8 Pro; not a received broadcast.
The [existing 20-second contract](../../specifications/m2-media-check-contract.md#frontrear-and-mute-extension-specified-before-execution)
applies. The owner confirmed update completion and identified the phone's build;
[read-only ADB inspection and APK deployment](android-controller-preflight-2026-10-29.md#post-update-adb-reinspection--2026-10-30)
preceded this run. The owner initiated the test and reported that everything
worked as expected after being asked to check scoreboard, live/muted/live audio
and rear → front → rear camera changes. This is owner-reported local playback,
not an independent assessment of timing, orientation or continuity.

## Collected metadata

Only the [diagnostic JSON report](android-media-transitions-post-update-2026-10-30/pixel-8-pro.json)
was copied from the uniquely matched authorized Pixel 8 Pro on Android 17/API 37,
build CP3A.261005.005. The private clip, image and recorded audio were not copied.
The report identifies `front_rear_mute_20s`, `twenty_second_timer` and
`publishing=not_run`; no error fields were present.

| Observation | Result |
| --- | --- |
| Mute / unmute API states and elapsed times | true at 4084 ms / false at 8084 ms |
| Front / rear camera IDs and elapsed times | 1 at 12084 ms / 0 at 16085 ms |
| AVC track | 1280 × 720, 572 samples, approximately 28.95 samples/s |
| Maximum video sample timestamp gap | 371.944 ms |
| AAC track | 48 kHz stereo, 920 samples |
| Decoded-frame availability | Yes |

The maximum timestamp gap is retained without attributing it to a particular
event. API state and metadata alone do not establish audible mute, scene changes,
scoreboard correctness or synchronization; the owner's playback report is a
separate observation. The result does not establish seamless switching, sustained
performance, actual received output, synchronization, quality adaptation or
physical rear-lens selection. M2.2 and G2 remain open.