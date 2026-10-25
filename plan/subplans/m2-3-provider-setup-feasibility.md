# M2.3: Verify both provider integration paths

Plan created: 2026-10-18  
Task status: In progress: research updated on 2026-10-23; registrations owner-reported, configuration/authorization evidence and D06/D12 dispositions pending.  
Parent: [M2 — Technical feasibility and selection](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-01; PRD 4.3 ([approved PRD](../../PRD.md))  
Blocked by: [M1.1](m1-1-planning-package-review.md)  
Open decisions: [D06, D12](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T03](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Establish a viable automatic-setup path independently for YouTube and Twitch.

## Entry conditions

G1 and M1 before prototypes, scaffolding, application dependencies, or executable tests. Documentation research can proceed during planning.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Verify current official requirements for registrations, account eligibility, scopes, authorization/renewal, and ingest configuration.
2. Trace YouTube broadcast selection/creation/binding and Twitch authorized stream-key/ingest setup, including a public-client-safe flow.
3. Record quotas, review/deployment dependencies, manual-entry behavior, and real-account checks to execute after G1.

## Acceptance criteria

- [x] Each provider has its own documented authorization-to-ready workflow and external prerequisites.
- [x] No embedded client secret or automatic go-live on sign-in is proposed.
- [x] Manual setup remains available but does not satisfy either required sign-in integration.
- [ ] Actual client/channel readiness and required provider feasibility evidence resolve D06/D12's remaining blockers before task completion.

## Evidence to retain

Retain dated official references and sanitized provider observations; store no tokens, keys, or account credentials in documentation.

The [2026-10-20 documentation findings](../specifications/m2-3-provider-setup-feasibility.md)
retain separate provider workflows, official references, scope/registration and
quota dependencies, token recovery proposals, Twitch documentation discrepancies,
manual setup, and twelve pending validation scenarios. The three documentation
criteria above have evidence. No real provider registration, authorization,
resource creation, ingest, or broadcast was performed.

Overall M2.3 remains In progress: the master plan also requires viable provider
feasibility evidence and D06/D12 dispositions. These are still pending, including
Twitch Public-client suitability/refresh observations and actual Google
project/channel readiness. M2.7's dependency is not satisfied by this research.
The original documentation task did not open G1 or confirm T03/T06. The
[2026-10-23 execution update](../specifications/m2-3-provider-setup-feasibility.md#execution-update-2026-10-23)
records later M2 authorization and confirmation of T01–T06. The owner reports
registrations exist but answered "unknown" to their nonsecret settings. No
provider console/grant/ingest was inspected or exercised; P01–P12 remain unrun.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

