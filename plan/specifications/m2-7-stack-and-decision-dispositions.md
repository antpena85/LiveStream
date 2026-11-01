# M2.7 stack comparison and decision dispositions

Recorded: 2026-10-23 (America/New_York)  
Status: Consolidation prepared; final application stack/G2 approval pending evidence.  
Task: [M2.7](../subplans/m2-7-technology-selection.md)

## Selected local tools and proposed application direction

The isolated JVM probe uses Temurin 17.0.20.1+1, Gradle 9.4.1, Kotlin 2.4.21,
JUnit 4.13.2, Truth 1.4.4 and SQLite JDBC 3.53.4.0. These are concrete local tool choices, not an
approval of the Android media/UI/storage stack. Download checksums and test
results belong to [M2.8](m2-8-tdd-tooling-and-boundaries.md).
Kotlin's [compatibility table](https://kotlinlang.org/docs/gradle-configure-project.html)
includes this JVM compiler/Gradle combination.

Recommend native Kotlin/Compose on phones, Wear Compose Material3 for the watch,
one integrated streaming pipeline, transactional local storage and authenticated
LAN control. Compare Java/Views for phone UI only if a specific integration
requires it; a native preview surface can be embedded without replacing the
entire UI. Recommend separate phone and watch artifacts with matching application
identity/signature. Minimum SDKs and final libraries remain proposals pending
actual OS inventory and the dependency graph.

