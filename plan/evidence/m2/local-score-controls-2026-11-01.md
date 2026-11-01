# Local diagnostic score controls — owner playback review

Date: 2026-11-01. Device: Pixel 10 Pro XL, build CP3A.261005.005.

The owner ran the operator-initiated 10-second local capture and reported that
both team + buttons worked as intended and that audio and video were good on
playback. This is an owner observation, not an automated verification or a
received broadcast. The clip remains private on the device and is not retained
here.

The app's diagnostic metadata identifies the baseline local-score fixture,
timer stop, AVC 1280×720 (292 samples) and AAC 48 kHz stereo (455 samples), and
a decoded frame. It reports `publishing: not_run`; no setup, recording, stop,
release, inspection or control error field appeared in the report. Metadata
does not independently establish score readability, audio quality, exact A/V
sync, live output, or sustained operation.

The Android diagnostic APK built and was installed and launched before the run.
The JVM score-display tests for both teams each had a behavioral red followed
by green, and the full JVM suite passed. The simplified fixture does not
implement complete volleyball scoring or numeric-fit handling. G2 and the
accelerated prototype's received-stream gate remain open.