# Local front/rear and mute experiment

Recorded: 2026-10-24 (workspace local date)

Scope: isolated M2 experiment, not a release application or received broadcast.
The [contract](../../specifications/m2-media-check-contract.md#frontrear-and-mute-extension-specified-before-execution)
was specified before execution. The owner reports running the twenty-second
test on both devices and subsequently confirmed the playback properties below.

## Collected metadata

| Observation | [Pixel 8 Pro](android-media-transitions-2026-10-24/pixel-8-pro.json) | [Pixel 10 Pro XL](android-media-transitions-2026-10-24/pixel-10-pro-xl.json) |
| --- | --- | --- |
| Stop reason | twenty_second_timer | twenty_second_timer |
| Mute API state / elapsed time | true / 4037 ms | true / 4046 ms |
| Unmute API state / elapsed time | false / 8036 ms | false / 8047 ms |
| Front camera ID / elapsed time | 1 / 12036 ms | 1 / 12047 ms |
| Rear camera ID / elapsed time | 0 / 16036 ms | 0 / 16047 ms |
| AVC dimensions | 1280×720 | 1280×720 |
| Video samples / approximate samples per second | 571 / 28.95 | 572 / 28.96 |
| Maximum video sample timestamp gap | 360.366 ms | 335.233 ms |
| AAC configuration / samples | 48 kHz stereo / 924 | 48 kHz stereo / 918 |
| Decoded frame available | Yes | Yes |

Both reports contain all four control events without an event error, no setup,
record, stop, release, inspection or control error field, and `publishing=not_run`.
Both phones were authorized when the reports were collected. Only JSON metadata
was copied; scene images, clips and voice recordings remain on the devices.

## Owner playback review

The owner confirmed on both Pixel 8 Pro and Pixel 10 Pro XL:

- The scoreboard looked correct.
- Audio muted and unmuted.
- Cameras changed rear → front → rear.

These are owner-observed local playback results, supporting the corresponding
API/encoded-track observations. They do not establish received-broadcast
behavior, measured audiovisual synchronization, absence of freezes, or explicit
orientation correctness; those properties were not separately confirmed.

## Limitations and next check

API mute state and camera IDs alone do not prove captured output; the owner
review above supplies separate evidence for mute, scene changes and scoreboard
correctness. The maximum
video timestamp gaps show discontinuities; their timing relative to switching
and perceived playback effects have not been established by this report.
Do not describe these switches as seamless or the output as sustained 30 fps.

Actual rear-lens selection, watch/phone connectivity, provider publishing,
network adaptation and lifecycle/sustained load remain separate checks.
M2.2 remains In progress and G2 has not passed.