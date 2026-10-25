# Project Guidelines

## Project Overview

LiveStream is a personal project for an Android application that livestreams
volleyball using the phone's cameras and microphones, adds a volleyball
scoreboard to the broadcast, and supports control from another mobile device
or wearable.

Confirmed initial scope:

- Stream to one destination at a time, including YouTube, Twitch, and compatible
  services. Offer optional destination sign-in for automatic connection setup,
  with manual stream URL/key entry also available.
- Provide controls on the streaming phone, a second Android phone or tablet,
  and a Wear OS watch.
- Remote controls include match information, camera selection, microphone
  mute, and confirmed stream start/stop, with a simpler watch interface.
- Concentrate initially on customizable indoor volleyball presets, deferring
  beach volleyball to later iterations.
- Available target devices: one Pixel 8 Pro, two regular Pixel 10 phones,
  two Pixel 10 Pro phones, and four Pixel Watch 4 watches. Any may be used for
  testing; main testing uses one Pro, its paired watch, and the Pixel 8 Pro.
  Phone roles are interchangeable, with no permanent streamer or
  controller assignment. Expected software is the latest published stable OS version or
  N−1, meaning the previous major stable OS version; actual installed OS/builds
  remain unknown. Each of the four Pixel 10-series phones has its own paired
  Pixel Watch 4; Pixel 8 Pro has no current watch setup. Retain direct pairing to the
  active streaming phone under PRD Q7.
- Version 1 uses the phone's built-in microphone. The owner confirmed on
  2026-10-24 that external wired/Bluetooth microphone support and testing follow
  version 1. No preferred accessory brand/model is imposed for that later work.
- Show numeric timeouts remaining labeled `T/O` and editable 1–5-character team
  abbreviations with validation on the broadcast scoreboard. Placement uses
  configurable presets: four corners and bottom-center, confirmed on 2026-10-18.
- Automatically adapt outgoing video quality to upload conditions, lowering
  quality when the active Wi-Fi or mobile-data connection deteriorates. Both
  connection types are available; upload speeds and adaptation parameters
  remain to be established during technical planning. On 2026-10-18 the owner
  selected mobile data as the default broadcast connection because venues
  change; venue-specific upload measurements are unavailable. Wi-Fi remains
  a supported alternative.
- No additional budget, deadline, technology, or distribution constraints were
  imposed by the user. Technology selections remain open.

Use [PRD.md](PRD.md) for product requirements, proposed scope, acceptance
criteria, and open questions. Keep confirmed requirements separate from
recommendations and decisions awaiting the user's answer.

## Current Phase

- M2 feasibility research and isolated probe/harness execution, authorized by
  the owner on 2026-10-23. On 2026-10-24 ADB authorized a Pixel 8 Pro and Pixel
  10 Pro XL; actual OS/build facts were retained. A diagnostic capability APK
  built and opened on both. Media, watch, network and provider checks remain pending.
- The project owner approved the PRD on 2026-10-17. Explicitly open decisions
  and technologies marked TBD remain unresolved.
- The owner requested execution of all M2 and confirmed T01–T06 on 2026-10-23.
  M2 probe/harness work is authorized. M3–M8 delivery remains subject to G2 and
  a further owner request; do not treat the isolated probe as the application.
- Documentation and the requested agent skills may be added during planning.

## IDE and Physical-device Workflow

- Android Studio is the primary IDE for the whole workspace, including the
  approved PRD, master plan, task plans, evidence, architecture, and project
  tooling. Open `D:/Playground/LiveStream` as a general project for repository
  work; it is not an Android Gradle root or the release application.
- Open each isolated Android feasibility project separately as documented in
  [ANDROID_STUDIO.md](ANDROID_STUDIO.md). Do not combine the M2 experiments into
  a release module or infer production technology choices from their toolchain.
- Before beginning development, debugging, or validation that depends on an
  Android phone, check ADB/Android Studio device visibility and authorization.
  If a required phone is not connected/authorized, ask the owner to connect or
  authorize the relevant phone before that dependent work. State which device
  role/model is needed. Do not claim device validation without evidence. Continue
  independent host-side or documentation work while waiting; do not block work
  that does not need the device. Apply the same rule to watches when physical
  Wear OS validation is required.
- Android Virtual Devices may be used for applicable UI, lifecycle, API-level,
  screen-size, and instrumented tests. Treat emulator results as supplementary:
  they do not establish physical camera/lens or microphone routing, sustained
  encoding/thermal behavior, real watch pairing, network handoff, or received
  broadcast behavior. Keep required physical-device and broadcast checks; do not
  claim hardware compatibility from an emulator.
