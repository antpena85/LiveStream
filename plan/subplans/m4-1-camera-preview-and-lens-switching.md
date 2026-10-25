# M4.1: Preview cameras and switch supported lenses

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M4 — Capture, overlay, publishing, and adaptation](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-02 ([approved PRD](../../PRD.md))  
Blocked by: [M2.2](m2-2-media-pipeline-feasibility.md), [M2.7](m2-7-technology-selection.md), [M2.8](m2-8-tdd-tooling-and-boundaries.md)  
Open decisions: [D03, D08](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T02, T06](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Let the operator choose available cameras and use supported switches with visible outcomes.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Expose detected app-usable cameras/lenses and identify unavailable options.
2. Deliver landscape preview, pre-stream selection, and supported live changes in separate slices.
3. Preserve match/overlay and audio configuration; measure physical interruptions rather than promising seamless changes.

## First behavioral slice

Proposed expected behavior at the listed public boundary: Selecting an unavailable lens returns a useful failure and preserves the active capture configuration.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] Only app-exposed supported options are offered as usable.
- [ ] Supported switches preserve score and intended audio configuration without a crash.
- [ ] Unsupported changes give an actionable result and retain a valid session state.

## Evidence to retain

Retain T02/T06 results plus physical lens matrix, switch observations, and device/build details for V04.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

