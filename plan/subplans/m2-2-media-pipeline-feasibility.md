# M2.2: Prove the proposed media path on devices

Plan created: 2026-10-18  
Task status: In progress: local AVC/AAC capture and owner-reviewed scoreboard, front/rear switching and mute/unmute observed on both phones on 2026-10-24; received output, measured synchronization, bitrate changes and sustained-load evidence pending.  
Parent: [M2 — Technical feasibility and selection](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-01–FR-04; PRD 4.2 ([approved PRD](../../PRD.md))  
Blocked by: [M2.1](m2-1-physical-capability-inventory.md)  
Open decisions: [D03, D07, D08](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T02, T05](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Determine whether a coherent camera/audio/compositor/encoder/publisher path can meet the PRD.

## Entry conditions

G1 and M1 before prototypes, scaffolding, application dependencies, or executable tests. Documentation research can proceed during planning.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Compare candidate paths using the approved scoring fields and a documented feasibility scenario.
2. After G1, measure encoded overlay output, audio/video timing, supported live lens/route changes, and live bitrate control.
3. Observe sustained load and interruptions on the intended phone; document unsupported transitions and integration constraints.

## Acceptance criteria

- [ ] Evidence shows scoreboard pixels in encoded output and synchronized usable audio.
- [ ] Live input changes and quality requests have measured outcomes, including interruptions.
- [x] License, integration, thermal, and hardware limitations are explicit inputs to G2.

## Evidence to retain

Retain candidate comparison and actual device/media observations. Confirm a public boundary before testing repeatable prototype behavior.

The [candidate comparison and MP01–MP07](../specifications/m2-2-media-pipeline-feasibility.md)
compare integrated RootEncoder, CameraX-based integration, and a lower-level
pipeline. Required encoded/received media, route changes and sustained load
remain incomplete. [Baseline capture evidence](../evidence/m2/media-owner-review-2026-10-24.md)
and [transition metadata and owner playback review](../evidence/m2/media-transitions-2026-10-24.md)
establish short local encoded captures, correct scoreboard, mute/unmute and
front/rear switching on both phones. Maximum video sample gaps of approximately
335–360 ms prevent a seamless-switching claim; exact synchronization, rear-lens
selection, publishing, live bitrate changes and sustained-load results remain
pending. No application media dependency is selected and no preview-only or JVM
result satisfies this task. M2.1's remaining physical prerequisites stay open.

Record the [M1.3 audio case ID](../inventories/m1-3-representative-audio-accessories.md)
with media observations. Begin with the available built-in baseline; accessory
path feasibility remains pending for cases whose equipment is unavailable.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

