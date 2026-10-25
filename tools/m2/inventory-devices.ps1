param(
    [string]$AdbPath = (Join-Path $env:LOCALAPPDATA 'Android/Sdk/platform-tools/adb.exe'),
    [string]$OutputPath
)
$ErrorActionPreference = 'Stop'
$m2DeviceListing = & $AdbPath devices -l 2>&1
if ($LASTEXITCODE -ne 0) { throw 'ADB discovery failed; no device conclusion can be drawn.' }
$m2DeviceFacts = @()
$m2DeviceNumber = 0
foreach ($m2DeviceLine in $m2DeviceListing) {
    if ($m2DeviceLine.ToString() -notmatch '^(\S+)\s+(device|unauthorized|offline)\b') { continue }
    $m2PrivateDeviceId = $matches[1]
    $m2DeviceState = $matches[2]
    $m2DeviceNumber++
    $m2Fact = [ordered]@{ label = "usb-device-$m2DeviceNumber"; adbState = $m2DeviceState }
    if ($m2DeviceState -eq 'device') {
        $m2PropertyMap = [ordered]@{
            model = 'ro.product.model'
            manufacturer = 'ro.product.manufacturer'
            androidRelease = 'ro.build.version.release'
            api = 'ro.build.version.sdk'
            buildId = 'ro.build.id'
            securityPatch = 'ro.build.version.security_patch'
        }
        foreach ($m2PropertyLabel in $m2PropertyMap.Keys) {
            $m2PropertyValue = & $AdbPath -s $m2PrivateDeviceId shell getprop $m2PropertyMap[$m2PropertyLabel] 2>$null
            if ($LASTEXITCODE -ne 0) { throw 'An allowlisted device-property read failed.' }
            $m2Fact[$m2PropertyLabel] = ($m2PropertyValue | Out-String).Trim()
        }
    }
    $m2DeviceFacts += [pscustomobject]$m2Fact
}
$m2DeviceReport = [ordered]@{
    observedAtUtc = [DateTime]::UtcNow.ToString('o')
    scope = 'ADB availability and allowlisted model/OS facts only; no capture, pairing or streaming validation'
    devices = @($m2DeviceFacts)
}
$m2DeviceJson = $m2DeviceReport | ConvertTo-Json -Depth 5
if ($OutputPath) {
    $m2DeviceDestination = [System.IO.Path]::GetFullPath($OutputPath)
    $m2DeviceWorkspace = [System.IO.Path]::GetFullPath((Join-Path $PSScriptRoot '../..'))
    if (-not $m2DeviceDestination.StartsWith($m2DeviceWorkspace + [System.IO.Path]::DirectorySeparatorChar, [StringComparison]::OrdinalIgnoreCase)) {
        throw 'Evidence output must remain within the workspace.'
    }
    [System.IO.File]::WriteAllText($m2DeviceDestination, $m2DeviceJson, [System.Text.UTF8Encoding]::new($false))
}
$m2DeviceJson
