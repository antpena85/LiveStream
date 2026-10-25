# M2.1 physical capability validation

Recorded: 2026-10-23 (America/New_York)  
Status: Two phones have observed OS/build and permission-granted capability reports; actual media/watch results pending.  
Task: [M2.1](../subplans/m2-1-physical-capability-inventory.md)

## Evidence available

Latest observation on 2026-10-24: [two authorized phones and Android build receipt](../evidence/m2/phone-capabilities-2026-10-24.md)
supersede the earlier unavailable/unconnected session state below. A Pixel 8
Pro and Pixel 10 Pro XL both report Android 17/API 37. The API 37 capability
APK installed and ran on both. Permission-granted reports now retain logical/
physical camera metadata, AVC/AAC advertisements and audio input types. No
actual camera/audio capture or watch-connectivity result is claimed yet.

Update 2026-10-24: the owner now reports devices available but not connected
to this workstation. The earlier session matrix below is historical; no new
installed build, capability or companion result is established by availability.
PRD Q48 limits version 1 to built-in audio and defers A01–A04 accessory support
and testing until after version 1. A00 and the camera/watch checks remain required.

The owner reports no physical devices available for this session. The installed
ADB server was started with approved access and returned an empty device list.
This means no connected test device was observed; it does not mean the listed
phones or watches are unsupported. Installed device OS/builds remain unknown.
The [owner inventory](../inventories/m1-2-device-roles-and-os-inventory.md) and
[audio cases](../inventories/m1-3-representative-audio-accessories.md) remain
the inventory authority. No accessories were newly reported available.

| Target | Session availability | Build / lenses / encoders / audio | Companion/app path |
| --- | --- | --- | --- |
| Pixel 8 Pro | Not available | Unknown / untested | No current watch setup reported; direct pairing needed if selected as streamer. |
| Pixel 10 #1 and #2 | Not available; two distinct units | Unknown / untested for each | Each has its own owner-reported watch; app connectivity untested. |
| Pixel 10 Pro #1 and #2 | Not available; two distinct units | Unknown / untested for each | Each has its own owner-reported watch; app connectivity untested. |
| Four Pixel Watch 4 units | Not available | Wear OS/build unknown for each | Owner-reported pairs retained; no pairing or reachability verified. |

## Observation procedure

Use one Pro, its watch, and Pixel 8 Pro first. Identify physical units with local
labels; never publish device serials, account IDs, keys, or complete ingest URLs.
Record date, role, installed OS/build, app build/signing label, expected result,
observed result, and sanitized evidence for each case below. Change roles in a
separate run; a result for one phone does not establish another's support.

| ID | Check | Evidence needed before a support claim |
| --- | --- | --- |
| C01 | Record Android/Wear versions and current direct companion | Device observations for every selected unit; matching application identity/signature. |
| C02 | Enumerate logical/physical camera IDs, facing, focal lengths and stream combinations | App-exposed options and permission outcomes; not the manufacturer's lens count. |
| C03 | Enumerate AVC/AAC encoders and verify the candidate 1280×720/30 surface path | Actual encoded output, supported configuration, and hardware/software implementation. |
| C04 | Select each exposed lens before capture and during publishing | Measured interruption, retained overlay/audio/match, unavailable choices identified. |
| C05 | Version 1: capture A00 built-in audio and exercise mute | Actual active input, audible encoded signal, levels and intentional mute. A01–A04 accessory support/testing is post-v1 under Q48. |
| C06 | Confirm direct-watch Data Layer and second-phone link while the Pro streams | Same session, correct phone companion, authorization and fresh snapshot, actual mobile uplink. |

Android distinguishes logical/physical camera exposure and supported stream
combinations; model names alone do not establish access.
[Multi-camera documentation](https://developer.android.com/media/camera/camera2/multi-camera).
For audio, a preferred input differs from the routed recording device; query
the active route during capture and listen to encoded audio.
[AudioRecord reference](https://developer.android.com/reference/android/media/AudioRecord#getRoutedDevice()).

C01's phone model/build portion is now observed; watch pairing/build remains
pending. C02/C03 advertisements have been collected by the diagnostic APK;
actual capture/encoding and C04–C06 remain **unrun**. No substitute measurements, emulator compatibility
claims, or assumed build numbers are supplied. M2.1 remains In progress; D01,
D03, and D09 still require these observations.
