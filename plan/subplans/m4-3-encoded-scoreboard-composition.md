# M4.3: Encode the scoreboard into outgoing landscape video

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M4 — Capture, overlay, publishing, and adaptation](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-04 ([approved PRD](../../PRD.md))  
Blocked by: [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M4.1](m4-1-camera-preview-and-lens-switching.md), [M4.2](m4-2-microphone-routing-and-mute.md)  
Open decisions: [D03, D10](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T02, T05](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Make the required scoreboard part of the actual encoded video.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Use the shared committed projection for preview and compositor input.
2. Deliver encoded overlay updates, swaps, and field/layout bounds through separate behavioral slices.
3. Inspect encoded frames at 720p and the selected quality floor using the [M1.6 readability review](../specifications/m1-6-overlay-display-specification.md#readability-review-and-retained-evidence), including all placement presets. Then verify received output with M4.4.
   Include Q39's live placement changes from the streaming phone and authorized Android controller, using [PL02/PL05/PL10](../specifications/d10-placement-control-policy-review.md). Reposition the complete scoreboard without swapping team order or altering match values or stream lifecycle. Exact geometry and actual readability still require evidence.

Use [Q41/Q42 and AV01–AV11](../specifications/d10-abbreviation-validation-policy-review.md) for accepted-code rendering: include one- and five-character codes, wide allowed characters, and an explicitly confirmed matching-code pair with asymmetric values. Identical text must not merge the two team groups or their serving/timeouts/score data. Actual encoded fit and readability remain required.

Use the [numeric-fit review and N01–N14](../specifications/d10-numeric-fit-and-overflow-policy-review.md)
to plan large values, wide codes, all anchors, and custom timeout/set fields.
Q44/Q45 select adaptive numeric layout and a visible stop with the full new
match/off-air scoring preserved if fit still fails. Record actual font/layout
limits; resolve durable stop sequencing and the D07 readable output envelope.
Unexpected renderer failure needs its own recovery policy. Fit arithmetic
alone does not establish encoded or received readability.

## First behavioral slice

Proposed expected behavior at the listed public boundary: An accepted score change updates the scoreboard pixels in encoded output using the committed match values.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] Outgoing frames contain abbreviations, points, sets, timeouts remaining, and serve in landscape 16:9.
- [ ] Encoded values agree with preview after accepted edits and identity-preserving swaps.
- [ ] Readability is reviewed over representative court imagery under D10.

## Evidence to retain

Retain T05 results and encoded-frame/device observations; V02 must still inspect the real received broadcast.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

