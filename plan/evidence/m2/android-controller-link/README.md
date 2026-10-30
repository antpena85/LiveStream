# T04 host controller authorization/revocation slice

Scope: isolated M2.4 host preparation following the
[public contract](../../../specifications/m2-android-controller-link-contract.md).
No physical device was queried, deployed to, rebooted or reconfigured during
this slice. The updating Pixel 8 Pro remains paused for device-dependent work.

## Observed red/green cycle

The public test authorizes `controller-a`, observes acknowledgment of
`request-1`, revokes the controller and expects denial of `request-2` without
an echoed request ID. The [pre-fix source](ControllerLinkProbe-before-revocation-fix.kt.txt)
left membership unchanged on revocation. The [red report](revocation-red.xml)
records one assertion failure: expected DENIED/null, received
ACKNOWLEDGED/request-2. Compilation succeeded; this was not a harness failure.

Red command, from the workspace root:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File tools/m2/run-feasibility.ps1 test --offline --tests livestream.feasibility.ControllerLinkProbeTest
```

The minimum fix removes the revoked controller from the in-memory permission
set. A fresh full-suite run passed seventeen tests, zero failures/errors/skips,
with four Gradle tasks executed and exit 0:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File tools/m2/run-feasibility.ps1 test --offline --rerun-tasks
```

Retained green results: [one controller case](revocation-green.xml),
[ten storage cases](regression-storage-green.xml),
[three pending-request cases](regression-pending-green.xml), and
[three session-readiness cases](regression-session-green.xml).
This is one new observed T04 red/green cycle. Review found no necessary
refactoring for this bounded permission-gate behavior.

## Limits and next behavior

Caller labels are fixture identities, not authentication. This is sequential,
host-only, in-memory permission enforcement; it opens no socket and supplies no
cryptographic channel, pairing ceremony, durable authorization, concurrent
revocation ordering, replay protection or match command handling. The result
must not be described as authenticated LAN delivery or RT01 completion.

Define channel identity/security and bounded framing before exposing any network
listener, then validate Android permissions and network selection separately.
M2.4/M2.8 remain In progress and G2 remains open. No application dependency or
production technology choice was added.

## Never-authorized controller: first-run verification

The next public-interface test authorizes `controller-a` but requests a ping as
never-authorized `controller-b`. It expects DENIED with no echoed request ID.
The implementation remained unchanged; the test passed on its first execution.
This verifies per-controller permission isolation in the host fixture, not
authenticated identity or resistance to identity spoofing over a network.
No additional observed red/green cycle is claimed.

The same full-suite command above was freshly rerun with `--rerun-tasks`:
eighteen tests passed, zero failures/errors/skips, four tasks executed, exit 0.
Retained results: [two controller cases](unauthorized-first-run-green.xml),
[ten storage cases](unauthorized-regression-storage-green.xml),
[three pending-request cases](unauthorized-regression-pending-green.xml), and
[three session-readiness cases](unauthorized-regression-session-green.xml).
The earlier seventeen-test reports remain historical checkpoints.

Next specify a reviewed channel identity/pairing mechanism and bounded request
framing before adding any LAN listener. Host caller labels must never become
network authentication. Physical network permission, cellular route and publishing
observations remain separate. No device was queried during this verification;
the updating phone remains paused.

## Channel/framing design continuation

The [reviewed-source proposal](../../../specifications/m2-controller-channel-and-framing.md)
records official Android references, separates authenticated channel identity
from local authorization, and specifies a proposed bounded diagnostic frame.
Certificate provisioning and trust enrollment remain unresolved prerequisites
to exposing a listener. The next proposed host slice rejects an oversized frame
before reading its body, within existing T04 request/result scope.
This is a design artifact only: no new test result, TLS implementation, Android
permission observation or physical device check is claimed.

## Oversized frame envelope: observed red/green cycle

Following the framing proposal, the host-only public operation
`DiagnosticFrameHeader.readBodyLength(InputStream)` reads a four-byte unsigned
big-endian length and rejects values above the experiment's 256-byte envelope
with `ProtocolException`. It is a header inspector, not a complete decoder.
The test supplies a real byte-array stream with literal header `00 00 01 01`
(257) and two body sentinel bytes. Rejection must leave both sentinel bytes
unconsumed. No private methods or internal membership/allocation state is tested.

