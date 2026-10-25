# M5.3: Recover and remove YouTube authorization

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M5 — Destination sign-in and automatic setup](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-01; PRD 4.3 ([approved PRD](../../PRD.md))  
Blocked by: [M5.1](m5-1-youtube-automatic-setup.md)  
Open decisions: [D06](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T03, T06](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Handle YouTube authorization failures and account changes without exposing secrets.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Deliver canceled sign-in, eligibility/scopes, quota, expired/revoked authorization, and renewal failure as separate cases.
2. Expose a useful next step while retaining manual-entry access and the deliberate start boundary.
3. Deliver account switching and disconnect with removal of saved authorization and consistent local results.

## First behavioral slice

Proposed expected behavior at the listed public boundary: Disconnecting the connected YouTube account removes its saved authorization and returns disconnected status.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] Cancellation never starts publishing or leaves an unintended connected account.
- [ ] Expired/revoked/missing authorization produces an actionable result.
- [ ] Disconnect removes the selected authorization; account changes are shown before later start.

## Evidence to retain

Retain controlled provider-response evidence and real renewal/disconnect checks appropriate to the selected flow.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

