# M2.5: Define lifecycle and crash-recovery feasibility

Plan created: 2026-10-18  
Task status: In progress: host SQLite reopen/retry, before/post-commit interruption, shared-store concurrency and reset-generation isolation passed through 2026-10-24; full storage and physical OS evidence pending.  
Parent: [M2 — Technical feasibility and selection](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: PRD 4.2; PRD 4.3; FR-05 ([approved PRD](../../PRD.md))  
Blocked by: [M2.1](m2-1-physical-capability-inventory.md)  
Open decisions: [D04, D08](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T01, T02](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Define permitted capture and durable-state recovery on the selected OS versions.

## Entry conditions

G1 and M1 before prototypes, scaffolding, application dependencies, or executable tests. Documentation research can proceed during planning.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Verify current official permission/foreground-capture rules and plan screen dimming, lock, calls, and audio-loss scenarios.
2. After G1, observe backgrounding, permission interruption, process recreation, and late callbacks on actual devices.
3. Compare transactional or atomic persistence approaches for match revisions plus command receipts, including failure before acknowledgment.

## Acceptance criteria

- [x] Supported lifecycle transitions and visible recovery steps are documented by OS/build.
- [x] A process restart preserves the last committed match and never silently restarts broadcasting.
- [x] Storage decisions require atomic state/receipt behavior rather than an assumed library guarantee.

## Evidence to retain

Retain lifecycle scenarios, physical results, and persistence-failure observations; proposed behavior remains D04/D08 until decided.

The [lifecycle/storage comparison and ST01–ST07](../specifications/m2-5-lifecycle-and-storage-feasibility.md)
make atomic state/Undo/receipt requirements and Q45's external stop ordering
explicit. A local T01 SQLite probe covers reopen/retry and a separate child
JVM abruptly halted before commit; [ST02 evidence](../evidence/m2/st02-interrupted-write.md)
shows preserved accepted state and one successful retry. The later
[ST04 host check](../evidence/m2/st04-concurrent-commands.md) accepts eight
concurrent distinct commands with separate durable revisions through one
instance. [Simultaneous duplicate verification](../evidence/m2/st04-duplicate-commands.md)
passes with the existing implementation: eight calls and a retry after reopening
apply one point in total. The [ST05 host experiment](../evidence/m2/st05-reset-isolation.md)
adds durable reset epochs, old-command rejection after reopen and epoch-scoped
receipts through three red/green slices. Cancellation and before-commit reset
interruption also pass with the existing store logic. On 2026-10-24,
[ST03 post-commit interruption](../evidence/m2/st03-lost-acknowledgment.md)
also passes on its first run: a lost acknowledgment can be retried without
applying another point or replacing newer state. Twelve tests now pass,
with eight observed red/green cycles and four first-run verifications.
The fixture does not select application identity/retention or support upgrades
from prior probe schemas. Independent writers remain untested.
No Android lifecycle or power-loss probe ran. These host cases do not establish full match/Undo
recovery or Android storage; D04/D08 and
the physical prerequisites remain unresolved.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

