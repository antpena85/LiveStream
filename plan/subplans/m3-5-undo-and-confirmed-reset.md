# M3.5: Undo an accepted edit and confirm match reset

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M3 — Match state, persistence, and local controls](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-05 ([approved PRD](../../PRD.md))  
Blocked by: [M3.2](m3-2-rally-points-and-serve.md), [M3.3](m3-3-timeout-counts-and-corrections.md), [M3.4](m3-4-set-and-match-confirmation.md)  
Open decisions: [D11](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T01, T06](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Recover from accidental edits and reset only with deliberate intent.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Apply the owner-confirmed one-step Undo depth, restart availability, scope, repeat behavior, and watch authority from [M1.5](../specifications/m1-5-match-correction-specification.md) and the [focused Undo/watch review](../specifications/d11-undo-and-watch-policy-review.md). U01–U12 are confirmed-policy written cases, not executable tests. Represent Undo as a new revision rather than rewinding revision history; technical revision/storage mechanisms remain open.
2. Restore the affected prior match state, including serve and timeouts; reject an undo against a stale revision.
   Apply Q36 from the confirmed [cap-continuation permissions review](../specifications/d11-cap-continuation-permissions-review.md): only permitted phones/tablets may Undo the latest continuation confirmation; the watch directs the operator to a phone and never skips it. CP04/CP06/CP07 distinguish that restricted Undo from Undo of a later allowed point, which preserves earlier continuation approval.
3. Add reset confirmation and a fresh match/session identity so old commands cannot affect the new match.
   Apply Q34/Q35 from the confirmed [new-set serve/reset review](../specifications/d11-new-set-serve-and-reset-policy-review.md): retain teams/names/codes, copied settings, and display order; clear scores/baseline/history/Undo/continuation; start set 1 at 0–0 with full timeouts and no server. Permit confirmed reset on the streaming phone and authorized Android controller; deny watch reset. S08–S14 cover cancellation, permissions, stale/duplicate reset, old commands, and restart. Fresh identity/epoch and revision handling remain technical proposals.

Later Q38 also requires reset to preserve the active streaming phone's saved
scoreboard placement. Q40 and PU01–PU08 in the
[placement/control review](../specifications/d10-placement-control-policy-review.md)
keep placement outside match Undo: preserve the existing opportunity and retain
the current position when Undo restores match values. A placement change does
not rearm consumed Undo or cross the reset boundary.

Q44/Q45 and [N03/N11](../specifications/d10-numeric-fit-and-overflow-policy-review.md)
add the numeric-fit exception: when a valid reset or Undo produces numbers
that cannot fit after adaptive layout, preserve its complete match result and
stop publishing with notice; scoring continues off-air. An accepted Undo is
consumed under Q26. This does not undo a prior stream stop or automatically
restart publishing; numeric capacity and durable stop mechanisms remain open.

## First behavioral slice

Proposed expected behavior at the listed public boundary: Undoing a sideout point restores the prior score and the prior serving team.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] Undo restores the intended previous values without undoing stream or account operations.
- [ ] Canceled reset preserves all match data.
- [ ] Confirmed reset prevents delayed commands from the prior match being accepted.
- [ ] Reset applies Q34's retained/cleared fields, requires confirmation on a permitted phone/tablet under Q35, and preserves the broadcast independently except for Q45's diagnosed numeric-fit stop.

## Evidence to retain

Retain before/after snapshots and red/green results for undo, stale undo, canceled reset, and old-session commands.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

