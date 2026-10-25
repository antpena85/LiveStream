# M5.1: Configure YouTube through optional sign-in

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M5 — Destination sign-in and automatic setup](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-01; PRD 4.3 ([approved PRD](../../PRD.md))  
Blocked by: [M2.3](m2-3-provider-setup-feasibility.md), [M2.8](m2-8-tdd-tooling-and-boundaries.md), [M4.4](m4-4-manual-destination-and-publishing.md)  
Open decisions: [D06](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T03, T06](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Prepare a YouTube destination without the operator manually finding or entering ingest credentials.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Use the decided official authorization/scopes flow and show the chosen account/channel.
2. Deliver broadcast selection/creation as required, stream binding, and automatic ingest configuration.
3. Return a ready configuration without publishing; use the shared confirmed-start flow for a real test broadcast.

## First behavioral slice

Proposed expected behavior at the listed public boundary: Successful YouTube setup returns the chosen account/broadcast and a ready configuration without going live.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] Successful setup requires no manual URL/key retrieval.
- [ ] The account/channel/broadcast is visible before confirmed go-live.
- [ ] Signing in or choosing a broadcast never automatically starts publishing.

## Evidence to retain

Retain T03/T06 results plus real authorization/setup/ingest observations; never retain credentials in the evidence.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

