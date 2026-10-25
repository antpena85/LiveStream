# Connected phone observations, 2026-10-24

Recorded: 2026-10-24 (America/New_York)  
Task: M2.1, with local Android tooling evidence for M2.8.  
Status: Both phones ADB-authorized; OS facts and app-visible capability reports collected with camera/microphone permissions granted. Actual media/watch checks pending.

## Observed model and installed builds

[Allowlisted ADB report](device-inventory-2026-10-24.json) was read after the
owner enabled USB debugging and accepted the workstation prompt. No device
serials or account identifiers are retained. The initial empty ADB list occurred
before debugging authorization; it is superseded by this observed connection.

| Local observation label | Device-reported model | Android / API | Build ID | Security patch |
| --- | --- | --- | --- | --- |
| usb-device-1 | Pixel 8 Pro | 17 / 37 | CP31.260608.007 | 2026-06-05 |
| usb-device-2 | Pixel 10 Pro XL | 17 / 37 | CP3A.261005.005 | 2026-10-05 |

The owner called the second model group “10 Pro XL and 8 Pro XL”; the actual
ADB model strings above control this observation. The connected 10-series Pro
is an XL variant. Other physical units remain as previously owner-reported;
this does not reclassify or verify them. Published stable/beta channel status
is not inferred from these build strings.

## Measurement contract

The [exploratory inventory APK](../../../feasibility/android-inventory/README.md)
lists app-visible logical/physical camera metadata, advertised AVC/AAC encoders
and audio input types. It records permission state and keeps capture, encoding
and publishing explicitly `not_run`. The owner can grant permissions through
the phone's visible prompt. Reports are collected only from this debug package.

C01's phone model/build portion is observed. The owner reports a Pixel Watch 4
paired to the Pixel 10 Pro XL is available; its build and actual Data Layer
path remain unverified. C02/C03 advertisements are evaluated separately from actual
capture, concurrent stream combinations and 720p/30 encoding. C04 live lens
changes, C05 built-in captured/received audio, C06 phone/watch topology, upload
measurements and provider checks remain pending. Q48 defers external microphone
support/testing until after v1; it does not waive built-in audio.

## Collected capability observations

The [Pixel 8 Pro report](android-inventory-2026-10-24/pixel-8-pro.json) and
[Pixel 10 Pro XL report](android-inventory-2026-10-24/pixel-10-pro-xl.json)
both confirm camera/microphone permission grants and no inventory errors.
Each lists logical camera IDs `0` and `1`, both advertising 1280×720
SurfaceTexture output. Reported physical IDs are metadata, not a count of
independently selectable lenses or proof of a supported stream combination.

| Observation | Pixel 8 Pro | Pixel 10 Pro XL |
| --- | --- | --- |
| Logical camera 0 reported physical IDs | 2, 3, 4, 5, 6 | 2, 3, 4, 5, 6, 9 |
| Logical camera 1 reported physical IDs | 7, 8 | 7, 8 |
| Hardware AVC encoder advertisement | `c2.exynos.h264.encoder` | `c2.google.avc.encoder` |
| Hardware AVC advertises 720p/30 and surface input | Yes | Yes |
| AVC entries / AAC entries | 3 / 3 | 3 / 3 |
| Advertised audio input entries | 5 | 5 |
| Built-in input metadata | Type 15 entries advertise 48 kHz and channel counts 1–4 | Same |

No encoder was configured or started and no microphone route was activated.
All four capture/audio/encoding/publishing fields remain `not_run`. C02's
enumeration and C03's advertisement portions now have device observations;
actual lens selection, surface integration, encoded frames and received audio
remain required. The twelve retained JVM tests and eight red/green cycles are
unchanged; these are exploratory measurements, not extra behavioral test passes.

## Tooling and scope

[Build/deployment receipt](android-inventory-build-2026-10-24.json) records the
successful API 37 APK build and installation/opening on both phones. The APK
is 2,490,034 bytes, SHA-256
`dcaa62e105d12b99c7f3e14f43786c5631bd58272d5abaff30c7abe075d52e38`.
The successful build returned exit 0 using cached dependencies, AGP 9.2.0,
Gradle 9.4.1, JDK 17, SDK Platform 37.0 revision 2 and Build Tools 36.0.0.
Platform Tools 37.0.1 is installed in the project-local SDK. The debug signing
key is project-local and ignored; no release signing identity is selected.

After an owner-reported visibility/prompt problem, an ADB retry confirmed both
devices authorized. Relaunch requests were accepted on both; filtered diagnostics
found no matching Java crash in the inventory app. Report collection then
verified both permission grants and successful inventories. This establishes
that the measurement tool ran, without assuming every UI state was observed.

The Android probe is independent of the existing twelve-test JVM suite. Its
SDK/build/dependency/compiler failures are infrastructure/tool implementation
issues, not missing-application-behavior red results. This exploratory hardware
inventory does not add a TDD cycle or establish the full Android/watch test
harness, the application stack or G2 completion.

No hardware compatibility, successful encoding, routed audio, network topology
or broadcast result is inferred from ADB authorization or a successful APK build.
