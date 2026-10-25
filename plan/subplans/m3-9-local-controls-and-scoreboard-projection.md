# M3.9: Expose local scoring controls and shared scoreboard values

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M3 — Match state, persistence, and local controls](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-04; FR-05; PRD 2.4 ([approved PRD](../../PRD.md))  
Blocked by: [M3.5](m3-5-undo-and-confirmed-reset.md), [M3.6](m3-6-historical-set-corrections.md), [M3.7](m3-7-atomic-persistence-and-restart.md), [M3.8](m3-8-identity-preserving-side-swaps.md), [M1.6](m1-6-overlay-display-specification.md)  
Open decisions: [D10](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T05, T06](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Make all approved streaming-phone match controls usable with one committed view of the match.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Expose setup, point/serve/timeout edits, confirmation, undo, reset, history correction, and side swap through the agreed local interactions.
2. Project abbreviations, points, compact sets-won count, timeouts remaining, serve, and display order from committed state.
3. Apply the [M1.6 specification](../specifications/m1-6-overlay-display-specification.md): owner-confirmed 1–5-character validation, numeric `T/O`, and configurable placement presets. Resolve remaining D10 rules and verify preview/controller projections consume equivalent values.
   Apply [Q37–Q40, PL01–PL10, and PU01–PU08](../specifications/d10-placement-control-policy-review.md): top-left without a saved preference, per-streaming-phone retention across matches/restarts/reset, and local/authorized Android placement changes before/during a stream. Keep match values/team order intact. Placement is outside match Undo: preserve its current opportunity and the selected position during match restoration.

For code editing, apply [Q41/Q42 and AV01–AV11](../specifications/d10-abbreviation-validation-policy-review.md): review trimmed uppercase A–Z/digit codes before saving; validate 1–5 characters; allow matches only after warning and explicit confirmation. Canceled/rejected drafts keep committed codes and Undo; an accepted code change is one match edit. Render matching codes with separate stable team values and preserve the current placement during Undo.

Review [NF1/NF2 and N01–N14](../specifications/d10-numeric-fit-and-overflow-policy-review.md)
for confirmed Q44/Q45: adaptive complete numbers; if a valid numeric result
still cannot fit, preserve the new match and stop publishing with notice while
scoring remains available off-air. An accepted numeric Undo consumes its
opportunity under Q26 and keeps placement under Q40. Numeric capacity and
durable acceptance/shutdown/recovery mechanisms remain open.

## First behavioral slice

Proposed expected behavior at the listed public boundary: An accepted local point edit appears in the shared scoreboard projection with the committed score and serve.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] All streaming-phone match controls in PRD 2.4 are represented.
- [ ] Displayed values agree after edits, corrections, undo, restart, and swaps.
- [ ] The projection is suitable for landscape 16:9 output and is readable under the D10 review criteria.

## Evidence to retain

Retain user-visible T05/T06 behavior evidence and local review observations; encoded/broadcast appearance remains M4.3/V02.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

