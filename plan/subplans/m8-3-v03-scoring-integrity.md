# M8.3: V03: Validate complete scoring scenarios

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M8 — Acceptance and personal release](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-05; PRD 2.3 ([approved PRD](../../PRD.md))  
Blocked by: [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md), [M5.3](m5-3-youtube-authorization-lifecycle.md), [M5.4](m5-4-twitch-authorization-lifecycle.md), [M6.4](m6-4-android-reconnect-and-resynchronization.md), [M7.3](m7-3-watch-media-controls.md)  
Open decisions: [D11](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T01, T06](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Validate required scoring behavior across whole match histories.

## Entry conditions

G3, with M3–M7 behavioral regression suites already passing. Required physical/provider access must be available.

Reproducible application failures return to one failing regression test at a confirmed boundary before the minimal fix. Keep a repeatable physical scenario for device-only findings.

## Steps

1. Exercise best-of-5/best-of-3, deuce, deciding sets, customized margin/cap/targets/timeouts, and starting values.
2. Exercise sideout/serve override, point subtraction versus undo, confirmations, final points, reset, and swaps.
3. Apply completed-set corrections using the decided reconciliation policy and compare independent expected totals/phases.

## Acceptance criteria

- [ ] All approved scoring and correction scenarios yield consistent valid state.
- [ ] Final completion retains final points and no extra set.
- [ ] Reproducible defects receive a failing regression test before their fix.

## Evidence to retain

Retain worked expected states and system observations; existing behavioral tests remain part of the evidence.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