- IDE migration does not pass G2, decide D01–D13, or authorize M3–M8 delivery.

## Collaboration and Clarification

- Always ask concise clarification questions when needed: if necessary
  information is missing, ambiguous, or conflicting and cannot be resolved
  from available project context, ask before proceeding with dependent work.
- For each clarification question, provide clear answer options. Put the
  recommended option first, label it "Recommended," and briefly explain why
  it is recommended or what tradeoff it involves.
- Always include an "Other — enter your own answer" option so the user can
  provide a custom response. When the question interface supplies its own
  custom-answer field, use that field instead of duplicating it.
- A recommended or preselected answer is not a confirmed decision.
- Record unresolved choices in the PRD and continue independent planning work.
- Preserve manual edits to this file and other project documents.

## Tech Stack

- Streaming application platform: Android; wearable platform: Wear OS.
- Language, UI toolkit, minimum Android version, streaming library, remote
  transport, and storage: TBD.
- Describe technology choices as proposals until they are decided. Verify
  current platform capabilities against official documentation.

## Project Structure

- `AGENTS.md`: instructions for agents working in this project.
- `PRD.md`: product requirements and scope decisions.
- `plan/MASTER_PLAN.md`: proposed milestones, dependencies, decision gates, and
  validation evidence derived from the approved PRD.
- `plan/STATUS.md`: derived plan status dashboard; refresh with the project
  `plan-status` skill using current task records and retained evidence.
- `.archify/`: editable Archify diagram candidates, standalone HTML progression
  views, and their validation receipts; the current view is linked from `plan/STATUS.md`.
- `plan/subplans/`: individual task plans and their dependency/coverage index.
- `plan/inventories/`: dated owner-reported device facts and explicit evidence gaps.
- `plan/specifications/`: worked planning specifications, owner policy answers,
  and explicit unresolved behavior choices.
- `plan/SKILL_DISCOVERY.md`: reviewed planning skills and installation provenance.
- `ARCHITECTURE.md`: proposed software architecture and Mermaid diagrams;
  unresolved technology choices remain proposals.
- `docs/diagrams/`: editable Mermaid source and rendered documentation diagrams.
- `.agents/skills/`: skills installed for this project.
- `skills-lock.json`: skill source and integrity metadata maintained by the
  skills installer.
- Application structure: TBD during implementation planning.
- `feasibility/`: isolated M2 Kotlin/JVM probe and behavioral tests; no Android
  release application or media pipeline is delivered by it.
- `feasibility/android-inventory/`: independent M2 Android capability measurement
  APK; lists advertisements and permission state, without capture or publishing.
- `feasibility/android-media/`: isolated M2 camera/audio feasibility experiment;
  not the release application. See [Android Studio workflow](ANDROID_STUDIO.md)
  for opening the separate Android projects without selecting the release stack.
- `tools/m2/`: project-local tooling bootstrap and runner scripts.
- `plan/evidence/m2/`: retained red/green outputs and pre-fix probe sources.

## Setup and Development

No release Android application build workflow exists yet. The authorized isolated M2
JVM runner is documented in [feasibility/README.md](feasibility/README.md) and
[M2.8](plan/specifications/m2-8-tdd-tooling-and-boundaries.md). Use those actual
commands. The actual diagnostic APK build/deployment commands are documented in
[android-inventory/README.md](feasibility/android-inventory/README.md); do not
treat them as the release application or a completed Android/watch test harness.
For Android Studio workspace use and separate isolated Android Gradle project
opening, follow [ANDROID_STUDIO.md](ANDROID_STUDIO.md); do not import the
workspace root as the release Android project or treat IDE setup as application
authorization.

## Skills

### Installed Skills

