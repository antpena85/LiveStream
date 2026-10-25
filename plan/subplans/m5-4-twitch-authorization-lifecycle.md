# M5.4: Recover and remove Twitch authorization

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M5 — Destination sign-in and automatic setup](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-01; PRD 4.3 ([approved PRD](../../PRD.md))  
Blocked by: [M5.2](m5-2-twitch-automatic-setup.md)  
Open decisions: [D06](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T03, T06](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Handle Twitch token/account lifecycle according to its verified flow.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Deliver cancellation, missing scope, eligibility, expired/revoked access, and renewal or reauthorization according to D06.
2. Show useful recovery actions and preserve manual setup without bypassing account restrictions.
3. Deliver account switching and disconnect cleanup; avoid assuming YouTube refresh behavior applies to Twitch.

## First behavioral slice

Proposed expected behavior at the listed public boundary: Revoked Twitch authorization produces an actionable disconnected/reauthorization result without going live.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] Lifecycle results reflect the actual Twitch flow and expose no keys/tokens.
- [ ] Disconnect removes saved authorization and prevents its accidental reuse.
- [ ] Changing accounts updates the visible channel before any confirmed broadcast.

## Evidence to retain

Retain T03/T06 cases and real provider lifecycle evidence where available; external account restrictions remain explicit.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

