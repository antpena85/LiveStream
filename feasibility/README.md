# M2 JVM feasibility probe

The independent [Android inventory tool](android-inventory/README.md) now has
an actual API 37 APK build/deployment workflow. It lists capability metadata
on the two connected phones and is not the streaming application or the JVM
behavioral suite described below.

Authorized: 2026-10-23. This isolated Kotlin/JVM project verifies two T02
readiness/confirmation behaviors and ten T01 real-SQLite cases, including
reopen/retry, abrupt child-JVM termination before commit and after commit
before acknowledgment,
eight concurrent distinct commands and simultaneous duplicate delivery through
a shared store instance, plus reset generations, old-command rejection, scoped
receipts, canceled reset and abrupt interruption during reset. The twelve-test
suite includes eight observed red/green cycles and four first-run verifications
of existing behavior. It is
not the Android application or a media pipeline, and it does not establish
device/provider feasibility, every crash timing or power-loss recovery.

From the repository workspace root on Windows:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File tools/m2/bootstrap-jvm.ps1
powershell -NoProfile -ExecutionPolicy Bypass -File tools/m2/run-feasibility.ps1 test
```

After dependencies are cached, add `--offline` after `test`. The scripts use
project-local JDK/Gradle distributions and caches. Windows' process-only policy
override does not change the system policy. The initial downloads and dependency
resolution need network access; scripts, source and dependency versions are
available for review before running.

The HTML report is generated under `feasibility/build/reports/tests/test/`.
The retained [TDD evidence](../plan/evidence/m2/README.md) distinguishes observed
missing-behavior failures from the initial dependency-resolution error. See
[M2.8](../plan/specifications/m2-8-tdd-tooling-and-boundaries.md) for confirmed
boundaries and remaining limits. No external account or broadcast is accessed.

The [ST05 contract/evidence](../plan/evidence/m2/st05-reset-isolation.md) specifies
the probe-only epoch/reset behavior. Commands explicitly target an epoch; old
commands cannot adopt the new one after reset. Epochs are local to one file.
The fixture uses fresh temporary databases and does not migrate earlier probe
schemas or select the application's final identity/receipt-retention mechanism.

The [ST03 verification](../plan/evidence/m2/st03-lost-acknowledgment.md) halts
the child after a point commits but before sending its receipt. Reopening and
retrying return the original receipt without applying another point or
replacing later accepted state. The existing store implementation is unchanged.
