# M1.4: Collect venue Wi-Fi and mobile upload measurements

Plan created: 2026-10-18  
Task status: Complete: owner-selected mobile-data default, changing-venue measurement gaps, and M2.6 handoff recorded on 2026-10-18; no uploads measured.  
Parent: [M1 — Planning package](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-01; PRD 5.4 ([approved PRD](../../PRD.md))  
Blocked by: [M1.2](m1-2-device-roles-and-os-inventory.md)  
Open decisions: [D01, D07](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: No application test boundary for this documentation/observation task.

## Outcome

Supply measured network conditions for a later quality policy.

## Entry conditions

Documentation/review only. Owner facts or answers are required where the task calls for them.

## Steps

1. Record intended venue, connection type, time, upload observations, measurement method, and the phone/installed software used. Phone roles are interchangeable under the completed M1.2 inventory; select a measurement configuration rather than a permanent streamer.
2. Sample available Wi-Fi and mobile data separately; note variability, handoff constraints, and any unavailable measurements.
3. Feed the results into M2.6 without choosing unmeasured bitrate floors or response thresholds.

## Acceptance criteria

- [x] Both connection types have measurements or explicit evidence gaps.
- [x] The stable-connection validation baseline stays 720p/30 fps.
- [x] No quality parameter is presented as owner-approved solely from these observations.

## Evidence to retain

Retain dated upload observations and uncertainty; do not include network passwords or personal identifiers.

The [dated venue upload inventory](../inventories/m1-4-venue-upload-baseline.md)
records N01 (Wi-Fi) and N02 (mobile data), their confirmed availability under
PRD Q14, and the absence of supplied measurements. Venue, time, physical
phone/build, method, upload samples, variability, and restrictions remain
explicit gaps. Completion uses the checklist's existing allowance for gaps
and the owner's 2026-10-18 answer that venue-specific measurements are unavailable
because venues keep changing: “Default to mobile data”. The default is confirmed;
Wi-Fi remains supported. Future observations are collected per session, with
mobile data prioritized and separate Wi-Fi cases when available.

The inventory includes a proposed collection procedure, session fields, and
handoffs to M2.6, V01, and V08. It preserves the stable 720p/30 fps validation
baseline and leaves D01/D07 open. No speed test, handoff check, or broadcast
has run, and no numeric quality parameter has been selected.

All three criteria have documentation evidence. M1.4's documentation prerequisite
for M2.6 is satisfied; M2.6's other dependencies, gates, and measured-policy
requirements still apply. D01/D07 remain open for actual evidence and decisions.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