The [pre-fix header reader](DiagnosticFrameHeader-before-size-fix.kt.txt)
returned 257 without rejecting it. The [red report](frame-oversize-red.xml)
records the intended assertion failure: expected `ProtocolException`, but none
was thrown. Compilation succeeded. Red command from the workspace root:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File tools/m2/run-feasibility.ps1 test --offline --tests livestream.feasibility.DiagnosticFrameHeaderTest
```

The minimum fix checks the unsigned length before returning or reading a body.
The full-suite command above with `--rerun-tasks` then passed nineteen tests,
zero failures/errors/skips, four Gradle tasks executed, exit 0. Retained results:
[one framing case](frame-oversize-green.xml),
[two controller cases](frame-regression-controller-green.xml),
[ten storage cases](frame-regression-storage-green.xml),
[three pending cases](frame-regression-pending-green.xml), and
[three session cases](frame-regression-session-green.xml).
This is one additional observed T04 red/green cycle. Review required no refactor.

This slice allocates no advertised-size body buffer and consumes only the header.
It does not yet validate v1's exact eighteen-byte body, minimum size, version,
type or request ID. Clean EOF/truncation semantics, partial-read coverage,
deadlines/cancellation and unsigned high-bit edge cases still need explicit
behavioral verification. No complete decoder, TLS channel or listener is exposed;
the operation must not be treated as full frame validation. Next add valid v1
ping decoding/ID preservation, one failing behavior at a time. No phone was
queried or changed; updating-device checks remain paused. M2.4/G2 remain open.

## Valid v1 ping decoding: observed red/green cycle

The next public T04 framing operation, `DiagnosticPingDecoder.read(InputStream)`,
returns a ping containing its sixteen request-ID bytes. A literal fixture has
length 18, version 1, type 1 and asymmetric bytes including zero/high-bit values.
The test asserts the exact independent byte list and verifies a subsequent
sentinel byte remains unread. No encoder generates the expected fixture.

The [pre-fix source](DiagnosticPingDecoder-before-id-fix.kt.txt) returned an empty
ID. The [red report](ping-decode-red.xml) records one assertion failure for the
sixteen missing bytes, with successful compilation. Red command:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File tools/m2/run-feasibility.ps1 test --offline --tests livestream.feasibility.DiagnosticPingDecoderTest
```

The decoder now uses the existing upper-bounded header reader, requires the
fixed v1 ping length/version/type and reads exactly sixteen ID bytes. It does
not buffer beyond the frame. The full-suite command with `--rerun-tasks`
passed twenty tests, zero failures/errors/skips, four tasks executed, exit 0.
Retained green reports:
[one ping case](ping-decode-green-DiagnosticPingDecoderTest.xml),
[one header case](ping-decode-green-DiagnosticFrameHeaderTest.xml),
[two permission cases](ping-decode-green-ControllerLinkProbeTest.xml),
[ten storage cases](ping-decode-green-DurableCommandProbeTest.xml),
[three pending cases](ping-decode-green-PendingProbeTest.xml), and
[three session cases](ping-decode-green-StreamSessionTest.xml).
This adds one observed red/green cycle. Review found no necessary refactor.

Length/version/type rejection guards are present but their negative outcomes
are not separately verified by this valid-frame test. Partial reads, truncation,
clean EOF, acknowledgment decoding, deadlines and cancellation remain unverified
or unimplemented. This is a ping-only host decoder, not complete v1 protocol
support, authentication, a listener or an Android adapter. No device was queried.
Next explicitly verify a wrong version is rejected, retaining first-run evidence
if existing guards already pass. M2.4/M2.8 and G2 remain open.

## Unsupported version: first-run verification

A literal, otherwise valid eighteen-byte ping body uses version 2 instead of
version 1. The public decoder rejects it with `ProtocolException` rather than
returning a ping. The new test passed on its first execution against unchanged
decoder code; no red/green cycle or implementation change is claimed.

