# M4.4: Publish to one manually configured destination

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M4 — Capture, overlay, publishing, and adaptation](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-01; PRD 4.3 ([approved PRD](../../PRD.md))  
Blocked by: [M4.3](m4-3-encoded-scoreboard-composition.md), [M2.3](m2-3-provider-setup-feasibility.md), [M2.5](m2-5-lifecycle-and-storage-feasibility.md)  
Open decisions: [D03, D06, D08](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T02, T06](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Start and stop a real broadcast through manual URL/key setup without sign-in.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Deliver capture-readiness rejection first, then masked/replaceable URL/key entry and protected local credential storage.
2. Require deliberate start/stop confirmation, one active destination, and accurate preparing/connecting/streaming/stopped/error states.
3. Run a real manual-setup broadcast with audio and encoded overlay; expose actionable failures without leaking keys.

Q43's [saved-code recovery rule](../specifications/d10-abbreviation-validation-policy-review.md#saved-code-recovery-confirmed-q43) is also a stream-start readiness condition. Reject local/Android/watch start while restored nonconforming codes await phone/tablet correction, preserve the match and scoring, and never start merely because correction succeeded. Capture/provider readiness and deliberate start confirmation still apply.

## First behavioral slice

Proposed expected behavior at the listed public boundary: A confirmed start while capture is unready returns a readiness failure and does not publish.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] An unready phone cannot start, and a canceled confirmation causes no start/stop.
- [ ] A failed connection never appears live; sign-in is unnecessary for manual setup.
- [ ] One destination receives video, audio, and overlay; destination restrictions still apply.

## Evidence to retain

Retain T02/T06 red/green evidence and sanitized real-ingest/playback observations. Full-duration checks remain V01.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

