# M4.5: Handle capture lifecycle and low-power screen use

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M4 — Capture, overlay, publishing, and adaptation](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: PRD 4.2; FR-02; FR-03 ([approved PRD](../../PRD.md))  
Blocked by: [M4.4](m4-4-manual-destination-and-publishing.md), [M2.5](m2-5-lifecycle-and-storage-feasibility.md)  
Open decisions: [D08](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T02, T06](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Keep capture state truthful through supported OS interruptions and screen modes.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Deliver foreground readiness, allowed background/lock behavior, and visible low-power screen mode according to D08.
2. Handle permission loss, incoming calls, audio interruption, and thermal constraints through separate slices.
3. Recreate the process with retained match data and a fresh confirmed start requirement; reject stale capture callbacks.

## First behavioral slice

Proposed expected behavior at the listed public boundary: Recreating the process restores match data while leaving broadcasting stopped until a fresh confirmed start.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] The interface reports the actual allowed capture state and useful recovery action.
- [ ] Screen dimming/lock behavior is documented for tested OS versions.
- [ ] Process recreation never silently resumes publishing; match state survives.

## Evidence to retain

Retain T02/T06 results plus actual-device interruption, dimming, lock, call, and thermal observations.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

