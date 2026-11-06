# LiveStream plan status dashboard

As of: 2026-11-05 (America/New_York)
Current phase: M2 feasibility, isolated probes, and an authorized accelerated prototype.
Implementation/release: Local media and watch diagnostics have been exercised; the publisher safety seam compiles for Android but is not integrated. No received broadcast or release application is delivered.
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
  builds were unknown in that inventory; subsequent device observations are linked below.
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

Current M2 work and limitations (later evidence supersedes older snapshots):

| Task | Retained evidence | Remaining condition |
| --- | --- | --- |
| [M2.1](subplans/m2-1-physical-capability-inventory.md) | [Capability audit and separate current Pixel 8 Pro inventories](evidence/m2/m2-1-capability-audit-2026-11-02.md). | Actual selected lens paths, active audio route, and simultaneous controller/publisher checks. |
| [M2.2](subplans/m2-2-media-pipeline-feasibility.md) | [Local media](evidence/m2/media-owner-review-2026-10-24.md), [transitions](evidence/m2/media-transitions-2026-10-24.md), and [owner-reviewed local score controls](evidence/m2/local-score-controls-2026-11-01.md). | Received output, sync, bitrate response, sustained load, and safe Android publishing. |
| [M2.3](subplans/m2-3-provider-setup-feasibility.md) | Provider research and owner-reported registrations. | Configuration, authorization, refresh and ingest evidence. |
| [M2.4](subplans/m2-4-remote-topology-feasibility.md) | [Bidirectional foreground watch diagnostics and a recovery observation](evidence/m2/watch-connectivity/README.md). | Full disconnect/recovery semantics, authenticated controller session, and uplink coexistence. |
| [M2.5](subplans/m2-5-lifecycle-and-storage-feasibility.md) | Host storage interruption and [ST06 pending-stop experiments](specifications/m2-st06-pending-stop-contract.md). | Android durable safety, lifecycle, independent writers, migration and remaining failure cases. |
| [M2.6](subplans/m2-6-adaptive-quality-policy.md) | Proposed quality experiments. | Measured operating policy, readable floor and recovery bounds. |
| [M2.7](subplans/m2-7-technology-selection.md) | Proposed decision ledger. | Feasibility evidence and final dispositions. |
| [M2.8](subplans/m2-8-tdd-tooling-and-boundaries.md) | Confirmed T01–T06 and host tooling; diagnostic Android builds. | Final Android/watch/real-store test tooling and M2.7 handoff. |

The [November 5 safety-seam record](evidence/m2/session-safety-seam-2026-11-05.md)
records a forced passing JVM suite and Android compilation, not Android execution.
The host SQLite fixture and separate safety reads are not a production authorization
contract. The [publisher trial preflight](evidence/m2/publisher-trial-preflight-2026-11-05.md)
records remaining trial blockers. Local recordings and diagnostic watch messages
are distinct from a received broadcast or a full controller session.

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

1. At confirmed T01/T02, continue the [consistent safety-read experiment](specifications/m2-consistent-safety-read.md).
   Two narrow T01 rollback-read slices are red/green; a synchronous in-flight
   Start/callback and rollback-callback verifications passed on their first runs.
   An explicitly delivered post-Live fit-failure notification has a narrow
   host red/green result; automatic delivery and recovery remain open. Next test
   notification failures and design the combined
   committed snapshot/marker view and T02 interleaving without treating scheduling
   timeouts as behavioral failures. Do not substitute a permissive fit or
   pending-stop stub. A consistent read alone cannot make publisher start atomic
   with later state changes.
2. Follow the [accelerated prototype gate](specifications/accelerated-prototype.md):
   implement and validate actual Android publisher control, fit safety, cleanup,
   and credential handling before attempting a received-stream trial.
3. For device-dependent work, recheck ADB authorization; identify the intended
   devices privately. Before a trial the operator must choose a destination and
   duration and initiate it; separately inspect received video, score and audio.
4. Continue [provider](subplans/m2-3-provider-setup-feasibility.md),
   [topology](subplans/m2-4-remote-topology-feasibility.md), and
   [quality](subplans/m2-6-adaptive-quality-policy.md) evidence, then review the
   [decision ledger](specifications/m2-7-stack-and-decision-dispositions.md).
   Q7 direct watch pairing and built-in-only v1 audio remain confirmed.

All [D01–D13 aggregate records](MASTER_PLAN.md#7-open-decision-register) remain open:

| IDs | Remaining subject |
| --- | --- |
| D01 | Actual builds, equipment, uploads and physical capabilities. |
| D02–D06 | Final Android/UI/SDK, media, storage, transport and provider evidence/choices. Working local Kotlin tools do not settle the application stack. |
| D07–D09 | Measured quality/routing/retry, lifecycle and watch/framework/signing feasibility. Mobile-data default and Q7 direct pairing remain confirmed. |
| D10–D11 | Visual/numeric capacity, preference/stop/recovery, and revision/identity/receipt/retention contracts with required evidence. Q44/Q45 stay confirmed. |
| D12–D13 | Distribution/support coverage and final Android/watch/tooling handoff. Q47 now settles T01–T06 confirmation; TDD remains required. |

Since the 2026-10-25 snapshot, ST06 and publisher simulations, local media,
watch diagnostics, and Android source/build compatibility have additional retained
evidence linked above. The former instruction to specify ST06 is superseded; it
is not the next action. Task counts and the G2 disposition have not changed.

[Graphical progression](../.archify/workflow-livestream-progression-20261018-164617/livestream-progression.html)
remains the earlier dated 4/53 snapshot. This Markdown file carries current
counts; it is derived output, not completion evidence. No release date or
hardware compatibility guarantee is committed.
