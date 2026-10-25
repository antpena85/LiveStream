# M3.1: Create a match from an indoor preset

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M3 — Match state, persistence, and local controls](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-04; FR-05; PRD 2.3 ([approved PRD](../../PRD.md))  
Blocked by: [M2.7](m2-7-technology-selection.md), [M2.8](m2-8-tdd-tooling-and-boundaries.md), [M1.5](m1-5-match-correction-specification.md)  
Open decisions: [D04, D11](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T01, T06](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Let the operator start a valid match with stable teams and a copied configuration.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Specify best-of-5/best-of-3 examples and valid/invalid customized targets, margin, cap, and timeout allowance. Resolve the [cap-input policy](../specifications/d11-cap-input-policy-review.md) before accepting tied-at-cap or above-cap starting values.
2. Create stable team identities, full names/abbreviations, and a per-match configuration. Apply Q23's ongoing starting-sets baseline without fabricated earlier scores or win order; apply Q28's completed entry only from complete ordered valid history. Use Q31/Q32 and B01–B12 in the confirmed [baseline/history review](../specifications/d11-baseline-history-policy-review.md) for single counting and valid reviewed starting-total corrections; resolve storage/revision mechanisms before dependent implementation.
3. Expose setup validation through public commands/results and the local setup interaction in small TDD slices.
   Apply [Q41–Q43, AV01–AV11, and RC01–RC08](../specifications/d10-abbreviation-validation-policy-review.md): validate 1–5 uppercase A–Z/digit codes after reviewed trimming/uppercase conversion; allow matching codes only after warning and explicit confirmation. Keep identities/full names separate. Restore nonconforming saved codes for phone/tablet correction without losing match data or disabling otherwise permitted scoring; require correction before broadcast start.

## First behavioral slice

Proposed expected behavior at the listed public boundary: Creating a match copies the chosen preset so a later preset change leaves that match unchanged.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] Editing a preset later cannot change an active match's copied configuration.
- [ ] Invalid/negative starting data is rejected with a useful result.
- [ ] Teams and starting set totals remain consistent under the decided D11 policy.

## Evidence to retain

Retain T01/T06 red/green evidence and independent setup examples; persistence/restart evidence is completed in M3.7.

The [D11 follow-up](../specifications/m1-5-match-correction-specification.md#owner-decisions-recorded-on-2026-10-20)
confirms margin precedence at a cap conflict, current-set continuation after
explicit confirmation, and totals-only baseline entry.
Q29 separately confirms initial/restored cap-input validation; Q31/Q32 confirm
matching-history replacement and valid reviewed baseline corrections. Storage
and atomic validation/commit mechanisms remain open. This task remains Not started.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

