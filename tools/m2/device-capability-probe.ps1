param(
    [Parameter(Mandatory = $true)][ValidateSet('Install', 'Collect', 'Diagnose')][string]$Action,
    [ValidateSet('inventory', 'media')][string]$Probe = 'inventory',
    [string]$OutputDirectory
)
$ErrorActionPreference = 'Stop'
$m2CapabilityWorkspace = [System.IO.Path]::GetFullPath((Join-Path $PSScriptRoot '../..'))
$m2CapabilityAdb = Join-Path $m2CapabilityWorkspace '.tools/m2/android-sdk/platform-tools/adb.exe'
if (-not (Test-Path -LiteralPath $m2CapabilityAdb)) {
    $m2CapabilityAdb = Join-Path $env:LOCALAPPDATA 'Android/Sdk/platform-tools/adb.exe'
}
$m2CapabilityApkDirectory = Join-Path $m2CapabilityWorkspace "feasibility/android-$Probe/build/outputs/apk/debug"
$m2CapabilityPackage = if ($Probe -eq 'inventory') { 'livestream.feasibility.inventory' } else { 'livestream.feasibility.mediaprobe' }
$m2CapabilityActivity = if ($Probe -eq 'inventory') { 'InventoryActivity' } else { 'MediaActivity' }
$m2CapabilityReportName = if ($Probe -eq 'inventory') { 'capability-report.json' } else { 'media-report.json' }
if (-not $OutputDirectory) { $OutputDirectory = "plan/evidence/m2/android-$Probe-2026-10-24" }
$m2CapabilityTargets = @('Pixel 8 Pro', 'Pixel 10 Pro XL')
$m2CapabilityListing = & $m2CapabilityAdb devices 2>&1
if ($LASTEXITCODE -ne 0) { throw 'ADB device discovery failed.' }
$m2CapabilityCount = 0
foreach ($m2CapabilityLine in $m2CapabilityListing) {
    if ($m2CapabilityLine.ToString() -notmatch '^(\S+)\s+device\s*$') { continue }
    $m2CapabilityPrivateId = $matches[1]
    $m2CapabilityModelOutput = & $m2CapabilityAdb -s $m2CapabilityPrivateId shell getprop ro.product.model 2>$null
    if ($LASTEXITCODE -ne 0) { throw 'Model read failed.' }
    $m2CapabilityModel = ($m2CapabilityModelOutput | Out-String).Trim()
    if ($m2CapabilityTargets -notcontains $m2CapabilityModel) { continue }
    $m2CapabilityCount++
    if ($Action -eq 'Install') {
        $m2CapabilityMetadataFile = Join-Path $m2CapabilityApkDirectory 'output-metadata.json'
        if (-not (Test-Path -LiteralPath $m2CapabilityMetadataFile)) { throw 'Build the inventory APK first.' }
        $m2CapabilityMetadata = Get-Content -Encoding UTF8 -Raw -LiteralPath $m2CapabilityMetadataFile | ConvertFrom-Json
        if ($m2CapabilityMetadata.applicationId -ne $m2CapabilityPackage -or @($m2CapabilityMetadata.elements).Count -ne 1) {
            throw 'Unexpected APK output metadata.'
        }
        $m2CapabilityOutputFile = $m2CapabilityMetadata.elements[0].outputFile
        if ([System.IO.Path]::GetFileName($m2CapabilityOutputFile) -ne $m2CapabilityOutputFile) { throw 'Unexpected APK output path.' }
        $m2CapabilityApk = Join-Path $m2CapabilityApkDirectory $m2CapabilityOutputFile
        if (-not (Test-Path -LiteralPath $m2CapabilityApk)) { throw 'Build the inventory APK first.' }
        $m2CapabilityInstallOutput = & $m2CapabilityAdb -s $m2CapabilityPrivateId install -r $m2CapabilityApk 2>&1
        if ($LASTEXITCODE -ne 0) { throw "Inventory APK installation failed on $m2CapabilityModel." }
        $m2CapabilityLaunchOutput = & $m2CapabilityAdb -s $m2CapabilityPrivateId shell am start -W -n "$m2CapabilityPackage/.$m2CapabilityActivity" 2>&1
        if ($LASTEXITCODE -ne 0 -or ($m2CapabilityLaunchOutput | Out-String) -match 'Error:') {
            throw "Inventory probe launch failed on $m2CapabilityModel."
        }
        [pscustomobject]@{ Model = $m2CapabilityModel; Result = 'Inventory APK installed and opened'; ApkSha256 = (Get-FileHash -LiteralPath $m2CapabilityApk -Algorithm SHA256).Hash }
    } elseif ($Action -eq 'Diagnose') {
        $m2CapabilityPid = (& $m2CapabilityAdb -s $m2CapabilityPrivateId shell pidof $m2CapabilityPackage 2>$null | Out-String).Trim()
        $m2CapabilityLaunch = & $m2CapabilityAdb -s $m2CapabilityPrivateId shell am start -W -n "$m2CapabilityPackage/.$m2CapabilityActivity" 2>&1
        $m2CapabilityLaunchText = ($m2CapabilityLaunch | Out-String)
        $m2CapabilityCrashLines = @(& $m2CapabilityAdb -s $m2CapabilityPrivateId logcat -d -v brief -t 300 -s 'AndroidRuntime:E' 2>$null)
        $m2CapabilityCrashPids = @($m2CapabilityCrashLines | ForEach-Object {
            if ($_.ToString() -match ('^E/AndroidRuntime\(\s*(\d+)\): Process: ' + [regex]::Escape($m2CapabilityPackage) + ',')) { $matches[1] }
        } | Select-Object -Unique)
        $m2CapabilityOwnCrash = @($m2CapabilityCrashLines | Where-Object {
            $m2CapabilityCrashLine = $_.ToString()
            @($m2CapabilityCrashPids | Where-Object { $m2CapabilityCrashLine -match ('^E/AndroidRuntime\(\s*' + $_ + '\):') }).Count -gt 0
        } | Select-Object -Last 45)
        [pscustomobject]@{
            Model = $m2CapabilityModel
            RunningPidBeforeRelaunch = $m2CapabilityPid
            LaunchAccepted = ($m2CapabilityLaunchText -notmatch 'Error:' -and $m2CapabilityLaunchText -match 'Status: ok')
            MatchedAppCrash = ($m2CapabilityOwnCrash.Count -gt 0)
            AppCrash = ($m2CapabilityOwnCrash | Out-String).Trim()
        } | Format-List
    } else {
        $m2CapabilityReportOutput = & $m2CapabilityAdb -s $m2CapabilityPrivateId exec-out run-as $m2CapabilityPackage cat "files/$m2CapabilityReportName" 2>$null
        if ($LASTEXITCODE -ne 0) { throw "Probe report is not yet available on $m2CapabilityModel." }
        $m2CapabilityReport = ($m2CapabilityReportOutput | Out-String).Trim()
        $m2CapabilityParsed = $m2CapabilityReport | ConvertFrom-Json
        if ($m2CapabilityParsed.probeVersion -ne "m2-$Probe-1" -or $m2CapabilityParsed.model -ne $m2CapabilityModel) {
            throw 'Unexpected inventory report identity.'
        }
        $m2CapabilityDestination = [System.IO.Path]::GetFullPath((Join-Path $m2CapabilityWorkspace $OutputDirectory))
        if (-not $m2CapabilityDestination.StartsWith($m2CapabilityWorkspace + [System.IO.Path]::DirectorySeparatorChar, [StringComparison]::OrdinalIgnoreCase)) {
            throw 'Evidence output must remain within the workspace.'
        }
        New-Item -ItemType Directory -Force -Path $m2CapabilityDestination | Out-Null
        $m2CapabilityFile = ($m2CapabilityModel.ToLowerInvariant() -replace '[^a-z0-9]+', '-') + '.json'
        [System.IO.File]::WriteAllText((Join-Path $m2CapabilityDestination $m2CapabilityFile), $m2CapabilityReport, [System.Text.UTF8Encoding]::new($false))
        if ($Probe -eq 'media') {
            [pscustomobject]@{
                Model = $m2CapabilityParsed.model
                StopReason = $m2CapabilityParsed.stopReason
                FileBytes = $m2CapabilityParsed.fileBytes
                VideoSamples = @($m2CapabilityParsed.tracks | Where-Object { $_.mime -like 'video/*' } | Select-Object -ExpandProperty samples)
                AudioSamples = @($m2CapabilityParsed.tracks | Where-Object { $_.mime -like 'audio/*' } | Select-Object -ExpandProperty samples)
                ObservedAudioInputTypes = @($m2CapabilityParsed.observedAudioInputTypes)
                InspectionError = $m2CapabilityParsed.inspectionError
                SetupError = $m2CapabilityParsed.setupError
                Report = (Join-Path $OutputDirectory $m2CapabilityFile)
            }
        } else { [pscustomobject]@{
            Model = $m2CapabilityParsed.model
            CameraPermission = $m2CapabilityParsed.cameraPermission
            AudioPermission = $m2CapabilityParsed.audioPermission
            CameraEntries = @($m2CapabilityParsed.cameras | Where-Object { $null -ne $_ }).Count
            EncoderEntries = @($m2CapabilityParsed.encoders | Where-Object { $null -ne $_ }).Count
            AudioInputs = @($m2CapabilityParsed.advertisedAudioInputs | Where-Object { $null -ne $_ }).Count
            CameraInventoryError = $m2CapabilityParsed.cameraInventoryError
            EncoderInventoryError = $m2CapabilityParsed.encoderInventoryError
            AudioInventoryError = $m2CapabilityParsed.audioInventoryError
            Capture = $m2CapabilityParsed.cameraCapture
            Report = (Join-Path $OutputDirectory $m2CapabilityFile)
        } }
    }
}
if ($m2CapabilityCount -eq 0) { throw 'Neither authorized target phone is currently visible to ADB.' }
