# M3.6: Correct completed sets consistently

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M3 — Match state, persistence, and local controls](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-05 ([approved PRD](../../PRD.md))  
Blocked by: [M3.4](m3-4-set-and-match-confirmation.md), [M1.5](m1-5-match-correction-specification.md)  
Open decisions: [D11](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T01, T06](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Let the operator correct history while keeping a possible match sequence.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Use the [M1.5 examples](../specifications/m1-5-match-correction-specification.md), [completed-match entry review](../specifications/d11-completed-start-policy-review.md), and [historical-correction review](../specifications/d11-history-reopen-policy-review.md) for changed set winners, totals, and subsequent sets. Apply Q17's rejection policy and Q30's conditional reopening; resolve revision and atomic validation/commit mechanisms before the affected behavior.
2. Deliver a correction that recomputes totals, then the confirmed [baseline/history review](../specifications/d11-baseline-history-policy-review.md) cases B01–B12. Q31 replaces a baseline with complete valid matching early history without double counting. Q32 permits reviewed starting-total corrections after later play only when the sequence remains valid; preview changed set numbers/targets and reject without losing later play.
3. When Q30's valid corrected history leaves neither team with enough wins, reopen the required next set at 0–0 with full timeouts and no active server until selected; otherwise preserve the completed phase. Never silently delete later sets.

## First behavioral slice

Proposed expected behavior at the listed public boundary: Correcting a completed set updates the related set totals according to the decided reconciliation policy.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] Completed scores and related totals agree after each accepted correction.
- [ ] Inconsistent later sets are handled by the decided policy, with clear rejection or deliberate resolution.
- [ ] Corrections preserve team identity and reopen a completed match only when Q30's valid corrected history removes both teams' required win threshold.

## Evidence to retain

Retain independent historical examples, proposed/approved policy references, and T01/T06 red/green evidence.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

