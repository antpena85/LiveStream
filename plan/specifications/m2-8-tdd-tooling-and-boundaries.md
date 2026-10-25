# M2.8 TDD boundaries and local tooling

Recorded: 2026-10-23 (America/New_York)  
Updated: 2026-10-24 (America/New_York)  
Status: All six boundaries confirmed; isolated JVM harness executed; full milestone handoff pending.  
Task: [M2.8](../subplans/m2-8-tdd-tooling-and-boundaries.md)

## Owner confirmation and scope

The owner requested completion of M2, then explicitly confirmed all six existing
T01–T06 boundaries and observable-behavior tests. The owner reported no physical
devices available and directed research/preparation to continue with measured
feasibility pending. The M2 authorization covers the isolated feasibility
harness. It does not satisfy G2 or authorize promoting it into M3–M8 delivery.

| Boundary | Observable interface and suitable fixtures | Does not establish |
| --- | --- | --- |
| T01 | Match commands/results/snapshots and reopen against the real temporary store; independent scoring examples and interrupted commits. Host cases cover file reopen/retry, before-commit point/reset interruption, post-commit lost acknowledgment, shared-store concurrency and reset-generation isolation. | Full match schema, store-replacement identity/migration, Android storage, other crash timings, power loss, independent writers and UI appearance. |
| T02 | Session controls/status/quality requests at the external media boundary; controlled capture readiness, time and network feedback. | Real capture, encoder behavior, thermal endurance or destination reception. |
| T03 | Destination setup and public results; controlled provider consent, scope, expiry and error responses. | Real client eligibility, grants, app review or live ingest. |
| T04 | Authorized protocol requests/results/snapshots; duplicates, revocation, stale revisions and dropped responses. | Actual Bluetooth/LAN reliability or latency. |
| T05 | Overlay input/rendered output; independent asymmetric values, all anchors, large numbers and swaps. | Human/received-video readability by itself. |
| T06 | Visible phone/controller/watch interactions; confirmations, disabled controls and authoritative/stale displays. | Physical touch ergonomics, microphone routing or sustained broadcasts. |

Use real internal components together; replace only provider/hardware/network/
time boundaries. Do not mock the match/store implementation to claim recovery.
Each new behavior must first fail for that missing behavior, then pass with the
minimum change before the next test. Harness/dependency failures are not red
behavioral results. Confirmation persists unless a boundary's scope changes.

## Concrete local tooling

The later [Android phone/tooling observation](../evidence/m2/phone-capabilities-2026-10-24.md)
adds a successful API 37 diagnostic APK build/install/open on a Pixel 8 Pro and
Pixel 10 Pro XL. Its independent build uses AGP 9.2.0, JDK 17, Gradle 9.4.1,
SDK Platform 37.0 revision 2, Build Tools 36.0.0 and Platform Tools 37.0.1 in
the project-local SDK. These update the earlier tooling gap below for this
diagnostic only; the final Android/watch test harness and stack handoff remain
pending. Exploratory capability enumeration is not a new behavioral TDD cycle.

| Tool | Selected local version / result |
| --- | --- |
| JDK | Eclipse Temurin 17.0.20.1+1, installed under `.tools/m2`, successfully invoked. |
| Gradle | 9.4.1, project-local distribution and cache; JVM build executed. |
| Kotlin JVM | 2.4.21, pinned in the probe build. |
| Test runner / assertions | JUnit 4.13.2 / Truth 1.4.4, resolved for the isolated T02 tests. |
| Host SQLite driver | SQLite JDBC 3.53.4.0, real temporary file for the isolated T01 reopen/retry fixture. |
| Android tooling observed | ADB 33.0.3; installed platforms 31/32 and old build tools. No device connected. No API 37 APK build is claimed. |

