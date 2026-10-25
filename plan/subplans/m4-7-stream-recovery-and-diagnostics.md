# M4.7: Bound reconnection and expose useful diagnostics

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M4 — Capture, overlay, publishing, and adaptation](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-01; PRD 4.2; PRD 4.3 ([approved PRD](../../PRD.md))  
Blocked by: [M4.5](m4-5-capture-lifecycle-and-low-power-screen.md), [M4.6](m4-6-automatic-quality-and-handoff.md)  
Open decisions: [D07, D08](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T02, T06](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Recover only within the decided retry policy and show truthful, sanitized status.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Deliver reconnecting status, retry budgets, failure exhaustion, and explicit recovery controls.
2. Make confirmed stop cancel connecting/retry work; reject late callbacks and prevent unintended restart.
3. Present actionable errors and sanitized timing/health observations without destination or pairing secrets.

Include [Q45 and N07–N14](../specifications/d10-numeric-fit-and-overflow-policy-review.md)
in session-recovery design: a diagnosed numeric fit failure stops broadcasting
with a clear notice while retaining the full accepted match and off-air scoring.
Cancel automatic publishing retries; resolving fit still requires normal
confirmed start/readiness. Define durable acceptance/shutdown/recovery ordering
and truthful stopping/stopped/error results before implementation. Match edit
acceptance must remain distinct from stream shutdown status.

## First behavioral slice

Proposed expected behavior at the listed public boundary: Confirmed stop during reconnect cancels pending retries and a late success cannot restart publishing.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] A stopped session remains stopped despite pending retries or callbacks.
- [ ] Retry limits and reconnect states follow D07/D08.
- [ ] Match data and local scoring survive publishing errors; diagnostics contain no keys/tokens.

## Evidence to retain

Retain controlled-time T02/T06 results and outage/recovery observations; document exactly which failures need a new confirmed start.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

