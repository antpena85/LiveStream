# M2 execution and readiness record

Recorded: 2026-10-23 (America/New_York)  
Updated: 2026-10-24 (America/New_York)  
Result: Research/preparation completed across M2; milestone completion pending physical/account evidence.  
Authority: Owner request to resume/complete M2, followed by the answers below.

## Authorization and observed inputs

Latest physical continuation on 2026-10-24: [two authorized phones and their capability reports](../evidence/m2/phone-capabilities-2026-10-24.md)
supersede the earlier unavailable/unconnected state below. The Pixel 8 Pro and
Pixel 10 Pro XL both report Android 17/API 37. A built API 37 diagnostic APK
ran on both and collected camera/codec/audio advertisements with both runtime
permissions granted. Actual capture, encoding and publishing remain `not_run`.
The owner reports a Pixel Watch 4 paired to the Pro XL is available; its
actual build/reachability remains unverified.

Owner update 2026-10-24: devices are available but are not connected to this
workstation. Q48 confirms built-in audio for version 1 and defers external
wired/Bluetooth microphone support and testing until after v1. External
accessory equipment is therefore no longer an M2/v1 gate condition. Built-in
audio, camera, watch, network and provider evidence remains required.

A connection permits installing/running M2 test builds and collecting scoped
app/device observations. USB, wireless debugging or owner-run manual tests are
possible workflows; software preparation can continue without a connection.
No newly connected device, installed build or physical result is claimed here.
The discovery attempt in this continuation could not start the ADB server;
it is not evidence of an empty device list or unsupported hardware.

The owner requested all of M2, explicitly confirmed T01–T06, and then reported:
no physical devices available now; continue research/preparation with measured
feasibility pending. Registrations were reported to exist, followed by
"unknown" for their nonsecret settings. Those answers establish neither a
Public Twitch client nor a configured Google client/channel.

