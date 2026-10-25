# Partial local media observation

Recorded: 2026-10-24 (workspace local date)

Scope: isolated M2 camera/audio experiment, not the release application or a
received broadcast. See the [pre-execution contract](../../specifications/m2-media-check-contract.md).

The existing media experiment built successfully offline and was installed and
opened on the authorized Pixel 10 Pro XL. The owner ran the ten-second test and
reported that the score looked correct and placement looked as expected.
This is an owner visual observation; audible audio, synchronization and explicit
orientation review have not yet been confirmed.

The [collected metadata](android-media-2026-10-24/pixel-10-pro-xl.json) reports:

- Stop reason `ten_second_timer`; 2,593,066-byte local clip.
- AVC video, 1280×720, 292 samples, approximately 29.91 samples/second.
- AAC audio, 48 kHz, two channels, 451 encoded samples.
- Observed audio input type 15 and a decoded frame available.
- No setup or inspection error field; publishing explicitly `not_run`.

Only JSON metadata was collected. No scene image, clip or voice recording was
copied from the phone. Track presence alone does not establish audible audio,
lip synchronization, destination reception or sustained performance.

At collection time the Pixel 10 Pro XL was authorized in ADB. A second connected
device remained unauthorized even after the owner reported unlocking the second
phone; its model could not be verified. The owner reports a watch paired with
the Pro XL, but actual Data Layer communication remains untested.

## Pixel 8 Pro follow-up

A later ADB query verified both phones authorized, including the Pixel 8 Pro.
The same existing media APK installed and opened successfully on that phone.
After being asked to review scoreboard, placement, orientation and audible audio,
the owner reported that the test completed as expected. This is a general
owner-reported successful result, not separately measured audiovisual
synchronization or an itemized confirmation of each playback property.

The [Pixel 8 Pro metadata](android-media-2026-10-24/pixel-8-pro.json) reports:

- Stop reason `ten_second_timer`; 2,591,823-byte local clip.
- AVC video, 1280×720, 296 samples, approximately 29.94 samples/second.
- AAC audio, 48 kHz, two channels, 466 encoded samples.
- Observed audio input type 15 and a decoded frame available.
- No setup, record, stop, release or inspection error field; publishing `not_run`.

Collection also reread the Pro XL's existing report; it is not a second capture.
No video, decoded image or voice recording was copied from either phone.

Next: continue the separately required lens/mute, watch/topology, provider,
lifecycle and quality experiments. Pro XL audible playback and explicit
orientation review remain unconfirmed. These short local results do not
complete M2.2, select the application stack or pass G2.