# M4.6: Adapt quality on Wi-Fi and mobile data

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M4 — Capture, overlay, publishing, and adaptation](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-01 ([approved PRD](../../PRD.md))  
Blocked by: [M4.4](m4-4-manual-destination-and-publishing.md), [M2.6](m2-6-adaptive-quality-policy.md)  
Open decisions: [D07](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T02](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Automatically lower quality as upload conditions deteriorate using the decided policy.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Deliver one controlled-time/feedback downgrade behavior first, then floor limits and hysteresis-controlled recovery.
2. Apply only demonstrated bitrate/resolution/fps changes and the decided Wi-Fi/mobile handoff behavior.
3. Run restricted-upload checks on both connection types and observe outgoing overlay/readability and retained match values.
4. Honor the owner-confirmed mobile-data default using the D07 routing design;
   retain Wi-Fi support and verify the actual broadcast connection. The
   [M1.4 inventory](../inventories/m1-4-venue-upload-baseline.md) records the
   changing-venue context and measurement gaps.

## First behavioral slice

Proposed expected behavior at the listed public boundary: Deteriorating upload feedback sustained for the decided observation window requests the next permitted lower quality.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] Quality requests follow D07's actual bounds/timing rather than invented thresholds.
- [ ] Both Wi-Fi and mobile degradation trigger the required reduction.
- [ ] Mobile data is the default broadcast connection, with Wi-Fi still supported and the actual route verified under the decided D07 behavior.
- [ ] Match/overlay state survives reduction, recovery, and the documented handoff path.

## Evidence to retain

Retain T02 controlled-time results and actual network/media observations, including any continuity limits.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

