# M8.5: V05: Validate both sign-in paths and manual setup

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M8 — Acceptance and personal release](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-01; PRD success criteria ([approved PRD](../../PRD.md))  
Blocked by: [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md), [M5.3](m5-3-youtube-authorization-lifecycle.md), [M5.4](m5-4-twitch-authorization-lifecycle.md), [M6.4](m6-4-android-reconnect-and-resynchronization.md), [M7.3](m7-3-watch-media-controls.md)  
Open decisions: [D06](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T03, T06](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Prove both required automatic setups and sign-in-free manual entry.

## Entry conditions

G3, with M3–M7 behavioral regression suites already passing. Required physical/provider access must be available.

Reproducible application failures return to one failing regression test at a confirmed boundary before the minimal fix. Keep a repeatable physical scenario for device-only findings.

## Steps

1. Complete actual YouTube setup and a test broadcast without manually finding/copying/entering URL/key.
2. Repeat independently for Twitch, then exercise usable manual entry without sign-in.
3. Observe the account/channel/broadcast preview and verify sign-in alone never publishes.

## Acceptance criteria

- [ ] Each sign-in integration independently completes automatic setup and a real broadcast.
- [ ] Manual setup remains available when login is skipped or unavailable.
- [ ] No integration is treated as optional release work or replaced by manual mode.

## Evidence to retain

Retain sanitized real-account/ingest observations and test identifiers without destination credentials.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

