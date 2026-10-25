# M6.4: Resynchronize Android controls before reconnect edits

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M6 — Second Android device and concurrent controls](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-06 ([approved PRD](../../PRD.md))  
Blocked by: [M6.3](m6-3-android-controller-controls.md)  
Open decisions: [D05, D11](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T04, T06](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Make connection loss visible and resume only from fresh phone state.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Deliver disconnected/stale display and prohibit edits until a fresh authoritative snapshot is received.
2. Discard or explicitly reject disconnected/stale/expired score taps; never silently replay them.
3. Exercise concurrent local changes, revoked access, and lost acknowledgments during reconnect; observe local scoring/publishing continuity.

## First behavioral slice

Proposed expected behavior at the listed public boundary: A reconnected controller cannot edit until it receives a fresh authoritative match snapshot.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] Reconnect displays the current committed revision before enabling edits.
- [ ] Disconnected taps cannot produce surprise score changes later.
- [ ] Local scoring and publishing continue when a controller is lost.

## Evidence to retain

Retain controlled fault/reconnect results plus physical topology observations; measure response later under V07.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