G1 is open for M2 feasibility/harness work. G2 remains pending. This is not
permission to count missing experiments as passed or to begin M3–M8 delivery.
The [TDD agreement](../MASTER_PLAN.md#tdd-working-agreement) applies to probes;
each implementation behavior used one observed failure then the minimum fix.

| Task | Work performed / retained evidence | Completion still requires |
| --- | --- | --- |
| M2.1 | [Two authorized phone inventories](../evidence/m2/phone-capabilities-2026-10-24.md): OS/build, camera/codec/audio advertisements and granted permissions. | Actual lens/encoder/audio paths, watch build/topology and remaining units. |
| M2.2 | [Three candidate paths and seven media experiments](../specifications/m2-2-media-pipeline-feasibility.md), including license and integration gaps. | Demonstrated encoded overlay/audio, live changes, routing, bitrate and sustained load. |
| M2.3 | [Existing workflows plus dated provider update](../specifications/m2-3-provider-setup-feasibility.md); registrations reported, details unknown. | Actual client/channel settings, grants, refresh and ingest evidence; Twitch discrepancy disposition. |
| M2.4 | [Topology/security proposal and seven physical cases](../specifications/m2-4-remote-topology-feasibility.md), including Android 17 LAN restrictions. | Actual Android LAN/hotspot plus cellular routing, direct-watch signing and reachability. |
| M2.5 | [Lifecycle/storage comparison and seven recovery cases](../specifications/m2-5-lifecycle-and-storage-feasibility.md); host reopen/retry, before/post-commit interruption, shared-store concurrency and reset-generation isolation. | Actual OS/lifecycle results, full match/Undo storage, other interruption timings, store replacement/migration, independent writers and Android/power-loss evidence. |
| M2.6 | [Eight quality/network cases and numerical lab hypotheses](../specifications/m2-6-adaptive-quality-policy.md). | Upload/media measurements and chosen operating/fallback/retry parameters. |
| M2.7 | [All thirteen decision dispositions](../specifications/m2-7-stack-and-decision-dispositions.md), with concrete local tools distinguished from proposed application choices. | Feasibility evidence, selected Android graph, required owner choices and G2 review. |
| M2.8 | [Confirmed boundaries and real JVM toolchain](../specifications/m2-8-tdd-tooling-and-boundaries.md); [eight red/green cycles and four first-run verifications](../evidence/m2/README.md). | M2.7 stack handoff plus final Android/watch/Room tooling and required failure evidence. |

All eight tasks remain **In progress**; none is reported complete merely because
its research is written. Dependency gates still apply to task completion and
production work. Preparatory research and the isolated M2.8 JVM runner do not
claim that the missing M2.7 feasibility prerequisites have passed.

## Host evidence and scope

Observed ADB 33.0.3, Android platforms 31/32 and older build tools; the existing
Android Studio Java runtime is 11.0.12. No Java/Gradle/ADB was initially on PATH.
Approved bootstrap installed checksum-verified Temurin 17.0.20.1+1 and Gradle
9.4.1 under `.tools/m2`, without changing global tools. Kotlin 2.4.21, JUnit
4.13.2, Truth 1.4.4 and SQLite JDBC 3.53.4.0 resolved and ran. Three behavioral
tests initially passed offline. ST02 adds a before-commit child-process
interruption and ST04 adds concurrent distinct/duplicate commands through one
store. ST05 adds reset-generation isolation/scoped receipts plus cancellation
and interrupted-reset verification. ST03 adds post-commit interruption with a
lost acknowledgment; the current full suite has twelve passing tests.
Eight observed red/green cycles and four separate first-run checks are
retained. First-run verification does not add invented red/green cycles.
The first sandbox dependency-resolution error was not counted as a TDD red.

No Android APK, emulator, physical device, accessory, upload experiment, OAuth
grant, stream endpoint, or received broadcast was exercised. No account was
created or reconfigured. No release signing secret was generated or retained.
The [JVM probes](../../feasibility/README.md) implement only the documented
readiness/confirmation and points-only file/receipt checks. They must not be
promoted as a complete session controller, match schema, Android storage
implementation or demonstrated media pipeline. The ST02 case supplies one
actual abrupt host-JVM termination before commit. ST04 adds shared-store
distinct-command ordering and simultaneous duplicate/reopen verification.
ST05 adds file-local reset epochs, old-command rejection, independent receipts
across epochs, cancellation and one before-commit reset interruption. ST03
adds one post-commit halt before a receipt is sent; retrying recovers the original
receipt without replacing later state. Other crash
timings, store replacement/migration, power loss, independent writers and Android
storage remain pending.

## Exact handoff to finish M2

1. Connect the Pro/watch and Pixel 8 Pro group when available; fill C01–C06 with
   actual builds/routes, then run candidate MP/RT/lifecycle checks with built-in
   audio. Q48 defers wired/Bluetooth support and checks until after version 1.
2. Inspect the existing Google/Twitch registrations locally. Record only client
   type, enabled APIs, consent/testing state, signing configuration, scope/quota/
   review status and channel eligibility. Keep credentials out of reports.
3. Run provider P01–P12 with the actual configured clients; confirm automatic
   and manual setup independently. Do not infer authorization from registration.
4. Measure separate cellular/Wi-Fi sessions and controlled restrictions; replace
   lab hypotheses with supported D07 values, decide fallback/retry behavior and
   the readable output envelope.
5. Resolve D01–D13, build the selected Android/watch graph and required harnesses,
   then review G2. A local JVM test cannot substitute for any of those results.

The owner has already stated that hardware and registration details are not
available in this session. Additional requests for the same unavailable facts
would not advance these checks. Retain the pending cases for the next equipped
session; the current work neither closes M2 nor invents a completion date.

## Initial validation retained on 2026-10-23

The final offline JVM run returned exit code 0: three tests passed, none failed.
Three observed missing-behavior red/green cycles and pre-fix sources are
retained. JUnit reports have the workstation hostname redacted; result counts,
assertions and timestamps are unchanged. Actual process-kill, Android and
physical media/network/provider evidence remain outside these local results.

`node plan/tools/check-planning-package.cjs` passed with exit code 0 and no
errors: 87 Markdown documents, 1,356 local links, 298 heading targets, 53 task
records, 159 dependency edges, eight diagram pairs and nine locked skills.
Separate reconciliation passed for all nine milestone totals, Q1–Q47,
D01–D13 ledger coverage, all eight M2 In progress records, three red/green
cycles, the current three-test green result and Markdown table structure.
Overall counts are 7 Complete, 8 In progress, 38 Not started and 0 Unverified.

skills-lock.json is unchanged. AGENTS.md intentionally records the later M2
authorization, confirmed boundaries and real local commands while retaining
the product scope and physical-evidence requirements. No application or
hardware completion is inferred from the documentation checker.

## ST02 continuation on 2026-10-23

Completed the next independent local check, [ST02 before-commit interruption](../evidence/m2/st02-interrupted-write.md),
within M2.5/T01. The initial child fixture committed before halting, producing
the observed red result. A probe-only interruption checkpoint now halts it
inside the existing transaction; recovery preserves the prior accepted state
and permits exactly one application of the retried command. The four-test
offline suite passed with exit code 0. This does not identify a defect in
SQLite or select an application storage schema.

Task counts/gates remain unchanged. The next independent local investigation
is ST04's concurrent-command storage behavior. Devices, provider settings,
Android lifecycle, full-match/Undo storage and other required evidence remain
pending as above.

Continuation documentation validation passed with no errors: 88 Markdown
documents, 1,375 local links, 298 heading targets, 53 task records, 159
dependency edges, eight diagram pairs and nine locked skills. Task/milestone
totals, Q1–Q47, D01–D13 coverage, the fourth red/green cycle and the current
four-test green result reconcile. PRD.md and skills-lock.json are unchanged
from the start of this continuation.

## ST04 continuation on 2026-10-23

Completed the distinct-command, single-instance host portion of
[ST04](../evidence/m2/st04-concurrent-commands.md) at the confirmed T01 boundary.
Eight simultaneous commands initially failed with a shared-connection
auto-commit SQL error; the previous four tests passed. Serializing the probe's
command method fixes that race. All commands receive separate committed
revisions and reopening retains points 15/revision 8 from the 7/0 baseline.
The complete offline suite now passes five tests, zero failures/errors.

This does not establish simultaneous duplicate handling, independent writers,
full match/Undo, stale-revision checks, Android lifecycle or physical media.
M2.5 remains In progress, task totals/gates are unchanged, and no application
stack or product policy was selected. The next independent local check is
simultaneous delivery of one command ID. Existing physical/account input gaps
remain recorded; no repeated request for those unavailable inputs is needed.

ST04 documentation validation passed with no errors: 89 Markdown documents,
1,395 local links, 298 heading targets, 53 task records, 159 dependency edges,
eight diagram pairs and nine locked skills. Reconciliation confirms all nine
milestone rows, 7 Complete / 8 In progress / 38 Not started / 0 Unverified,
Q1–Q47 and the thirteen decision records. Retained reports show the new red
failure followed by five passing tests; the implementation change is command
serialization only. PRD.md and skills-lock.json retain their baseline hashes.

## ST04 duplicate-delivery verification on 2026-10-23

The next [T01 host verification](../evidence/m2/st04-duplicate-commands.md)
submits one command ID eight times together. All calls return 8/1, the current
state is 8/1 and reopening/retrying leaves 8/1. It passed on its first run
against the existing implementation. No probe implementation changed and no
sixth red/green cycle is claimed; the current complete suite passes six tests,
zero failures/errors. The unchanged probe matches its pre-check SHA-256.

This completes the shared-instance distinct/duplicate host checks within
ST04, not the full application/controller concurrency work or M2.5. Independent
writers, reset/late commands, full match/Undo and Android/device evidence remain
pending. Next independent local work is ST05's probe identity/reset contract
and late-command isolation. Task states, decisions and gates are unchanged.

Duplicate-check documentation validation passed with no errors: 90 Markdown
documents, 1,412 local links, 298 heading targets, 53 task records, 159 dependency
edges, eight diagram pairs and nine locked skills. Task/milestone totals,
Q1–Q47 and thirteen decision records reconcile; retained reports contain six
passing tests and the retained observed-red cycle count remains five.
PRD.md, skills-lock.json and the store implementation retain baseline hashes.

## ST05 continuation on 2026-10-23

The next [T01 host experiment](../evidence/m2/st05-reset-isolation.md) records
the probe-only contract before execution, then three vertical red/green slices:
durable confirmed reset, rejection of old point commands after reopen, and
receipts scoped to epoch plus command ID. A new match stays isolated even when
its revision matches the old match's revision. Retained red results show the
specific wrong snapshots or missing stale-match failure; earlier tests pass.

Two additional verifications pass on their first runs with unchanged store logic:
canceled reset preserves state/receipts, and a child JVM halted during reset
before commit restores the old 8/1/0 snapshot and allows a deliberate reset retry.
The child launcher was extracted during green review. Final offline run: eleven
tests passed, zero failures/errors/skips, exit 0; nine SQLite and two session cases.
There are eight observed red/green cycles and three first-run verifications total.

The epoch is local to the probe database, not a final application identity
scheme. Full match/Undo/reset contents, duplicate reset acknowledgment, receipt
expiry, file replacement/restore, migration, other crash timings, independent
writers and Android/device results remain pending. M2.5/M2.8 stay In progress;
task totals, D01–D13 and all gates are unchanged. Recommended next host work is
ST03's explicit post-commit interruption before acknowledgment, extending the
existing partial reopen/retry evidence. Physical/account input gaps remain as
already reported by the owner.

ST05 documentation validation passed with no errors: 91 Markdown documents,
1,472 local links, 298 heading targets, 53 task records, 159 dependency edges,
eight diagram pairs and nine locked skills. Separate reconciliation confirms
all nine milestone rows, 7 Complete / 8 In progress / 38 Not started /
0 Unverified, thirteen decision records, eight retained observed-red cycles
and the current eleven-test green result. Each new red report has one assertion
failure and no harness error; each corresponding green report has zero failures.
The store hash remains unchanged through the two additional verifications;
PRD.md, AGENTS.md and skills-lock.json retain their pre-continuation hashes.

## ST03 verification on 2026-10-24

Completed the next retained handoff, [post-commit lost acknowledgment](../evidence/m2/st03-lost-acknowledgment.md),
at the confirmed T01 boundary. A separate JVM commits a point, then halts before
sending its receipt or closing the store. Reopening preserves 9/2/0; retries
recover that receipt without applying another point or replacing later 10/3/0
state, including after a second reopen. All twelve offline tests pass: ten
SQLite and two session cases, zero failures/errors/skips, exit 0.

This passed on its first run with the unchanged store. Only the child fault
fixture and public test/launcher changed. Eight observed red/green cycles and
four first-run verifications are retained. The probe's identity, receipt expiry,
full match/Undo, migration, independent writers, ST06 Q45 shutdown and physical
Android/media/provider/network evidence remain pending. M2.5/M2.8, task totals,
D01–D13 and gates do not change.

Recommended next independent host work is to specify ST06's durable pending-stop
contract and interruption checkpoints at T01/T02 before an isolated TDD
experiment. The Q45 policy is confirmed; final stop/recovery mechanisms remain
D10 proposals. Hardware and account inputs retain the owner's existing
unavailable/unknown answers.

Documentation validation passed with no errors: 92 Markdown documents,
1,488 local links, 298 heading targets, 53 task records, 159 dependency edges,
eight diagram pairs and nine locked skills. Separate reconciliation confirms
all nine milestone totals, 7 Complete / 8 In progress / 38 Not started /
0 Unverified, thirteen open aggregate decisions, eight observed-red reports
and twelve passing tests in the current retained reports. PRD.md, AGENTS.md,
skills-lock.json and the store implementation retain their baseline SHA-256
hashes. No application dependency, hardware result or gate completion is added.
