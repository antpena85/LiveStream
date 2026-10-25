# M3.8: Swap display sides without moving team data

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M3 — Match state, persistence, and local controls](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-04; FR-05 ([approved PRD](../../PRD.md))  
Blocked by: [M3.2](m3-2-rally-points-and-serve.md), [M3.3](m3-3-timeout-counts-and-corrections.md)  
Open decisions: No additional decision beyond the common gates.  
Public boundaries: [T01, T05](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Match the overlay's visual order to court sides while preserving ownership.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Keep display order separate from stable team identity and match configuration.
2. Deliver a swap with different scores, serve, and timeout counts on the two teams.
3. Verify subsequent commands target the team identity chosen from the displayed snapshot; leave automatic side-change reminders outside scope.

## First behavioral slice

Proposed expected behavior at the listed public boundary: Swapping sides changes visual order while preserving the serving team's identity and each team's counts.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] A swap changes positions only; points, sets, timeouts, and serve remain owned by the same team.
- [ ] Swapping twice restores the original order.
- [ ] No automatic scoring or display swap is introduced at the deciding-set threshold.

## Evidence to retain

Retain asymmetric worked examples and T01/T05 results showing values remain attached to identities.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

