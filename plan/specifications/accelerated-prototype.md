# Accelerated broadcast prototype

Date: 2026-11-01  
Status: Prototype direction authorized; implementation and physical broadcast pending.

## Objective and scope

Demonstrate one manually configured destination receiving video, built-in audio,
and a locally controlled scoreboard from one phone. Use a provisional media
candidate and the existing physical capture evidence as input, not as a final
stack decision. No sign-in, remote controller, adaptive quality, full scoring
rules, or sustained-match validation is claimed in this initial slice.

## Prototype gate and evidence

Before a trial, establish a buildable Android publisher with explicit confirmed
start and stop, actual connection/failure status, credential-safe handling,
cleanup on exit or failure, and the applicable scoreboard fit and stop safety
contracts. Test repeatable behavior at the previously confirmed T01–T06
boundaries. Never substitute the local-only capture permission gate for stream
authorization. The operator provides an approved test destination on-device;
no key, credential-bearing URL, raw provider error, or personal media is retained.

Check ADB visibility and authorization for the selected streaming phone before
deployment. The owner initiates a bounded trial and separately inspects the
received picture, updated score and audio. Record sanitized device/build and
results; API callbacks alone do not prove received output. A build or local
recording alone does not pass this gate.

## Deferred, not waived

Provider sign-in, direct watch pairing, phone remote, complete match behavior,
adaptation, recovery, sustained operation and the full device/provider matrix
remain required for the approved release. Existing G2 and release acceptance
remain open; this narrower prototype is not a G2 pass, compatibility claim, or
match-ready application. Review the initial dependency license risk before
adoption and complete the release dependency/license review before release.

## Immediate sequence

Owner-confirmed priority on 2026-11-06: **broadcast-first, code-first and
document-on-green**. Keep TDD: write a focused behavioral test at an already
confirmed boundary, observe red, implement the minimum and run green. Record
concise evidence and remaining risks after each coherent green slice; group
related cases and commits rather than creating a document for each test.
Pause speculative host-only race permutations unless they unblock integration
or address a concrete safety defect. First-run passes are verification, not
red/green cycles. Keep existing probe tests and evidence.

1. Reuse the isolated Android media experiment and its provisional candidate.
   Build a manual-destination publisher path with explicit confirmation,
   observable connection/failure state, safe credential handling and cleanup.
   Implement in small tested vertical slices; do not expose a real trial path
   before the safety requirements below are in place.
2. Integrate a real Android-compatible safety state and fit/stop safeguards:
   preserve values, fail closed on unresolved fit or pending stop, and do not
   claim stopped without appropriate confirmation. Do not substitute permissive
   stubs or infer Android guarantees from the host SQLite fixture.
3. Connect the local scoreboard to the publishing/encoded path. Build, run the
   relevant automated tests and document results after green. Continue host and
   build work while a physical device is unavailable.
4. Once the prototype gate is met, recheck ADB for the chosen streaming phone.
   The owner chooses destination and duration and initiates the bounded trial;
   separately inspect received video, changed score and built-in audio. Record
   sanitized observations and failures without retaining keys or personal media.

Provider sign-in, phone/watch controls, adaptation tuning, recovery stress and
the full device/provider matrix can proceed after this first demonstration or
in parallel when useful. They are deferred from this prototype, not removed
from release. M2 completion, G2 and M3–M8 authorization are unchanged.

No fixed time-box duration or test provider has been selected. Choose these with
the owner when needed rather than treating a proposed value as confirmed.

## Host build and device availability — 2026-11-06

The offline `media assembleDebug` runner built the isolated diagnostic APK with
the current shared session source (35 tasks: five executed, 30 up to date).
The Kotlin compiler reported an existing listener-parameter-name warning. This
is a source/build compatibility check only: the activity does not use the shared
session as an Android publisher and has no Android durable safety-state adapter.

The ADB inventory runner returned an empty device list. Before any physical
deployment or validation, connect and authorize the intended streaming phone and
repeat the visibility check. No device, capture, network publisher, or received
broadcast was exercised in this checkpoint; the prototype gate remains open.

## Local scoreboard checkpoint — 2026-11-01

The owner reviewed a local Pixel 10 Pro XL recording and reported that both
score buttons worked and audio/video were good. [Sanitized evidence](../evidence/m2/local-score-controls-2026-11-01.md)
distinguishes this observation from metadata. This completes neither publishing
nor the prototype gate: the local diagnostic does not implement numeric-fit
safety, real match scoring, or an Android publisher.

## Host safety checkpoint — 2026-11-02

The [T02 store-unavailable review](../evidence/m2/session-store-unavailable-review-2026-11-02.md)
records an observed red/green host slice: if safety state cannot be read after a
connection callback, the session enters `Stopping`. This does not dispatch a
publisher stop or make the JVM store Android-compatible. The Android-compatible
boundary, durable state, fit enforcement and publisher integration remain open.

## Reset during connection — host verification

At the confirmed T02 boundary, a real temporary store is reset to a new epoch
after Start is accepted but before the connection callback. The existing session
returns `Stopping` on that callback, not `Live`. The [first-run result](../evidence/m2/session-reset-during-connect-first-run.xml)
passed without an implementation change; this is verification, not a red/green
cycle. The full forced offline JVM suite subsequently passed 70 tests with no
failures, errors or skips. This sequential check does not establish concurrency
atomicity, Android integration, real stop dispatch, or provider termination.