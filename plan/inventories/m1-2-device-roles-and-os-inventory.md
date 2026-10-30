# M1.2 device roles and installed OS inventory

Recorded: 2026-10-18 (America/New_York)  
Status: Complete on 2026-10-18; owner facts and explicit software unknowns recorded.  
Task: [M1.2](../subplans/m1-2-device-roles-and-os-inventory.md)  
Authority: [PRD Q7, Q8, Q15, and device clarifications](../../PRD.md#53-confirmed-scope-decisions); [remaining details](../../PRD.md#54-details-to-confirm-before-implementation)  
Decision: [D01](../MASTER_PLAN.md#7-open-decision-register), device/role portion only.

## Evidence and provenance

Subsequent owner clarification: the Pixel 8 Pro previously observed on Android
16 / CP1A.260505.005 is updating. Its post-update OS/build and availability for
testing remain pending; pause checks on that unit until completion is reported
and installed properties are re-read. Earlier measurements remain pre-update
evidence only. No destination version or update completion is assumed.

Update 2026-10-29: the owner confirmed **two** connected Pixel 8 Pro phones
belong to them and are available, with Android 16 tested first and Android 17
separately afterward. The [dated controller preflight](../evidence/m2/android-controller-preflight-2026-10-29.md)
retains observed OS/build/patch values and read-only network observations.
This supersedes the single-Pixel-8-Pro quantity below; the original October 18
table remains a historical snapshot, not a current installed-build inventory.
Other device quantities are unchanged; this adds one phone to that earlier
five-phone inventory. Availability and installed facts do not establish app
compatibility, stable-release enrollment or controller/uplink feasibility.

Session update 2026-10-24: the owner reports devices are available but are not
connected to this workstation. The request to complete M2 remains active.
No new installed build, physical capability or watch-companion observation
is inferred from this availability report.

Later on 2026-10-24, [ADB observations](../evidence/m2/phone-capabilities-2026-10-24.md)
verified authorized Pixel 8 Pro and Pixel 10 Pro XL connections, both reporting
Android 17/API 37. Build/security-patch values are retained in the linked report.
The connected Pro is an XL variant; other units and watch companions remain
unverified. This verifies installed model/build facts, not capture compatibility.

The owner confirmed the four target models and direct watch-to-streamer pairing
in the PRD on 2026-10-17. A review of project documents on 2026-10-18 found no
installed OS/build values, initial phone assignments, or current watch companion.
The owner answered on 2026-10-18:

- Phone roles are interchangeable; no phone has a specific assigned role.
- All devices are expected to have the latest published version or N−1.
- There are two Pixel 10 phones, two Pixel 10 Pro phones, and one Pixel 8 Pro.
- Each of the four Pixel 10-series phones has its own paired Pixel Watch 4.
- N−1 means the previous major stable OS version.
- Any device can be used for testing; the main test group is one Pixel 10 Pro,
  its Pixel Watch 4, and the Pixel 8 Pro. Phone roles remain interchangeable.

The watch count/pairings and N−1 definition were explicitly clarified in a
follow-up answer on the same date. A later owner clarification expanded the
quantities to five phones and four watches and selected the main test group.
This supersedes the earlier inventory's assumption of one phone per model.
These are owner-reported facts. No device was inspected or reconfigured for
this record.

Unknown means the value has not been supplied or observed. It does not mean a
device or feature is unavailable. Published launch software, available updates,
and model specifications are not evidence of installed software or app support.

## Device inventory

| Device | Installed OS version | Installed build number | Confirmed role policy | Source / observation date |
| --- | --- | --- | --- | --- |
| Pixel 8 Pro | Unknown | Unknown | Streamer or Android controller; interchangeable per session | Target model: PRD Q8, 2026-10-17; role policy: owner, 2026-10-18 |
| Pixel 10 #1 | Unknown | Unknown | Streamer or Android controller; interchangeable per session | Quantity and role policy: owner, 2026-10-18 |
| Pixel 10 #2 | Unknown | Unknown | Streamer or Android controller; interchangeable per session | Quantity and role policy: owner, 2026-10-18 |
| Pixel 10 Pro #1 | Unknown | Unknown | Streamer or Android controller; interchangeable per session | Quantity and role policy: owner, 2026-10-18 |
| Pixel 10 Pro #2 | Unknown | Unknown | Streamer or Android controller; interchangeable per session | Quantity and role policy: owner, 2026-10-18 |
| Pixel Watch 4 — paired with Pixel 10 #1 | Unknown (Wear OS version) | Unknown | Watch controller; current companion is Pixel 10 #1 | Separate watch and pairing: owner, 2026-10-18 |
| Pixel Watch 4 — paired with Pixel 10 #2 | Unknown (Wear OS version) | Unknown | Watch controller; current companion is Pixel 10 #2 | Separate watch and pairing: owner, 2026-10-18 |
| Pixel Watch 4 — paired with Pixel 10 Pro #1 | Unknown (Wear OS version) | Unknown | Watch controller; current companion is Pixel 10 Pro #1 | Separate watch and pairing: owner, 2026-10-18 |
| Pixel Watch 4 — paired with Pixel 10 Pro #2 | Unknown (Wear OS version) | Unknown | Watch controller; current companion is Pixel 10 Pro #2 | Separate watch and pairing: owner, 2026-10-18 |

The #1/#2 labels distinguish inventory entries for identical models; they are
documentation labels, not observed physical identifiers. Record which physical
unit and its paired watch are used in each test session.

There is no permanent primary or alternate phone assignment. Select and record
the actual streamer/controller combination for each session. The confirmed role
policy expresses intended use; every supported role/combination still requires
physical validation. It does not require an automatic transfer of an active
broadcast between phones.

## Main test group

The owner selected one of the two Pixel 10 Pro phones, its paired Pixel Watch 4,
and the Pixel 8 Pro for the main testing. Either Pro/watch pair may be selected;
no particular physical Pro unit or permanent phone role is assigned.
All five phones and four watches are available for additional testing.

When the selected Pro streams, its paired watch meets the Q7 companion
prerequisite and Pixel 8 Pro can be the Android controller. When Pixel 8 Pro
streams with watch controls, pair/transfer the selected watch directly to it
before testing. Record the actual roles and companion for each session.

## Expected software versus installed software

The owner expects all devices to use the latest published stable OS version or
the previous major stable OS version (N−1). The version unit is the major OS
version, not the preceding patch/build. This is an expectation, not an
observation of installed software. Actual OS versions and builds remain
explicitly unknown for all five phones and all four physical watches. No numeric
version, minimum SDK, upgrade, or compatibility guarantee is inferred. The four
watches have separate inventory rows; their installed versions are not assumed equal.

## Watch companion and pairing prerequisites

| Item | Current record |
| --- | --- |
| Required topology | Confirmed Q7: Pixel Watch 4 paired directly to the streaming phone, using Wear OS Data Layer |
| Reported current setups | Four separate Pixel Watch 4 devices: one paired with each of two Pixel 10 and two Pixel 10 Pro phones; Pixel 8 Pro has no current watch setup |
| Intended companion phone | The phone selected to stream for that session when watch controls are used |
| Pairing change needed | Conditional on that session's streamer and the selected watch's actual companion; verify before use |
| Pairing or transfer performed | None for this task |
| Actual app connectivity | Untested; retained for M2.1/M2.4 physical validation |

| Session streamer | Watch pairing prerequisite |
| --- | --- |
| Either Pixel 10 | Use that phone's currently paired Pixel Watch 4; no companion change is indicated |
| Either Pixel 10 Pro | Use that phone's currently paired Pixel Watch 4; no companion change is indicated |
| Pixel 8 Pro | If watch controls are used, pair/transfer a selected watch directly to Pixel 8 Pro before validation |

These configurations preserve interchangeable phone roles. The Pixel 8 Pro
remains a streaming/controller target; its current lack of a paired watch does
not remove it from scope. A watch paired to a phone acting only as the Android
controller cannot be assumed to control another streaming phone through that
companion. Q7 still requires direct pairing to the active streamer.

After both companion values are known, record one of these dispositions:

- Same phone: no companion change is indicated; app connectivity still needs validation.
- Different phone: transfer to the selected streamer is a prerequisite for the Q7 validation setup.
- Unpaired watch: pair to the selected streamer before that validation.
- Either value unknown: keep the pairing disposition unknown.

Google documents a transfer flow under the watch's System settings and the
companion Pixel Watch app. Consult the installed software and
[Google's transfer instructions](https://support.google.com/googlepixelwatch/answer/13579590?hl=en)
when a transfer is needed. This is a conditional prerequisite, not evidence
that a transfer has happened. A different companion phone does not authorize
adding a relay topology.

## Collecting the missing values

On each Pixel phone, open Settings, then About phone, then Android version;
copy the Android version and build number exactly as displayed.
[Google's Android version instructions](https://support.google.com/pixelphone/answer/7680439?hl=en)
describe these fields.

On Pixel Watch 4, open Settings, then System, then About, then Versions;
record the Wear OS version and build number shown on the watch.
The version of a phone's Wear OS app is a different value.
[Google's Pixel Watch version instructions](https://support.google.com/googlepixelwatch/answer/13044412?hl=en),
[Wear OS version guidance](https://support.google.com/wearos/answer/6321140?hl=en).

Record when each value was checked and whether it came from the owner or a
direct observation. Keep an unavailable value explicitly unknown. When the
software or assigned roles change, update this inventory before reusing it
for a physical validation result.

## Handoff and remaining evidence

M1.2 is complete for its documentation/observation scope: all nine physical
devices have confirmed intended roles and either actual or explicitly unknown
software/builds; all four current watch companions and conditional pairing changes
are recorded; Q7 and the separation from capability evidence are preserved.
Installed versions/builds remain unknown in PRD section 5.4 and must be collected
when a later check depends on the actual software.

[M1.4](../subplans/m1-4-venue-upload-baseline.md) must record the phone used for
each venue measurement; no permanent streamer assignment is needed.
[M2.1](../subplans/m2-1-physical-capability-inventory.md)
and [M2.4](../subplans/m2-4-remote-topology-feasibility.md) must retain the actual
device/build/companion configuration with their observations. Their other
entry conditions still apply.

All lens exposure, live camera switching, encoder capabilities, wired/Bluetooth
audio routes, capture lifecycle behavior, and controller connectivity remain
untested. No compatibility claim follows from this inventory. D01 also needs
the separate M1.3 accessory, M1.4 network, and M2.1 capability evidence.

## Documentation validation

Before the owner clarifications, the planning-package checker passed on
2026-10-18 against the then-unchanged PRD hash
`3a18442da93d7cad724813d048f75b3c02b1668b1cac7305bd2b9550d8a51263`.
The PRD is now intentionally revised to record the owner's 2026-10-18 answers.
Check the updated planning package with:

```powershell
node plan/tools/check-planning-package.cjs
```

The checker covers local links/anchors, task/index coverage, dependency
consistency, existing diagram artifacts, and recorded skill metadata. It does
not validate any device behavior or turn expected software into an observation.

After recording the owner answers on 2026-10-18, the checker returned **PASS**
with no errors. All 53 tasks, 159 dependency edges, eight existing diagrams,
and six installer-managed skill entries passed the mechanical checks.
A manual consistency review confirmed the nine device rows, the main Pro/watch
and Pixel 8 Pro test group, interchangeable phone roles, the major-version meaning
of N−1, separate watch companions, and
the continuing absence of physical capability evidence.