For a future Android build, API 37 and an appropriate AGP/JDK/Gradle combination
need installation/validation. The installed old SDK platforms 31/32 do not
establish that readiness. AGP 9.2's published matrix includes JDK 17 and Gradle
9.4.1; AGP 9.3 instead requires Gradle 9.5.0. Do not combine those entries.
[AGP 9.2](https://developer.android.com/build/releases/agp-9-2-0-release-notes),
[AGP 9.3](https://developer.android.com/build/releases/agp-9-3-0-release-notes).
AGP 9 has built-in Kotlin: the eventual Android plugin/compiler/Compose setup
must be resolved together rather than copying the JVM plugin into Android
modules. [Built-in Kotlin](https://developer.android.com/build/migrate-to-built-in-kotlin).

The current Wear guide shows Material3 1.7.1, while the older dedicated M3 release
page still lists 1.5.0. Resolve exact stable artifacts from official Maven
metadata/AAR requirements before creating the watch build; do not infer the
selected version from an installed skill or an older page.
[Current guide](https://developer.android.com/training/wearables/compose),
[release page](https://developer.android.com/jetpack/androidx/releases/wear-compose-m3),
[Maven metadata](https://dl.google.com/dl/android/maven2/androidx/wear/compose/compose-material3/maven-metadata.xml).

## D01–D13 disposition ledger

All aggregate records remain open. Each row identifies the available evidence
and the specific unresolved work; a blocker disposition does not pass G2.

| ID | Evidence / proposed direction | Remaining condition |
| --- | --- | --- |
| D01 | [Capability matrix and C01–C06](m2-1-capability-validation.md); [phone capability observations](../evidence/m2/phone-capabilities-2026-10-24.md) and [subsequent device/build observations](../evidence/m2/android-controller-preflight-2026-10-29.md) are retained. | Complete the device/build-specific usable lens, encoder and built-in microphone matrix, direct watch pairing, and upload observations; external microphones are deferred until after v1 under Q48. |
| D02 | Local Kotlin JVM compiler runs; native phone Compose proposed. | Select/resolve Android/UI/minimum SDK graph and confirm it against device OS inventory. |
| D03 | [Three media candidates and MP01–MP07](m2-2-media-pipeline-feasibility.md). | Demonstrate encoded overlay/audio, switches, cellular routing, bitrate changes and sustained load; review resolved licenses. |
| D04 | [SQLite/Room versus atomic document](m2-5-lifecycle-and-storage-feasibility.md); host reopen/retry, before/post-commit interruption, shared-store concurrency and reset-generation isolation passed. | Select application storage; demonstrate full match/Undo/receipt recovery, remaining failure timings, migration and concurrency on the required platform. |
| D05 | [LAN/hotspot alternatives and RT01–RT07](m2-4-remote-topology-feasibility.md). | Physical topology, LAN permissions, trust bootstrap and simultaneous cellular uplink evidence. |
| D06 | [Provider workflows and owner-reported registration details](m2-3-provider-setup-feasibility.md#owner-reported-registration-details--2026-11-01): YouTube Data API v3 enabled, Google Android OAuth client, External/Testing audience and intended account listed as a test user; Twitch public client. | Verify scopes, signing and current channel eligibility; resolve Twitch discrepancies and run actual grants/renewals. |
| D07 | [AQ01–AQ08 and experimental numbers](m2-6-adaptive-quality-policy.md). | Measured operating bounds, readable floor, routing/fallback and retry/handoff decisions. |
| D08 | [Lifecycle table and ST01–ST07](m2-5-lifecycle-and-storage-feasibility.md); isolated readiness probe only. | Physical background/lock/call/permission/thermal outcomes by OS/build. |
| D09 | Q7 retained; Wear M3 proposed and direct Data Layer constraints reviewed. | Exact artifacts/SDKs, matching signing identity and direct-watch physical evidence. |
| D10 | Q19–Q21/Q37–Q45 confirmed; [numeric-fit cases](d10-numeric-fit-and-overflow-policy-review.md). | Safe bounds/coverage/type, numeric capacity, durable stop/recovery and encoded/received readability. |
| D11 | Confirmed [match policies](m1-5-match-correction-specification.md); atomic protocol proposal; [file-local epoch experiment](../evidence/m2/st05-reset-isolation.md) passed. | Final revision/reset/store-replacement identity, retention/expiry, application receipts and Q45 external-effect recovery. |
| D12 | PRD Q49 confirms private installation of signed phone/watch APKs on the owner's devices; one stable signing plan remains proposed. | Resolve signing and updates, provider registration/signing compatibility and validated support matrix. The distribution preference alone does not close D12. |
| D13 | T01–T06 owner-confirmed; local JVM runner and T01/T02 cycles retained. | Final Android/watch/instrumentation graph and remaining real-store/device failure coverage. Boundary confirmation itself is settled. |

Before finalizing the personal distribution implementation, review the chosen
platform's current installation/signing requirements. No store submission, account change, APK
installation, release key generation or publication occurred. The proposal uses
the [Android signing model](https://developer.android.com/studio/publish/app-signing)
without claiming that personal installation bypasses platform requirements.

M2.7 remains In progress because M2.2–M2.6 lack required demonstrated evidence.
The ledger completes consolidation/preparation only; it selects no product
scope reduction and does not open G2.

## Connection check for decision work — 2026-10-31

A read-only `adb devices -l` check showed one authorized Pixel 8 Pro and one
authorized Pixel Watch 4. The Pixel 10 Pro XL used in the retained direct-watch
experiment was not listed. Device identifiers and connection addresses were not
retained here. This is a momentary ADB visibility check, not evidence that the
visible watch is paired with the visible phone, that either device can run the
required scenario, or that any D01–D13 decision is complete. Recheck the
required devices and their roles before each dependent experiment.

After the owner connected the requested phone, a second read-only ADB check
listed the Pixel 8 Pro, Pixel 10 Pro XL and Pixel Watch 4 as authorized. The
Pro XL reported build `CP3A.261005.005` and the watch reported
`CP3A.260905.002`; the watch diagnostic package was present on both. Package
presence and ADB authorization do not verify direct pairing, matching installed
signatures, current listener readiness, or any new round trip. Prior retained
round trips and their limitations are documented in the
[watch execution record](../evidence/m2/watch-connectivity/README.md).
