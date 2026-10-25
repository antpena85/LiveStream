# M3.2: Score rallies and correct points or serve

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M3 — Match state, persistence, and local controls](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-05; PRD 2.3 ([approved PRD](../../PRD.md))  
Blocked by: [M3.1](m3-1-match-setup-and-presets.md)  
Open decisions: [D11](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T01](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Apply rally scoring and deliberate corrections without losing team identity.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Deliver a receiving-team point and automatic serve transfer first, then a serving-team point with unchanged serve.
2. Add manual serve override and point removal through separate behavioral slices.
   Apply Q33 from the confirmed [new-set serve/reset review](../specifications/d11-new-set-serve-and-reset-policy-review.md): leave a fresh set's server unset and reject ordinary rally input until explicit selection. S01–S07 are written examples; Q26/Q27 permit Undo of the latest selection, restoring the unset state and its rally block.
3. Reject negative points; keep point-removal semantics distinct from restoring prior state through undo.
4. Apply confirmed Q24: pause ordinary rally scoring at a potential winner until confirmation or correction, while keeping score corrections available. A cap/margin conflict is not itself a potential winner; Q25 requires explicit "continue win by two" confirmation before scoring beyond the cap for the current set only.

## First behavioral slice

Proposed expected behavior at the listed public boundary: A rally point for the receiving team increases its score by one and transfers serve to it.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] A receiving-team rally increments that team and transfers serve; a serving-team rally retains serve.
- [ ] Point corrections follow the decided D11 semantics and never infer prior history.
- [ ] Commands refer to stable team IDs, including after later display swaps.
- [ ] Each fresh set requires explicit server selection before ordinary rally input; restoring an ongoing set preserves its recorded server.

## Evidence to retain

Retain known-score examples and T01 red/green results for both serve cases and invalid correction.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

