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

1. Keep the local capture experiment functional; select a provisional Android
   publishing approach without assuming the diagnostic code is production-ready.
2. At the confirmed session/media boundary, test start/stop and failure behavior
   in small red/green slices. Integrate an Android-compatible implementation of
   pending-stop and numeric-fit safeguards rather than stubbing the JVM store.
3. Connect a minimal local scoreboard to the encoded output and publishing path;
   test its observable behavior at the confirmed boundaries.
4. Build, run the relevant automated tests, then perform the operator-controlled
   physical and received-stream checks. Record failures as well as successes.

No fixed time-box duration or test provider has been selected. Choose these with
the owner when needed rather than treating a proposed value as confirmed.

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