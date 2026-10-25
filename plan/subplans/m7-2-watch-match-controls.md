# M7.2: Provide the approved watch scoring controls

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M7 — Wear OS controller](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-06; PRD 2.4 ([approved PRD](../../PRD.md))  
Blocked by: [M7.1](m7-1-direct-watch-session.md), [M3.9](m3-9-local-controls-and-scoreboard-projection.md)  
Open decisions: [D09, D11](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T04, T06](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Let the scorekeeper make the approved quick match edits from the watch.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Deliver readable scores/status, point addition/removal, Undo, serve toggle, and timeout correction in separate slices. Under [Q27's confirmed rule](../specifications/d11-undo-and-watch-policy-review.md#owner-decisions-recorded-on-2026-10-21), watch Undo may reverse the latest phone/watch point, serve, timeout, display-swap, or set-confirmation edit. It must direct names/history and other restricted latest edits to a phone without skipping them. Never infer direct names/history/preset editing permission from Undo.
2. Add deliberate set confirmation and display swap through the same revision-aware phone commands.
3. Keep names/history/presets view-only and exclude watch camera selection from the initial interface.
   Apply Q33/Q35 from the confirmed [new-set serve/reset review](../specifications/d11-new-set-serve-and-reset-policy-review.md): permit explicit first-server selection through the approved serve control, keep ordinary rally input blocked while unset, and deny watch reset. Q26/Q27 Undo permissions cannot bypass the reset restriction.
4. Apply Q36 from the confirmed [cap-continuation permissions review](../specifications/d11-cap-continuation-permissions-review.md): show the paused conflict and direct live continuation confirmation and Undo of that latest confirmation to a phone; deny both on the watch without skipping the restricted edit. CP01–CP10 preserve ordinary allowed scoring/Undo after continuation and existing setup/history/reset restrictions.

Q39 denies watch placement changes before/during a live stream. Omit that
control and reject direct watch placement requests at the streaming phone;
see the [placement/control review](../specifications/d10-placement-control-policy-review.md).
Q40 keeps placement outside match Undo. An intervening phone/tablet placement
change preserves watch Undo of a permitted latest match edit; it does not grant
Undo of a restricted edit. Keep the current placement when Undo restores match
values; never skip a restricted match edit. PU01–PU08 cover these interactions.

## First behavioral slice

Proposed expected behavior at the listed public boundary: A watch point edit updates the intended stable team and displays the phone's acknowledged result.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] All allowed watch match actions work with explicit results.
- [ ] Stale confirmations/edits are rejected consistently with phone/mobile semantics.
- [ ] Physical touch targets, readability, and interaction effort are reviewed on Pixel Watch 4.

## Evidence to retain

Retain T04/T06 behavior evidence and watch observations; no emulator-only ergonomics claim.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

