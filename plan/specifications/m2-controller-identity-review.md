# M2 controller identity and enrollment review

Recorded: 2026-10-30. Status: review candidate; **not approval to implement a
listener**, a pairing workflow, or a production security selection.
Scope is the isolated M2.4 foreground diagnostic experiment described in the
[channel proposal](m2-controller-channel-and-framing.md) and
[controller contract](m2-android-controller-link-contract.md).

## Evidence and prerequisites

The host permission gate uses caller-supplied labels, not authenticated
identities. Its authorization and revocation results cannot establish secure
peer identification. The host decoder does not close channels, authenticate
peers, impose deadlines or implement request/reply ordering. Physical Android
and uplink evidence remains separate; see the [execution record](../evidence/m2/android-controller-link/README.md).

Before any listener, review and resolve all of the following without putting
private keys, pairing secrets, identifying certificates, addresses or tokens in
source or retained evidence:

1. **Local identity lifecycle:** define how each experiment installation creates,
   protects, identifies and destroys its private key; specify process restart,
   reinstall, device replacement and failure to access the key. Decide whether
   identity persists beyond a foreground run; do not infer this from debug signing.
2. **Enrollment ceremony:** specify which public identity material is exchanged,
   its canonical representation and fingerprint, who compares it on each device,
   how the comparison is authenticated, when it expires, and how cancel, mismatch
   and interrupted enrollment leave no active grant. QR display alone is not proof.
3. **Peer verification:** define both roles' trust anchors and expected endpoint
   identity, including changing LAN addresses, and the exact rejection behavior
   for an unrecognized, replaced, expired, untrusted or wrong-role identity.
   TLS channel authentication and the local permission grant are separate checks.
4. **Grant lifetime and ordering:** define the session identifier and local
   approval boundary, enforce one foreground-run grant, and serialize revocation
   against requests. Document the linearization point: a request checked after
   revocation is denied; a completed earlier reply is not retroactively withdrawn.
   Close the active peer channel on revocation; reconnect requires new verification
   and approval. A caller-provided display name never supplies peer identity.
5. **Frame and resource failure:** specify the owner of each socket/worker, bounded
   reads and writes, monotonic absolute deadlines, and observable cleanup on pause,
   permission or selected-network loss, revocation, malformed input and timeout.
   No automatic retry, plaintext fallback, echo of malformed data or resynchronizing
   by scanning input. Confirm no worker or grant survives terminal cleanup.
6. **Reviewable evidence:** define sanitized, reproducible host and physical
   records identifying test case, role, expected/observed outcome and timing,
   without sensitive identity material. Distinguish fixture results from actual
   handshake, Android permission and device/network results.

## Acceptance matrix for the design review

These are proposed checks, **not executed results**. Test seams and fixtures must
be agreed before executable tests are added. A design must identify a separate
verification boundary and expected observable result for each row.

| Case | Required observable outcome | Evidence still needed |
| --- | --- | --- |
| Known identities, locally approved session | Authenticated and authorized diagnostic exchange | Real positive handshake and separate grant observation |
| Unknown, changed, wrong-role or untrusted identity | No grant or acknowledgment | Independent negative handshake cases |
| Enrollment mismatch, cancellation or interruption | No active grant; cleanup visible | Controlled enrollment interruption cases |
| Valid channel without local approval | No diagnostic acknowledgment | Distinct authentication/authorization check |
| Revocation and reconnect | Later requests denied and channel closed; reconnect reverified and reapproved | Ordered revocation and reconnect observations |
| Malformed or truncated frame | No diagnostic echo; channel closes | Adapter-level negative cases, distinct from host parsing |
| Deadline, pause or network/permission loss | Bounded termination and worker/grant cleanup | Controlled interruption and cleanup evidence |
| Wi-Fi controller with cellular publisher | Both actual selected routes observed independently | Physical app-level coexistence and actual publisher evidence |

## Decisions and stop conditions

Key storage, public identity encoding, certificate/endpoint policy, authenticated
out-of-band comparison and test fixture provisioning are unresolved. Select none
by default and do not fabricate credentials for retained evidence. If a proposed
mechanism cannot verify the intended peer without a permissive trust or hostname
shortcut, stop rather than weaken authentication. The owner subsequently confirmed
the Pixel 8 Pro update completed and identified its installed build; see the
[post-update reinspection](../evidence/m2/android-controller-preflight-2026-10-29.md#post-update-adb-reinspection--2026-10-30).
This document alone does not pass M2.4 or G2.