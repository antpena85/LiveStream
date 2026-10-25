# M8.10: V10: Validate every allowed and restricted control

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M8 — Acceptance and personal release](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-06; PRD 2.4 ([approved PRD](../../PRD.md))  
Blocked by: [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md), [M5.3](m5-3-youtube-authorization-lifecycle.md), [M5.4](m5-4-twitch-authorization-lifecycle.md), [M6.4](m6-4-android-reconnect-and-resynchronization.md), [M7.3](m7-3-watch-media-controls.md)  
Open decisions: [D05, D09](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T04, T06](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Demonstrate the complete approved control allocation on physical devices.

## Entry conditions

G3, with M3–M7 behavioral regression suites already passing. Required physical/provider access must be available.

Reproducible application failures return to one failing regression test at a confirmed boundary before the minimal fix. Keep a repeatable physical scenario for device-only findings.

## Steps

1. Turn every PRD 2.4 row into allowed/denied observations for the streamer, Android remote, and directly paired Pixel Watch 4.
2. Exercise confirmations, readiness failures, concurrent actions, disconnected/stale views, and revocation.
3. Check watch view-only fields, deferred camera choice, local-only credentials/controller administration, and Q27's allowed cross-device Undo versus its required phone-only denial for a restricted latest edit.
   Include Q36 and CP01–CP10 from the confirmed [cap-continuation permissions review](../specifications/d11-cap-continuation-permissions-review.md): permitted phones/tablets confirm continuation and Undo its latest confirmation; the watch shows the conflict and directs both actions to a phone without skipping it. Validate a later allowed point's Undo separately.
4. Include [Q37–Q40, PL01–PL10, and PU01–PU08](../specifications/d10-placement-control-policy-review.md): allow streaming-phone/authorized Android placement changes before/during a stream; deny direct watch requests as well as omitting the watch control. Check the no-preference top-left default, saved-preference precedence, restart/reset retention, and per-phone preferences when roles change. Placement preserves existing match Undo without replacing/consuming/rearming it; match Undo keeps the current placement and follows Q27 authority for the latest match edit.

Include [Q41/Q42 and AV01–AV11](../specifications/d10-abbreviation-validation-policy-review.md) in the code-edit matrix: phones/tablets validate the prepared uppercase code and deliberately confirm a duplicate warning; the watch stays view-only for codes and cannot Undo a restricted latest code edit. Canceling/rejecting creates no match edit; accepting a changed code creates one Undo-eligible edit and preserves stable identities.

Add [Q43 and RC01–RC08](../specifications/d10-abbreviation-validation-policy-review.md#saved-code-recovery-confirmed-q43): invalid saved codes retain match/scoring state; local/Android/watch start all fail until phone/tablet correction. Exercise cancel, duplicate-code repair confirmation, and pre-start Undo of a repair restoring the invalid-code restriction. Code repair or restart never auto-publishes; ordinary readiness and start confirmation still apply.

## Acceptance criteria

- [ ] Every allowed action works on its approved device and every restricted action is denied.
- [ ] Remote start/stop and reset use the approved deliberate interactions.
- [ ] All three device roles participate consistently in one session.

## Evidence to retain

Retain a completed control matrix with device/build/pairing context and actual outcomes.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

