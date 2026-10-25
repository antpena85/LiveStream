# M8.6: V06: Validate command delivery and durable recovery

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M8 — Acceptance and personal release](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-05; FR-06 ([approved PRD](../../PRD.md))  
Blocked by: [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md), [M5.3](m5-3-youtube-authorization-lifecycle.md), [M5.4](m5-4-twitch-authorization-lifecycle.md), [M6.4](m6-4-android-reconnect-and-resynchronization.md), [M7.3](m7-3-watch-media-controls.md)  
Open decisions: [D04, D11](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T01, T04](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Prove accepted edits remain consistent under delivery and process failures.

## Entry conditions

G3, with M3–M7 behavioral regression suites already passing. Required physical/provider access must be available.

Reproducible application failures return to one failing regression test at a confirmed boundary before the minimal fix. Keep a repeatable physical scenario for device-only findings.

## Steps

1. Inject lost acknowledgments, duplicate/concurrent commands, stale revisions, expiry, and old-match actions.
2. Reconnect and revoke controllers, then recreate the phone process around durable commits.
3. Compare acknowledged state with reopened public snapshots and observe independent local scoring/publishing during controller loss.

## Acceptance criteria

- [ ] Each accepted edit is applied once and restored durably.
- [ ] Stale/revoked/expired commands have explicit results and cannot replay silently.
- [ ] Controller loss does not stop local scoring/publishing.

## Evidence to retain

Retain protocol/snapshot observations and real-store failure results without asserting private storage structure.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

