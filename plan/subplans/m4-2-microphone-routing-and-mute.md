# M4.2: Capture built-in audio and preserve intentional mute

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M4 — Capture, overlay, publishing, and adaptation](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-03 ([approved PRD](../../PRD.md))  
Blocked by: [M2.1](m2-1-physical-capability-inventory.md), [M2.2](m2-2-media-pipeline-feasibility.md), [M2.8](m2-8-tdd-tooling-and-boundaries.md)  
Open decisions: [D03, D08](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T02, T06](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Provide built-in audio capture, visible input/levels and deliberate mute for
version 1. Q48 defers external input selection/fallback and testing until after v1.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Expose built-in capture status, active input, levels and permission/input errors.
2. Deliver deliberate mute and preserve it through supported capture/lifecycle changes.
3. Validate built-in captured/received audio, mute and timing on selected phones.
   External USB/wired/receiver/Bluetooth support and testing follow v1 under Q48.

## First behavioral slice

Proposed expected behavior at the listed public boundary: denied microphone
permission prevents audio-ready capture and reports an actionable reason while
retaining the match and intentional mute setting.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] Active route and levels are visible before and during streaming.
- [ ] Supported built-in capture/lifecycle transitions preserve intentional mute and match state.
- [ ] Unavailable or denied built-in capture is reported clearly.

## Evidence to retain

Retain controlled T02/T06 cases and physical built-in/received-audio observations
per selected phone/build. Keep post-v1 accessory cases separate under Q48.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

