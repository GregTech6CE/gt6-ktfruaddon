<#
.SYNOPSIS
Boots the addon next to GT6CE and checks that GT6CE accepted it.

.DESCRIPTION
Runs `gradlew runGameTestServer`: a NeoForge run that goes through mod loading and stops by itself.
GT6CE dispatches onRecipesReady to the addon during common setup, so every unknown part, duplicate
registry key, unresolved recipe stack and failed self-check shows up here. A GameTestServer needs no
EULA. The addon has no game tests, so the server stops right after mod loading with "No test
functions were given!". That error is expected; the checks below are about mod loading.

(`runData` does not work for this: a data run never fires the common setup event, so it passes
without GT6CE ever dispatching the addon.)

The check passes when
  - Gradle exits with 0,
  - the log has the addon's "gt6ktfruaddon: ready on" line,
  - the log has GT6CE's "GT addon API <n> ready: [...]" line naming gt6ktfruaddon, which GT6CE
    prints only after every addon's onRecipesReady returned,
  - none of "Loading errors encountered", "GT addon recipes failed", "source stacks did not resolve",
    "uses unknown part" or "declared blocks or items are not registered" appears.

A failed run prints the first loading error from the log. Gradle can exit with 0 even when mod
loading fails (a material form GT6CE rejects did that), so the log markers are what counts. The
Gradle output is kept in build/smoke-boot.log.

.PARAMETER TimeoutMinutes
Stop the run and fail after this long. Default 20.

.PARAMETER LogPath
Where to keep the Gradle output. Default: <repo>\build\smoke-boot.log.

.EXAMPLE
.\tools\smoke-boot.ps1
#>
[CmdletBinding()]
param(
    [int] $TimeoutMinutes = 20,
    [string] $LogPath
)

Set-StrictMode -Version 2.0
$ErrorActionPreference = 'Stop'

$repoRoot = Split-Path -Parent $PSScriptRoot
if (-not $LogPath) { $LogPath = Join-Path $repoRoot 'build\smoke-boot.log' }
New-Item -ItemType Directory -Force -Path (Split-Path -Parent $LogPath) | Out-Null
$errPath = $LogPath + '.err'
Remove-Item $LogPath, $errPath -ErrorAction SilentlyContinue

$process = Start-Process -FilePath (Join-Path $repoRoot 'gradlew.bat') `
    -ArgumentList @('runGameTestServer', '--console=plain') -WorkingDirectory $repoRoot `
    -RedirectStandardOutput $LogPath -RedirectStandardError $errPath -NoNewWindow -PassThru
$null = $process.Handle   # keeps the exit code readable after the process ends
if (-not $process.WaitForExit($TimeoutMinutes * 60 * 1000)) {
    & taskkill /PID $process.Id /T /F | Out-Null
    throw "Timed out after $TimeoutMinutes minutes; the output so far is in $LogPath"
}

function Test-Log([string] $Pattern, [switch] $Regex) {
    if ($Regex) { return [bool](Select-String -Path $LogPath -Pattern $Pattern -Quiet) }
    return [bool](Select-String -Path $LogPath -Pattern $Pattern -SimpleMatch -Quiet)
}

$problems = New-Object System.Collections.Generic.List[string]
if ($process.ExitCode -ne 0) { $problems.Add("Gradle exited with $($process.ExitCode)") }
if (-not (Test-Log 'gt6ktfruaddon: ready on')) { $problems.Add("the addon's 'ready on' line is missing") }
if (-not (Test-Log 'GT addon API \d+ ready: \[.*gt6ktfruaddon' -Regex)) {
    $problems.Add("GT6CE's 'GT addon API ... ready' line naming gt6ktfruaddon is missing")
}
foreach ($bad in 'Loading errors encountered', 'GT addon recipes failed', 'source stacks did not resolve',
        'uses unknown part', 'declared blocks or items are not registered') {
    if (Test-Log $bad) { $problems.Add("the log contains '$bad'") }
}

# The stretch of the log that names the cause: the loading error block, then the first causes.
function Show-Cause {
    $lines = @(Get-Content -Path $LogPath)
    for ($i = 0; $i -lt $lines.Count; $i++) {
        if ($lines[$i] -match 'Loading errors encountered') {
            for ($j = $i; $j -lt [Math]::Min($i + 6, $lines.Count) -and $lines[$j].Trim() -ne ''; $j++) {
                Write-Host "  | $($lines[$j].TrimEnd())"
            }
            break
        }
    }
    foreach ($cause in @($lines | Where-Object { $_ -match '^Caused by:' } | Select-Object -First 2)) {
        Write-Host "  | $cause"
    }
}

if ($problems.Count -gt 0) {
    Write-Host 'Smoke boot FAILED:' -ForegroundColor Red
    $problems | ForEach-Object { Write-Host "  - $_" }
    Show-Cause
    Write-Host "Log: $LogPath"
    exit 1
}
Write-Host 'Smoke boot passed: GT6CE accepted the addon.' -ForegroundColor Green
Write-Host "Log: $LogPath"
