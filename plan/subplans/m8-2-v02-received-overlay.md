# M8.2: V02: Validate the received scoreboard

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M8 — Acceptance and personal release](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-04; PRD success criteria ([approved PRD](../../PRD.md))  
Blocked by: [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md), [M5.3](m5-3-youtube-authorization-lifecycle.md), [M5.4](m5-4-twitch-authorization-lifecycle.md), [M6.4](m6-4-android-reconnect-and-resynchronization.md), [M7.3](m7-3-watch-media-controls.md)  
Open decisions: [D10](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T05](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Show that viewers receive every required scoreboard update correctly.

## Entry conditions

G3, with M3–M7 behavioral regression suites already passing. Required physical/provider access must be available.

Reproducible application failures return to one failing regression test at a confirmed boundary before the minimal fix. Keep a repeatable physical scenario for device-only findings.

## Steps

1. Create 20 independent scripted name/score/set/timeout changes for an indoor preset and repeat relevant coverage for a customized format.
2. Observe received destination video and compare with expected committed values and local preview.
3. Review abbreviations, numeric `T/O`, serve, compact sets, display swaps, landscape, configurable placement presets, and readability at 720p and the decided quality floor using the [M1.6 criteria](../specifications/m1-6-overlay-display-specification.md#readability-review-and-retained-evidence).

## Acceptance criteria

- [ ] All 20 scripted changes appear correctly in received video.
- [ ] Customized rules and display swaps preserve team identity and values.
- [ ] Readability passes the decided D10 criteria; preview alone is insufficient evidence.

## Evidence to retain

Include the accepted-code outcomes from [Q41/Q42 and AV01–AV11](../specifications/d10-abbreviation-validation-policy-review.md): verify the prepared uppercase code reaches received video, invalid/canceled drafts leave prior text on air, and confirmed matching codes keep both teams' asymmetric values and serve ownership separate. Cover short and five-character allowed codes; do not treat the static illustration as this evidence.

Retain script, expected/observed results, received-frame observations, and sanitized configuration details.

Include the applicable [N01–N14 numeric-fit cases](../specifications/d10-numeric-fit-and-overflow-policy-review.md)
under Q44/Q45 after technical limits are decided: full multi-digit points, compact
set totals, numeric custom `T/O`, wide codes, five placements, and selected
quality modes. Validate the diagnosed numeric-fit stop notice and retained
full match/off-air scoring without assuming a final received frame can display
the unfit value. No static illustration or controller acknowledgment
substitutes for received frames in readable configurations.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

