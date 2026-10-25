# LiveStream individual task plans

Created: 2026-10-18  
Authority: [Approved PRD](../../PRD.md)  
Delivery gates, TDD agreement, and decisions: [Master plan](../MASTER_PLAN.md)  
Design: [Architecture](../../ARCHITECTURE.md)  
Skill discovery: [Reviewed skills](../SKILL_DISCOVERY.md)

The owner confirmed one detailed plan per independently verifiable task in the
Grill-me interview. These 53 plans decompose the existing master plan; they do
not approve technology choices, change release scope, or authorize implementation.
Task order/dependencies and unresolved technical approaches remain proposed.
M0.1 records an already completed baseline. M1.1's documentation review is
complete as of 2026-10-18; its [review record](../reviews/m1-1-planning-package-review.md)
contains findings and validation evidence. M1.2 is complete as of 2026-10-18: its
[dated inventory](../inventories/m1-2-device-roles-and-os-inventory.md) records
five phones, four watch pairings, the main Pro/watch and Pixel 8 Pro test group,
interchangeable phone roles, stable OS expectation, and
explicitly unknown installed software/builds. M1.3 is complete as of 2026-10-18:
its [microphone inventory](../inventories/m1-3-representative-audio-accessories.md)
records built-in-only current availability and pending accessory cases, with
physical checks assigned to M2.1/M2.2 and V04. M1.4 is complete as of 2026-10-18:
its [upload inventory](../inventories/m1-4-venue-upload-baseline.md) records
explicit Wi-Fi/mobile measurement gaps under the checklist's permitted gap
path and the owner's mobile-data default for changing venues. Wi-Fi remains
supported; no uploads were measured. M1.5 is complete as of 2026-10-18: its
[worked specification](../specifications/m1-5-match-correction-specification.md)
retains 36 original examples and three initial owner decisions. The 2026-10-20
follow-up adds twelve supplemental cases and four decisions under Q22–Q25;
the 2026-10-21 follow-up adds twelve Undo/watch cases and Q26/Q27, C01–C07 and
Q28 for completed-match entry, K01–K09 and Q29 for cap input, and H01–H06 and
Q30 for conditional reopening after a valid historical correction. Remaining D11
choices stay open. The [baseline/history review](../specifications/d11-baseline-history-policy-review.md)
records Q31/Q32 and B01–B12 for complete matching-history replacement without
double counting and valid reviewed baseline corrections; task states are unchanged.
The [new-set serve/reset review](../specifications/d11-new-set-serve-and-reset-policy-review.md)
records Q33–Q35 and S01–S14: explicit first-server selection, retained reset
fields, and confirmed reset on phones/tablets only. Task completion is unchanged.
The [cap-continuation permissions review](../specifications/d11-cap-continuation-permissions-review.md)
records Q36 and CP01–CP10: permitted phones/tablets confirm live continuation
and Undo its latest confirmation; the watch directs both actions to a phone
without skipping the restricted edit. Task completion remains unchanged.
M1.6 is complete as of 2026-10-18: its
[scoreboard specification](../specifications/m1-6-overlay-display-specification.md)
includes 16 display cases, static artwork, and three original owner display decisions.
The [placement/control review](../specifications/d10-placement-control-policy-review.md)
adds three confirmed choices under Q37–Q39 and PL01–PL10: top-left without a
saved preference, per-streaming-phone retention across matches/restarts/reset,
and phone/tablet-only changes before/during a live stream. Q40 and PU01–PU08
then exclude placement from match Undo, preserving its opportunity and the
current position during match restoration. Technical preference contracts
remain open; task completion is unchanged.
The [abbreviation-validation review](../specifications/d10-abbreviation-validation-policy-review.md)
records Q41/Q42 and AV01–AV11: uppercase A–Z/digit codes with reviewed
trimming/uppercase conversion, and matching codes allowed after warning and
explicit confirmation. Stable identities remain separate; no application
behavior or additional task completion is implied.
Q43 and RC01–RC08 in that review settle saved-code recovery: preserve original
nonconforming codes and match data, keep scoring/corrections available, and
require phone/tablet correction before any broadcast-start control succeeds.
Recovery mechanisms remain open; task states and gates are unchanged.
The [2026-10-23 numeric-fit review](../specifications/d10-numeric-fit-and-overflow-policy-review.md)
records Q44/Q45 and N01–N14: adaptive layout with full numbers and readable
type; if numbers still cannot fit, stop broadcasting with notice, preserve the
full new match, and continue scoring off-air. Numeric capacity and durable stop
mechanisms remain open; task states and gates do not change.
All six M1 documentation tasks are complete. All eight M2 tasks are In progress
as of 2026-10-23: [execution/readiness evidence](../reviews/m2-readiness-and-execution.md)
links their research/preparation and the isolated JVM harness. Q46 opens G1 for
M2 and Q47 confirms T01–T06. Two T02 behaviors and one T01 real-SQLite file
reopen/retry behavior passed after their observed red results. A later ST02
child-JVM interruption before commit and the ST04 shared-store distinct-command
case also passed. Simultaneous duplicate delivery then passed on its first run
with the existing implementation. The [ST05 reset-generation experiment](../evidence/m2/st05-reset-isolation.md)
then adds durable reset, old-command rejection and scoped receipts through three
further red/green slices, plus first-run cancellation and interrupted-reset checks.
[ST03 post-commit interruption](../evidence/m2/st03-lost-acknowledgment.md)
passes on its first run on 2026-10-24, recovering the original receipt after a
lost acknowledgment without applying the point again or replacing newer state.
The local suite now has twelve passing tests, with eight observed red/green
cycles and four first-run verifications. Physical devices are unavailable;
provider settings remain unknown.
No M2 completion dependency, aggregate decision or G2 is passed from these
preparations. M3–M8 delivery remains Not started. Overall counts: 7 Complete,
8 In progress, 38 Not started, 0 Unverified; total 53.

