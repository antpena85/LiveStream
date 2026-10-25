# M8.1: V01: Validate sustained real broadcasts

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M8 — Acceptance and personal release](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-01; PRD success criteria; PRD 4.2 ([approved PRD](../../PRD.md))  
Blocked by: [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md), [M5.3](m5-3-youtube-authorization-lifecycle.md), [M5.4](m5-4-twitch-authorization-lifecycle.md), [M6.4](m6-4-android-reconnect-and-resynchronization.md), [M7.3](m7-3-watch-media-controls.md)  
Open decisions: [D12](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: No application test boundary for this documentation/observation task.

## Outcome

Collect real-duration reliability evidence separately for both providers.

## Entry conditions

G3, with M3–M7 behavioral regression suites already passing. Required physical/provider access must be available.

Reproducible application failures return to one failing regression test at a confirmed boundary before the minimal fix. Keep a repeatable physical scenario for device-only findings.

## Steps

1. Enter after G3 with the behavioral regression suite green and record the exact configuration.
2. Run the PRD's 60-minute smoke and 90–120 minute full-match coverage, including a YouTube session and a separate Twitch session at 720p/30 fps on stable upload.
3. Observe received playback, thermal/battery/dimming behavior, crashes, and unrequested stops; extend evidence before claiming additional streamer roles.

## Acceptance criteria

- [ ] Required sessions finish without an application crash or unrequested stream stop.
- [ ] Destination, device/OS, connection, actual duration/quality, and interruptions are recorded.
- [ ] A failed required run keeps V01/G4 open.

## Evidence to retain

Retain sanitized session logs and real destination observations. No full-duration or compatibility result exists yet.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

