# M7.3: Provide watch mute and confirmed stream controls

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M7 — Wear OS controller](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-06; PRD 2.4 ([approved PRD](../../PRD.md))  
Blocked by: [M7.2](m7-2-watch-match-controls.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md)  
Open decisions: [D08, D09](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T04, T06](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Operate mute and deliberate stream start/stop from the simplified watch interface.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Deliver acknowledged mute/unmute and current capture/stream status.
2. Add deliberate start/stop confirmation with the same phone readiness and cancellation behavior.
3. Exercise simultaneous phone/Android/watch operation and visible disconnected/error states on the physical pairing.

Apply [Q43 and RC05](../specifications/d10-abbreviation-validation-policy-review.md#saved-code-recovery-confirmed-q43) to watch start: a restored nonconforming code requires phone/tablet correction first. The watch cannot repair codes or bypass this shared readiness check. Its otherwise permitted scoring remains available under the existing guards; successful code correction does not start a broadcast automatically.

## First behavioral slice

Proposed expected behavior at the listed public boundary: Canceling watch start confirmation leaves the stream stopped.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] Watch start/stop never bypasses confirmation or streaming-phone readiness.
- [ ] Mute state agrees across participating devices.
- [ ] Camera choice, credentials, account management, and controller authorization remain unavailable on watch.

## Evidence to retain

Retain T04/T06 results plus physical concurrent-controller observations for V07/V10.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

