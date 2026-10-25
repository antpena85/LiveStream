# M2.1: Inventory exposed physical-device capabilities

Plan created: 2026-10-18  
Task status: In progress: Pixel 8 Pro and Pixel 10 Pro XL OS/build and permission-granted camera/codec/audio inventory observed on 2026-10-24; actual media/watch validation pending.  
Parent: [M2 — Technical feasibility and selection](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-02; FR-03; FR-06 ([approved PRD](../../PRD.md))  
Blocked by: [M1.2](m1-2-device-roles-and-os-inventory.md), [M1.3](m1-3-representative-audio-accessories.md)  
Open decisions: [D01, D03, D09](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T02, T04](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Create an evidence-backed capability matrix for the selected devices.

## Entry conditions

G1 and M1 before prototypes, scaffolding, application dependencies, or executable tests. Documentation research can proceed during planning.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Define each lens, encoder, audio-route, and companion-pairing check before executing it after G1.
2. Enumerate capabilities exposed to third-party apps; distinguish physical hardware from usable app routes. The M1.2 inventory offers five phones with interchangeable roles and four separate watches. Prioritize one Pixel 10 Pro, its paired Pixel Watch 4, and the Pixel 8 Pro for main testing; any unit may be tested. Validate each role before claiming it supported.
3. Record tested, unavailable, and untested results per actual OS/build, role, accessory, and watch pairing. Collect unknown installed versions/builds before checks that depend on them; an expected current/previous major stable OS version is not an observed build.

## Acceptance criteria

- [x] The matrix distinguishes lens exposure, encoder capability, and usable microphone input.
- [x] Watch pairing is verified against the intended streaming phone.
- [x] A failed or unrun probe cannot be reported as compatibility support.

## Evidence to retain

The later [connected-phone record](../evidence/m2/phone-capabilities-2026-10-24.md)
supersedes the earlier unavailable/unconnected session state. Both phones report
Android 17/API 37 and have opened the API 37 diagnostic APK. This supplies the
phone model/build portion of C01, without yet establishing camera/encoder paths,
built-in capture or watch connectivity. Permission-granted capability reports
are now collected on both. The owner reports the paired watch available;
its actual build/reachability remains unverified.

Retain device/build-specific observations and sanitized probe results. Exploratory measurements do not replace behavioral TDD.

The [2026-10-23 matrix and C01–C06](../specifications/m2-1-capability-validation.md)
record unavailable session equipment and untested capabilities separately.
Approved ADB enumeration found no connected device; the owner confirmed none
are available now. Direct companion/build/lens/audio observations remain
pending, so this task is not complete. G1 is authorized for M2 and T01–T06 are
confirmed; those decisions do not supply physical evidence.

Use the [M1.3 audio cases A00–A04](../inventories/m1-3-representative-audio-accessories.md).
Version 1 requires the built-in A00 baseline. Q48 defers A01–A04 external
microphone support/testing until after version 1; they no longer block M2.
The earlier unavailable-device report above is historical: devices are now
owner-reported available but not connected, with no new physical result.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

