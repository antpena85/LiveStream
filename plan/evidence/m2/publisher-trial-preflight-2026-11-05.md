# Publisher trial preflight — 2026-11-05

Scope: read-only device visibility and build checks for the isolated M2 media
experiment. No APK was installed, capture started, destination contacted, or
broadcast attempted. This is not application or hardware acceptance evidence.

## Observations

- The project-local ADB reported authorized `device` entries for two Pixel 8
  Pro phones, one Pixel 10 Pro XL and one Pixel Watch 4. No serial numbers were
  retained. Visibility does not establish current pairing, roles, installed
  builds, permissions, or suitability for the planned trial.
- The documented offline `media assembleDebug` command succeeded; all 35
  actionable tasks were up to date. This verifies the current local-only build
  is available, not that it was freshly installed or run on a phone.
- A forced offline JVM suite and the confirmed-stop publisher simulation passed
  in the preceding continuation. The latter is a first-run verification, not
  a red/green cycle or proof of an actual publisher stopping.

## Trial gate and next action

The [accelerated prototype gate](../../specifications/accelerated-prototype.md)
requires a buildable Android publisher, explicit confirmed start/stop, actual
connection/failure status, credential-safe handling, cleanup, and applicable
numeric-fit and durable pending-stop safeguards. The existing
[media experiment](../../../feasibility/android-media/README.md) performs only
local recording and its permission gate cannot authorize publishing. The
host-only publisher adapter and SQLite probe are not Android integration or
proof of real external termination. These gaps block a received-stream trial.

Continue at the confirmed T01/T02 boundaries with a small test-driven slice
toward an Android-compatible safety boundary and its durable implementation;
do not replace pending-stop or fit checks with a permissive stub. Preserve the
existing uncommitted publisher tests and specification changes. Before any
physical trial, recheck device authorization, select the specific phone and
build without exposing serials, and have the operator select an approved test
destination and trial duration. Credentials belong on the device, not in
source, chat, reports, screenshots, or logs. The operator must initiate the
trial and inspect received video, score updates, and audio separately from
connection callbacks.

M2.2 and G2 remain open. Local recording and a successful build do not
establish received media, safe publishing, synchronization, or sustained load.