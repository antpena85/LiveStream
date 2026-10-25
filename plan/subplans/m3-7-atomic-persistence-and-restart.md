# M3.7: Persist accepted edits and restore after restart

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M3 — Match state, persistence, and local controls](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-05; FR-06 ([approved PRD](../../PRD.md))  
Blocked by: [M3.2](m3-2-rally-points-and-serve.md), [M3.3](m3-3-timeout-counts-and-corrections.md), [M3.4](m3-4-set-and-match-confirmation.md), [M3.5](m3-5-undo-and-confirmed-reset.md), [M3.6](m3-6-historical-set-corrections.md)  
Open decisions: [D04, D11](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T01](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Restore the latest durable match and prevent retries from applying it twice.

## Entry conditions

G1 and G2, plus the listed task dependencies and resolved decisions needed for the behavior. Confirm each public boundary before its first test.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Exercise public match operations against a real temporary store selected in D04.
2. Commit match data, revision, Q26 Undo data, and command receipt atomically before acknowledging an accepted edit; a successful Undo consumes the opportunity across restart without weakening duplicate protection.
3. Reopen after process recreation and inject commit/acknowledgment failures; apply the decided retention/expiry and match-identity rules.

Apply [Q43 and RC01–RC08](../specifications/d10-abbreviation-validation-policy-review.md#saved-code-recovery-confirmed-q43) when recovered codes fail current entry rules: preserve original codes, match data, and durable Undo; keep permitted scoring/corrections available; report the phone/tablet correction required before broadcast start. No silent truncation, replacement, or automatic publishing resume. Schema detection and atomic recovery mechanisms remain technical work.

## First behavioral slice

Proposed expected behavior at the listed public boundary: After accepting a match edit, reopening the session against the real store returns that committed edit.
Agree the exact boundary and expected result before writing its first executable test.

## Acceptance criteria

- [ ] A reopened session returns the latest committed match, including history and undo information.
- [ ] A retried command acknowledged before restart is not applied again.
- [ ] Interrupted commits expose a consistent state and receipt; no acceptance is acknowledged before durability.

## Evidence to retain

Retain real-store behavioral results and crash/failure scenarios through T01; do not assert storage layout or private calls.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

