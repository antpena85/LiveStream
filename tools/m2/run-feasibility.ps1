param([Parameter(ValueFromRemainingArguments = $true)][string[]]$GradleArguments = @('test'))
$ErrorActionPreference = 'Stop'
$m2Workspace = [System.IO.Path]::GetFullPath((Join-Path $PSScriptRoot '../..'))
$m2ToolRoot = Join-Path $m2Workspace '.tools/m2'
$m2Gradle = Join-Path $m2ToolRoot 'gradle-9.4.1/bin/gradle.bat'
$m2JavaRoot = Join-Path $m2ToolRoot 'jdk-17.0.20.1+1'
if (-not (Test-Path -LiteralPath $m2Gradle) -or -not (Test-Path -LiteralPath (Join-Path $m2JavaRoot 'bin/java.exe'))) {
    throw 'Run tools/m2/bootstrap-jvm.ps1 before this command.'
}
$m2PreviousJava = $env:JAVA_HOME
$m2PreviousGradle = $env:GRADLE_USER_HOME
try {
    $env:JAVA_HOME = $m2JavaRoot
    $env:GRADLE_USER_HOME = Join-Path $m2ToolRoot 'gradle-user-home'
    $m2JavaExecutable = Join-Path $m2JavaRoot 'bin/java.exe'
    $m2GradleLauncher = Join-Path $m2ToolRoot 'gradle-9.4.1/lib/gradle-gradle-cli-main-9.4.1.jar'
    & $m2JavaExecutable '-Xmx64m' '-Xms64m' '-Dorg.gradle.appname=gradle' -jar $m2GradleLauncher --no-daemon --console=plain --project-dir (Join-Path $m2Workspace 'feasibility') @GradleArguments
    $m2Result = $LASTEXITCODE
} finally {
    $env:JAVA_HOME = $m2PreviousJava
    $env:GRADLE_USER_HOME = $m2PreviousGradle
}
exit $m2Result
