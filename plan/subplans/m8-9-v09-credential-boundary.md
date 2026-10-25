# M8.9: V09: Validate credential and authorization boundaries

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M8 — Acceptance and personal release](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-01; PRD 4.3 ([approved PRD](../../PRD.md))  
Blocked by: [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md), [M5.3](m5-3-youtube-authorization-lifecycle.md), [M5.4](m5-4-twitch-authorization-lifecycle.md), [M6.4](m6-4-android-reconnect-and-resynchronization.md), [M7.3](m7-3-watch-media-controls.md)  
Open decisions: [D06](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T03, T04, T06](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Verify secrets remain protected and authorization changes take effect.

## Entry conditions

G3, with M3–M7 behavioral regression suites already passing. Required physical/provider access must be available.

Reproducible application failures return to one failing regression test at a confirmed boundary before the minimal fix. Keep a repeatable physical scenario for device-only findings.

## Steps

1. Review normal UI, errors, logs, local storage behavior, controller payloads, and diagnostic exports for credential handling.
2. Exercise cancellation, expiry, revocation, least scopes, account switching, and disconnect cleanup for each provider.
3. Verify masking and absence of destination/pairing secrets from controller results and retained evidence.

## Acceptance criteria

- [ ] Stream keys/tokens are absent from documentation, logs, remote payloads, and ordinary errors.
- [ ] Protected storage and disconnect deletion meet the selected design.
- [ ] Restricted account/controller operations are rejected at the phone boundary.

## Evidence to retain

Retain sanitized pass/fail observations and redacted diagnostic checks; never save the secrets being checked.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