- [find-skills](.agents/skills/find-skills/SKILL.md), from
  [vercel-labs/skills](https://github.com/vercel-labs/skills): use to discover
  relevant skills when planning or starting a new area of work.
- [prd](.agents/skills/prd/SKILL.md), from
  [github/awesome-copilot](https://github.com/github/awesome-copilot): use when
  drafting or revising product requirements.
- [tdd](.agents/skills/tdd/SKILL.md), from
  [mattpocock/skills](https://github.com/mattpocock/skills): use for test-driven
  development when implementation is requested.
- [grill-me](.agents/skills/grill-me/SKILL.md), from
  [mattpocock/skills](https://github.com/mattpocock/skills): use when the owner
  requests an interview to sharpen a plan; delegates to the installed
  [grilling](.agents/skills/grilling/SKILL.md) skill.
- [volleyball-rules](.agents/skills/volleyball-rules/SKILL.md), project skill:
  use for indoor volleyball scoring logic, sideout serving transitions, and
  broadcast scoreboard overlay specifications.
- [plan-status](.agents/skills/plan-status/SKILL.md), project skill: use for
  plan progress, gates, blockers, next tasks, and the Markdown status dashboard.
- [pretty-mermaid](.agents/skills/pretty-mermaid/SKILL.md), from
  [imxv/pretty-mermaid-skills](https://github.com/imxv/pretty-mermaid-skills):
  use to author and render Mermaid documentation diagrams. Installed for this
  project; its rendering dependencies are skill tooling, not application dependencies.
- [camerax](.agents/skills/camerax/SKILL.md), from
  [android/skills](https://github.com/android/skills/tree/main/camera/camerax):
  use to evaluate CameraX camera access and capture lifecycles, and for camera
  implementation after authorization. Installation does not select the media pipeline.
- [wear-compose-m3](.agents/skills/wear-compose-m3/SKILL.md), from
  [android/skills](https://github.com/android/skills/tree/main/wear/wear-compose-m3):
  use for Wear OS Compose Material 3 interface evaluation and future implementation.
  The UI framework, SDK levels, and application dependencies remain undecided.
- [archify](.agents/skills/archify/SKILL.md), from
  [tt-a1i/archify](https://github.com/tt-a1i/archify): use for interactive standalone
  HTML diagrams, including a high-level plan progression with a status brief.

Read a skill's `SKILL.md` before applying it. The user's instructions take
precedence over skill guidance.

### Discovery Workflow

1. Use `find-skills` to check the skills directory and run focused searches
   for the current task.
2. Review each candidate's instructions, source, relevance, install count,
   and repository before recommending it. Popularity alone is insufficient.
3. Prefer skills maintained by the relevant platform or tool publisher.
4. Keep installations scoped to this project and install only skills needed
   for the authorized work. Record future candidates separately.
5. If a suitable skill is unavailable, state that and continue work that does
   not depend on it. Do not claim that a skill was installed or used without
   verifying it.

Discovery on 2026-10-17 identified these skills for a later implementation
phase. The owner requested their project-scoped installation on 2026-10-18;
both are now installed. Installation does not establish technology decisions:

| Candidate | Intended use | Source |
| --- | --- | --- |
| `camerax` | Evaluate native camera access and lens capabilities | [Android camera skill](https://github.com/android/skills/tree/main/camera/camerax) |
| `wear-compose-m3` | Wear OS interface guidance for the selected wearable platform | [Android wearable skill](https://github.com/android/skills/tree/main/wear/wear-compose-m3) |

The current skills CLI did not run on the system's Node.js 16 runtime. This
session used a temporary Node.js 22 runtime without changing the global
installation. A compatible discovery command is
`npx --yes --package=node@22 --package=skills skills find <query>`.

## Coding Conventions

TBD when implementation begins.

## Testing and Validation

- The owner requires TDD for future application implementation and bug fixes.
  Follow the installed [tdd skill](.agents/skills/tdd/SKILL.md) and the
  [master plan's TDD working agreement](plan/MASTER_PLAN.md#tdd-working-agreement):
  confirm public test boundaries before writing tests, observe one failing
  behavioral test, implement the minimum to pass, then review/refactor with
  the relevant suite green. Deliver one behavior at a time throughout each
  implementation milestone.
- During planning, check requirements for completeness, consistency, clear
  acceptance criteria, and explicit unresolved decisions.
- Do not add application tests during the documentation phase.
- The later 2026-10-23 M2 authorization permits isolated feasibility tests at
  the now-confirmed T01–T06 boundaries. Continue one red/green behavior at a
  time; final Android/watch tooling and physical evidence remain pending.
- Version 1 validation must cover a real broadcast, the received scoreboard,
  supported lens changes, built-in audio/mute, and remote controls
  on the selected physical devices. Do not claim hardware support from
  emulator results alone.
- External microphone support and physical routing tests follow version 1
  under PRD Q48; do not treat unavailable accessory equipment as a v1 blocker.

## Git Workflow

Commit and branch conventions: TBD. Keep changes focused on the current
request and preserve unrelated user work.

## Additional Notes

- Device capabilities must be detected; do not assume every lens or accessory
  is available to third-party applications.
- Stream keys, access tokens, pairing secrets, and personal credentials must
  not be placed in source files, documentation, or logs.
- Keep this a manageable personal project. Propose additional features
  separately from the user's confirmed requirements.