## Working agreement

1. Use the PRD as product authority. Keep D01–D13 unresolved until actual
   evidence and required owner answers are recorded. T01–T06 are confirmed by
   the owner's 2026-10-23 answer under Q47; changes need renewed confirmation.
2. Planning research, inventories, and written specifications can proceed now.
   G1 is required before application code, probes/prototypes, scaffolding,
   application dependencies, or executable tests. M2 may collect unknown
   hardware facts experimentally after G1; repeatable application behavior
   still follows TDD at a confirmed boundary. M2.8 records the eventual tools
   and confirmations; it does not waive earlier boundary confirmation.
3. G2 requires the master plan's M2 feasibility and decision evidence. A task's
   listed dependencies supplement that gate; they never bypass it. G3 requires
   integrated M3–M7 coverage, including both sign-in providers and the watch.
   G4 requires all V01–V10 acceptance evidence.
4. For implementation, choose one observable behavior within a task. Confirm
   its boundary, observe one failing behavioral test, implement the minimum,
   then review/refactor with the relevant suite green. Finish that slice before
   the next; a task plan is not permission to batch all tests or implementation.
5. Retain requirement/behavior references, actual red/green results, and physical
   evidence separately. Use real components and a real temporary store for
   persistence; substitute only external boundaries. Hardware support requires
   physical evidence, and received-overlay checks require real destination video.
6. Record completion only with the task's actual acceptance evidence. Update
   affected dependencies, decisions, and mapping together. Do not invent build
   commands, deadlines, coverage percentages, or compatibility guarantees.

The dependency lists below form an acyclic task graph. Tasks within an open
milestone may proceed when their real prerequisites are met; numeric order
is for navigation and does not impose unrelated blockers. Dependencies on G1–G4,
owner answers, actual accounts/devices, and public-boundary confirmation are
entry conditions even where they are not separate task IDs.

## M0: Requirements baseline

| Task plan | Blocked by | Requirements |
| --- | --- | --- |
| [M0.1 — Retain the approved requirements baseline](m0-1-approved-requirements-baseline.md) | None | FR-01–FR-06 |

## M1: Planning package

| Task plan | Blocked by | Requirements |
| --- | --- | --- |
| [M1.1 — Review the planning package and task backlog](m1-1-planning-package-review.md) | [M0.1](m0-1-approved-requirements-baseline.md) | FR-01–FR-06 |
| [M1.2 — Record device roles and installed operating systems](m1-2-device-roles-and-os-inventory.md) | [M0.1](m0-1-approved-requirements-baseline.md) | PRD 5.4; FR-02; FR-06 |
| [M1.3 — Choose representative microphone validation inputs](m1-3-representative-audio-accessories.md) | [M0.1](m0-1-approved-requirements-baseline.md) | FR-03 |
| [M1.4 — Collect venue Wi-Fi and mobile upload measurements](m1-4-venue-upload-baseline.md) | [M1.2](m1-2-device-roles-and-os-inventory.md) | FR-01; PRD 5.4 |
| [M1.5 — Specify correction, cap, and undo decisions](m1-5-match-correction-specification.md) | [M0.1](m0-1-approved-requirements-baseline.md) | FR-05; PRD 2.3 |
| [M1.6 — Specify abbreviations, layout, and readability](m1-6-overlay-display-specification.md) | [M0.1](m0-1-approved-requirements-baseline.md) | FR-04 |

