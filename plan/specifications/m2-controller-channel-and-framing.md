# M2 controller channel and bounded framing proposal

Recorded: 2026-10-29 (workspace date)
Status: Proposed isolated experiment contract; no network implementation.
Authority: [M2 controller contract](m2-android-controller-link-contract.md),
existing owner-confirmed T04 scope. D05/D09/D11 remain open.

## Purpose and threat boundary

Evaluate a foreground, single-controller diagnostic exchange on shared Wi-Fi
before adding match commands. Assume another LAN participant can discover the
listener and send arbitrary bytes. Being on the same network, knowing an IP
address, or supplying a controller name must not grant application authority.
No destination credential, real score or stream-control action is in scope.

Existing `ControllerLinkProbe` labels are host fixtures only. Do not expose its
`authorize` method to a remote request or map an unverified payload name to its
caller argument. The cryptographic channel must supply the peer identity; local
operator approval supplies a separate permission grant for that identity.

## Channel candidate and unresolved implementation prerequisites

Propose TLS using platform APIs, with mutual certificate authentication and an
operator-reviewed out-of-band exchange of public identity material. This is an
experiment candidate, not a production library or pairing-UX selection.

- Validate certificate chains, expected endpoint identity and enrolled peer key.
  Reject an unknown or changed identity; no trust-all manager, permissive
  hostname verifier, plaintext fallback or auto-accept-on-first-network-contact.
- On the streaming phone, approve a verified controller key locally before
  granting even diagnostic ping authority. Do not equate TLS success with an
  application permission grant. A controller display name is presentation only.
- Keep private keys on their originating devices. Public identity exchange must
  be authenticated by a locally reviewed process; a QR code is a possible carrier,
  not automatically a secure pairing protocol. Never retain bootstrap secrets,
  private keys, certificates with identifying fields or payloads in evidence.
- For the first experiment, grants last only for the current foreground run.
  Reopening after process death requires reapproval. Revoke by removing the
  grant and closing that peer's channel. Serialize revocation and request
  authorization: a request checked after revocation is denied; an earlier
  completed ping is not retroactively withdrawn. No queued match mutation exists.
- A new connection is authenticated and authorized again; transport reconnect
  does not restore a grant by a supplied name. Durable enrollment/rotation and
  production reconnect snapshots remain separate, unresolved work.

**Do not implement a listener yet.** Certificate/key generation, protected key
storage, trust-anchor enrollment, expected name/SAN policy for changing LAN
addresses, authenticated exchange, expiry/rotation and negative handshake tests
need a concrete reviewed design first. Do not add a certificate-generation
library or reuse a shared debug signing key as the TLS peer identity by default.
This proposal is not a security audit or evidence of a secure working channel.

The [identity and enrollment review checklist](m2-controller-identity-review.md)
separates the concrete decisions, failure outcomes and evidence required before
the listener. It records no selected mechanism and does not authorize one.

Android's [network security guidance](https://developer.android.com/privacy-and-security/security-ssl)
warns that `SSLSocket` does not perform hostname verification automatically.
Mutual authentication and the enrollment policy above are this project's proposed
experiment controls, not claims that the platform creates them automatically.

## Diagnostic framing proposal v1

On 2026-10-30 the owner selected the isolated host ping-reader result: a
complete ping returns `Ping`, EOF before any new header byte returns `null`,
and EOF after a frame has begun throws `EOFException`. Malformed frames remain
errors. This is a probe API decision, not a production transport selection.

Only inside the authenticated, locally authorized TLS channel:

| Field | Encoding / bound |
| --- | --- |
| Length | Four-byte unsigned big-endian body length; valid range 18–256 bytes |
| Version | One byte, exactly `1` |
| Type | One byte: `1` ping request, `2` matching acknowledgment |
| Request ID | Exactly sixteen opaque bytes, newly generated per request |
| Extra body bytes | Forbidden in v1; valid v1 body length is exactly eighteen |

The 256-byte limit is a proposed hard transport envelope for the experiment,
not approval for optional v1 payloads. Reject a larger advertised length before
allocating/reading its body. Reject zero, undersized, malformed, unknown-version,
unknown-type or trailing-body data; close the channel without a diagnostic echo.
Do not interpret unknown messages as an extension or a match command. A length
between 19 and 256 is still invalid for v1. No text codec or JSON dependency is
needed for this proposed diagnostic frame.

Read exactly one header and body with partial-read support. Clean EOF before a
new header is a normal close; EOF partway through a header/body is truncated.
Reject the entire invalid frame, not a parsed prefix. Do not scan for a later
valid header after malformed input. Distinct subsequent frames are permitted
only after the preceding valid exchange completes.

Proposed experiment limits (not measured production policy): one active client,
one outstanding ping, five seconds for connect, five seconds for TLS handshake,
and ten seconds absolute monotonic deadline per frame/request. The frame deadline
starts before its header; a peer sending one byte at a time must not reset it.
Cancel/close on activity pause, permission loss, selected-network loss, revocation
or deadline expiry. Bound writes too: a socket read timeout alone does not
establish write cancellation. Worker/thread cleanup must be verified before an
Android adapter is called complete. No background service or automatic retry.

Echo a request ID only for a well-formed, authorized ping. Match acknowledgments
to the active request and channel; mismatched or late acknowledgments cannot
complete it. Request IDs provide correlation, not durable deduplication or replay
protection for future commands. Reuse of an ID never grants authorization.

## Android permission and route evaluation

