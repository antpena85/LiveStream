# LiveStream plan status dashboard

As of: 2026-10-25 (America/New_York)  
Current phase: M2 feasibility research/preparation and isolated JVM probes.  
Implementation/release: Twelve retained JVM probe tests pass; an API 37 capability APK built/opened on two phones. The release application is not delivered.  
Sources: [Instructions](../AGENTS.md), [PRD](../PRD.md), [master plan](MASTER_PLAN.md),
[53-task index](subplans/README.md), all task status/entry/acceptance/evidence
records, and retained evidence below. [Architecture](../ARCHITECTURE.md) remains proposed.

**7 of 53 tasks complete · 8 in progress · 38 not started · 0 unverified ·
13 aggregate decision records open.** Counts are unweighted tasks, not effort,
implementation percentage or release readiness. All 53 plans exist.

| Milestone | Complete / total | Status | Next unmet condition |
| --- | --- | --- | --- |
| [M0 — Requirements](subplans/README.md#m0-requirements-baseline) | 1 / 1 | Complete | Explicit PRD open details remain separate from approval. |
| [M1 — Planning package](subplans/README.md#m1-planning-package) | 6 / 6 | Complete for documentation | Remaining D10/D11 technical/visual decisions and physical inputs feed later gates. |
| [M2 — Feasibility and stack](subplans/README.md#m2-technical-feasibility-and-selection) | 0 / 8 | All eight In progress | Actual devices/media/accounts/uploads, final decisions and Android/watch toolchain handoff. Preparation is not measured feasibility. |
| [M3 — Match and controls](subplans/README.md#m3-match-state-persistence-and-local-controls) | 0 / 9 | Not started | G2, applicable implementation authorization and task dependencies. |
| [M4 — Capture and publishing](subplans/README.md#m4-capture-overlay-publishing-and-adaptation) | 0 / 7 | Not started | G2, applicable authorization, media evidence and task dependencies. |
| [M5 — Provider sign-in](subplans/README.md#m5-destination-sign-in-and-automatic-setup) | 0 / 4 | Not started | G2, applicable authorization and both provider paths. |
| [M6 — Android remotes](subplans/README.md#m6-second-android-device-and-concurrent-controls) | 0 / 4 | Not started | G2, applicable authorization and local-state/publishing/protocol prerequisites. |
| [M7 — Wear OS](subplans/README.md#m7-wear-os-controller) | 0 / 3 | Not started | G2, applicable authorization and direct-watch/protocol evidence. |
| [M8 — Acceptance and release](subplans/README.md#m8-acceptance-and-personal-release) | 0 / 11 | Not started | G3 for acceptance; all V01–V10 and G4 for personal release. |

Completed baseline/planning evidence:

- [M0.1](subplans/m0-1-approved-requirements-baseline.md): [PRD approval](../PRD.md)
  on 2026-10-17; open details remain open.
- [M1.1](subplans/m1-1-planning-package-review.md): [documentation review](reviews/m1-1-planning-package-review.md)
  passed on 2026-10-18; proposed technologies were not thereby approved.
- [M1.2](subplans/m1-2-device-roles-and-os-inventory.md): [owner inventory](inventories/m1-2-device-roles-and-os-inventory.md)
  records five phones, four paired watches and interchangeable roles. Installed
  builds and physical capabilities remain unknown/untested.
- [M1.3](subplans/m1-3-representative-audio-accessories.md): [audio inventory](inventories/m1-3-representative-audio-accessories.md)
  records built-in-only reported availability and post-v1 accessory cases under Q48;
  no wired/Bluetooth route is validated.
- [M1.4](subplans/m1-4-venue-upload-baseline.md): [network-gap inventory](inventories/m1-4-venue-upload-baseline.md)
  satisfies its explicit-gap path; mobile-data default is confirmed, Wi-Fi
  supported, and no upload/handoff observation exists.
- [M1.5](subplans/m1-5-match-correction-specification.md): [match specification](specifications/m1-5-match-correction-specification.md)
  and follow-ups retain confirmed policies through Q36; revision/identity/
  receipt/retention mechanisms remain open.
- [M1.6](subplans/m1-6-overlay-display-specification.md): [scoreboard specification](specifications/m1-6-overlay-display-specification.md)
  retains Q19–Q21/Q37–Q45, static artwork and review cases. Geometry, numeric
  capacity, durable preference/stop contracts and real readability remain open.

Current M2 work is summarized in the [execution/readiness record](reviews/m2-readiness-and-execution.md):

| Task | Work retained | Required evidence still missing |
| --- | --- | --- |
| [M2.1](subplans/m2-1-physical-capability-inventory.md) | Pixel 8 Pro and Pixel 10 Pro XL Android 17/API 37 build facts and permission-granted camera/codec/audio inventories observed. | Actual media paths and direct-watch observations; other units remain unverified. |
| [M2.2](subplans/m2-2-media-pipeline-feasibility.md) | Three media candidates, integration/license constraints, MP01–MP07. | Encoded/received scoreboard/audio, live switches, cellular routing, bitrate response and sustained load. |
| [M2.3](subplans/m2-3-provider-setup-feasibility.md) | Provider research rechecked; registrations owner-reported to exist. | All requested registration/channel facts unknown; P01–P12 unrun, including actual grants/refresh/ingest. |
| [M2.4](subplans/m2-4-remote-topology-feasibility.md) | LAN/hotspot/watch proposal, trust/permissions/routing cases RT01–RT07. | Physical topology and reachability alongside the uplink. An explicit blocker is not a demonstrated path. |
| [M2.5](subplans/m2-5-lifecycle-and-storage-feasibility.md) | Host SQLite reopen/retry, before/post-commit interruption, shared-store concurrency and reset-generation isolation passed. | Android lifecycle, full match/Undo/reset, other crash timings, store replacement/migration, independent writers, power loss and device recovery. |
| [M2.6](subplans/m2-6-adaptive-quality-policy.md) | Numerical laboratory hypotheses and AQ01–AQ08. | Measured uploads/media response, approved bounds/readable floor and fallback/retry choices. |
| [M2.7](subplans/m2-7-technology-selection.md) | D01–D13 ledger and local-versus-application tool distinction. | Required M2.2–M2.6 evidence and final Android/stack/distribution choices. |
| [M2.8](subplans/m2-8-tdd-tooling-and-boundaries.md) | Confirmed boundaries, JVM tools/evidence and successful API 37 diagnostic APK build/install/open on both phones. | M2.7 final handoff and complete Android/watch/storage-failure test harnesses. |

[TDD evidence](evidence/m2/README.md) retains red/pre-fix/green records for
camera-permission rejection, canceled Start, SQLite accepted-command
reopen/retry, abrupt before-commit child-JVM interruption and concurrent
distinct commands, then [ST05 reset-generation isolation](evidence/m2/st05-reset-isolation.md).
Duplicate delivery, canceled reset and interrupted reset pass on their first runs
with the existing store logic; those checks add no red/green cycles. The current
local suite passes all twelve tests offline: ten SQLite and two session cases.
[ST02](evidence/m2/st02-interrupted-write.md) preserves accepted state after a
real host process halt; [ST04](evidence/m2/st04-concurrent-commands.md) gives
eight distinct commands separate durable revisions through one store instance.
Eight simultaneous deliveries of one command ID return 8/1, retained after
reopening/retry. ST05 proves durable reset, old-command rejection even at matching
revisions and independent receipts per epoch. Canceled reset preserves state;
abrupt interruption before reset commit restores the old state/epoch. The epoch
is file-local; final identity, expiry, store replacement and migration remain open.
[ST03](evidence/m2/st03-lost-acknowledgment.md) then halts a child after commit
before sending its receipt. Reopening/retrying preserves the original receipt,
applies no extra point and retains newer accepted state. It passed on its first
run with unchanged store logic, bringing first-run verifications to four.
Independent writers, other crash timings, power loss, Android Room and physical
recovery remain untested. No actual camera, accessory, watch,
upload, OAuth grant, stream endpoint or received broadcast was exercised.

| Gate | Status | Evidence or action still needed |
| --- | --- | --- |
| G0 — Requirements | Complete | Approved PRD retained; explicit open details remain open. |
| G1 — Authorization | Open for M2 | Q46 authorizes M2 feasibility/probe/harness work. Later delivery milestones need applicable authorization and G2. |
| G2 — Feasible design | Pending | Required M2 demonstrations and dispositions/evidence for D01–D13. Local probes do not pass it. |
| G3 — Integrated coverage | Not reached | M3–M7 behaviors, tests and complete requirement coverage. |
| G4 — Release readiness | Not reached | Passing M8/V01–V10 and supported configurations/distribution. |

[Gate definitions](MASTER_PLAN.md#3-delivery-sequence-and-gates) supplement task
dependencies. Research or runner preparation does not waive M2.7/M2.8 dependencies.
Checked M2 acceptance boxes do not supply the physical/tooling evidence
explicitly missing in those same records; their In progress status remains controlling.

Recommended next work:

Available independently now: specify ST06's durable pending-stop contract and
interruption checkpoints at T01/T02 before an isolated TDD experiment. Q45's
policy is confirmed; final stop/recovery mechanisms remain D10 proposals.
[ST03's specified host check](evidence/m2/st03-lost-acknowledgment.md) and
[ST05's host cases](evidence/m2/st05-reset-isolation.md) pass; M2.5 remains In progress.

1. Follow [M2.1](specifications/m2-1-capability-validation.md) with the Pro/watch
   and Pixel 8 Pro group; then run media, topology and lifecycle cases.
2. Inspect existing provider registrations using [P01 and the dated update](specifications/m2-3-provider-setup-feasibility.md#execution-update-2026-10-23),
   recording nonsecret status only; keep keys/tokens out of evidence.
3. Execute the [quality experiments](specifications/m2-6-adaptive-quality-policy.md),
   replace numerical hypotheses with measured policy, and settle the readable
   output envelope and fallback/retry choices.
4. Use the [decision ledger](specifications/m2-7-stack-and-decision-dispositions.md)
   for final stack/tooling/owner dispositions and G2 review. Documentation for
   remaining D10/D11 technical choices can continue independently.

On 2026-10-24, [ADB and APK observations](evidence/m2/phone-capabilities-2026-10-24.md)
verify two authorized phones and their installed OS/build facts, followed by
successful diagnostic APK deployment. Watch and actual media paths remain
pending. App-visible capability reports are collected on both with camera/
microphone permissions granted; the paired watch is owner-reported available.
Q48 confirms built-in audio
for version 1, with external microphone support/testing deferred until after v1.
Registration settings remain unknown. Built-in audio, camera/watch/network and
provider validation still require real observations. The exact handoff is
retained in the execution record.

All [D01–D13 aggregate records](MASTER_PLAN.md#7-open-decision-register) remain open:

| IDs | Remaining subject |
| --- | --- |
| D01 | Actual builds, equipment, uploads and physical capabilities. |
| D02–D06 | Final Android/UI/SDK, media, storage, transport and provider evidence/choices. Working local Kotlin tools do not settle the application stack. |
| D07–D09 | Measured quality/routing/retry, lifecycle and watch/framework/signing feasibility. Mobile-data default and Q7 direct pairing remain confirmed. |
| D10–D11 | Visual/numeric capacity, preference/stop/recovery, and revision/identity/receipt/retention contracts with required evidence. Q44/Q45 stay confirmed. |
| D12–D13 | Distribution/support coverage and final Android/watch/tooling handoff. Q47 now settles T01–T06 confirmation; TDD remains required. |

Change since the previous 2026-10-23 snapshot: ST03 adds an abrupt post-commit
lost-acknowledgment check. The suite grows from eleven to twelve passing tests;
first-run verifications grow from three to four. The store implementation and
eight observed red/green cycles are unchanged. No task is newly complete;
counts remain 7 complete / 8 in progress / 38 not started, all thirteen aggregate
decisions remain open and G2 has not passed. PRD requirements and application
technology choices are preserved.

[Graphical progression](../.archify/workflow-livestream-progression-20261018-164617/livestream-progression.html)
remains the earlier dated 4/53 snapshot. This Markdown file carries current
counts; it is derived output, not completion evidence. No release date or
hardware compatibility guarantee is committed.