M1.1 is complete for the documentation review only; G1–G4 remain unmet.
See its [completed acceptance checklist](m1-1-planning-package-review.md#acceptance-criteria).
M1.2's documentation/observation work is complete. Its dependency is satisfied
for M1.4 and M2.1; their other prerequisites and actual evidence requirements
still apply. No device compatibility has been validated by the inventory.

M1.3's documentation work is complete with the owner's built-in-only availability
answer and explicitly pending USB, other wired, receiver, and Bluetooth cases.
Its prerequisite for M2.1 is satisfied; M2.1 still requires its other entry
conditions and actual equipment for each check. Q48 defers external microphone support/testing until after v1;
D01 remains open for built-in audio and other required physical evidence.

M1.4's documentation work is complete with N01/N02 evidence gaps, proposed
collection fields, and the M2.6 handoff. Its documentation prerequisite for
M2.6 is satisfied; M2.2, entry gates, and measured-policy evidence are still
required. D01/D07 remain open, and the stable validation baseline is 720p/30 fps.

M1.5's documentation work is complete with explicit correction, cap, starting
value, Undo/reset, and protocol examples. Point removal preserving serve,
rejection of conflicting historical corrections, and one-step Undo across
restart are owner-confirmed. Its documentation prerequisite for M3.1/M3.6
is satisfied. Q22–Q25 add margin precedence at cap conflicts, totals-only
starting baselines, a rally pause at a potential winner, and confirmed
current-set continuation beyond the cap on 2026-10-20. Cap-input validation
and remaining D11 policies, G1/G2, and public-boundary confirmation
still apply. No application or behavioral-test result is implied.

M1.6's written specification and static illustration are complete. Configurable
placement presets, 1–5-character validated abbreviations, and numeric `T/O`
counts are owner-confirmed. Its documentation prerequisite for M3.9 is satisfied;
remaining D10 details, other task dependencies, G1/G2, and boundary confirmation
still apply. Readability in application, encoded, and received output is untested.

## M2: Technical feasibility and selection

| Task plan | Blocked by | Requirements |
| --- | --- | --- |
| [M2.1 — Inventory exposed physical-device capabilities](m2-1-physical-capability-inventory.md) | [M1.2](m1-2-device-roles-and-os-inventory.md), [M1.3](m1-3-representative-audio-accessories.md) | FR-02; FR-03; FR-06 |
| [M2.2 — Prove the proposed media path on devices](m2-2-media-pipeline-feasibility.md) | [M2.1](m2-1-physical-capability-inventory.md) | FR-01–FR-04; PRD 4.2 |
| [M2.3 — Verify both provider integration paths](m2-3-provider-setup-feasibility.md) | [M1.1](m1-1-planning-package-review.md) | FR-01; PRD 4.3 |
| [M2.4 — Verify Android and direct-watch connectivity](m2-4-remote-topology-feasibility.md) | [M2.1](m2-1-physical-capability-inventory.md) | FR-06; PRD 2.4 |
| [M2.5 — Define lifecycle and crash-recovery feasibility](m2-5-lifecycle-and-storage-feasibility.md) | [M2.1](m2-1-physical-capability-inventory.md) | PRD 4.2; PRD 4.3; FR-05 |
| [M2.6 — Set measurable adaptation and reconnection policy](m2-6-adaptive-quality-policy.md) | [M1.4](m1-4-venue-upload-baseline.md), [M2.2](m2-2-media-pipeline-feasibility.md) | FR-01; PRD 4.2 |
| [M2.7 — Record evidence-backed stack and distribution choices](m2-7-technology-selection.md) | [M2.2](m2-2-media-pipeline-feasibility.md), [M2.3](m2-3-provider-setup-feasibility.md), [M2.4](m2-4-remote-topology-feasibility.md), [M2.5](m2-5-lifecycle-and-storage-feasibility.md), [M2.6](m2-6-adaptive-quality-policy.md) | FR-01–FR-06; PRD 4 |
| [M2.8 — Confirm test boundaries and prepare TDD tooling](m2-8-tdd-tooling-and-boundaries.md) | [M2.7](m2-7-technology-selection.md) | FR-01–FR-06 |

## M3: Match state, persistence, and local controls

| Task plan | Blocked by | Requirements |
| --- | --- | --- |
| [M3.1 — Create a match from an indoor preset](m3-1-match-setup-and-presets.md) | [M2.7](m2-7-technology-selection.md), [M2.8](m2-8-tdd-tooling-and-boundaries.md), [M1.5](m1-5-match-correction-specification.md) | FR-04; FR-05; PRD 2.3 |
| [M3.2 — Score rallies and correct points or serve](m3-2-rally-points-and-serve.md) | [M3.1](m3-1-match-setup-and-presets.md) | FR-05; PRD 2.3 |
| [M3.3 — Record and correct team timeouts](m3-3-timeout-counts-and-corrections.md) | [M3.1](m3-1-match-setup-and-presets.md) | FR-04; FR-05; PRD 2.3 |
| [M3.4 — Confirm set results and complete the match](m3-4-set-and-match-confirmation.md) | [M3.2](m3-2-rally-points-and-serve.md), [M3.3](m3-3-timeout-counts-and-corrections.md) | FR-05; PRD 2.3 |
| [M3.5 — Undo an accepted edit and confirm match reset](m3-5-undo-and-confirmed-reset.md) | [M3.2](m3-2-rally-points-and-serve.md), [M3.3](m3-3-timeout-counts-and-corrections.md), [M3.4](m3-4-set-and-match-confirmation.md) | FR-05 |
| [M3.6 — Correct completed sets consistently](m3-6-historical-set-corrections.md) | [M3.4](m3-4-set-and-match-confirmation.md), [M1.5](m1-5-match-correction-specification.md) | FR-05 |
| [M3.7 — Persist accepted edits and restore after restart](m3-7-atomic-persistence-and-restart.md) | [M3.2](m3-2-rally-points-and-serve.md), [M3.3](m3-3-timeout-counts-and-corrections.md), [M3.4](m3-4-set-and-match-confirmation.md), [M3.5](m3-5-undo-and-confirmed-reset.md), [M3.6](m3-6-historical-set-corrections.md) | FR-05; FR-06 |
| [M3.8 — Swap display sides without moving team data](m3-8-identity-preserving-side-swaps.md) | [M3.2](m3-2-rally-points-and-serve.md), [M3.3](m3-3-timeout-counts-and-corrections.md) | FR-04; FR-05 |
| [M3.9 — Expose local scoring controls and shared scoreboard values](m3-9-local-controls-and-scoreboard-projection.md) | [M3.5](m3-5-undo-and-confirmed-reset.md), [M3.6](m3-6-historical-set-corrections.md), [M3.7](m3-7-atomic-persistence-and-restart.md), [M3.8](m3-8-identity-preserving-side-swaps.md), [M1.6](m1-6-overlay-display-specification.md) | FR-04; FR-05; PRD 2.4 |

## M4: Capture, overlay, publishing, and adaptation

| Task plan | Blocked by | Requirements |
| --- | --- | --- |
| [M4.1 — Preview cameras and switch supported lenses](m4-1-camera-preview-and-lens-switching.md) | [M2.2](m2-2-media-pipeline-feasibility.md), [M2.7](m2-7-technology-selection.md), [M2.8](m2-8-tdd-tooling-and-boundaries.md) | FR-02 |
| [M4.2 — Capture built-in audio and preserve intentional mute](m4-2-microphone-routing-and-mute.md) | [M2.1](m2-1-physical-capability-inventory.md), [M2.2](m2-2-media-pipeline-feasibility.md), [M2.8](m2-8-tdd-tooling-and-boundaries.md) | FR-03 |
| [M4.3 — Encode the scoreboard into outgoing landscape video](m4-3-encoded-scoreboard-composition.md) | [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M4.1](m4-1-camera-preview-and-lens-switching.md), [M4.2](m4-2-microphone-routing-and-mute.md) | FR-04 |
| [M4.4 — Publish to one manually configured destination](m4-4-manual-destination-and-publishing.md) | [M4.3](m4-3-encoded-scoreboard-composition.md), [M2.3](m2-3-provider-setup-feasibility.md), [M2.5](m2-5-lifecycle-and-storage-feasibility.md) | FR-01; PRD 4.3 |
| [M4.5 — Handle capture lifecycle and low-power screen use](m4-5-capture-lifecycle-and-low-power-screen.md) | [M4.4](m4-4-manual-destination-and-publishing.md), [M2.5](m2-5-lifecycle-and-storage-feasibility.md) | PRD 4.2; FR-02; FR-03 |
| [M4.6 — Adapt quality on Wi-Fi and mobile data](m4-6-automatic-quality-and-handoff.md) | [M4.4](m4-4-manual-destination-and-publishing.md), [M2.6](m2-6-adaptive-quality-policy.md) | FR-01 |
| [M4.7 — Bound reconnection and expose useful diagnostics](m4-7-stream-recovery-and-diagnostics.md) | [M4.5](m4-5-capture-lifecycle-and-low-power-screen.md), [M4.6](m4-6-automatic-quality-and-handoff.md) | FR-01; PRD 4.2; PRD 4.3 |

## M5: Destination sign-in and automatic setup

| Task plan | Blocked by | Requirements |
| --- | --- | --- |
| [M5.1 — Configure YouTube through optional sign-in](m5-1-youtube-automatic-setup.md) | [M2.3](m2-3-provider-setup-feasibility.md), [M2.8](m2-8-tdd-tooling-and-boundaries.md), [M4.4](m4-4-manual-destination-and-publishing.md) | FR-01; PRD 4.3 |
| [M5.2 — Configure Twitch through optional sign-in](m5-2-twitch-automatic-setup.md) | [M2.3](m2-3-provider-setup-feasibility.md), [M2.8](m2-8-tdd-tooling-and-boundaries.md), [M4.4](m4-4-manual-destination-and-publishing.md) | FR-01; PRD 4.3 |
| [M5.3 — Recover and remove YouTube authorization](m5-3-youtube-authorization-lifecycle.md) | [M5.1](m5-1-youtube-automatic-setup.md) | FR-01; PRD 4.3 |
| [M5.4 — Recover and remove Twitch authorization](m5-4-twitch-authorization-lifecycle.md) | [M5.2](m5-2-twitch-automatic-setup.md) | FR-01; PRD 4.3 |

## M6: Second Android device and concurrent controls

| Task plan | Blocked by | Requirements |
| --- | --- | --- |
| [M6.1 — Pair, authorize, and revoke an Android controller](m6-1-android-controller-pairing.md) | [M2.4](m2-4-remote-topology-feasibility.md), [M2.8](m2-8-tdd-tooling-and-boundaries.md), [M3.7](m3-7-atomic-persistence-and-restart.md), [M4.4](m4-4-manual-destination-and-publishing.md) | FR-06; PRD 2.4; PRD 4.3 |
| [M6.2 — Apply concurrent commands once in accepted order](m6-2-ordered-idempotent-commands.md) | [M6.1](m6-1-android-controller-pairing.md), [M3.7](m3-7-atomic-persistence-and-restart.md) | FR-05; FR-06 |
| [M6.3 — Expose the complete Android remote control matrix](m6-3-android-controller-controls.md) | [M6.2](m6-2-ordered-idempotent-commands.md), [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M4.4](m4-4-manual-destination-and-publishing.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md) | FR-06; PRD 2.4 |
| [M6.4 — Resynchronize Android controls before reconnect edits](m6-4-android-reconnect-and-resynchronization.md) | [M6.3](m6-3-android-controller-controls.md) | FR-06 |

## M7: Wear OS controller

| Task plan | Blocked by | Requirements |
| --- | --- | --- |
| [M7.1 — Connect an authorized watch directly to the streaming phone](m7-1-direct-watch-session.md) | [M2.4](m2-4-remote-topology-feasibility.md), [M2.8](m2-8-tdd-tooling-and-boundaries.md), [M6.2](m6-2-ordered-idempotent-commands.md), [M6.4](m6-4-android-reconnect-and-resynchronization.md) | FR-06; PRD 2.4; Q7 |
| [M7.2 — Provide the approved watch scoring controls](m7-2-watch-match-controls.md) | [M7.1](m7-1-direct-watch-session.md), [M3.9](m3-9-local-controls-and-scoreboard-projection.md) | FR-06; PRD 2.4 |
| [M7.3 — Provide watch mute and confirmed stream controls](m7-3-watch-media-controls.md) | [M7.2](m7-2-watch-match-controls.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md) | FR-06; PRD 2.4 |

## M8: Acceptance and personal release

| Task plan | Blocked by | Requirements |
| --- | --- | --- |
| [M8.1 — V01: Validate sustained real broadcasts](m8-1-v01-sustained-broadcasts.md) | [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md), [M5.3](m5-3-youtube-authorization-lifecycle.md), [M5.4](m5-4-twitch-authorization-lifecycle.md), [M6.4](m6-4-android-reconnect-and-resynchronization.md), [M7.3](m7-3-watch-media-controls.md) | FR-01; PRD success criteria; PRD 4.2 |
| [M8.2 — V02: Validate the received scoreboard](m8-2-v02-received-overlay.md) | [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md), [M5.3](m5-3-youtube-authorization-lifecycle.md), [M5.4](m5-4-twitch-authorization-lifecycle.md), [M6.4](m6-4-android-reconnect-and-resynchronization.md), [M7.3](m7-3-watch-media-controls.md) | FR-04; PRD success criteria |
| [M8.3 — V03: Validate complete scoring scenarios](m8-3-v03-scoring-integrity.md) | [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md), [M5.3](m5-3-youtube-authorization-lifecycle.md), [M5.4](m5-4-twitch-authorization-lifecycle.md), [M6.4](m6-4-android-reconnect-and-resynchronization.md), [M7.3](m7-3-watch-media-controls.md) | FR-05; PRD 2.3 |
| [M8.4 — V04: Validate physical lens and audio continuity](m8-4-v04-lens-and-audio-continuity.md) | [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md), [M5.3](m5-3-youtube-authorization-lifecycle.md), [M5.4](m5-4-twitch-authorization-lifecycle.md), [M6.4](m6-4-android-reconnect-and-resynchronization.md), [M7.3](m7-3-watch-media-controls.md) | FR-02; FR-03; PRD success criteria |
| [M8.5 — V05: Validate both sign-in paths and manual setup](m8-5-v05-signed-in-and-manual-setup.md) | [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md), [M5.3](m5-3-youtube-authorization-lifecycle.md), [M5.4](m5-4-twitch-authorization-lifecycle.md), [M6.4](m6-4-android-reconnect-and-resynchronization.md), [M7.3](m7-3-watch-media-controls.md) | FR-01; PRD success criteria |
| [M8.6 — V06: Validate command delivery and durable recovery](m8-6-v06-command-recovery.md) | [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md), [M5.3](m5-3-youtube-authorization-lifecycle.md), [M5.4](m5-4-twitch-authorization-lifecycle.md), [M6.4](m6-4-android-reconnect-and-resynchronization.md), [M7.3](m7-3-watch-media-controls.md) | FR-05; FR-06 |
| [M8.7 — V07: Measure mobile and watch response](m8-7-v07-remote-response.md) | [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md), [M5.3](m5-3-youtube-authorization-lifecycle.md), [M5.4](m5-4-twitch-authorization-lifecycle.md), [M6.4](m6-4-android-reconnect-and-resynchronization.md), [M7.3](m7-3-watch-media-controls.md) | FR-06; PRD success criteria |
| [M8.8 — V08: Validate network and lifecycle stress](m8-8-v08-network-and-lifecycle-stress.md) | [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md), [M5.3](m5-3-youtube-authorization-lifecycle.md), [M5.4](m5-4-twitch-authorization-lifecycle.md), [M6.4](m6-4-android-reconnect-and-resynchronization.md), [M7.3](m7-3-watch-media-controls.md) | FR-01; PRD 4.2 |
| [M8.9 — V09: Validate credential and authorization boundaries](m8-9-v09-credential-boundary.md) | [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md), [M5.3](m5-3-youtube-authorization-lifecycle.md), [M5.4](m5-4-twitch-authorization-lifecycle.md), [M6.4](m6-4-android-reconnect-and-resynchronization.md), [M7.3](m7-3-watch-media-controls.md) | FR-01; PRD 4.3 |
| [M8.10 — V10: Validate every allowed and restricted control](m8-10-v10-full-controller-matrix.md) | [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md), [M5.3](m5-3-youtube-authorization-lifecycle.md), [M5.4](m5-4-twitch-authorization-lifecycle.md), [M6.4](m6-4-android-reconnect-and-resynchronization.md), [M7.3](m7-3-watch-media-controls.md) | FR-06; PRD 2.4 |
| [M8.11 — Prepare personal release and supported-configuration guide](m8-11-personal-release-and-operator-guide.md) | [M8.1](m8-1-v01-sustained-broadcasts.md), [M8.2](m8-2-v02-received-overlay.md), [M8.3](m8-3-v03-scoring-integrity.md), [M8.4](m8-4-v04-lens-and-audio-continuity.md), [M8.5](m8-5-v05-signed-in-and-manual-setup.md), [M8.6](m8-6-v06-command-recovery.md), [M8.7](m8-7-v07-remote-response.md), [M8.8](m8-8-v08-network-and-lifecycle-stress.md), [M8.9](m8-9-v09-credential-boundary.md), [M8.10](m8-10-v10-full-controller-matrix.md) | FR-01–FR-06; PRD 4.2; PRD 4.3 |

## Coverage and decisions

| Requirement or evidence | Task coverage |
| --- | --- |
| FR-01: Destinations and adaptation | [M2.2](m2-2-media-pipeline-feasibility.md), [M2.3](m2-3-provider-setup-feasibility.md), [M2.6](m2-6-adaptive-quality-policy.md), [M4.4](m4-4-manual-destination-and-publishing.md), [M4.6](m4-6-automatic-quality-and-handoff.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md), [M5.1](m5-1-youtube-automatic-setup.md), [M5.2](m5-2-twitch-automatic-setup.md), [M5.3](m5-3-youtube-authorization-lifecycle.md), [M5.4](m5-4-twitch-authorization-lifecycle.md), [M8.1](m8-1-v01-sustained-broadcasts.md), [M8.5](m8-5-v05-signed-in-and-manual-setup.md), [M8.8](m8-8-v08-network-and-lifecycle-stress.md), [M8.9](m8-9-v09-credential-boundary.md) |
| FR-02: Cameras/lenses | [M2.1](m2-1-physical-capability-inventory.md), [M2.2](m2-2-media-pipeline-feasibility.md), [M4.1](m4-1-camera-preview-and-lens-switching.md), [M8.4](m8-4-v04-lens-and-audio-continuity.md) |
| FR-03: Built-in/accessory microphones | [M1.3](m1-3-representative-audio-accessories.md), [M2.1](m2-1-physical-capability-inventory.md), [M2.2](m2-2-media-pipeline-feasibility.md), [M4.2](m4-2-microphone-routing-and-mute.md), [M8.4](m8-4-v04-lens-and-audio-continuity.md) |
| FR-04: Broadcast scoreboard | [M1.6](m1-6-overlay-display-specification.md), [M3.8](m3-8-identity-preserving-side-swaps.md), [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M4.3](m4-3-encoded-scoreboard-composition.md), [M8.2](m8-2-v02-received-overlay.md) |
| FR-05: Match edits and retention | [M1.5](m1-5-match-correction-specification.md), [M3.1](m3-1-match-setup-and-presets.md), [M3.2](m3-2-rally-points-and-serve.md), [M3.3](m3-3-timeout-counts-and-corrections.md), [M3.4](m3-4-set-and-match-confirmation.md), [M3.5](m3-5-undo-and-confirmed-reset.md), [M3.6](m3-6-historical-set-corrections.md), [M3.7](m3-7-atomic-persistence-and-restart.md), [M3.8](m3-8-identity-preserving-side-swaps.md), [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M8.3](m8-3-v03-scoring-integrity.md), [M8.6](m8-6-v06-command-recovery.md) |
| FR-06: Android and watch controls | [M2.4](m2-4-remote-topology-feasibility.md), [M6.1](m6-1-android-controller-pairing.md), [M6.2](m6-2-ordered-idempotent-commands.md), [M6.3](m6-3-android-controller-controls.md), [M6.4](m6-4-android-reconnect-and-resynchronization.md), [M7.1](m7-1-direct-watch-session.md), [M7.2](m7-2-watch-match-controls.md), [M7.3](m7-3-watch-media-controls.md), [M8.6](m8-6-v06-command-recovery.md), [M8.7](m8-7-v07-remote-response.md), [M8.10](m8-10-v10-full-controller-matrix.md) |
| PRD 4.2: Lifecycle and sustained operation | [M2.5](m2-5-lifecycle-and-storage-feasibility.md), [M4.5](m4-5-capture-lifecycle-and-low-power-screen.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md), [M8.1](m8-1-v01-sustained-broadcasts.md), [M8.8](m8-8-v08-network-and-lifecycle-stress.md) |
| PRD 4.3: Security/privacy | [M2.3](m2-3-provider-setup-feasibility.md), [M2.4](m2-4-remote-topology-feasibility.md), [M2.5](m2-5-lifecycle-and-storage-feasibility.md), [M4.4](m4-4-manual-destination-and-publishing.md), [M4.7](m4-7-stream-recovery-and-diagnostics.md), [M5.3](m5-3-youtube-authorization-lifecycle.md), [M5.4](m5-4-twitch-authorization-lifecycle.md), [M6.1](m6-1-android-controller-pairing.md), [M7.1](m7-1-direct-watch-session.md), [M8.9](m8-9-v09-credential-boundary.md) |
| D01: Device/accessory/network facts | [M1.2](m1-2-device-roles-and-os-inventory.md), [M1.3](m1-3-representative-audio-accessories.md), [M1.4](m1-4-venue-upload-baseline.md), [M2.1](m2-1-physical-capability-inventory.md) |
| D02–D06: Stack, storage, transport, providers | [M2.2](m2-2-media-pipeline-feasibility.md), [M2.3](m2-3-provider-setup-feasibility.md), [M2.4](m2-4-remote-topology-feasibility.md), [M2.5](m2-5-lifecycle-and-storage-feasibility.md), [M2.7](m2-7-technology-selection.md) |
| D07–D08: Quality and lifecycle | [M2.5](m2-5-lifecycle-and-storage-feasibility.md), [M2.6](m2-6-adaptive-quality-policy.md) |
| D09: Watch framework/signing/direct pairing | [M2.4](m2-4-remote-topology-feasibility.md), [M2.7](m2-7-technology-selection.md), [M7.1](m7-1-direct-watch-session.md) |
| D10: Overlay display rules | [M1.6](m1-6-overlay-display-specification.md), [M3.9](m3-9-local-controls-and-scoreboard-projection.md), [M4.3](m4-3-encoded-scoreboard-composition.md) |
| D11: Corrections, undo, cap, protocol retention | [M1.5](m1-5-match-correction-specification.md), [M3.5](m3-5-undo-and-confirmed-reset.md), [M3.6](m3-6-historical-set-corrections.md), [M3.7](m3-7-atomic-persistence-and-restart.md), [M6.2](m6-2-ordered-idempotent-commands.md) |
| D12: Distribution and validation coverage | [M2.3](m2-3-provider-setup-feasibility.md), [M2.7](m2-7-technology-selection.md), [M8.11](m8-11-personal-release-and-operator-guide.md) |
| D13: Tools and confirmed public test boundaries | [M2.8](m2-8-tdd-tooling-and-boundaries.md) |

V01–V10 each have their own plan, M8.1–M8.10 respectively. M8.11 is blocked
by all ten and prepares the personal release only after G4. Full-system evidence
is collected alongside implementation when useful and completed during M8;
automated behavioral testing starts with the first application behavior.

## Review record

- Confirmed by the owner: one detailed plan per independently verifiable task,
  with dependencies, steps, and acceptance criteria, following a Grill-me question.
- Proposed: the 53-task decomposition, dependency graph, and first behavioral
  slices. These can be refined during implementation without silently changing
  requirements or treating open technology choices as approved.
- Still open: the PRD's existing facts/decisions, master-plan D01–D13, and
  first-test confirmation at each public boundary. Current documentation work
  creates no application code, executable tests, or hardware/provider results.

## Documentation checks

Initial backlog checks on 2026-10-18 (before the M1.2 PRD clarifications):

- All 53 task files exist, are indexed, and include entry conditions, steps,
  acceptance criteria, and evidence requirements.
- All dependency IDs resolve and the task graph has no cycles.
- All 869 local links and anchors across 60 project Markdown documents resolve.
- The root master plan was moved; no duplicate remains at the original path.
- The PRD's SHA-256 at that check was
  `3A18442DA93D7CAD724813D048F75B3C02B1668B1CAC7305BD2B9550D8A51263`.
- Both requested interview skills exist locally and have installer-maintained
  lock entries. No application tests or device/provider validation ran.

M1.2 intentionally updated the PRD with the owner's subsequent answers.
Its [validation record](../inventories/m1-2-device-roles-and-os-inventory.md#documentation-validation)
describes the current documentation checks; the original hash above is historical.