The full-suite command with `--rerun-tasks` passed twenty-one tests, zero
failures/errors/skips, four Gradle tasks executed, exit 0. Retained reports:
[two decoder cases](unsupported-version-green-DiagnosticPingDecoderTest.xml),
[one header case](unsupported-version-green-DiagnosticFrameHeaderTest.xml),
[two permission cases](unsupported-version-green-ControllerLinkProbeTest.xml),
[ten storage cases](unsupported-version-green-DurableCommandProbeTest.xml),
[three pending cases](unsupported-version-green-PendingProbeTest.xml), and
[three session cases](unsupported-version-green-StreamSessionTest.xml).
Earlier reports remain historical checkpoints. No refactoring was necessary.

This verifies one unsupported-version example, not all malformed frames or a
secure network channel. Next verify that an acknowledgment/unknown message type
cannot be decoded as a ping. Negative length cases, partial reads, truncation,
clean EOF, acknowledgment decoding and deadlines remain pending. No device was
queried or changed; M2.4/M2.8 and G2 remain open.

## Acknowledgment type: first-run verification

Resumed on 2026-10-30. A literal eighteen-byte v1 body uses type 2
(acknowledgment) with a complete sixteen-byte request ID. At the existing T04
public decoder boundary, `DiagnosticPingDecoder.read(InputStream)` throws
`ProtocolException` rather than returning a ping. This verifies that an
acknowledgment cannot be mistaken for a request; it does not decode acknowledgments.

The new test passed on its first execution against unchanged decoder code.
The [focused first-run report](ack-type-first-run-DiagnosticPingDecoderTest.xml)
contains one passing test. Command from the workspace root:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File D:/Playground/LiveStream/tools/m2/run-feasibility.ps1 test --offline --tests livestream.feasibility.DiagnosticPingDecoderTest.acknowledgmentFrameIsRejectedInsteadOfReturningAPing
```

The full-suite command with `--rerun-tasks` then passed twenty-two tests,
zero failures/errors/skips, four Gradle tasks executed, exit 0. Retained reports:
[three decoder cases](ack-type-green-DiagnosticPingDecoderTest.xml),
[one header case](ack-type-green-DiagnosticFrameHeaderTest.xml),
[two permission cases](ack-type-green-ControllerLinkProbeTest.xml),
[ten storage cases](ack-type-green-DurableCommandProbeTest.xml),
[three pending cases](ack-type-green-PendingProbeTest.xml), and
[three session cases](ack-type-green-StreamSessionTest.xml).

This is first-run verification, not a new red/green cycle. No implementation
change or refactor was needed. Next verify an unknown type is rejected, then
continue length/partial-read/truncation cases one behavior at a time. Clean EOF,
acknowledgment decoding, deadlines, cancellation, authenticated TLS and Android
network behavior remain unimplemented or unverified. No device was queried or
changed. The updating Pixel 8 Pro remains paused pending owner confirmation and
build reinspection. M2.4/M2.8 remain In progress; no gate or technology decision changed.

## Unknown message type: first-run verification

On 2026-10-30, an otherwise valid v1 frame with unknown type 3 was supplied to
`DiagnosticPingDecoder.read(InputStream)`. The public reader throws
`ProtocolException` rather than returning a ping. This verifies one unknown type,
not every malformed input or channel-level close behavior.

The [focused first-run report](unknown-type-first-run-DiagnosticPingDecoderTest.xml)
shows the new test passed against unchanged decoder code. Command from the
workspace root:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File D:/Playground/LiveStream/tools/m2/run-feasibility.ps1 test --offline --tests livestream.feasibility.DiagnosticPingDecoderTest.unknownMessageTypeIsRejectedInsteadOfReturningAPing
```

The full suite was freshly rerun with `--rerun-tasks`: twenty-three tests passed,
zero failures/errors/skips, four Gradle tasks executed, exit 0. Retained reports:
[four decoder cases](unknown-type-green-DiagnosticPingDecoderTest.xml),
[one header case](unknown-type-green-DiagnosticFrameHeaderTest.xml),
[two permission cases](unknown-type-green-ControllerLinkProbeTest.xml),
[ten storage cases](unknown-type-green-DurableCommandProbeTest.xml),
[three pending cases](unknown-type-green-PendingProbeTest.xml), and
[three session cases](unknown-type-green-StreamSessionTest.xml).