The official [local-network permission guide](https://developer.android.com/privacy-and-security/local-network-permission)
describes Android 17/API 37 targeting enforcement, system-mediated picker options
and explicit local-network permission. Evaluate the applicable path for both
incoming and outgoing diagnostic connections; denial/revocation must remain
visible and must not trigger settings changes or retries behind the owner.
Do not infer app permission from successful shell pings. The earlier Android 16
device is updating and must not be queried until completion is confirmed.

The [Network API](https://developer.android.com/reference/android/net/Network)
supports a network-bound socket factory and binding an unconnected socket.
Propose keeping controller sockets on the selected Wi-Fi network while measuring
a separately requested cellular socket. No process-wide binding or silent
fallback from requested cellular to Wi-Fi. This is a feasibility requirement,
not proof a future RTMP library exposes the necessary socket integration.
No provider endpoint or upload measurement is authorized by this design alone.

## Next vertical test and evidence split

At existing T04 request/result scope, propose a bounded diagnostic decoder's
public read result: given an oversized advertised body length, reject it before
reading any body bytes. Use a real byte-stream fixture, no private-method tests;
retain one behavioral red before the minimum fix. Follow with one behavior per
cycle: valid ping/ID round trip, partial reads, malformed/truncated messages and
absolute-deadline/cancellation outcomes. No networking is required for that first
framing slice. The subsequent [host header-slice evidence](../evidence/m2/android-controller-link/README.md#oversized-frame-envelope-observed-redgreen-cycle)
implements only unsigned header inspection and the upper envelope rejection,
with one retained red/green case. It is not a complete decoder: v1 body validity,
EOF semantics and deadlines remain unimplemented or unverified as documented
there. No channel/listener is implemented.

The subsequent [valid-ping cycle](../evidence/m2/android-controller-link/README.md#valid-v1-ping-decoding-observed-redgreen-cycle)
adds a host-only ping reader preserving all sixteen request-ID bytes without
consuming the next frame. It checks fixed length/version/type, but negative
guard behavior still needs explicit tests. Acknowledgment decoding, clean EOF,
deadlines and the secure channel remain outside this implemented slice.

The [unsupported-version verification](../evidence/m2/android-controller-link/README.md#unsupported-version-first-run-verification)
now explicitly checks rejection of version 2 with an otherwise valid ping.
It passed first-run without changing the decoder; other malformed-frame
behaviors still require separate tests.

Keep separate receipts for host frame behavior, real TLS negative/positive
handshakes, Android permission outcomes, physical authenticated ping, network
selection and actual publishing. None substitutes for another. The current
thirty-two-test host suite, shell reachability and watch pings establish none of
the proposed channel security. M2.4/M2.8 and G2 remain open.

The [acknowledgment-type verification](../evidence/m2/android-controller-link/README.md#acknowledgment-type-first-run-verification)
on 2026-10-30 checks that a v1 type-2 frame is rejected by the ping reader.
It passed first-run against unchanged decoder code; this is not acknowledgment
decoding or channel evidence.

The [unknown-type verification](../evidence/m2/android-controller-link/README.md#unknown-message-type-first-run-verification)
checks rejection of type 3 with an otherwise valid v1 frame. It passed first-run
against unchanged decoder code; other malformed inputs require separate evidence.

The [undersized-body verification](../evidence/m2/android-controller-link/README.md#undersized-advertised-body-first-run-verification)
checks rejection of an advertised, complete 17-byte body. It passed first-run
against unchanged decoder code, without establishing all invalid-length cases.

The [long-v1-body verification](../evidence/m2/android-controller-link/README.md#long-v1-body-within-envelope-first-run-verification)
checks rejection of a complete 19-byte body within the 256-byte envelope. It
passed first-run against unchanged decoder code; other cases remain unverified.

The [short-read verification](../evidence/m2/android-controller-link/README.md#short-reads-first-run-verification)
checks one-byte bulk reads preserve the complete ping ID and trailing sentinel.
It passed first-run without a decoder change; blocking and socket behavior are
not established.

The [truncated-ID verification](../evidence/m2/android-controller-link/README.md#truncated-request-id-first-run-verification)
checks that EOF after fifteen request-ID bytes throws `EOFException` and does
not return a ping. It passed first-run against unchanged decoder code; clean EOF
distinction and network-channel closure remain unverified.

The [truncated-header verification](../evidence/m2/android-controller-link/README.md#truncated-length-header-first-run-verification)
checks EOF after three header bytes throws `EOFException`. It passed first-run
against unchanged decoder code; this historical test alone did not specify clean EOF.

The [clean-EOF cycle](../evidence/m2/android-controller-link/README.md#clean-eof-observed-redgreen-cycle)
subsequently implements the owner's nullable host result. The empty-stream test
was observed red and then green; sampled truncated header and ID cases remain
green. This does not establish channel closure or real network behavior.

The [clean EOF after ping verification](../evidence/m2/android-controller-link/README.md#clean-eof-after-a-ping-first-run-verification)
checks that a valid ping followed by EOF returns the ping and then `null` on a
second read. It passed first-run, without establishing real socket behavior.

The [partial-next-header verification](../evidence/m2/android-controller-link/README.md#partial-next-header-after-ping-first-run-verification)
checks that a valid ping followed by one header byte and EOF raises
`EOFException` on the next read, rather than returning clean-close `null`.
It passed first-run; actual socket and channel behavior remain unverified.

The [consecutive-pings verification](../evidence/m2/android-controller-link/README.md#consecutive-pings-first-run-verification)
checks two distinct host frames preserve their IDs and order, followed by clean
EOF. It passed first-run; it does not model authorization or complete exchanges.