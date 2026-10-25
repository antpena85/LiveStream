# M1.3: Choose representative microphone validation inputs

Plan created: 2026-10-18  
Task status: Complete: owner-reported built-in-only availability and pending accessory cases recorded on 2026-10-18.  
Parent: [M1 — Planning package](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-03 ([approved PRD](../../PRD.md))  
Blocked by: [M0.1](m0-1-approved-requirements-baseline.md)  
Open decisions: [D01](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: No application test boundary for this documentation/observation task.

## Outcome

Prepare an accessory matrix covering the approved wired and Bluetooth scope.

## Entry conditions

Documentation/review only. Owner facts or answers are required where the task calls for them.

## Steps

1. List accessories the owner can make available, including connector, adapter/receiver, and microphone type.
2. Distinguish USB, other wired inputs, wireless receivers, and Bluetooth inputs in the matrix.
3. Select representative wired and Bluetooth cases for physical checks; mark unavailable combinations as pending.

## Acceptance criteria

- [x] The matrix includes representative wired and Bluetooth cases without imposing a brand requirement.
- [x] Availability is distinguished from tested routing support.
- [x] Physical checks are assigned to M2.1/M2.2 and V04.

## Evidence to retain

The [dated microphone inventory](../inventories/m1-3-representative-audio-accessories.md)
retains the owner's 2026-10-18 answer, “built in only for now.” A00 is the
available built-in baseline; A01–A04 cover USB, other wired, wireless-receiver,
and direct Bluetooth cases with unavailable equipment explicitly pending.
Models, microphone types, and connector/adapter/receiver details are unspecified
until actual accessories are available. All app routing and capture remain untested.

Update 2026-10-24: Q48 explicitly defers A01–A04 external microphone support
and testing until after version 1. The completed historical matrix is retained;
A00 built-in audio remains the required M2/v1 path. No accessory case is passed
or treated as a version 1 gate condition by this scope change.

All three acceptance criteria have documentation evidence. This completes the
planning inventory under the task's provision for pending unavailable cases;
it does not remove accessory support from the release, close D01, or validate
any input. Physical checks remain assigned to M2.1/M2.2 and V04.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

