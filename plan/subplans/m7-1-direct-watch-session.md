# M7.1: Connect an authorized watch directly to the streaming phone

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M7 — Wear OS controller](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-06; PRD 2.4; Q7 ([approved PRD](../../PRD.md))  
Blocked by: [M2.4](m2-4-remote-topology-feasibility.md), [M2.8](m2-8-tdd-tooling-and-boundaries.md), [M6.2](m6-2-ordered-idempotent-commands.md), [M6.4](m6-4-android-reconnect-and-resynchronization.md)  
Open decisions: [D09](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T04, T06](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Provide a fresh, sanitized session over Data Layer to the directly paired watch.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Use the confirmed direct companion topology and the D09 identity/signing prerequisites.
2. Apply application authorization and the common ordered command contract over the watch adapter.
3. Deliver stale/disconnected status and fresh-snapshot gating before permitting edits.

## First behavioral slice

Proposed expected behavior at the listed public boundary: A disconnected watch cannot submit an edit until it receives a fresh authoritative snapshot.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] The watch communicates with its directly paired streaming phone.
- [ ] No edit is accepted before authorization and a fresh session snapshot.
- [ ] No account credentials or controller-administration capability is exposed.

## Evidence to retain

Retain T04/T06 results and actual Pixel Watch 4 pairing/Data Layer observations.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

