# M2.1 capability evidence audit

Date: 2026-11-02 (local workstation date). This audit reconciles the
[C01–C06 contract](../../specifications/m2-1-capability-validation.md) with
retained observations; it does not change task acceptance or assert compatibility.

| Case | Evidence retained | Remaining gap |
| --- | --- | --- |
| C01 — builds and direct companion | [Phone inventory](phone-capabilities-2026-10-24.md); [same-APK watch deployment](watch-connectivity/deployment.json); [watch report](watch-connectivity/bidirectional-pixel-watch-4.json) records build and diagnostic acknowledgments. | Confirm current builds and intended pairing for the specific units selected for the streaming trial; diagnostic delivery is not a full remote session. |
| C02 — camera enumeration | Permission-granted [Pixel 8 Pro](android-inventory-2026-10-24/pixel-8-pro.json) and [Pixel 10 Pro XL](android-inventory-2026-10-24/pixel-10-pro-xl.json) metadata. | Map currently exposed options and stream combinations to the selected physical units/builds; advertisements are not independently usable-lens proof. |
| C03 — AVC/AAC and 720p surface path | [Local capture](media-owner-review-2026-10-24.md) and [transition metadata](media-transitions-2026-10-24.md) record 720p AVC/AAC on both phones. | Verify the required selected configuration and actual implementation on the current builds, and separately establish received and sustained behavior. Short samples do not establish sustained 30 fps. |
| C04 — exposed lens changes | [Local front/rear review](media-transitions-2026-10-24.md) and [post-update repeat](media-transitions-post-update-2026-10-30.md). | Individual rear-lens availability/selection and changes during publishing, with continuity measurements, remain unverified. |
| C05 — built-in audio and mute | [Local mute/playback review](media-transitions-2026-10-24.md) and [later Pro XL local review](local-score-controls-2026-11-01.md). | Actual active route and levels need evidence; received audio and mute during publishing remain unverified. External accessory cases are deferred under Q48. |
| C06 — watch and second phone while streaming | [Watch foreground diagnostic round trips and recovery](watch-connectivity/README.md); [Android controller preflight](android-controller-preflight-2026-10-29.md). | Same-session direct-watch and authenticated second-phone link, fresh snapshot and actual mobile uplink while publishing are not demonstrated. |

## Current read-only ADB check

ADB returned four authorized entries: Pixel 10 Pro XL on Android 17/build
`CP3A.261005.005`; Pixel Watch 4 reporting Android release 17/build
`CP3A.260905.002`; and two Pixel 8 Pro entries on Android 17, with builds
`DP11.261016.003` and `CP3A.261005.005`. No serials are retained. This is a
visibility/build observation only: it does not identify which earlier local
label corresponds to either Pixel 8 Pro, determine a release channel, or prove
the watch is currently paired to the Pro XL.

The existing inventory collector writes filenames based on model, so using it
unchanged with both Pixel 8 Pro entries could overwrite or conflate reports.
Before any fresh collection, select a distinct device identifier privately and
use a verified unique local label/output path; do not publish serials. No app was
installed, capture started, report collected or radio changed in this audit.

## Next useful work

1. Identify the intended Pixel 8 Pro unit by build with the owner before any
   targeted inventory or media run; do not infer its identity from its model.
2. Compare the selected current build's app-visible camera/encoder inventory to
   the historical reports using a collision-safe collection procedure.
3. Continue the separately authorized publisher safety work before C04–C06
   live-session checks. C06 additionally requires both controllers and a real
   mobile uplink in the same session. Check ADB authorization again before use.

M2.1 remains **In progress**. Its checked planning criteria do not override the
case-specific remaining evidence above or pass G2.

## Both Pixel 8 Pro units selected

The owner selected both builds for separate comparison. The inventory APK's
offline build succeeded with all tasks up to date. Initial read-only inspection
found the diagnostic package on the `DP11.261016.003` unit, but not on the
`CP3A.261005.005` unit; existing report reads could not be parsed and were not
used as current evidence. An attempt to install and open the diagnostic on both
units timed out without a conclusive result. A subsequent package-path check
still showed the package present on the DP11 unit and absent on the CP3A unit.
No fresh capability reports were collected and the timed-out operation's other
effects are unknown. Confirm screen/permission state before retrying, and keep
the two units' output separate. This is an operational blocker, not a failed
capability test or a behavioral TDD result.

## Separate current inventories collected

After the owner confirmed both phones were unlocked, bounded per-device retries
installed the diagnostic APK and accepted launch commands on each unique build.
The owner then reported completing **Read capabilities** and granting both
permissions on both phones. The separately collected reports identify the
matching model/build and show both permission flags true:

- [Pixel 8 Pro, CP3A.261005.005](android-inventory-2026-11-02/pixel-8-pro-cp3a.261005.005.json)
- [Pixel 8 Pro, DP11.261016.003](android-inventory-2026-11-02/pixel-8-pro-dp11.261016.003.json)

Both report two logical cameras, six AVC/AAC encoder entries and identical
camera, encoder and advertised-input arrays to each other. Camera IDs 0 and 1
both advertise 720p SurfaceTexture; camera 0 lists physical IDs 2–6 and camera 1
lists 7–8. Both include the advertised `c2.exynos.h264.encoder` AVC entry.
No inventory error field appeared. The reports each explicitly say camera
capture, audio capture, encoding and publishing were `not_run`. The arrays are
advertisements, not proof of independently selectable physical lenses, supported
concurrent combinations, actual active audio route or usable media output.

This updates the C02/C03 advertisement evidence for both current builds without
assigning either unit to a historical local label. M2.1 remains In progress:
live lens changes, active-route evidence and C06's simultaneous publisher and
controller scenario remain open.