# M2 simulated publisher: check-to-connect experiment contract

Status: proposed host-only experiment; not an implemented publisher, selected
production API, or evidence of safe broadcasting. It refines the
[ST06 race scenario](m2-st06-pending-stop-contract.md#proposed-check-to-connect-race-experiment--2026-10-30)
at the owner-confirmed T01/T02 behavioral boundaries.

## Observable participant contract

- A T02 start attempt has a fresh, nonsecret fixture session identity. A
  controllable external publisher records each connect and stop request with
  that identity. Identity reuse, persistence, and production authentication
  are not selected by this fixture.
- The fake can announce that a connect request has arrived while withholding
  its effect. A test releases the effect explicitly, without sleeps. Connect
  acceptance, an actual simulated connected effect, and a callback are distinct
  observations. A canceled request is allowed to take effect late.
- A stop request records the exact target session but does not itself establish
  termination. The fixture separately permits confirmed stop, failed/unknown
  stop, and a late connection or callback. No network or credentials are used.
- T01 continues to use the real temporary SQLite probe; test coordination
  replaces only the external publisher. Bounded latches and worker cleanup
  prevent a hung test from masquerading as a behavioral failure.

## First proposed behavior and timeline

1. Commit a point in a fresh store to produce snapshot `8/1/0`. Supply a fit
   assessment for exactly that snapshot and request a confirmed T02 start. Wait
   until the fake publisher records that session's connect request, with the
   simulated connection effect still held.
2. Through T01, commit another point with simulated fit failure for that same
   session. Observe `9/2/0`, its repeatable receipt, and the durable pending-stop
   marker. This is a supplied fit condition, not a rendered readability result.
3. Allow the connection effect and callback despite any prior cancellation. The
   observable invariant is **not Live**, with an explicit `Stopping` or
   equivalent unknown/reconciliation state and a stop request addressed to the
   affected session. The point and marker remain intact until a separately
   verified stop confirmation or other reliable reconciliation. A stop request
   alone must not claim that the provider is stopped.
4. Assert T01/T02 results and the fake publisher's externally recorded effects;
   release any blocked worker in `finally`, use bounded waits and terminate the
   executor. A timeout, inability to construct the harness or compilation error
   is an infrastructure issue, not a behavioral red.

The initial test must fail against a runnable baseline because the required
observable behavior is absent, not because a new interface does not compile.
Record the pre-fix implementation and the actual red before a minimal fix; a
first-run pass is verification, not a red/green cycle. Existing confirmed T01
and T02 cover the intended behavioral scope, but this fixture's precise API is
not a production technology decision.

## Explicit exclusions and follow-ups

This one interleaving does not show that an unsafe frame never leaves a real
device. It does not establish stop success, same-session marker clearing,
thread-safe callbacks, independent writers, writes after connection, Android
process death, fit rendering or provider reception. A later case must cover a
normal revision change without fit failure and explicitly decide whether the
attempt cancels or requires a fresh assessment. Failure/late-callback and
stale-session confirmation behaviors each require their own slice.

The existing `StreamSession` has no external publisher participant and depends
on a JVM-only store; adding one is necessary before this proposed experiment can
actually observe a publisher effect. A lock around `StreamStatus` alone is not a
solution. No network listener, Android publisher, or production store adapter is
authorized by this document, and G2 and M2.1's C06 remain open.

## First controlled interleaving — host result

The isolated `PublisherSessionProbe` now delegates T02 to the existing session
and uses a fake external `ProbePublisher`; the real temporary SQLite store remains
the T01 participant. The test holds a callback in the fake, not a worker thread:
the test itself releases it synchronously after committing the fit failure. This
deterministic ordering uses no sleeps and requires no worker cleanup, but does not
exercise simultaneous threads or an asynchronous transport.

With snapshot `8/1/0`, a connect request was recorded for `session-a`; a second
point committed `9/2/0`, a repeatable receipt and its pending-stop marker before
the fake delivered the connection callback. The existing session reported
`Stopping`, but the runnable [pre-fix adapter](../evidence/m2/publisher-fit-race-red-source.txt)
did not issue a stop: the [red report](../evidence/m2/publisher-fit-race-red.xml)
shows the missing `session-a` request. The minimal change requests stop for the
captured session when the callback leaves the session `Stopping`. The
[green report](../evidence/m2/publisher-fit-race-green.xml) passes and checks that
the new point and marker remain. A forced full offline JVM run passed 71 tests,
zero failures/errors/skips. Report hostnames were redacted.

The fake records a connect request and a released callback, but does not model
an independently observable connection effect, cancellation, stop acknowledgment,
failures or retries. The adapter has no confirmed-stop operation, callback identity
validation, concurrency serialization or Android integration. It must not be
connected to a real publisher or treated as a solution to the full race: safety
state may change between checks, and no claim is made about frames, actual
termination or independent store instances. These remain separate experiments.

## Confirmed stop while connecting — host slice

At T02, an accepted start for `session-a` leaves the fake connection callback
held. A confirmed stop must send a stop request to that session and expose
`Stopping`, not `Stopped`. The [pre-fix adapter](../evidence/m2/publisher-confirmed-stop-red-source.txt)
changed session state but sent no request; the [red result](../evidence/m2/publisher-confirmed-stop-red.xml)
records the missing request. The minimal change retains the accepted session ID
and sends the stop on the transition into `Stopping`. The [green report](../evidence/m2/publisher-confirmed-stop-green.xml)
passes both publisher cases; a forced full offline JVM run passed 72 tests with
zero failures/errors/skips. Report hostnames are redacted.

This is a single-threaded host probe. The test does not deliver a late connection
after this stop, simulate confirmation or failure, establish idempotence, or
validate cancellation. In particular, the prior callback path may issue another
stop request on a later callback; its safety and ordering are unverified. The
probe does not persist active session identity or establish Android, network or
provider behavior. Do not use it for publishing.

## Late callback after confirmed stop — first-run verification

The fake holds a connection callback, then receives a confirmed stop request for
`session-a`. When the test releases the callback afterward, the session remains
`Stopping` and the fake records a second stop request for the same session. The
[targeted report](../evidence/m2/publisher-late-after-stop-first-run.xml) passed
on its first run without an implementation change; this is verification, not a
red/green cycle. A forced full offline JVM run passed 73 tests with zero failures,
errors or skips.

The fake records requests only; it does not simulate independent connection
effects, stop confirmation or failure. Two recorded requests do not establish
that a real publisher can safely process repeated stops or that either succeeded.
Concurrency, callback identity, session replacement, persistence and Android or
provider behavior remain unverified; this probe must not publish.

## Synchronous stop-request failure — first-run verification

For an accepted `session-a` start, the fake records the stop request and then
throws a simulated synchronous failure. The public stop call propagates that
exception; after the caller catches it, the probe remains `Stopping`, rejects a
new `session-b` start as `PendingStop`, and records no second connect request.
The [targeted first run](../evidence/m2/publisher-stop-failure-first-run.xml)
passed without changing implementation, so this is verification, not a red/green
cycle. The forced full offline JVM suite passed 74 tests with zero failures,
errors or skips.

This does not handle or sanitize the exception, surface an operator-facing
notice, establish an asynchronous failure callback, retry or confirm termination.
The existing probe suppresses a further ordinary stop request while already
`Stopping`; recovery after a failed request remains unimplemented. No durable
marker was created by this ordinary-stop scenario. Android, concurrency and real
publisher behavior remain unverified; do not use this probe for publishing.

## Confirmed retry after stop-request failure — host slice

The fake records a first `session-a` stop request and throws. After the caller
catches the exception, it deliberately confirms a retry; the fake accepts the
second request. The expected result is two requests for the same session,
`Stopping`, and a rejected new start with no second connection. The [pre-fix
adapter](../evidence/m2/publisher-stop-retry-red-source.txt) suppressed the retry;
the [red report](../evidence/m2/publisher-stop-retry-red.xml) records one request
instead of two. The minimal change sends a confirmed stop while already stopping.
The [green report](../evidence/m2/publisher-stop-retry-green.xml) passes five
publisher cases; a forced full offline JVM run passed 75 tests with no failures,
errors or skips. Report hostnames were redacted.

The fake only records requests and does not establish publisher idempotence or
termination. Retries are operator-driven in this fixture; no bounded retry policy,
exception sanitization, asynchronous failure, acknowledgment, durable ordinary
stop marker, concurrent callback or Android/provider integration is implemented.
In particular, accepting a call is not proof of stopping. This probe must not be
used to publish.

## Synchronous connect-request failure — host slice

The fake records a connect request for `session-a` and throws before returning;
the connection effect is treated as unknown. The test expects the exception to
propagate, the session to remain `Stopping`, a same-session stop request, and a
new start to be rejected without another connection. The [pre-fix adapter](../evidence/m2/publisher-connect-failure-red-source.txt)
left the session `Connecting`; the [red result](../evidence/m2/publisher-connect-failure-red.xml)
records that mismatch. The minimum change requests a stop and rethrows the
original exception; a synchronous cleanup exception is attached as suppressed.
The [green publisher suite](../evidence/m2/publisher-connect-failure-green.xml)
passes six cases. A forced full offline JVM run passed 76 tests with zero
failures/errors/skips. Report hostnames were redacted.

This slice does not verify the simultaneous connect-and-stop failure path,
asynchronous callback behavior, error sanitization or operator notices. A
synchronous publisher callback can itself throw through `connect`; the fixture
does not distinguish that from a connect-request failure. No real stop or provider
termination is established. Session concurrency, durable ordinary-stop recovery,
Android integration and the publisher trial remain open; do not use this probe to
publish.

## Connect and cleanup failure — first-run verification

The fake throws after recording the connect request and also throws after
recording the cleanup stop request. The caller observes the original simulated
connect exception with the simulated stop exception suppressed; the session stays
`Stopping`, and a new start is rejected without a second connect request. The
[targeted result](../evidence/m2/publisher-connect-cleanup-failure-first-run.xml)
passed on its first run with unchanged implementation: it is verification, not
an observed red/green cycle. A forced full offline JVM run passed 77 tests with
zero failures, errors or skips. The retained exception messages are fictional.

This does not sanitize real errors, confirm a stop, exercise asynchronous or
concurrent callbacks, or establish Android/provider behavior. Propagating raw
real-provider exceptions is not approved for a production interface. Durable
recovery and safe operator-visible diagnostics remain open.