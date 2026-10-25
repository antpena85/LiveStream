# M2.6: Set measurable adaptation and reconnection policy

Plan created: 2026-10-18  
Task status: In progress: measurable experiment hypotheses and AQ01–AQ08 prepared on 2026-10-23; measured operating policy pending.  
Parent: [M2 — Technical feasibility and selection](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-01; PRD 4.2 ([approved PRD](../../PRD.md))  
Blocked by: [M1.4](m1-4-venue-upload-baseline.md), [M2.2](m2-2-media-pipeline-feasibility.md)  
Open decisions: [D07, D08](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T02](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Turn observed upload/media behavior into explicit quality and recovery rules.

## Entry conditions

G1 and M1 before prototypes, scaffolding, application dependencies, or executable tests. Documentation research can proceed during planning.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Use venue observations and media evidence to propose bitrate bounds, observation windows, downgrade thresholds, and recovery hysteresis.
2. Define outage/reconnect budgets, cancellation, Wi-Fi/mobile handoff, and whether resolution/fps changes are supported.
3. Specify controlled-time/feedback cases and physical restricted-upload scenarios before choosing final parameters.

### M1.4 input disposition

The [M1.4 upload inventory](../inventories/m1-4-venue-upload-baseline.md)
completes its planning prerequisite with explicit N01 (Wi-Fi) and N02 (mobile)
measurement gaps. The owner selected mobile data as the default because venues
keep changing; Wi-Fi remains supported. Both connections are available under
PRD Q14; no upload
samples, variability observations, or handoff results have been supplied.
Use its session fields when measurements become available. Do not derive
numeric bitrate bounds or response thresholds from these unknowns. M2.2,
the entry gates, and actual measured inputs remain necessary for this task's
policy and validation evidence; 720p/30 fps remains the stable validation baseline.
Prioritize representative mobile-data sessions, keep separate Wi-Fi scenarios,
and define how to honor the default when Wi-Fi is also connected. Network
routing mechanisms and handoff/fallback behavior remain unresolved under D07;
the connection preference itself is confirmed.

Apply [Q44/Q45 and N12](../specifications/d10-numeric-fit-and-overflow-policy-review.md)
when selecting the D07/D10 output envelope: full numbers, reviewed readable
type, and supported safe bounds must fit at every selected output size.
Q45 requires a visible stop preserving the full match/off-air scoring after
a diagnosed numeric-fit failure. It does not choose a quality floor or retry
policy for insufficient bitrate; those decisions still need measured evidence.

## Acceptance criteria

- [x] D07 records measurable parameters with rationale and owner decisions where needed.
- [x] Automatic quality reduction preserves match/overlay state across both connection types.
- [x] No unsupported seamless handoff or resolution change is promised.

## Evidence to retain

Retain policy tables, network scenarios, measured inputs, and unresolved limits; 720p/30 fps remains the stable validation baseline.

The [quality/recovery proposal](../specifications/m2-6-adaptive-quality-policy.md)
provides numeric laboratory starting hypotheses, separated from measured or
approved operating values, plus AQ01–AQ08. No upload, handoff, queue response,
encoder compliance or readable floor has been measured. M2.2 and D07/D08 remain
pending; numeric experiment inputs do not close this task.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

