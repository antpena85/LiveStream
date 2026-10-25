# M6.2: Apply concurrent commands once in accepted order

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M6 — Second Android device and concurrent controls](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-05; FR-06 ([approved PRD](../../PRD.md))  
Blocked by: [M6.1](m6-1-android-controller-pairing.md), [M3.7](m3-7-atomic-persistence-and-restart.md)  
Open decisions: [D04, D11](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T01, T04](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Keep one durable accepted match state despite duplicate or concurrent delivery.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. First deliver duplicate score-command handling through the authorized protocol into the real match/store path.
2. Add phone/mobile/watch ordering, command IDs, explicit acknowledgments, revision checks, match/session identity, and Q27 capability enforcement so a restricted latest action is not skipped by watch Undo.
3. Exercise lost acknowledgments, process recreation, receipt retention, stale/expired actions, and retry outcomes.

Include [Q45 and N14](../specifications/d10-numeric-fit-and-overflow-policy-review.md):
an accepted valid numeric edit may also require publishing to stop because
its full value cannot fit. Return the accepted match result separately from
stream state/reason; a lost response and retry must not count that edit twice.
Durable receipt and shutdown-recovery mechanisms remain open.

## First behavioral slice

Proposed expected behavior at the listed public boundary: Sending the same authorized score-command ID twice changes the score exactly once.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] One command ID cannot increment a score twice, including across restart.
- [ ] Acknowledgments reflect the durable accepted result and revision.
- [ ] Stale/expired/wrong-match actions have explicit outcomes rather than silent replay.

## Evidence to retain

Retain T01/T04 end-to-end results through a real temporary store and controlled external transport.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

