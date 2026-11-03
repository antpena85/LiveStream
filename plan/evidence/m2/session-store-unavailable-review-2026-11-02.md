# T02 safety-store failure during connection

Date: 2026-11-02 (local workstation date). Isolated JVM session probe only.

At the confirmed T02 session boundary, a real temporary SQLite store provides an
assessed snapshot for a confirmed start. After start acceptance, the test closes
the store before reporting a successful connection. The expected public status is
`Stopping`, not `Live` or indefinitely `Connecting`.

The [pre-fix source](session-store-unavailable-red-source.txt) threw a closed
database exception from the callback, as shown by the [one-test red report](session-store-unavailable-red.xml).
This is a behavior failure, not a compilation or harness failure. The minimal
change maps exceptions while rechecking safety state to `Stopping`.
The [green session report](session-store-unavailable-green.xml) contains 19
passing cases, including the new case and existing pending-stop, stale snapshot,
and connection checks. Report hostnames are redacted. Timestamps are UTC and
fall on November 3.

The forced full JVM run passed 68 tests, with zero failures, errors or skips:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File tools/m2/run-feasibility.ps1 test --offline --rerun-tasks
```

The separate media `assembleDebug --offline` also succeeded with all tasks up to
date. That Android project does not compile or use `StreamSession`; its successful
build is not Android validation of the change. No device or provider was used.

## Limits and next work

`Stopping` is a probe state only: this code does not dispatch or confirm a real
stop, ensure a publisher has stopped, surface an error, or handle every timing.
The broad failure mapping does not distinguish recovery causes. Read failures
at start, synchronization between snapshot and pending-stop reads, Android
persistence, fit rendering, callback concurrency and frames in flight remain
unverified. Do not connect this fixture to a publisher as a safety guarantee.

The Android-compatible safety-state boundary and implementation remain to be
designed and tested, preserving durable pending-stop and revision-bound fit
requirements without a permissive stub. The local-only gate cannot authorize
publishing. G2 and the accelerated prototype's received-stream gate stay open.

## Start-time safety-store failure continuation

At the same T02 boundary, a confirmed, otherwise ready start with a closed real
SQLite fixture must return `SafetyStateUnavailable` and remain `Stopped`. The
[pre-fix source](session-start-store-unavailable-red-source.txt) propagated a
closed-database exception, as retained in the [red report](session-start-store-unavailable-red.xml).
The minimal change denies the start when a safety-state read throws. The test
initially asserted the result's name so that the missing result could be observed
as a runtime behavior failure; after adding the result, it asserts the enum value.
The [green session report](session-start-store-unavailable-green.xml) contains
20 passing cases. Hostnames are redacted. A forced full run with the command above
passed 69 tests, zero failures, errors or skips. This adds no Android integration,
atomic combined read, publisher stop, or physical evidence; the limits above
otherwise remain. Exceptions are mapped to a generic denial and are not yet
distinguished for operator recovery.