This is first-run verification, not a new red/green cycle; no implementation
change or refactor was needed. Next verify rejection of an undersized advertised
body length. Partial reads, truncation, clean EOF, acknowledgment decoding,
deadlines, cancellation, authenticated TLS and Android network behavior remain
unimplemented or unverified. No device was queried or changed. The updating
Pixel 8 Pro remains paused pending owner confirmation and post-update build
inspection. M2.4/M2.8 remain In progress and G2 remains open.

## Truncated length header: first-run verification

On 2026-10-30, `DiagnosticPingDecoder.read(InputStream)` received three of the
four length-header bytes followed by EOF. It threw `EOFException` rather than
returning a ping. This checks one truncated-header position; it does not establish
that clean EOF before a header is distinguishable or that a network channel closes.

The [focused first-run report](truncated-header-first-run-DiagnosticPingDecoderTest.xml)
shows the test passed against unchanged decoder code. Command:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File D:/Playground/LiveStream/tools/m2/run-feasibility.ps1 test --offline --tests livestream.feasibility.DiagnosticPingDecoderTest.truncatedLengthHeaderCannotProduceAPing
```

The full suite was freshly rerun with `--rerun-tasks`: twenty-eight tests passed,
zero failures/errors/skips, four Gradle tasks executed, exit 0. Retained reports:
[nine decoder cases](truncated-header-green-DiagnosticPingDecoderTest.xml),
[one header case](truncated-header-green-DiagnosticFrameHeaderTest.xml),
[two permission cases](truncated-header-green-ControllerLinkProbeTest.xml),
[ten storage cases](truncated-header-green-DurableCommandProbeTest.xml),
[three pending cases](truncated-header-green-PendingProbeTest.xml), and
[three session cases](truncated-header-green-StreamSessionTest.xml).

This is first-run verification, not a new red/green cycle; no implementation
change or refactor was needed. Clean EOF distinction remains unresolved at the
host decoder boundary: the proposed normal-close result needs an explicit public
contract before implementation. Other truncation points, acknowledgment decoding,
deadlines, cancellation, authenticated TLS and Android network behavior remain
unimplemented or unverified. No device was queried or changed. The updating
Pixel 8 Pro remains paused pending owner confirmation and post-update build
inspection. M2.4/M2.8 remain In progress and G2 remains open.

## Undersized advertised body: first-run verification

On 2026-10-30, the public `DiagnosticPingDecoder.read(InputStream)` operation
received a complete 17-byte body advertised as length 17, one byte below the
required v1 ping length. It threw `ProtocolException` rather than returning a
ping. This is one undersized length example, not evidence for all invalid lengths,
truncated input or channel-level close behavior.

The [focused first-run report](undersized-first-run-DiagnosticPingDecoderTest.xml)
shows the test passed against unchanged decoder code. Command:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File D:/Playground/LiveStream/tools/m2/run-feasibility.ps1 test --offline --tests livestream.feasibility.DiagnosticPingDecoderTest.undersizedAdvertisedBodyIsRejectedInsteadOfReturningAPing
```

The full suite was freshly rerun with `--rerun-tasks`: twenty-four tests passed,
zero failures/errors/skips, four Gradle tasks executed, exit 0. Retained reports:
[five decoder cases](undersized-green-DiagnosticPingDecoderTest.xml),
[one header case](undersized-green-DiagnosticFrameHeaderTest.xml),
[two permission cases](undersized-green-ControllerLinkProbeTest.xml),
[ten storage cases](undersized-green-DurableCommandProbeTest.xml),
[three pending cases](undersized-green-PendingProbeTest.xml), and
[three session cases](undersized-green-StreamSessionTest.xml).

This is first-run verification, not a new red/green cycle; no implementation
change or refactor was needed. Next verify rejection of a 19-byte v1 body within
the 256-byte envelope. Partial reads, truncation, clean EOF, acknowledgment
decoding, deadlines, cancellation, authenticated TLS and Android network behavior
remain unimplemented or unverified. No device was queried or changed. The
updating Pixel 8 Pro remains paused pending owner confirmation and post-update
build inspection. M2.4/M2.8 remain In progress and G2 remains open.

