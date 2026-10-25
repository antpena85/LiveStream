# Android Studio workflow

Android Studio is the primary IDE for the **whole LiveStream workspace**:
planning documents, evidence, tooling, and the isolated Android feasibility
projects. Using Android Studio does **not** make an M2 project the livestream
application, select the release application's technology stack, pass G2, or
authorize M3–M8 implementation.

## Open the whole plan and workspace

Open `D:/Playground/LiveStream` in Android Studio as a general project for
repository-wide work on `PRD.md`, `plan/`, `ARCHITECTURE.md`, agent guidance,
tools, and evidence. Do not import or attach the workspace root as an Android
Gradle project: it is not a Gradle root and has no release-app module. Use the
Project view and normal file editing for Markdown, scripts, diagrams, and other
non-Android assets. There is no VS Code dependency in the plan.

The checked-in root `.idea` metadata is generic IDE workspace metadata, not an
Android project model. Android Studio may update local workspace settings when
opening the root; do not commit machine-specific workspace state.

## Open the Android feasibility projects separately

Open each Gradle project separately in Android Studio using **File → Open** and
select the indicated directory (the directory containing `settings.gradle.kts`):

| Project | Directory | Purpose |
| --- | --- | --- |
| Capability inventory | `D:/Playground/LiveStream/feasibility/android-inventory` | Lists app-visible camera, codec and audio capability metadata. It does not capture, encode or publish. |
| Media experiment | `D:/Playground/LiveStream/feasibility/android-media` | Isolated, operator-started M2 camera/audio feasibility experiment; it is not the release app. |
| Phone/watch connectivity | `D:/Playground/LiveStream/feasibility/android-watch` | Isolated foreground Data Layer ping/ack experiment; no release controls or UI framework decision. |

The Android feasibility projects are separate IDE projects from the whole-plan
workspace. Keep each experiment isolated; do not combine them into a release-app
module as part of M2.

## Gradle, JDK and SDK

Both Android feasibility projects use AGP 9.2.0, compile/target API 37 and
probe-only minimum API 31. The project-local M2 tools are under
`D:/Playground/LiveStream/.tools/m2` and are excluded from source control:

- Gradle: `D:/Playground/LiveStream/.tools/m2/gradle-9.4.1`
- Gradle JDK: `D:/Playground/LiveStream/.tools/m2/jdk-17.0.20.1+1`
- Android SDK: `D:/Playground/LiveStream/.tools/m2/android-sdk`

If Android Studio requests a Gradle JDK, select the project-local JDK 17 above.
If it requests an Android SDK location, use the project-local SDK above. The
Android projects do not currently include a Gradle wrapper; do not add one or
change shared/system tool installations just to make IDE import succeed. If
Studio's Gradle import cannot use the project-local Gradle distribution, use
the documented PowerShell runners instead; those configure the local tools,
cache and SDK explicitly and restore the caller's environment afterward.

Do not accept new SDK licenses automatically. The runner reuses previously
accepted licenses and intentionally does not accept additional licenses.

## Build and device workflow

The documented workspace-root PowerShell commands remain the reproducible
build/deploy path. See the project-specific instructions:

- [Capability inventory README](feasibility/android-inventory/README.md)
- [Media experiment README](feasibility/android-media/README.md)

Use Studio for source navigation, Gradle inspection, and (if needed) manual
device interaction. Device deployments and retained reports should follow the
documented M2 scripts and evidence procedures. Keep credentials and device
serials out of committed files and logs.

## Android Virtual Devices for supplementary testing

Use Android Studio AVDs where they provide useful, repeatable coverage, such as
UI flows, screen sizes/orientations, selected Android API levels, lifecycle and
permission behavior, and applicable instrumented tests. Select API levels and
device profiles based on the eventual supported-device matrix; those production
targets remain undecided. AVD installation/configuration is optional for current
M2 work and must not silently change the isolated probes' stack or SDK choices.

An emulator is not a substitute for the required Pixel phone/watch and real
broadcast checks. Do not use emulator results to claim support for physical
camera/lens selection, microphone routes, sustained hardware encoding/thermal
behavior, direct watch pairing, real Wi-Fi/mobile-data handoff, or received
platform broadcasts. Keep emulator and physical-device evidence labeled
separately. Check the required physical devices are connected when a task needs
them; ask the owner to connect/authorize them if not, while continuing work that
does not need those devices.

## Physical device connection before device-dependent work

Before starting development, debugging, or validation that depends on an
Android phone, explicitly check whether the required phone(s) are connected and
authorized in ADB/Android Studio. If not, ask the owner to connect or authorize
the relevant phone(s) before proceeding with that dependent work. Name the
needed phone role/model and the action needed (USB debugging/authorization or
the already-approved wireless debugging path). Do not claim a device test ran
when it did not. Continue independent documentation, host-side tests, and
research while waiting; never block unrelated work on device availability.
Apply the same check to a watch when the task needs physical Wear OS validation.

## Scope and decision boundary

These projects are exploratory M2 tools, not release deliverables. Their AGP,
SDK, JDK, library, module and tool settings do not decide the production
language/UI/media/storage/transport stack or minimum SDK. Those remain subject
to the M2 decision ledger and G2. M3–M8 work still requires G2 and a further
owner request. Continue to follow the approved [TDD working agreement](plan/MASTER_PLAN.md#tdd-working-agreement)
for authorized behavioral probe changes.