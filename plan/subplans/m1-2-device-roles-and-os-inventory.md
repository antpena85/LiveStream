# M1.2: Record device roles and installed operating systems

Plan created: 2026-10-18  
Task status: Complete: owner facts, interchangeable roles, and explicit installed-software unknowns recorded on 2026-10-18.  
Parent: [M1 — Planning package](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: PRD 5.4; FR-02; FR-06 ([approved PRD](../../PRD.md))  
Blocked by: [M0.1](m0-1-approved-requirements-baseline.md)  
Open decisions: [D01](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: No application test boundary for this documentation/observation task.

## Outcome

Identify the actual phones and watches that future feasibility checks will exercise.

## Entry conditions

Documentation/review only. Owner facts or answers are required where the task calls for them.

## Steps

1. Record actual or explicitly unknown installed OS versions/builds for all five phones and each of the four Pixel Watch 4 devices, separately from the owner's software expectation.
2. Record the owner's role policy. The owner confirmed interchangeable phone roles on 2026-10-18; record the actual streamer/controller combination per session instead of requiring a permanent assignment.
3. Record each watch's current companion phone and any pairing change needed for direct connection to the session's streamer.

## Acceptance criteria

- [x] Each device has an actual or explicitly unknown OS/build and proposed or confirmed role.
- [x] Direct watch pairing remains Q7; role choices are not inferred from device capability.
- [x] No untested lens, encoder, audio route, or OS behavior is marked supported.

## Evidence to retain

The [dated device/role inventory](../inventories/m1-2-device-roles-and-os-inventory.md)
records the confirmed target devices, interchangeable phone roles, expected
current or previous major stable OS software, explicitly unknown installed versions/builds,
and conditional direct-pairing prerequisites. Owner answers and follow-up
clarifications were received on 2026-10-18: there are two regular Pixel 10 and
two Pixel 10 Pro phones, each with its own Pixel Watch 4, plus one Pixel 8 Pro.
The main test group is one Pro, its watch, and the Pixel 8 Pro; any device may
be tested. N−1 means the previous major stable OS version.

All listed acceptance criteria have documentation evidence. The inventory has
separate rows for five phones and four watches, owner-reported companion pairs,
and pairing prerequisites for each phone when it is selected to stream.
Actual installed software/builds remain explicitly unknown, as allowed by this
task. No physical-device checks have been performed. M1.2 is complete; D01 and
later implementation/device-validation work remain open.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

