# Planning skill discovery

Reviewed: 2026-10-18

The requested [find-skills](../.agents/skills/find-skills/SKILL.md) workflow
checked installed skills, the [skills directory](https://skills.sh/), focused
CLI searches for `grill-me` and `planning`, and relevant source instructions.
The CLI used temporary Node.js 22; the global Node.js installation was unchanged.
Registry access initially failed in the sandbox; the authorized network retry
completed. Installation below is scoped to this project.

## Installed and used for this request

| Skill | Source and relevance | Verification |
| --- | --- | --- |
| [grill-me](../.agents/skills/grill-me/SKILL.md) | [mattpocock/skills](https://github.com/mattpocock/skills); owner explicitly requested a planning interview. | [Directory](https://skills.sh/mattpocock/skills/grill-me) reported about 1.4M installs. Source wrapper delegates to grilling. |
| [grilling](../.agents/skills/grilling/SKILL.md) | Same source; required interview discipline used by grill-me. | CLI reported about 934.6K installs. Reviewed questions-in-rounds, environment fact gathering, and shared-understanding instructions before use. |
| [find-skills](../.agents/skills/find-skills/SKILL.md) | [vercel-labs/skills](https://github.com/vercel-labs/skills); already installed discovery workflow. | Read local instructions and completed focused CLI searches; no reinstall. |

The [source repository](https://github.com/mattpocock/skills) reported about
295K stars during review. Popularity was only a supporting signal: actual
instructions, source, relevance, and required dependencies were reviewed.
The installer added only `grill-me` and `grilling`; their source paths and hashes
are recorded in [skills-lock.json](../skills-lock.json). No application
packages or implementation skills were installed by this request.

The Grill-me question asked whether each subplan should cover one independently
verifiable task with dependencies, steps, and acceptance criteria. The owner
answered yes. That confirms document granularity; proposed task decomposition
and open product/technology decisions are identified separately in the index.
A read-only fact-gathering sub-agent checked source coverage as required by
the grilling instructions; it made no file edits.

## Additional reviewed candidate

[to-tickets](https://skills.sh/mattpocock/skills/to-tickets), from
[mattpocock/skills](https://github.com/mattpocock/skills), can help if the owner
later wants these plans converted into issue-tracker tickets. The directory
reported about 674K installs. Its
[source instructions](https://github.com/mattpocock/skills/blob/main/skills/engineering/to-tickets/SKILL.md)
were reviewed: they describe independently verifiable vertical slices,
blocking dependencies, and an approved ticket breakdown before publication.
The workflow adds tracker configuration and publishing steps beyond the current
local-document request, so it was **not installed or invoked**.

A future project-scoped installation, if needed, is:

```powershell
npx --yes --package=node@22 --package=skills skills add mattpocock/skills --skill to-tickets --agent codex --yes
```

Existing [TDD](../.agents/skills/tdd/SKILL.md) instructions informed preservation
of the master plan's working agreement; no executable tests were written.
Existing [volleyball rules](../.agents/skills/volleyball-rules/SKILL.md) guidance
was checked for scoring/overlay task coverage, with the PRD taking precedence
for final-set retention, customized allowances, and watch controls. The already
recorded CameraX and Wear Compose candidates were deferred during this review;
their later owner-requested installation is recorded below. No technology
choice follows from skill discovery or installation.

## M1.1 planning-package review, 2026-10-18

Re-read the installed `find-skills` instructions, checked the
[skills directory](https://skills.sh/), and ran the focused search:

```powershell
npx --yes --package=node@22 --package=skills skills find "planning review"
```

The sandbox attempt failed with npm registry `EACCES`; the approved network
retry succeeded using temporary Node.js 22. The results did not establish a
need for another skill for this document review. No new candidate was
recommended, installed, or invoked. Existing local guidance covered the work.

Reviewed installed TDD instructions for agreement consistency, volleyball
instructions for scoring/overlay coverage with PRD precedence, and
pretty-mermaid instructions and tooling for temporary diagram rendering.
The six installer-managed skills have local `SKILL.md` files and complete
source/path/hash metadata in [skills-lock.json](../skills-lock.json), which
was preserved. This checks recorded provenance, not a fresh upstream integrity
attestation. `volleyball-rules` is the project-authored skill identified in
[AGENTS.md](../AGENTS.md#installed-skills); it is not an installer lock entry.

Full review evidence is in the [M1.1 review record](reviews/m1-1-planning-package-review.md).

## M1.2 device-role and OS inventory, 2026-10-18

Read the installed `find-skills` instructions, checked the
[skills directory](https://skills.sh/), and ran the focused search:

```powershell
npx --yes --package=node@22 --package=skills skills find "android device inventory"
```

The sandbox attempt failed with npm registry `EACCES`; the approved network
retry succeeded using temporary Node.js 22. The results concerned an SDK and
Honor-device security auditing, rather than recording owner-supplied Pixel
software versions, phone roles, and watch companion facts. No candidate was
recommended, installed, or invoked. Source/repository review for installation
was unnecessary because neither result was selected for this task.

The existing discovery workflow and Google's official device-settings guidance
were sufficient for the [M1.2 inventory](inventories/m1-2-device-roles-and-os-inventory.md).
No new skill, application dependency, or application test was added.

The existing PRD skill was also read and applied when the owner supplied the
M1.2 answers: interchangeable phone roles, current/previous major stable OS
expectations, and watch companion pairings. The owner subsequently clarified
five phones and four watches, with main testing on one Pro, its paired watch,
and the Pixel 8 Pro. Confirmed answers were added to
the PRD while actual installed versions/builds and compatibility evidence remain
explicitly unknown. No additional installation was needed for this continuation.

## Plan status dashboard skill, 2026-10-18

The owner requested the current plan status and a skill to provide a plan status
dashboard. Read the built-in `skill-creator` guidance and the installed
[find-skills](../.agents/skills/find-skills/SKILL.md) instructions. Checked local
skills, the [skills directory](https://skills.sh/), and the focused search:

```powershell
npx --yes --package=node@22 --package=skills skills find "plan status"
```

The sandbox search failed with npm registry `EACCES`; the approved network
retry succeeded with temporary Node.js 22. Results included broad deployment,
sprint-planning, roadmap, and project-artifact skills. No external candidate was
recommended, installed, or invoked; source/install-count/repository review for
installation was therefore unnecessary. The requested workflow was authored
locally around this project's existing task records and gates.

Created the project-scoped [plan-status skill](../.agents/skills/plan-status/SKILL.md)
and its UI metadata, registered it in [AGENTS.md](../AGENTS.md#installed-skills),
and generated the first [status dashboard](STATUS.md). The skill distinguishes
task completion, gate readiness, unresolved decisions, and evidence gaps;
future dashboard requests refresh the snapshot from current source documents.
The first use reconciled all 53 task statuses and checked completion evidence
for M0.1, M1.1, and M1.2.

`plan-status` is project-authored, like `volleyball-rules`; it has no external
installer provenance and does not add an entry to [skills-lock.json](../skills-lock.json).
No application dependencies, prototypes, or application tests were added.

Validation: the existing planning-package checker passed for 53 tasks, 159
dependency edges, eight diagrams, and all checked documentation links. A separate
count reconciliation matched the dashboard's nine milestone rows, three complete
tasks, 50 not-started tasks, and 13 open decisions. Skill frontmatter and UI
metadata format checks passed. The bundled `quick_validate.py` could not run
because no accessible Python runtime was available; the supported fields,
naming, description limits, UI fields, and absence of unfinished placeholders
were checked locally with Node.js instead.

## Requested Android skills and Archify progression, 2026-10-18

The owner requested project-scoped installation of `camerax` and
`wear-compose-m3`, and use of `archify` for a high-level progression HTML file
with a brief of [STATUS.md](STATUS.md). Read the installed discovery guidance
and the built-in installer guidance. The available installer helpers require
Python, which is unavailable here; the established project Skills CLI workflow
was used to preserve installer-maintained lock metadata.

Checked local skills, the [skills directory](https://skills.sh/), focused CLI
searches for all three names, the source instructions, and the source
repositories. Reported directory counts were about 1.1K installs for CameraX,
1K for Wear Compose M3, and 160K for Archify. The source repositories reported
about 7.7K stars for [android/skills](https://github.com/android/skills) and 85.3K
for [tt-a1i/archify](https://github.com/tt-a1i/archify). These are dated discovery
signals; the actual instructions and relevance determined suitability.

| Installed skill | Reviewed source and use |
| --- | --- |
| [camerax](../.agents/skills/camerax/SKILL.md) | [Official Android source](https://github.com/android/skills/tree/main/camera/camerax); camera capture, lifecycle, hardware interop, and future media-pipeline evaluation. |
| [wear-compose-m3](../.agents/skills/wear-compose-m3/SKILL.md) | [Official Android source](https://github.com/android/skills/tree/main/wear/wear-compose-m3); Wear Compose Material 3 components, compatibility, and future watch UI evaluation. |
| [archify](../.agents/skills/archify/SKILL.md) | [Upstream source](https://github.com/tt-a1i/archify/tree/main/archify); standalone interactive workflow HTML with schema, artifact, and browser checks. |

Installed only the named skills, using temporary Node.js 22:

```powershell
npx --yes --package=node@22 --package=skills skills add android/skills --skill camerax wear-compose-m3 --agent codex --yes
npx --yes --package=node@22 --package=skills skills add tt-a1i/archify --skill archify --agent codex --yes
```

All three local `SKILL.md` files and Archify's renderer are present.
[skills-lock.json](../skills-lock.json) contains their installer-recorded source
paths and hashes alongside the six existing entries. Installation does not
select CameraX, Compose, SDK levels, test runners, or any application dependency.
The project remains in planning.

Applied Archify's workflow guidance to the
[progression graph and status brief](../.archify/workflow-livestream-progression-20261018-133947/livestream-progression.html).
Its [editable candidate](../.archify/workflow-livestream-progression-20261018-133947/candidate.json)
uses the master plan's gates and the current task counts. M3–M7 are grouped
as 27 feature-delivery tasks for this high-level view; individual dependencies
remain in the task plans. The HTML includes completed evidence, four remaining
planning tasks, and the open-decision/evidence limitations from STATUS.md.

The first Node.js execution was blocked by npm registry `EACCES`; the authorized
retry completed. Archify's
[finalization receipt](../.archify/workflow-livestream-progression-20261018-133947/livestream-progression.finalize-summary.json)
reports passing validation, delivery, artifact checks, and real-browser checks
with no diagnostics. These are automated checks; no screenshot or perceptual
review was requested or performed. The HTML is a dated documentation snapshot.
No application code, prototype, application dependency, or application test was added.

## M1.3 microphone validation inventory, 2026-10-18

Read the installed discovery guidance, checked the
[skills directory](https://skills.sh/), and ran the focused search
`npx --yes --package=node@22 --package=skills skills find "android microphone audio routing"`.
The search returned no matching skills. No new skill was recommended or
installed; current project guidance and the owner's availability answer were
sufficient for the documentation task.

The owner answered **“built in only for now.”** Recorded that answer in the
[M1.3 inventory](inventories/m1-3-representative-audio-accessories.md) and used
the existing PRD guidance to keep equipment availability separate from the
approved accessory requirements. A00 is the available built-in baseline;
A01–A04 record pending USB, other wired, receiver, and direct Bluetooth cases.
Their connector, adapter/receiver, and model details remain pending until
equipment is available. Each case is assigned to M2.1/M2.2 and V04.

Reviewed Google's [AudioDeviceInfo reference](https://developer.android.com/reference/android/media/AudioDeviceInfo)
for device-type and input/source distinctions; it does not establish support
on these phones. No physical input, media, or broadcast test ran.

M1.3's task checklist, index, master plan, PRD availability note, and status
dashboard now reflect completed planning with pending accessory equipment.
Applied the installed Archify workflow to refresh the
[progression HTML](../.archify/workflow-livestream-progression-20261018-164617/livestream-progression.html)
to four completed tasks and 3/6 M1 tasks, preserving the earlier snapshot.
Its [automated receipt](../.archify/workflow-livestream-progression-20261018-164617/livestream-progression.finalize-summary.json)
reports passing validation, delivery, artifact, and real-browser checks with
no diagnostics. No screenshot or perceptual review was performed.

This completes the planning task's provision for pending unavailable cases.
D01 and the implementation/release gates remain open; no application code,
prototype, dependency, or application test was added.

## M1.4 upload observations and connection default, 2026-10-18

Applied the installed discovery guidance, checked the
[skills directory](https://skills.sh/), and ran the focused search:

```powershell
npx --yes --package=node@22 --package=skills skills find "venue upload measurement"
```

The sandbox attempt failed with npm registry `EACCES`; the authorized network
retry completed with temporary Node.js 22 and returned no matching skills.
No new candidate was recommended or installed. Existing project guidance was
sufficient; no upstream candidate source/install-count/repository assessment
was needed for an installation.

Applied the existing PRD guidance to the owner's answer: venues keep changing,
no venue-specific measurements are available, and mobile data should be the
default broadcast connection. Wi-Fi remains supported. The
[M1.4 inventory](inventories/m1-4-venue-upload-baseline.md) retains the answer,
separate N01/N02 measurement gaps, proposed collection fields, and the M2.6
handoff. This completes M1.4 under its explicit-evidence-gap acceptance path;
it does not establish measured throughput or settle D01/D07.

Updated the affected requirements, task records, master plan, and proposed
architecture. Applied the project plan-status skill to refresh the Markdown
dashboard from current task records and retained evidence. The earlier Archify
HTML remains a dated snapshot, now labeled as preceding M1.4 completion.
No application code, prototype, application dependency, or application test
was added; no phone, speed test, or broadcast was exercised.

## M1.5 match-policy specification, 2026-10-18

Read the installed discovery guidance, checked the [skills directory](https://skills.sh/),
and ran the focused search:

```powershell
npx --yes --package=node@22 --package=skills skills find "volleyball scoring"
```

The sandbox attempt failed with npm registry `EACCES`; the approved network
retry returned no matching skills. No external candidate was recommended or
installed. Read and applied the existing project volleyball-rules skill and
PRD guidance, with the approved PRD overriding generic skill examples. Checked
FIVB rules 6.1.3, 6.2, 6.3, and 15.1 against the linked official indoor rules;
custom caps and application correction mechanics remain project policy work.

The [M1.5 specification](specifications/m1-5-match-correction-specification.md)
contains 36 worked examples. The owner answered three focused questions:
point removal preserves serve; inconsistent historical corrections are rejected;
and one-step Undo for the latest match edit survives restart. Recorded those
answers in PRD Q16–Q18 and kept remaining D11 choices explicit. Updated the task,
index, master plan, architecture, and affected downstream task guidance.
Applied the project plan-status guidance to refresh the Markdown snapshot;
the earlier HTML remains a labeled historical snapshot. No new skill,
application code, prototype, application dependency, executable application
test, or physical result was added.

## M1.6 scoreboard specification, 2026-10-18

Read the installed discovery guidance, checked the [skills directory](https://skills.sh/),
and ran the focused search with the previously authorized network workflow:

```powershell
npx --yes --package=node@22 --package=skills skills find "broadcast scoreboard overlay"
```

The search returned no matching skills. No new candidate was recommended or
installed. Applied the existing volleyball-rules and PRD guidance, preserving
PRD exceptions and separating candidate numeric layout limits from approved
requirements. W3C contrast/use-of-color references support proposed review
criteria; they do not establish application or broadcast accessibility results.

The owner requested configurable placement and selected preset positions in a
follow-up, plus editable 1–5-character codes with validation and numeric `T/O`
remaining counts. PRD Q19–Q21 retain those answers. The
[M1.6 specification](specifications/m1-6-overlay-display-specification.md)
contains 16 display examples, five placement anchors, remaining D10 choices,
and future readability checks. The hand-authored SVG/PNG uses existing skill
rendering dependencies; its PNG was inspected without creating application code
or a prototype. Applied plan-status guidance to refresh the Markdown snapshot.
The older HTML remains explicitly historical. No application tests, device,
encoder, or received-broadcast checks ran, and no application stack was selected.

## M2.3 provider documentation research, 2026-10-20

Read find-skills, checked the [skills directory](https://skills.sh/), and ran:

```powershell
npx --yes --package=node@22 --package=skills skills find "youtube twitch oauth"
```

The sandboxed attempt failed with npm registry connection EACCES; the approved
network retry completed with exit code 0. npm reported engine warnings from
the host Node.js 16 bootstrap; the temporary-runtime search returned results.
The three results were sales-heepsy (19 installs), external-api-tos-check
(2 installs), and universal-downloader (1 install). No provider-specific,
publisher-maintained authorization skill was identified by this focused
search. These results were not recommended or installed; their instructions
and repositories were not reviewed for installation. No global runtime or
project application dependency was selected or added.

Used official Google/Android, YouTube, Twitch, and IETF documentation directly
for the [M2.3 findings](specifications/m2-3-provider-setup-feasibility.md).
Applied the existing plan-status guidance to refresh the Markdown dashboard.
No additional skill was needed, and skills-lock.json was preserved. The work
records research and validation scenarios; it does not establish registered
clients, account authorization, live ingest, or completed provider feasibility.

## M2 execution and preparation, 2026-10-23

On 2026-10-24, read PRD guidance for the owner's confirmed Q48 scope change:
built-in audio for v1, external microphone support/testing after v1. Updated
the affected requirements and validation assignments while preserving historical
inventories. CameraX/Wear guidance was read for the physical M2 continuation;
official Android ADB, SDK and AGP references were checked. No new skill,
dependency, APK, connected-device result or hardware measurement is claimed.
The owner reports devices available but not connected. Existing TDD and
plan-status guidance remain in effect; no test boundary or delivery gate changed
apart from removing the deferred external-microphone requirement from v1.

The later ST02 continuation used the installed TDD instructions and
tests/mocking references, plan-status guidance and the existing find-skills
workflow. Focused local searches covered fault, crash, recovery and public
boundary guidance. This continues the established M2 storage area; no fresh
CLI/directory-web search, upstream review or installation is claimed. The
fourth retained red/green cycle covers a host child-process halt before
commit, with Android and physical-device evidence still pending.

The subsequent ST04 continuation re-read TDD/tests/mocking and plan-status,
checked the existing find-skills workflow, and searched installed TDD guidance
for concurrency, public-boundary and real-component testing. Existing skills
cover this continuation of the M2 storage area; no fresh CLI/directory-web
search, upstream review or installation is claimed. The fifth red/green cycle
exposes and fixes a shared-connection command transaction race. No dependency,
platform capability or physical/provider result is newly selected or inferred.

The ST04 duplicate-delivery follow-up re-read TDD/tests/mocking and plan-status,
checked existing find-skills guidance and searched installed TDD instructions
for public-boundary testing. This continues the same storage area, with no new
CLI/directory-web search, upstream review or installation claimed. The check
passes on its first run against the existing implementation; it is additional
verification, not a sixth red/green cycle. The suite has six passing tests and
the probe implementation is unchanged. Physical/provider evidence remains pending.

Read the installed find-skills, CameraX, Wear Compose M3, TDD and plan-status
instructions, including TDD tests/mocking references. Checked installed skills,
the skills directory, and ran the focused search:

```powershell
npx --yes --package=node@22 --package=skills skills find "android streaming testing"
```

The sandbox attempt failed with registry EACCES; the approved network retry
completed. It returned `skydoves/android-testing-skills`'s screenshot/recording
skill with 71 installs. It was not recommended or installed: existing official
Android skills and direct publisher documentation cover the authorized work,
and another screen-recording workflow is not needed. No candidate instructions
or repository were reviewed for installation, and no new skill was invoked.
skills-lock.json is preserved.

The owner confirmed T01–T06 before application tests. Applied the existing TDD
guidance to two isolated T02 and one real-SQLite T01 red/green cycles, CameraX/Wear guidance to candidate
evaluation, and plan-status to the refreshed dashboard. Official Kotlin,
Android, RootEncoder, Google/YouTube, Twitch, Adoptium and Gradle references are
linked from the M2 records. The SDK/device/network/provider gaps remain explicit.
No new skill or physical/device/provider compatibility result was introduced.

## D10 numeric-fit continuation, 2026-10-23

Read the installed find-skills, plan-status, volleyball-rules, and PRD
instructions. Checked the existing local guidance for scoreboard/numeric
overflow. This continues the previously searched M1.6 scoreboard area; no
fresh CLI/directory-web check, external candidate review, or installation is
claimed. Existing skills cover this written policy review, with the approved
PRD taking precedence over the volleyball skill's generic layout and timeout
examples.

The [numeric-fit review](specifications/d10-numeric-fit-and-overflow-policy-review.md)
records the two owner answers as Q44/Q45 with N01–N14: adaptive numeric layout
and a visible stop preserving the full new match/off-air scoring if numbers
still cannot fit. The rejection recommendation was not selected. No platform
facts, upstream skill changes, new dependencies,
application behavior, or physical readability results are asserted.

## D11 policy follow-up, 2026-10-20

Re-read the installed find-skills, PRD, and volleyball-rules instructions;
checked the installed skill directories and ran focused local searches for
cap, starting-value, Undo, historical-correction, and confirmation guidance.
This continues the existing M1.5 area: the installed project volleyball skill
and PRD guidance cover the review, so no new skill was recommended or installed.
No fresh CLI/directory-web or upstream integrity check is claimed.

The [focused D11 review](specifications/m1-5-match-correction-specification.md#focused-d11-policy-review-2026-10-20)
prepares owner questions and eight supplemental written cases. Preserved PRD
exceptions for customized timeouts, final-set points, explicit confirmation,
and the watch control matrix. The PRD change records unresolved review choices;
no selected answer or scoring behavior is inferred. Existing planning-status
counts and gates are preserved, and no application tests or dependencies were added.

The owner subsequently answered four follow-up choices on 2026-10-20, now
recorded as PRD Q22–Q25. Applied PRD/volleyball guidance to update confirmed
versus open policy, corrected the cap examples, and used the already installed
pretty-mermaid renderer for matching lifecycle source/SVG and a temporary PNG
inspection. The existing Node.js runtime and skill dependencies were sufficient;
no skill or application dependency was installed. The prior eight-case review
now has twelve supplemental cases, including canceled/confirmed cap continuation,
continued scoring to a margin, and restoration of the configured cap for the next set.

## ST05 reset-generation continuation, 2026-10-23

Read plan-status, TDD/tests/mocking and the installed find-skills instructions;
searched installed guidance for TDD, reset and recovery. This continues the
existing M2 storage area. Existing skills cover the work; no new CLI/web skill
search, upstream review, installation or dependency change is claimed.

The [ST05 record](evidence/m2/st05-reset-isolation.md) retains the probe-only
contract, three additional observed red/green cycles and two first-run checks
with unchanged store logic. Eleven tests pass, with eight observed cycles in
total. T01 remains the previously confirmed boundary. Application identity,
retention, tooling and physical/provider evidence remain unresolved; PRD.md,
AGENTS.md and skills-lock.json preserve their pre-continuation hashes.

