# M6.3: Expose the complete Android remote control matrix

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M6 — Second Android device and concurrent controls](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-06; PRD 2.4 ([approved PRD](../../PRD.md))  
Blocked by: [M6.2](m6-2-ordered-idempotent-commands.md), [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M4.4](m4-4-manual-destination-and-publishing.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md)  
Open decisions: [D05, D11](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T04, T06](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Operate every approved phone/tablet remote control with visible acknowledgment.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Deliver score/serve/timeout/undo/set/reset/swap controls, then team/abbreviation/history editing and pre-match preset customization.
2. Add supported camera choice, mute, and deliberate start/stop using the phone's capture-readiness rules.
3. Render authoritative status, disabled/unavailable controls, and command failures while leaving credentials/admin local.
4. Apply [Q38/Q39 placement policy](../specifications/d10-placement-control-policy-review.md): an authorized synchronized Android controller may select any approved preset before/during a live stream. Update the active streaming phone's saved preference; preserve the controller phone's own streamer preference and all match values. Retain normal authorization/freshness handling.
   Under Q40, placement preserves the current match Undo opportunity, and Undo of the latest match edit retains the selected position. An operator changes placement back with the existing permitted control; no separate placement Undo feature is selected.

Apply [Q41/Q42 and AV01–AV11](../specifications/d10-abbreviation-validation-policy-review.md) to Android code editing too: show the prepared uppercase code, validate the allowed characters/length, warn and require explicit confirmation for matching codes, and leave accepted data/Undo intact on cancel or rejection. Revalidate obsolete drafts under the eventual reviewed-state contract; a matching code never replaces stable team identity.

Under [Q43 and RC01–RC08](../specifications/d10-abbreviation-validation-policy-review.md#saved-code-recovery-confirmed-q43), keep remote scoring/corrections usable for a restored match with nonconforming codes. Permit phone/tablet code repair, but deny stream start until the currently accepted codes are corrected and other readiness conditions pass. Canceling a repair preserves restored values and Undo.

## First behavioral slice

Proposed expected behavior at the listed public boundary: A remote confirmed start is rejected with the phone's readiness reason when capture is unavailable.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] Every allowed second-device action in PRD 2.4 is represented and acknowledged.
- [ ] Remote start cannot bypass phone permission/lifecycle readiness.
- [ ] Denied administration and credentials never appear in remote operations or snapshots.

## Evidence to retain

Retain user-visible T04/T06 matrix results and physical Android interaction observations for V10.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

