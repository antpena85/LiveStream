# M2.4: Verify Android and direct-watch connectivity

Plan created: 2026-10-18  
Task status: In progress: T04 correlation red/green and bidirectional foreground round trips retained; October 29 Bluetooth-off acknowledgment timeout followed by Bluetooth-restored foreground recovery observed. Timeout cause, full transport-disconnection/protocol recovery and Android/uplink topology remain unverified.
Parent: [M2 — Technical feasibility and selection](../MASTER_PLAN.md#4-work-breakdown)  
Requirements: FR-06; PRD 2.4 ([approved PRD](../../PRD.md))  
Blocked by: [M2.1](m2-1-physical-capability-inventory.md)  
Open decisions: [D05, D09](../MASTER_PLAN.md#7-open-decision-register)  
Public boundaries: [T04](../MASTER_PLAN.md#proposed-public-interfaces-for-tests)

## Outcome

Demonstrate supported controller connectivity alongside the internet uplink.

## Entry conditions

G1 and M1 before prototypes, scaffolding, application dependencies, or executable tests. Documentation research can proceed during planning.

For repeatable application behavior, follow the [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) and [TDD skill](../../.agents/skills/tdd/SKILL.md): one observed behavioral red result, minimum implementation, then review/refactor with the relevant suite green.

## Steps

1. Compare authenticated LAN/hotspot options, gym client isolation, discovery, and mobile-uplink coexistence.
2. After G1, exercise pairing, acknowledgments, lost connections, and resynchronization on the selected Android devices.
3. Check direct Data Layer reachability between Pixel Watch 4 and the streaming phone, including app identity/signature requirements.

## Acceptance criteria

- [x] The intended Android topology and direct watch path have physical evidence or explicit blockers.
- [x] Authorization is distinguished from transport pairing.
- [x] No relay through a different companion phone is silently added.
- [ ] The master plan's required Android/uplink and direct-watch topology is demonstrated on the selected physical devices.

## Evidence to retain

Retain topology, OS/signing prerequisites, connection observations, and limitations; radio/network support requires physical evidence.

The [isolated phone/watch execution record](../evidence/m2/watch-connectivity/README.md)
retains host correlation red/green and same-APK deployment on Pro XL and watch.
Collected reports establish both listeners and successful round trips initiated
from both devices, with one phone-side acknowledgment timeout of unknown cause.
The October 29 continuation adds a fresh 541 ms watch baseline, a Bluetooth-off
trial with one non-nearby peer and a 10,044 ms acknowledgment timeout, then
Bluetooth-restored discovery of one nearby peer and a 271 ms matched reply.
The operator restored phone Bluetooth; read-only setting samples were off/on.
This establishes one foreground recovery after Bluetooth interruption, not a
full Data Layer disconnection, a specific route or authoritative snapshot
recovery. The broader disconnect/reconnect protocol case remains pending.
Installation alone is not delivery evidence.

The [October 29 Android-controller preflight](../evidence/m2/android-controller-preflight-2026-10-29.md)
records owner-confirmed availability of two Pixel 8 Pro phones, tested Android
16 first and Android 17 separately. Both have bidirectional shell ICMP
reachability to Pro XL over Wi-Fi (three replies from three requests per
direction). This is not app-level permission/authentication evidence or verified
simultaneous cellular publishing. The LAN harness and RT06 remain pending.

Independent [T04 host permission-gate evidence](../evidence/m2/android-controller-link/README.md)
adds one observed revocation red/green cycle and a fresh seventeen-test green
suite. Explicitly authorized fixture callers receive diagnostic acknowledgments;
revoked callers are denied. Caller labels are not authenticated identities.
No socket, Android adapter or physical validation was added; this is not RT01
completion. The updating Pixel 8 Pro remains paused pending owner confirmation
and post-update build inspection.

The next host test verifies denial for a never-authorized controller while
another controller is authorized. It passes on its first run with unchanged
permission-gate code; retained reports in the same execution record show a
fresh eighteen-test green suite. This adds a first-run verification, not a
red/green cycle or app-level LAN/authentication evidence.

The same evidence record now adds an oversized-header T04 red/green cycle:
reject advertised body length 257 before consuming body bytes. A fresh
nineteen-test host suite passes. Only header inspection/upper-envelope rejection
is implemented; full v1 parsing, TLS, Android LAN permissions and RT06 remain
pending. No device was queried during that host slice.

The next retained host cycle decodes a valid v1 diagnostic ping, preserving its
sixteen-byte ID and leaving subsequent bytes unread. Twenty tests pass on a
fresh full-suite run. Negative guards, partial/truncated reads, deadlines and
TLS/Android adapter behavior still need separate evidence; no physical check or
gate completion is added.

The subsequent unsupported-version test passes first-run against unchanged
decoder code: version 2 is rejected instead of yielding a ping. Retained reports
show twenty-one passing host tests on a fresh full-suite execution. This adds
verification only, not another red/green cycle or physical/channel evidence.

On 2026-10-30, the next host test verifies that a type-2 acknowledgment cannot
be decoded as a ping. It passes first-run against unchanged decoder code; the
[retained execution record](../evidence/m2/android-controller-link/README.md#acknowledgment-type-first-run-verification)
includes focused and fresh full-suite reports with twenty-two tests passing.
This adds verification only, not a red/green cycle or physical/channel evidence.

The next [host verification](../evidence/m2/android-controller-link/README.md#unknown-message-type-first-run-verification)
checks rejection of unknown message type 3. It passes first-run against unchanged
decoder code; focused and fresh full-suite reports retain twenty-three passing
tests. This does not establish transport security or physical connectivity.

The [undersized-body host verification](../evidence/m2/android-controller-link/README.md#undersized-advertised-body-first-run-verification)
checks rejection of a complete 17-byte body. It passes first-run against unchanged
decoder code; the fresh full-suite report retains twenty-four passing tests.
This does not establish other invalid-length cases or physical connectivity.

The [long-v1-body host verification](../evidence/m2/android-controller-link/README.md#long-v1-body-within-envelope-first-run-verification)
checks rejection of a complete 19-byte body within the envelope. It passes
first-run against unchanged decoder code; the fresh full suite has twenty-five
passing tests. It does not establish channel security or physical connectivity.

The [short-read host verification](../evidence/m2/android-controller-link/README.md#short-reads-first-run-verification)
checks a valid ping with one-byte bulk reads. It passes first-run against unchanged
decoder code; the fresh full suite has twenty-six passing tests. No asynchronous
transport, channel security or physical connectivity is established.

The [truncated-ID host verification](../evidence/m2/android-controller-link/README.md#truncated-request-id-first-run-verification)
checks EOF after fifteen ID bytes cannot produce a ping. It passes first-run
against unchanged decoder code; the fresh full suite has twenty-seven passing
tests. Other truncation points and actual channel closure remain unverified.

The [truncated-header host verification](../evidence/m2/android-controller-link/README.md#truncated-length-header-first-run-verification)
checks EOF after three header bytes cannot produce a ping. It passes first-run
against unchanged decoder code; the fresh full suite has twenty-eight passing
tests. Clean-close distinction and channel closure remain unresolved.

The subsequent [clean-EOF host cycle](../evidence/m2/android-controller-link/README.md#clean-eof-observed-redgreen-cycle)
implements the owner's selected nullable result, with an observed red and a
fresh twenty-nine-test green suite. Sampled partial frames still throw EOF;
actual network channel closure and topology remain unverified.

The [clean EOF after ping host verification](../evidence/m2/android-controller-link/README.md#clean-eof-after-a-ping-first-run-verification)
checks a complete ping followed by `null` on the next read. It passes first-run
without a decoder change; a fresh thirty-test suite passes. No socket closure or
physical network behavior is established.

The [partial-next-header host verification](../evidence/m2/android-controller-link/README.md#partial-next-header-after-ping-first-run-verification)
checks EOF after one next-header byte is truncation even after a valid ping. It
passes first-run with unchanged decoder code; a fresh thirty-one-test suite passes.
This does not establish physical network or channel-close behavior.

The [consecutive-pings host verification](../evidence/m2/android-controller-link/README.md#consecutive-pings-first-run-verification)
checks two distinct frames are decoded in order followed by clean EOF. It passes
first-run without a decoder change; a fresh thirty-two-test host suite passes.
This does not establish exchange ordering, authorization or network behavior.

The [identity and enrollment review checklist](../specifications/m2-controller-identity-review.md)
identifies listener prerequisites and proposed positive/negative evidence; it
does not select identity storage or pairing, authorize a listener, or complete RT01.

The [topology review and RT01–RT07](../specifications/m2-4-remote-topology-feasibility.md)
retain proposals, Android 17 LAN constraints, signing/trust distinctions and
the owner's no-device blocker. The checklist's explicit-blocker branch is
documented; the master milestone still requires demonstrated connectivity.
M2.4 remains In progress, with M2.1/physical prerequisites and D05/D09 open.

Record actual outcomes, configuration, and unresolved limitations. A written plan is not evidence that the task passed. Update the [task index](README.md) and master plan when real evidence changes status.

