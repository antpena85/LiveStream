# M6.1: Pair, authorize, and revoke an Android controller

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M6 — Second Android device and concurrent controls](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-06; PRD 2.4; PRD 4.3 ([approved PRD](../../PRD.md))  
Blocked by: [M2.4](m2-4-remote-topology-feasibility.md), [M2.8](m2-8-tdd-tooling-and-boundaries.md), [M3.7](m3-7-atomic-persistence-and-restart.md), [M4.4](m4-4-manual-destination-and-publishing.md)  
Open decisions: [D05](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T04, T06](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Give a deliberately paired Android device only its approved session capabilities.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Deliver deliberate pairing approval and an authorized initial snapshot using the selected transport.
2. Enforce the PRD control matrix at the phone boundary; keep credentials, account management, and controller administration local.
3. Deliver revocation and visible connection status without disclosing pairing secrets.

## First behavioral slice

Proposed expected behavior at the listed public boundary: An unpaired device's score command is rejected without changing the match.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] Unpaired/revoked clients cannot edit or issue media commands.
- [ ] Authorized controllers receive sanitized status and match snapshots.
- [ ] Pairing is not treated as unrestricted authorization.

## Evidence to retain

Retain T04/T06 allow/deny results and actual phone/tablet pairing observations.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

