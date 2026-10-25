# M1.3 microphone validation inventory

Recorded: 2026-10-18 (America/New_York)  
Status: Complete for M1.3 planning; version 1 built-in audio, external support/testing deferred under Q48.  
Task: [M1.3](../subplans/m1-3-representative-audio-accessories.md)  
Authority: [PRD FR-03](../../PRD.md#fr-03-use-built-in-and-accessory-microphones)  
Open evidence: [D01](../MASTER_PLAN.md#7-open-decision-register)

## Owner facts and scope

Update 2026-10-24: PRD Q48 confirms built-in audio for version 1 and defers
external wired/Bluetooth microphone support and testing until after version 1.
A00 remains required for M2/v1; A01–A04 are a retained post-v1 backlog and do
not block G2/G4 for version 1. This is a scope change, not a passed audio test.
The original availability report and planning matrix below are retained as
history; their accessory stage assignments now apply after version 1.

Asked which microphones and adapters could be made available, the owner
answered on 2026-10-18: **“built in only for now.”**

- Only the phones' built-in microphones are available for current validation.
- No external microphone, adapter, or receiver has been identified as available
  for these checks. This records present availability, not permanent ownership.
- USB, other wired, receiver, and direct Bluetooth cases remain pending.
  Their model names, microphone types, connectors, cables, adapters, and receiver
  outputs remain unspecified until an actual accessory is available.
- The original wired/Bluetooth request has no brand preference; Q48 now
  explicitly defers its delivery and testing until after version 1.

The available phones and interchangeable role policy are recorded in the
[M1.2 inventory](m1-2-device-roles-and-os-inventory.md). No hardware was inspected,
paired, connected, or tested for this record.

## Representative validation matrix

These cases select the input categories and connection chains to cover, without
selecting a brand or claiming a route is supported. The task plan explicitly
allows unavailable combinations to remain pending.

| Case | Representative input and phone-side connection | Current availability | App capture/routing evidence | Assigned physical checks |
| --- | --- | --- | --- | --- |
| A00 — Built-in baseline | Selected phone's built-in microphone; no external cable, adapter, or receiver. Exact physical phone/build recorded per session. | Available, owner-reported | Untested | M2.1, M2.2, V04 |
| A01 — USB | USB microphone or microphone-equipped USB headset → required cable/adapter → phone. Exact connector, microphone type, and any adapter remain TBD. | Pending; no available accessory identified | Untested; no input route observed | M2.1, M2.2, V04 when equipment is available |
| A02 — Other wired | Analog wired microphone or microphone-equipped headset → matching input-capable adapter/interface if needed → phone. Plug, wiring, power needs, and complete adapter chain remain TBD. | Pending; no available accessory or adapter identified | Untested; no input route observed | M2.1, M2.2, V04 when equipment is available |
| A03 — Wireless receiver kit | Microphone/transmitter → wireless receiver → receiver's wired/USB output and any adapter → phone. Transmitter, receiver, output connector, and adapter remain TBD. | Pending; no available kit identified | Untested; no input route observed | M2.1, M2.2, V04 when equipment is available |
| A04 — Direct Bluetooth input | Microphone-equipped Bluetooth headset or microphone → direct Bluetooth connection to phone. Model and exposed input route remain TBD. | Pending; no available accessory identified | Untested; pairing/playback would not establish captured input | M2.1, M2.2, V04 when equipment is available |

A03 records a receiver kit separately from A04, whose phone connection is
Bluetooth. If a future receiver has multiple phone-side outputs, record each
actual chain separately rather than treating one result as proof for all outputs.

Availability and support are separate: **pending** means equipment is unavailable
for current checks; **untested** means no application capture result exists.
Neither means the product has failed a compatibility check. No row is supported,
passed, or waived by this inventory.

## Physical validation handoff

Use one Pixel 10 Pro and the Pixel 8 Pro as the main phone group, as recorded
in M1.2; identify the actual unit and streaming role in each session. Any of
the available phones may be checked. An observed result applies only to the
recorded phone, build, accessory chain, and media configuration.

| Stage | Work assigned to A00–A04 | Evidence to retain |
| --- | --- | --- |
| [M2.1 — Capability inventory](../subplans/m2-1-physical-capability-inventory.md) | Record the actual device/OS/build and available input sources. For an available accessory, record its model/type, full connector/adapter/receiver chain, exposed input route, and any access restriction. Keep unavailable cases pending. | Case ID, physical phone, installed build, accessory details, observed route, and available/untested/failed outcomes. |
| [M2.2 — Media feasibility](../subplans/m2-2-media-pipeline-feasibility.md) | Establish A00 capture first. For each available accessory case, verify actual captured audio through the proposed encoded media path, active input/level indication, mute, and supported switching. Exercise disconnect/fallback with intentional mute preserved and match information retained. | Case/configuration, observed audio and synchronization, switch/disconnect behavior, mute state, interruptions, and limitations. |
| [V04 / M8.4 — Physical continuity](../subplans/m8-4-v04-lens-and-audio-continuity.md) | Repeat supported route changes during a real broadcast and inspect received audio/video. Preserve separate USB, other wired, receiver, and Bluetooth records. Use A00 as baseline and permitted fallback; do not apply accessory disconnect steps to A00 itself. | Received-broadcast evidence, active route/levels, mute-preserving fallback, retained match data, crash outcome, and unsupported or unrun combinations. |

Android's official [AudioDeviceInfo reference](https://developer.android.com/reference/android/media/AudioDeviceInfo)
distinguishes built-in, USB, wired-headset, and Bluetooth device types and
provides input/source identification. Device type or connection alone is not
the physical capture evidence required by the PRD. This reference informs
future observations; it does not select an implementation API or minimum SDK.

No physical checks ran in M1.3. G1 and the M2 entry conditions still apply
before probes or application work; V04 requires its later integration gate.
Future repeatable application behaviors follow the existing TDD agreement at
confirmed public boundaries.

## Completion and remaining gaps

M1.3's planning deliverable is complete: the owner supplied current availability,
the matrix separates all required input categories and marks unavailable cases
pending, and each case has an assigned physical-validation stage. The original
task acceptance criteria allow pending equipment; completion records that
disposition rather than asserting that accessories have been obtained or tested.

M2.1's M1.3 documentation prerequisite is satisfied. Its other entry conditions
and the equipment needed for each actual check still apply. Built-in availability
does not establish tested built-in audio or close D01/G2/G4. Under Q48,
wired/Bluetooth validation follows version 1 and does not block those gates.

When equipment becomes available, update the relevant row with its actual model
or description, microphone type, connector, adapter/receiver, and availability
date before selecting the physical test combination. Until then, accessory
compatibility and received-broadcast evidence remain explicitly pending.