## Long v1 body within envelope: first-run verification

On 2026-10-30, `DiagnosticPingDecoder.read(InputStream)` received a complete
19-byte v1 ping-shaped body, one byte longer than the specified fixed 18-byte
ping and below the 256-byte envelope limit. It threw `ProtocolException` rather
than returning a ping. This checks one invalid length, not all malformed inputs
or whether a network adapter closes the channel after rejection.

The [focused first-run report](long-v1-first-run-DiagnosticPingDecoderTest.xml)
shows the test passed against unchanged decoder code. Command:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File D:/Playground/LiveStream/tools/m2/run-feasibility.ps1 test --offline --tests livestream.feasibility.DiagnosticPingDecoderTest.oversizedV1BodyWithinEnvelopeIsRejectedInsteadOfReturningAPing
```

The full suite was freshly rerun with `--rerun-tasks`: twenty-five tests passed,
zero failures/errors/skips, four Gradle tasks executed, exit 0. Retained reports:
[six decoder cases](long-v1-green-DiagnosticPingDecoderTest.xml),
[one header case](long-v1-green-DiagnosticFrameHeaderTest.xml),
[two permission cases](long-v1-green-ControllerLinkProbeTest.xml),
[ten storage cases](long-v1-green-DurableCommandProbeTest.xml),
[three pending cases](long-v1-green-PendingProbeTest.xml), and
[three session cases](long-v1-green-StreamSessionTest.xml).

This is first-run verification, not a new red/green cycle; no implementation
change or refactor was needed. Next verify a valid ping is decoded correctly
when the underlying stream provides short reads. Truncation, clean EOF,
acknowledgment decoding, deadlines, cancellation, authenticated TLS and Android
network behavior remain unimplemented or unverified. No device was queried or
changed. The updating Pixel 8 Pro remains paused pending owner confirmation and
post-update build inspection. M2.4/M2.8 remain In progress and G2 remains open.

## Short reads: first-run verification

On 2026-10-30, the public `DiagnosticPingDecoder.read(InputStream)` operation
decoded a complete v1 ping from a host byte stream whose bulk reads supply at
most one byte. The test verifies all sixteen request-ID bytes and that a trailing
sentinel remains unread. The fixture does not simulate asynchronous arrival,
blocking, zero-progress reads, network cancellation or a real socket.

The [focused first-run report](short-read-first-run-DiagnosticPingDecoderTest.xml)
shows the test passed against unchanged decoder code. Command:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File D:/Playground/LiveStream/tools/m2/run-feasibility.ps1 test --offline --tests livestream.feasibility.DiagnosticPingDecoderTest.validPingPreservesIdWhenStreamReturnsShortReads
```

The full suite was freshly rerun with `--rerun-tasks`: twenty-six tests passed,
zero failures/errors/skips, four Gradle tasks executed, exit 0. Retained reports:
[seven decoder cases](short-read-green-DiagnosticPingDecoderTest.xml),
[one header case](short-read-green-DiagnosticFrameHeaderTest.xml),
[two permission cases](short-read-green-ControllerLinkProbeTest.xml),
[ten storage cases](short-read-green-DurableCommandProbeTest.xml),
[three pending cases](short-read-green-PendingProbeTest.xml), and
[three session cases](short-read-green-StreamSessionTest.xml).

This is first-run verification, not a new red/green cycle; no implementation
change or refactor was needed. Next verify a truncated request ID cannot produce
a ping. Clean EOF, acknowledgment decoding, deadlines, cancellation,
authenticated TLS and Android network behavior remain unimplemented or
unverified. No device was queried or changed. The updating Pixel 8 Pro remains
paused pending owner confirmation and post-update build inspection. M2.4/M2.8
remain In progress and G2 remains open.

## Truncated request ID: first-run verification

On 2026-10-30, `DiagnosticPingDecoder.read(InputStream)` received a v1 ping
advertising an 18-byte body but ending after 15 of the required 16 request-ID
bytes. It threw `EOFException` rather than returning a partially decoded ping.
This checks one body truncation point; it does not establish clean-close versus
truncated-header semantics or network channel closure.