The bootstrap pins the JDK SHA-256
`e53a79c3c3d86865bd7e787903884331068e71321714ffd44f145785affc7cb0`
and Gradle SHA-256
`2ab2958f2a1e51120c326cad6f385153bb11ee93b3c216c5fccebfdfbb7ec6cb`.
Both archive hashes were verified locally before extraction. JDK metadata came
from [Adoptium's API](https://api.adoptium.net/v3/assets/latest/17/hotspot?architecture=x64&heap_size=normal&image_type=jdk&jvm_impl=hotspot&os=windows&vendor=eclipse);
the Gradle value matches the [publisher checksum list](https://gradle.org/release-checksums/).
No global runtime or execution-policy setting was changed.

## Executed commands and retained evidence

From the project root on this Windows workstation:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File tools/m2/bootstrap-jvm.ps1
powershell -NoProfile -ExecutionPolicy Bypass -File tools/m2/run-feasibility.ps1 test
powershell -NoProfile -ExecutionPolicy Bypass -File tools/m2/run-feasibility.ps1 test --offline
```

The execution-policy override applies to that process. Downloads/initial
dependency resolution required approved network access; later cached runs
worked offline. The first sandbox plugin-resolution failure was infrastructure,
not the behavioral red. Observed missing or incorrect behavior, including the
concurrent transaction failure, counts below; build/harness errors do not.

| Cycle | Boundary / required behavior | Red observed before implementation | Green evidence |
| --- | --- | --- | --- |
| 01 | T02: confirmed Start without camera permission stays stopped and reports the permission requirement. | Expected `CameraPermissionRequired`, got `Accepted`; one failing test. | [Cycle 01 result](../evidence/m2/tdd-cycle-01-green.xml). |
| 02 | T02: unconfirmed Start stays stopped even with camera permission. | Expected `Cancelled`, got `Accepted`; cycle 01 remained green. | [Two passing tests](../evidence/m2/tdd-cycle-02-green.xml), exit code 0; [evidence index](../evidence/m2/README.md). |
| 03 | T01: an accepted command survives file reopen and its retry is not applied twice. | Expected points 8/revision 1 after reopen, got 7/0; both T02 cases remained green. | [Passing SQLite case](../evidence/m2/tdd-cycle-03-green.xml); final suite: three passed, zero failed, exit code 0. |
| 04 | T01/ST02: halt a child JVM before commit; preserve accepted state and allow one retry. | Initial child fixture halted after committing: expected 8/1 after reopen, got 9/2; existing three cases passed. | [ST02 evidence](../evidence/m2/st02-interrupted-write.md) and [green SQLite suite](../evidence/m2/tdd-cycle-04-green.xml); full suite: four passed, zero failed, exit code 0. |
| 05 | T01/ST04: concurrent distinct point commands receive separate durable revisions. | Shared-connection transaction race caused an auto-commit SQL error; existing four cases passed. | [ST04 evidence](../evidence/m2/st04-concurrent-commands.md), [three SQLite tests](../evidence/m2/tdd-cycle-05-green.xml) and [two session tests](../evidence/m2/tdd-cycle-05-green-session.xml); full suite: five passed, zero failed, exit code 0. |
| 06 | T01/ST05: confirmed reset starts a durable new match generation. | Expected 0/0/1, got old 8/1/0 (points/revision/epoch). | [ST05 contract and evidence](../evidence/m2/st05-reset-isolation.md); [five SQLite](../evidence/m2/tdd-cycle-06-green.xml) and [two session tests](../evidence/m2/tdd-cycle-06-green-session.xml) pass; exit 0. |
| 07 | T01/ST05: reject old-epoch commands after reset/reopen. | Old accepted ID returned its receipt instead of a stale-match failure. | [Six SQLite](../evidence/m2/tdd-cycle-07-green.xml) and [two session tests](../evidence/m2/tdd-cycle-07-green-session.xml) pass; exit 0. |
| 08 | T01/ST05: scope command receipts to the match generation. | A new-epoch command with a reused ID returned old 8/1/0 instead of new 1/1/1. | [Seven SQLite](../evidence/m2/tdd-cycle-08-green.xml) and [two session tests](../evidence/m2/tdd-cycle-08-green-session.xml) pass; exit 0. |

[Probe source](../../feasibility/src/main/kotlin/livestream/feasibility/StreamSession.kt)
and [behavioral tests](../../feasibility/src/test/kotlin/livestream/feasibility/StreamSessionTest.kt)
are a bounded session example. They do not implement Android permission
requests, audio readiness, a publisher, providers, controllers or the release
application. The [SQLite fixture](../../feasibility/src/main/kotlin/livestream/feasibility/DurableCommandProbe.kt)
and [its T01 test](../../feasibility/src/test/kotlin/livestream/feasibility/DurableCommandProbeTest.kt)
add real host file/receipt evidence, without selecting the application's
schema, Room implementation or device recovery. Review found no necessary
refactor after these small behaviors. The
[child-process fixture](../../feasibility/src/main/kotlin/livestream/feasibility/InterruptedWriteProcess.kt)
supplies the abrupt before-commit fault through an optional probe checkpoint.
This extends the existing confirmed T01 fault/recovery scope; it selects no
new production API or storage schema. The ST04 change serializes commands on
the one shared probe instance; independent writers and the full controller
protocol remain outside the demonstrated result.

Additional [ST04 duplicate-delivery verification](../evidence/m2/st04-duplicate-commands.md)
passed on its first run: eight calls for one ID and a retry after reopening
all return 8/1 and retain state 8/1. Existing receipt handling and serialization
already supported this case; no implementation changed and that verification
adds no red/green cycle. Its six-test result is a historical checkpoint.

[ST05](../evidence/m2/st05-reset-isolation.md) then adds the three cycles above
at the same confirmed T01 boundary. Two further checks pass on their first runs
with unchanged store logic: canceled reset preserves state/receipts, and abrupt
child termination during reset before commit restores the old generation and
allows a deliberate retry. The child launcher was shared during green review.
[ST03 post-commit interruption](../evidence/m2/st03-lost-acknowledgment.md)
then passes on its first run with unchanged store logic. The child sends no
receipt; reopening/retrying recovers the committed result without applying it
again or replacing newer state. Only the child fault fixture/test launcher
changes; no ninth red/green cycle is claimed.

The current suite passes twelve tests: [ten SQLite](../evidence/m2/st03-lost-acknowledgment-green.xml)
and [two session cases](../evidence/m2/st03-lost-acknowledgment-green-session.xml).
Eight observed red/green cycles and four first-run verifications are retained.
Probe epochs are local to one database; production identity/retention and schema
migration remain unresolved. Future missing behavior still requires red before its fix.

M2.8 remains In progress: local preparation and boundary confirmation are done,
but M2.7's final stack/dependency handoff and Android/watch harnesses are not.
Do not count the local harness as completion of those prerequisites or G2.
