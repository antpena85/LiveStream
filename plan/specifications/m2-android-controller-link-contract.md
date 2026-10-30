# M2 Android controller link contract

Scope: isolated M2.4 feasibility under the owner-confirmed T04 public boundary.
Not the release application or a production transport/security selection.

## First vertical slice: explicit authorization and revocation

The host-only `ControllerLinkProbe` exposes `authorize(controller)`,
`revoke(controller)` and `ping(controller, requestId)`. Its public result is
either a diagnostic acknowledgment echoing the request ID or a denial with no
echoed request ID. After explicit authorization, a controller can obtain an
acknowledgment; after revocation, its next request is denied. Test through those
operations and returned results, not internal membership state. This refines
existing T04 authorization/revocation scope without changing its boundary.

Controller names in the host fixture are caller-supplied test labels, **not
authenticated identities**. A future network adapter must supply an identity
only after a separately reviewed authenticated channel/pairing mechanism; never
accept a client-supplied name as proof of identity. This slice opens no socket,
stores no credential, mutates no match and makes no radio/Android claim.
It is an in-memory permission gate, not replay prevention, durable receipts,
session generations, concurrent revocation ordering or cryptographic validation.

First behavioral check: authorize one controller, observe its acknowledgment,
revoke it, then observe denial of a fresh request. Retain the observed failing
assertion before implementing revocation. Add other behaviors one at a time.

Additional verified behavior: authorizing one controller does not authorize a
different, never-authorized controller. The latter receives DENIED with no
echoed request ID. This passed first-run against unchanged permission-gate code;
see the [retained verification](../evidence/m2/android-controller-link/README.md#never-authorized-controller-first-run-verification).
It proves fixture permission isolation, not authenticated network identity.

## Later app-level harness work, not implemented by this slice

The [channel and framing proposal](m2-controller-channel-and-framing.md) now
records a TLS candidate, unresolved enrollment prerequisites, a bounded v1
diagnostic frame and the next proposed T04 framing test. It implements no
listener and does not resolve production transport/security decisions.

- Define authenticated pairing, identity/session lifetime and revocation races.
- Define bounded wire framing, message-size limits, cancellation and timeouts
  before exposing a foreground LAN listener. Do not transmit destination secrets.
- Evaluate runtime network permissions and denial on the installed Android
  builds against official documentation before adding the Android adapter.
- Keep Wi-Fi controller sockets separate from a requested cellular publisher
  network; do not bind the entire process as a routing shortcut.
- Retain actual app-selected network observations and physical request/reply
  results separately from the [shell preflight](../evidence/m2/android-controller-preflight-2026-10-29.md).
- Leave the updating Pixel 8 Pro untouched until the owner reports completion
  and its installed build is re-read. Do not infer completion from ADB visibility.

No production stack decision, RT01/RT06 completion or G2 passage is implied.