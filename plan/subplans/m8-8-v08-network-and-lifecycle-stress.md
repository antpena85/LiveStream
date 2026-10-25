# M8.8: V08: Validate network and lifecycle stress

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M8 — Acceptance and personal release](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-01; PRD 4.2 ([approved PRD](../../PRD.md))  
Blocked by: [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md), [M5.3](m5-3-youtube-authorization-lifecycle.md), [M5.4](m5-4-twitch-authorization-lifecycle.md), [M6.4](m6-4-android-reconnect-and-resynchronization.md), [M7.3](m7-3-watch-media-controls.md)  
Open decisions: [D07, D08](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T02](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Validate the decided quality/recovery policy under actual restricted conditions.

## Entry conditions

G3, with M3–M7 behavioral regression suites already passing. Required physical/provider access must be available.

Reproducible application failures return to one failing regression test at a confirmed boundary before the minimal fix. Keep a repeatable physical scenario for device-only findings.

## Steps

1. Restrict upload independently on Wi-Fi and mobile data and observe automatic reduction and recovery.
2. Exercise documented handoff, outage/retry exhaustion, stop during reconnect, and late callbacks.
3. Exercise lock/backgrounding, permission loss, calls/audio interruption, process recreation, and thermal limits on selected OS versions.

## Acceptance criteria

- [ ] Reduction preserves match and scoreboard state on both connection types.
- [ ] Measured behavior meets the decided D07/D08 bounds and exposes useful recovery actions.
- [ ] No canceled or recreated session restarts broadcasting without required intent.

## Evidence to retain

Retain controlled conditions, actual media/device observations, policy comparisons, and explicitly unresolved failures.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

