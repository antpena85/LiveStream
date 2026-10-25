# M3.4: Confirm set results and complete the match

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M3 — Match state, persistence, and local controls](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-05; PRD 2.3 ([approved PRD](../../PRD.md))  
Blocked by: [M3.2](m3-2-rally-points-and-serve.md), [M3.3](m3-3-timeout-counts-and-corrections.md)  
Open decisions: [D11](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T01, T06](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Advance or finish a match only after a deliberate current-state confirmation.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Specify regular/deciding targets, deuce, custom margin/cap, and best-of-3/best-of-5 examples.
2. Detect a potential winner only when the applicable target and configured margin are met; a cap does not waive that margin (Q22). Pause ordinary rally input until confirmation or correction (Q24), keep corrections available, bind confirmation to the reviewed result, and reject stale confirmation. At a cap conflict, require confirmed current-set continuation before scoring beyond the cap (Q25); later sets retain their configured cap. Resolve initial/restored cap-input validation using the [cap-input policy](../specifications/d11-cap-input-policy-review.md).
3. On nonfinal confirmation, retain completed scores and reset next-set points/timeouts; on final confirmation, retain final points and end without an empty extra set.
   Apply Q33 from the confirmed [new-set serve/reset review](../specifications/d11-new-set-serve-and-reset-policy-review.md): leave the next set's server unset and require explicit selection before ordinary rally input. S01–S07 cover progression, Undo, and Q30 reopening; completed matches create no extra set or first-server prompt.
4. Apply Q36 from the confirmed [cap-continuation permissions review](../specifications/d11-cap-continuation-permissions-review.md): permit deliberate live continuation confirmation on the streaming phone and authorized Android controller; deny watch confirmation and Undo of that latest confirmation without skipping it. CP01–CP10 distinguish that control from Q29 setup acknowledgement; preserve current-set scope and the configured margin.

## First behavioral slice

Proposed expected behavior at the listed public boundary: Reaching a winning score exposes a candidate result but leaves the current set active until confirmed.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] Reaching a potential winning score alone never advances the set or ends the match.
- [ ] Set totals/history and the deciding-set target follow the copied configuration.
- [ ] A stale confirmation cannot finalize a changed score.

## Evidence to retain

Retain independent deuce/deciding/final-set examples and T01/T06 results, including explicit confirmation interactions.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

