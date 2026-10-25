# M8.4: V04: Validate physical lens and audio continuity

Plan created: 2026-10-18  
Task status: Not started; this file is a proposed task plan.  
Parent: [M8 — Acceptance and personal release](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-02; FR-03; PRD success criteria ([approved PRD](../../PRD.md))  
Blocked by: [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md), [M5.3](m5-3-youtube-authorization-lifecycle.md), [M5.4](m5-4-twitch-authorization-lifecycle.md), [M6.4](m6-4-android-reconnect-and-resynchronization.md), [M7.3](m7-3-watch-media-controls.md)  
Open decisions: [D01, D03, D08](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T02](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Document actual supported camera and microphone combinations.

## Entry conditions

G3, with M3–M7 behavioral regression suites already passing. Required physical/provider access must be available.

Reproducible application failures return to one failing regression test at a confirmed boundary before the minimal fix. Keep a repeatable physical scenario for device-only findings.

## Steps

1. Use the physical matrix to exercise exposed lenses and supported live switches during a broadcast.
2. Exercise built-in audio and deliberate mute on each selected phone/build.
3. Observe received audio, timing, input/levels, mute preservation, match retention and interruptions.
   Q48 defers external USB/wired/receiver/Bluetooth support/testing until after v1.

## Acceptance criteria

- [ ] Tested supported changes preserve match data and produce no crash.
- [ ] Actual built-in input/audio and mute outcomes are recorded per phone/build.
- [ ] Unavailable and untested combinations remain visibly distinct from supported combinations.

## Evidence to retain

Retain physical-device observations and received-audio/video evidence; emulator results cannot establish hardware support.

Use the [M1.3 audio matrix](../inventories/m1-3-representative-audio-accessories.md)
to retain separate built-in, USB, other wired, receiver, and Bluetooth outcomes.
Under Q48, A01–A04 accessory cases are deferred until after version 1 and do not
block its release. A00 built-in capture/received audio remains required; no
external case is counted as passed from the built-in baseline.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

