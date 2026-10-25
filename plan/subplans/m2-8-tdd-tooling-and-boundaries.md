# M2.8: Confirm test boundaries and prepare TDD tooling

Plan created: 2026-10-18  
Task status: In progress: T01–T06 confirmed, JVM harness executed and API 37 diagnostic APK built/opened on two phones; final Android/watch test harness and M2.7 handoff pending.  
Parent: [M2 — Technical feasibility and selection](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-01–FR-06 ([approved PRD](../../PRD.md))  
Blocked by: [M2.7](m2-7-technology-selection.md)  
Open decisions: [D13](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T01, T02, T03, T04, T05, T06](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Make the agreed test-first method executable with owner-confirmed public boundaries.

## Entry conditions

G1 and M1 before prototypes, scaffolding, application dependencies, or executable tests. Documentation research can proceed during planning.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. For T01–T06, document the public interface, behaviors caught, limits, fixtures, and substituted external inputs.
2. Obtain owner confirmation before the first test at each applicable boundary; preserve confirmation unless its scope changes.
3. After G1 and stack selection, establish real runners/device tooling and document actual commands; deliver the first behavior through one red-to-green slice.

## Acceptance criteria

- [x] D13 records selected local tools and actual T01–T06 boundary confirmations; final Android/watch tools remain pending.
- [x] An observed missing-behavior failure precedes its minimal implementation; harness errors are recorded separately.
- [x] Probe/harness work stays within the owner's newly authorized M2 scope; M3–M8 delivery is not begun.
- [x] M2.7's final stack handoff and the required Android/watch/real-store tooling are completed and evidenced.

## Evidence to retain

The [2026-10-24 phone/tooling record](../evidence/m2/phone-capabilities-2026-10-24.md)
retains the first successful API 37 diagnostic APK build and deployment. This
is exploratory hardware measurement, not a new behavioral red/green cycle or
completion of the final Android/watch instrumentation and stack handoff.

Retain boundary decisions and, after authorization, real harness/first-slice red and green results. Earlier feasibility behavior tests also need prior boundary confirmation.

The [2026-10-23 tooling record](../specifications/m2-8-tdd-tooling-and-boundaries.md)
retains the owner's explicit confirmation, selected local versions, actual
commands, [eight T01/T02 red/green cycles](../evidence/m2/README.md),
[first-run duplicate-delivery verification](../evidence/m2/st04-duplicate-commands.md)
and [ST05 cancellation/interrupted-reset verification](../evidence/m2/st05-reset-isolation.md).
[ST03 lost acknowledgment](../evidence/m2/st03-lost-acknowledgment.md) passes
on its first run on 2026-10-24 with unchanged store logic. The current twelve-test
suite covers ten SQLite and two session cases, with eight observed red/green
cycles and four first-run verifications.
The previous
documentation-only checklist restriction is superseded by the owner's M2
execution request. This is preparatory local evidence: M2.7's completion
dependency is not waived, final Android/watch/real-store tooling remains
pending, and M2.8 is not complete or a G2 pass.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

