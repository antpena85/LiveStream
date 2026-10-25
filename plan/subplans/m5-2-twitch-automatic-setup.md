# M5.2: Configure Twitch through optional sign-in

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M5 — Destination sign-in and automatic setup](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-01; PRD 4.3 ([approved PRD](../../PRD.md))  
Blocked by: [M2.3](m2-3-provider-setup-feasibility.md), [M2.8](m2-8-tdd-tooling-and-boundaries.md), [M4.4](m4-4-manual-destination-and-publishing.md)  
Open decisions: [D06](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T03, T06](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Prepare a Twitch destination through its own approved mobile-safe workflow.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Use the provider-specific public-client-safe authorization/scopes flow selected in D06.
2. Retrieve authorized stream configuration and choose ingest automatically; show the connected account/channel.
3. Return ready status without publishing and validate a real broadcast through deliberate start.

## First behavioral slice

Proposed expected behavior at the listed public boundary: Successful Twitch setup returns the selected account/channel and a ready configuration without starting a broadcast.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] No client secret is embedded and no manual URL/key retrieval is needed.
- [ ] Twitch decisions are verified independently of YouTube's flow.
- [ ] Manual setup remains usable when sign-in is skipped or unavailable.

## Evidence to retain

Retain T03/T06 results and sanitized actual authorization/ingest evidence for Twitch.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

