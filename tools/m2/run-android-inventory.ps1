[CmdletBinding(PositionalBinding = $false)]
param(
    [ValidateSet('inventory', 'media', 'watch')][string]$Probe = 'inventory',
    [Parameter(Position = 0, ValueFromRemainingArguments = $true)][string[]]$GradleArguments = @('assembleDebug')
)
$ErrorActionPreference = 'Stop'
$m2InventoryWorkspace = [System.IO.Path]::GetFullPath((Join-Path $PSScriptRoot '../..'))
$m2InventoryTools = Join-Path $m2InventoryWorkspace '.tools/m2'
$m2InventorySdk = Join-Path $m2InventoryTools 'android-sdk'
$m2InventoryJava = Join-Path $m2InventoryTools 'jdk-17.0.20.1+1'
$m2InventoryLauncher = Join-Path $m2InventoryTools 'gradle-9.4.1/lib/gradle-gradle-cli-main-9.4.1.jar'
$m2InventoryExistingSdk = Join-Path $env:LOCALAPPDATA 'Android/Sdk'
if (-not (Test-Path -LiteralPath $m2InventoryLauncher)) { throw 'Run bootstrap-jvm.ps1 first.' }
$m2InventoryDebugKey = Join-Path $m2InventoryTools 'inventory-debug.keystore'
if (-not (Test-Path -LiteralPath $m2InventoryDebugKey)) {
    & (Join-Path $m2InventoryJava 'bin/keytool.exe') -genkeypair -keystore $m2InventoryDebugKey -storepass android -keypass android -alias androiddebugkey -dname 'CN=Android Debug,O=Android,C=US' -keyalg RSA -keysize 2048 -validity 3650
    if ($LASTEXITCODE -ne 0) { throw 'Project-local diagnostic debug-key generation failed.' }
}
New-Item -ItemType Directory -Force -Path (Join-Path $m2InventorySdk 'licenses') | Out-Null
if (Test-Path -LiteralPath (Join-Path $m2InventoryExistingSdk 'licenses')) {
    Get-ChildItem -LiteralPath (Join-Path $m2InventoryExistingSdk 'licenses') -File | ForEach-Object {
        Copy-Item -LiteralPath $_.FullName -Destination (Join-Path $m2InventorySdk 'licenses')
    }
}
# Reuse previously accepted SDK licenses; never accept a new license automatically.
$m2InventoryPreviousJava = $env:JAVA_HOME
$m2InventoryPreviousGradle = $env:GRADLE_USER_HOME
$m2InventoryPreviousSdk = $env:ANDROID_HOME
$m2InventoryPreviousUser = $env:ANDROID_USER_HOME
try {
    $env:JAVA_HOME = $m2InventoryJava
    $env:GRADLE_USER_HOME = Join-Path $m2InventoryTools 'gradle-user-home'
    $env:ANDROID_HOME = $m2InventorySdk
    $env:ANDROID_USER_HOME = Join-Path $m2InventoryTools 'android-user-home'
    New-Item -ItemType Directory -Force -Path $env:ANDROID_USER_HOME | Out-Null
    & (Join-Path $m2InventoryJava 'bin/java.exe') '-Xmx64m' '-Xms64m' '-Dorg.gradle.appname=gradle' -jar $m2InventoryLauncher --no-daemon --console=plain --project-dir (Join-Path $m2InventoryWorkspace "feasibility/android-$Probe") @GradleArguments
    $m2InventoryResult = $LASTEXITCODE
} finally {
    $env:JAVA_HOME = $m2InventoryPreviousJava
    $env:GRADLE_USER_HOME = $m2InventoryPreviousGradle
    $env:ANDROID_HOME = $m2InventoryPreviousSdk
    $env:ANDROID_USER_HOME = $m2InventoryPreviousUser
}
exit $m2InventoryResult
