# M2 Android capability inventory

Exploratory measurement tool for C01–C03 and advertised audio inputs. This is
separate from the JVM store/session probe and the future release application.
It does not open a camera, record audio, encode media or publish. A capability
advertisement does not demonstrate that a capture/streaming path works.

The probe uses AGP 9.2.0, compile/target API 37 and probe-only minimum API 31.
These do not select the release application's minimum SDK, UI or media stack.
[AGP compatibility](https://developer.android.com/build/releases/agp-9-2-0-release-notes)
documents JDK 17, Gradle 9.4.1 and Build Tools 36.0.0. The runner uses the
project-local JVM tools, Gradle cache and Android SDK, reusing existing accepted
SDK licenses. The diagnostic debug key lives in ignored `.tools/m2` and is
not a release signing identity.

From the workspace root on Windows:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File tools/m2/run-android-inventory.ps1 assembleDebug
powershell -NoProfile -ExecutionPolicy Bypass -File tools/m2/inventory-devices.ps1 -OutputPath plan/evidence/m2/device-inventory-2026-10-24.json
powershell -NoProfile -ExecutionPolicy Bypass -File tools/m2/device-capability-probe.ps1 -Action Install
```

The first build needs network access; use `assembleDebug --offline` only after
all dependencies and SDK packages are cached. The scripts do not accept new
SDK licenses automatically. The installation script targets only the observed
Pixel 8 Pro and Pixel 10 Pro XL, installs this diagnostic package and opens its
activity. It does not clear phone data or change system settings.

On each phone, tap **Read capabilities**, then grant camera/microphone access
when prompted. The app saves its allowlisted JSON report in private storage.
Collect it with:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File tools/m2/device-capability-probe.ps1 -Action Collect
```

Reports include model/OS/build, permission state, app-visible camera metadata,
AVC/AAC encoder advertisements and audio input types. They omit device serials,
account identifiers, audio device names, network names and publishing secrets.
Capture/encoding/publishing remain explicitly `not_run`. USB or wireless ADB
authorization is required for deployment and collection; it is not needed to
build the APK. [ADB setup](https://developer.android.com/tools/adb).

To open this isolated project in Android Studio, select this directory—the one
containing `settings.gradle.kts`—with **File → Open**. Use the project-local
JDK/Gradle/SDK and M2-only scope described in the root
[Android Studio workflow](../../ANDROID_STUDIO.md). IDE import does not turn
this diagnostic into the release application.

Unknown hardware capabilities are exploratory M2 observations. Building this
inventory tool is not a behavioral red/green cycle or completion of T02/T05/T06
application behavior. The established TDD agreement remains required for each
future implementation behavior or bug fix.
