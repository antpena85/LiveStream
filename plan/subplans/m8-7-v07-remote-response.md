# M8.7: V07: Measure mobile and watch response

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M8 — Acceptance and personal release](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-06; PRD success criteria ([approved PRD](../../PRD.md))  
Blocked by: [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md), [M5.3](m5-3-youtube-authorization-lifecycle.md), [M5.4](m5-4-twitch-authorization-lifecycle.md), [M6.4](m6-4-android-reconnect-and-resynchronization.md), [M7.3](m7-3-watch-media-controls.md)  
Open decisions: [D05, D09](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: No application test boundary for this documentation/observation task.

## Outcome

Measure acknowledged controller changes through the phone and outgoing overlay.

## Entry conditions

G3, with M3–M7 behavioral regression suites already passing. Required physical/provider access must be available.

Reproducible application failures return to one failing regression test at a confirmed boundary before the minimal fix. Keep a repeatable physical scenario for device-only findings.

## Steps

1. Use the supported physical connection and record tap/send, durable commit, acknowledgment, and outgoing-overlay timestamps.
2. Measure 50 acknowledged score changes against the PRD target; exercise Android mobile and watch paths, with per-path samples proposed for diagnosis.
3. Measure destination viewer playback separately; record losses/failures alongside the acknowledged sample.

## Acceptance criteria

- [ ] At least 95% of 50 acknowledged changes reach the phone and outgoing overlay within 2 seconds on the supported connection.
- [ ] Viewer playback delay is reported separately.
- [ ] Both control paths and measurement method/configuration are documented.

## Evidence to retain

Retain sanitized timing samples, counts, and network/device context; do not substitute emulator timing or playback latency.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

