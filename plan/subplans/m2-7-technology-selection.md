# M2.7: Record evidence-backed stack and distribution choices

Plan created: 2026-10-18  
Task status: In progress: stack comparison and D01–D13 dispositions prepared on 2026-10-23; final application selection blocked by missing feasibility evidence.  
Parent: [M2 — Technical feasibility and selection](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-01–FR-06; PRD 4 ([approved PRD](../../PRD.md))  
Blocked by: [M2.2](m2-2-media-pipeline-feasibility.md), [M2.3](m2-3-provider-setup-feasibility.md), [M2.4](m2-4-remote-topology-feasibility.md), [M2.5](m2-5-lifecycle-and-storage-feasibility.md), [M2.6](m2-6-adaptive-quality-policy.md)  
Open decisions: [D01, D02, D03, D04, D05, D06, D07, D08, D09, D10, D11, D12](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: No application test boundary for this documentation/observation task.

## Outcome

Select a manageable stack using actual constraints and feasibility evidence.

## Entry conditions

G1 and M1 before prototypes, scaffolding, application dependencies, or executable tests. Documentation research can proceed during planning.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Compare language/UI/API level, media pipeline, persistence, transport, and watch UI candidates.
2. Review maintenance effort, licensing, device evidence, direct surface integration, provider constraints, and signing/distribution needs.
3. Record each selected option, alternatives, rationale, remaining risk, and owner approval or unresolved status.
4. Consolidate D01–D12 from the M1 specifications and M2 evidence, including overlay, correction/protocol, quality, and lifecycle policies. Hand D13 to M2.8; unresolved required choices keep G2 open.

## Acceptance criteria

- [x] Choices are explicit decisions rather than implications of installed skills.
- [x] Phone/watch signing and provider deployment configuration have a viable path.
- [x] Unresolved required feasibility issues keep G2 open.
- [x] Every D01–D12 entry has a linked decision/evidence record or an explicit blocker; D13 remains assigned to M2.8 before G2 passes.

## Evidence to retain

Retain consolidated records cross-referenced to D01–D12 and the M2.8/D13
handoff; do not invent application setup commands before tools exist.

The [stack/disposition ledger](../specifications/m2-7-stack-and-decision-dispositions.md)
links every aggregate decision to evidence or a specific blocker, distinguishes
selected local JVM tools from proposed Android technologies, and preserves all
M2.2–M2.6 completion dependencies. No final media/UI/storage/distribution stack
or G2 passage is inferred from the working isolated runner.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

