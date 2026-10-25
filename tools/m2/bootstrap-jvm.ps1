param()
$ErrorActionPreference = 'Stop'
$m2Workspace = [System.IO.Path]::GetFullPath((Join-Path $PSScriptRoot '../..'))
$m2ToolRoot = Join-Path $m2Workspace '.tools/m2'
$m2Downloads = Join-Path $m2ToolRoot 'downloads'
New-Item -ItemType Directory -Force -Path $m2Downloads | Out-Null

function Get-VerifiedArchive {
    param([string]$Name, [string]$Url, [string]$Sha256)
    $m2Archive = Join-Path $m2Downloads $Name
    if (-not (Test-Path -LiteralPath $m2Archive)) {
        Write-Host "Downloading $Name"
        & curl.exe --fail --location --retry 2 --output $m2Archive $Url
        if ($LASTEXITCODE -ne 0) { throw "Download failed: $Name" }
    }
    $m2Actual = (Get-FileHash -LiteralPath $m2Archive -Algorithm SHA256).Hash.ToLowerInvariant()
    if ($m2Actual -ne $Sha256) { throw "Checksum mismatch: $Name; retain the archive for inspection." }
    Write-Host "Verified $Name SHA256 $m2Actual"
    return $m2Archive
}

$m2JdkArchive = @(Get-VerifiedArchive -Name 'temurin-17.0.20.1_1.zip' -Url 'https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.20.1%2B1/OpenJDK17U-jdk_x64_windows_hotspot_17.0.20.1_1.zip' -Sha256 'e53a79c3c3d86865bd7e787903884331068e71321714ffd44f145785affc7cb0')
$m2Java = Join-Path $m2ToolRoot 'jdk-17.0.20.1+1/bin/java.exe'
if (-not (Test-Path -LiteralPath $m2Java)) {
    Expand-Archive -LiteralPath $m2JdkArchive[-1] -DestinationPath $m2ToolRoot
}
$m2GradleArchive = @(Get-VerifiedArchive -Name 'gradle-9.4.1-bin.zip' -Url 'https://services.gradle.org/distributions/gradle-9.4.1-bin.zip' -Sha256 '2ab2958f2a1e51120c326cad6f385153bb11ee93b3c216c5fccebfdfbb7ec6cb')
$m2Gradle = Join-Path $m2ToolRoot 'gradle-9.4.1/bin/gradle.bat'
if (-not (Test-Path -LiteralPath $m2Gradle)) {
    Expand-Archive -LiteralPath $m2GradleArchive[-1] -DestinationPath $m2ToolRoot
}
& $m2Java -version
if ($LASTEXITCODE -ne 0) { throw 'The verified JDK did not run.' }
Write-Output 'M2 JVM tooling is ready under .tools/m2; global installations are unchanged.'