The [focused first-run report](truncated-id-first-run-DiagnosticPingDecoderTest.xml)
shows the test passed against unchanged decoder code. Command:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File D:/Playground/LiveStream/tools/m2/run-feasibility.ps1 test --offline --tests livestream.feasibility.DiagnosticPingDecoderTest.truncatedRequestIdCannotProduceAPing
```

The full suite was freshly rerun with `--rerun-tasks`: twenty-seven tests passed,
zero failures/errors/skips, four Gradle tasks executed, exit 0. Retained reports:
[eight decoder cases](truncated-id-green-DiagnosticPingDecoderTest.xml),
[one header case](truncated-id-green-DiagnosticFrameHeaderTest.xml),
[two permission cases](truncated-id-green-ControllerLinkProbeTest.xml),
[ten storage cases](truncated-id-green-DurableCommandProbeTest.xml),
[three pending cases](truncated-id-green-PendingProbeTest.xml), and
[three session cases](truncated-id-green-StreamSessionTest.xml).

This is first-run verification, not a new red/green cycle; no implementation
change or refactor was needed. Next verify rejection of a truncated length header;
clean EOF distinction, other truncation points, acknowledgment decoding,
deadlines, cancellation, authenticated TLS and Android network behavior remain
unimplemented or unverified. No device was queried or changed. The updating
Pixel 8 Pro remains paused pending owner confirmation and post-update build
inspection. M2.4/M2.8 remain In progress and G2 remains open.

## Clean EOF: observed red/green cycle

On 2026-10-30 the owner chose `null` for clean EOF before a new frame at the
isolated `DiagnosticPingDecoder.read(InputStream)` boundary; EOF after a frame
begins remains an `EOFException`. This choice does not select a production API.
The new empty-input test initially failed with `EOFException` after successful
compilation. Retained [red report](clean-eof-red.xml) and
[pre-fix decoder](DiagnosticPingDecoder-before-clean-eof.kt.txt):

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File D:/Playground/LiveStream/tools/m2/run-feasibility.ps1 test --offline --tests livestream.feasibility.DiagnosticPingDecoderTest.cleanEofBeforeHeaderReturnsNull
```

The minimum change checks the first byte and preserves it for the existing header
reader. Successful ping callers now handle the nullable result explicitly. The
fresh full suite with `--rerun-tasks` passed twenty-nine tests, zero failures,
errors or skips, four Gradle tasks executed, exit 0. Retained reports:
[ten decoder cases](clean-eof-green-DiagnosticPingDecoderTest.xml),
[one header case](clean-eof-green-DiagnosticFrameHeaderTest.xml),
[two permission cases](clean-eof-green-ControllerLinkProbeTest.xml),
[ten storage cases](clean-eof-green-DurableCommandProbeTest.xml),
[three pending cases](clean-eof-green-PendingProbeTest.xml), and
[three session cases](clean-eof-green-StreamSessionTest.xml).

Existing truncated-header and truncated-ID tests still pass; they establish only
their sampled truncation points. Review found no necessary refactor. This is one
observed host red/green cycle, not TLS, socket, Android or physical evidence.
No device was queried or changed; the updating Pixel 8 Pro remains paused pending
owner confirmation and post-update inspection. Acknowledgment decoding, deadlines,
cancellation and secure transport remain unresolved; M2.4/M2.8 and G2 remain open.

## Clean EOF after a ping: first-run verification

