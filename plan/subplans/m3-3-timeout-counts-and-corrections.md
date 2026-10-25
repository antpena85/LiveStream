# M3.3: Record and correct team timeouts

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M3 — Match state, persistence, and local controls](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-04; FR-05; PRD 2.3 ([approved PRD](../../PRD.md))  
Blocked by: [M3.1](m3-1-match-setup-and-presets.md)  
Open decisions: [D11](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T01](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Maintain each team's remaining timeout allowance for the current set.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Use the active match's copied timeout allowance, including customized values.
2. Deliver timeout use, correction, and invalid-boundary rejection one behavior at a time.
3. Specify resets as part of confirmed next-set creation, with no carryover or countdown feature.

## First behavioral slice

Proposed expected behavior at the listed public boundary: Using a timeout reduces that team's remaining count by one without changing the other team.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] Counts remain between zero and the configured allowance.
- [ ] A correction affects the intended stable team only.
- [ ] A confirmed next set restores the full allowance; final match completion retains its final state.

## Evidence to retain

Retain customized-allowance examples and T01 results; coordinate reset evidence with M3.4.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