On 2026-10-30, a host byte stream containing exactly one complete v1 ping was
read twice through `DiagnosticPingDecoder.read(InputStream)`. The first result
preserved all sixteen ID bytes and the second returned `null` at clean EOF.
The [focused report](clean-eof-after-ping-first-run.xml) passed first-run against
unchanged decoder code; this is not another red/green cycle. Command:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File D:/Playground/LiveStream/tools/m2/run-feasibility.ps1 test --offline --tests livestream.feasibility.DiagnosticPingDecoderTest.cleanEofAfterCompletePingReturnsNullOnNextRead
```

A fresh full-suite run with `--rerun-tasks` passed thirty tests, zero failures,
errors or skips, four Gradle tasks executed, exit 0. Retained reports:
[eleven decoder cases](clean-eof-after-ping-green-DiagnosticPingDecoderTest.xml),
[one header case](clean-eof-after-ping-green-DiagnosticFrameHeaderTest.xml),
[two permission cases](clean-eof-after-ping-green-ControllerLinkProbeTest.xml),
[ten storage cases](clean-eof-after-ping-green-DurableCommandProbeTest.xml),
[three pending cases](clean-eof-after-ping-green-PendingProbeTest.xml), and
[three session cases](clean-eof-after-ping-green-StreamSessionTest.xml).

No code change or refactor was necessary. This host fixture does not establish
socket close, asynchronous arrival, TLS or Android behavior. Other truncation
points, acknowledgement decoding, deadlines and cancellation remain unverified.
No device was queried or changed. The updating Pixel 8 Pro remains paused pending
owner confirmation and post-update build inspection; M2.4/M2.8 and G2 remain open.

## Partial next header after ping: first-run verification

On 2026-10-30, a host stream contained a complete v1 ping followed by one byte
of a new header and EOF. The first public decoder call preserved all sixteen ID
bytes; the second threw `EOFException` rather than returning clean-close `null`.
The [focused report](partial-next-header-first-run.xml) passed first-run with
unchanged decoder code, so this is verification rather than a red/green cycle.

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File D:/Playground/LiveStream/tools/m2/run-feasibility.ps1 test --offline --tests livestream.feasibility.DiagnosticPingDecoderTest.partialNextHeaderAfterPingIsTruncationNotCleanEof
```

The fresh full suite with `--rerun-tasks` passed thirty-one tests, zero failures,
errors or skips, four Gradle tasks executed, exit 0. Retained reports:
[twelve decoder cases](partial-next-header-green-DiagnosticPingDecoderTest.xml),
[one header case](partial-next-header-green-DiagnosticFrameHeaderTest.xml),
[two permission cases](partial-next-header-green-ControllerLinkProbeTest.xml),
[ten storage cases](partial-next-header-green-DurableCommandProbeTest.xml),
[three pending cases](partial-next-header-green-PendingProbeTest.xml), and
[three session cases](partial-next-header-green-StreamSessionTest.xml).

No implementation change or refactor was needed. This samples one header
truncation position after one valid frame; it does not establish transport close,
asynchronous socket behavior, TLS or Android connectivity. Acknowledgment
decoding, deadlines, cancellation and other truncation cases remain unverified.
No device was queried or changed; the updating Pixel 8 Pro remains paused until
owner confirmation and post-update inspection. M2.4/M2.8 and G2 remain open.

## Consecutive pings: first-run verification

On 2026-10-30, `DiagnosticPingDecoder.read(InputStream)` read two complete v1
ping frames with different sixteen-byte IDs from one host byte stream, in order,
then returned `null` on the following clean EOF. The
[focused report](consecutive-pings-first-run.xml) passed first-run against
unchanged decoder code. This is verification, not a red/green cycle.

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File D:/Playground/LiveStream/tools/m2/run-feasibility.ps1 test --offline --tests livestream.feasibility.DiagnosticPingDecoderTest.consecutivePingsPreserveTheirDistinctIdsInOrder
```

A fresh full-suite run with `--rerun-tasks` passed thirty-two tests, zero failures,
errors or skips, four Gradle tasks executed, exit 0. Retained reports:
[thirteen decoder cases](consecutive-pings-green-DiagnosticPingDecoderTest.xml),
[one header case](consecutive-pings-green-DiagnosticFrameHeaderTest.xml),
[two permission cases](consecutive-pings-green-ControllerLinkProbeTest.xml),
[ten storage cases](consecutive-pings-green-DurableCommandProbeTest.xml),
[three pending cases](consecutive-pings-green-PendingProbeTest.xml), and
[three session cases](consecutive-pings-green-StreamSessionTest.xml).

No implementation change or refactor was needed. This fixture checks decoder
frame boundaries only; it does not model the required completed exchange between
requests, authorization, actual sockets, TLS or Android behavior. No device was
queried or changed. The updating Pixel 8 Pro remains paused pending owner
confirmation and post-update inspection; M2.4/M2.8 and G2 remain open.