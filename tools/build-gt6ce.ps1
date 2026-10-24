<#
.SYNOPSIS
Builds the two GT6CE jars this addon compiles and runs against.

.DESCRIPTION
Mirrors the "Build GT6CE" step of .github/workflows/build.yml for the commit pinned in
libs/gt6ce/build-commit.txt:

  1. clones the GT6CE repository into a private working copy and checks the pinned commit out,
  2. runs prepareNativeTools,
  3. builds :multiversion:1.21.1-neoforge:addonSdkJar and :multiversion:1.21.1-neoforge:jar,
  4. copies the SDK jar and the mod jar to libs/gt6ce/ and compares the mod jar with
     libs/gt6ce/gt6ce-1.21.1-neoforge.jar.sha256.

The source repository is only read. The build runs in the working copy, so a GregTech6 checkout
you are working in is never modified. Both jars are git-ignored and replaced on every run.

Requirements: git, Python 3 on PATH (GT6CE's prepareNativeTools runs a Python script), JDK 17 and
JDK 21. JDKs are looked up in the parameters, JAVA17_HOME / JAVA21_HOME, JAVA_HOME, the usual
install folders and the Gradle JDK cache (~/.gradle/jdks).

.PARAMETER Source
Git URL or path of the GT6CE repository to clone from. Default: $env:GT6CE_REPO, then a sibling
..\GregTech6 that contains the pinned commit, then https://github.com/GregTech6CE/GregTech6.git.

.PARAMETER WorkDir
Where the pinned checkout lives. Default: <repo>\.gt6ce-src (git-ignored). It is reused between
runs, so Gradle's incremental state survives.

.PARAMETER Commit
Full commit SHA to build. Default: the content of libs/gt6ce/build-commit.txt.

.PARAMETER Jdk17
JDK 17 home. Default: $env:JAVA17_HOME, then a search of the usual places.

.PARAMETER Jdk21
JDK 21 home. Default: $env:JAVA21_HOME, then $env:JAVA_HOME if it is a JDK 21, then a search.

.PARAMETER Python
Python executable for prepareNativeTools. Default: whatever GT6CE's build picks ("python").

.PARAMETER AutoDownloadJdk
Let Gradle download a missing JDK 17 (about 190 MB, into ~/.gradle/jdks). Off by default, as in CI.

.PARAMETER UpdateChecksum
Rewrite libs/gt6ce/gt6ce-1.21.1-neoforge.jar.sha256 with the hash of the jar just built. Use it
after bumping build-commit.txt.

.PARAMETER NoBuild
Only prepare the working copy at the pinned commit and stop. The generators in tools/ read GT6CE
sources and resources from it (see tools/paths.py).

.PARAMETER DryRun
Print what would be used and stop before cloning or building.

.EXAMPLE
.\tools\build-gt6ce.ps1

.EXAMPLE
.\tools\build-gt6ce.ps1 -DryRun
#>
[CmdletBinding()]
param(
    [string] $Source,
    [string] $WorkDir,
    [string] $Commit,
    [string] $Jdk17,
    [string] $Jdk21,
    [string] $Python,
    [switch] $AutoDownloadJdk,
    [switch] $UpdateChecksum,
    [switch] $NoBuild,
    [switch] $DryRun
)

Set-StrictMode -Version 2.0
$ErrorActionPreference = 'Stop'

$DefaultRemote = 'https://github.com/GregTech6CE/GregTech6.git'
$Node = '1.21.1-neoforge'
$ModJarName = 'gt6ce-1.21.1-neoforge.jar'

function Write-Step([string] $Message) {
    Write-Host ('==> ' + $Message) -ForegroundColor Cyan
}

# Runs a native command and throws when it exits non-zero. Native stderr is not an error here:
# Windows PowerShell 5.1 would turn git's progress output into one under $ErrorActionPreference='Stop'.
function Invoke-Native([string] $What, [string] $File, [string[]] $Arguments, [string] $WorkingDirectory) {
    $previous = $ErrorActionPreference
    $ErrorActionPreference = 'Continue'
    Push-Location $WorkingDirectory
    try {
        & $File @Arguments
        $code = $LASTEXITCODE
    } finally {
        Pop-Location
        $ErrorActionPreference = $previous
    }
    if ($code -ne 0) { throw "$What failed (exit code $code)." }
}

# Returns git's trimmed stdout (possibly empty), or $null when git fails.
function Get-Git([string] $Directory, [string[]] $Arguments) {
    $previous = $ErrorActionPreference
    $ErrorActionPreference = 'Continue'
    try {
        $out = & git -C $Directory @Arguments 2>$null
        if ($LASTEXITCODE -ne 0) { return $null }
        return (($out | Out-String).Trim())
    } finally {
        $ErrorActionPreference = $previous
    }
}

function Test-HasCommit([string] $Repository, [string] $Sha) {
    return $null -ne (Get-Git $Repository @('cat-file', '-e', ($Sha + '^{commit}')))
}

function Get-JdkMajor([string] $JdkHome) {
    $release = Join-Path $JdkHome 'release'
    if (-not (Test-Path $release)) { return 0 }
    $line = Select-String -Path $release -Pattern '^JAVA_VERSION="?(\d+)' | Select-Object -First 1
    if ($null -eq $line) { return 0 }
    return [int] $line.Matches[0].Groups[1].Value
}

function Find-Jdk([int] $Major, [string] $Explicit, [string] $EnvName) {
    $candidates = New-Object System.Collections.Generic.List[string]
    if ($Explicit) { $candidates.Add($Explicit) }
    $fromEnv = [Environment]::GetEnvironmentVariable($EnvName)
    if ($fromEnv) { $candidates.Add($fromEnv) }
    if ($Major -eq 21 -and $env:JAVA_HOME) { $candidates.Add($env:JAVA_HOME) }
    $roots = @(
        (Join-Path $env:ProgramFiles 'Eclipse Adoptium'),
        (Join-Path $env:ProgramFiles 'Java'),
        (Join-Path $env:ProgramFiles 'Microsoft'),
        (Join-Path $env:USERPROFILE '.jdks'),
        (Join-Path $env:USERPROFILE '.gradle\jdks')
    )
    foreach ($root in $roots) {
        if (-not (Test-Path $root)) { continue }
        Get-ChildItem $root -Directory -Recurse -Depth 2 -ErrorAction SilentlyContinue |
            Where-Object { Test-Path (Join-Path $_.FullName 'bin\javac.exe') } |
            ForEach-Object { $candidates.Add($_.FullName) }
    }
    foreach ($candidate in $candidates) {
        if ((Test-Path (Join-Path $candidate 'bin\javac.exe')) -and ((Get-JdkMajor $candidate) -eq $Major)) {
            return $candidate
        }
    }
    return $null
}

$repoRoot = Split-Path -Parent $PSScriptRoot
$libs = Join-Path $repoRoot 'libs\gt6ce'
if (-not $WorkDir) { $WorkDir = Join-Path $repoRoot '.gt6ce-src' }

if (-not $Commit) {
    $pinFile = Join-Path $libs 'build-commit.txt'
    if (-not (Test-Path $pinFile)) { throw "Missing $pinFile; pass -Commit." }
    $Commit = (Get-Content $pinFile -Raw).Trim()
}
if ($Commit -notmatch '^[0-9a-f]{40}$') { throw "Commit must be a full 40-character SHA-1, got '$Commit'." }

if (-not $Source) { $Source = $env:GT6CE_REPO }
if (-not $Source) {
    $sibling = Join-Path (Split-Path -Parent $repoRoot) 'GregTech6'
    if ((Test-Path $sibling) -and (Test-HasCommit $sibling $Commit)) { $Source = $sibling }
}
if (-not $Source) { $Source = $DefaultRemote }

$jdk21 = Find-Jdk 21 $Jdk21 'JAVA21_HOME'
$jdk17 = Find-Jdk 17 $Jdk17 'JAVA17_HOME'
$pythonExe = if ($Python) { $Python } else { 'python' }

Write-Step 'Plan'
Write-Host "  commit   : $Commit"
Write-Host "  source   : $Source"
Write-Host "  workdir  : $WorkDir"
Write-Host "  JDK 21   : $(if ($jdk21) { $jdk21 } else { '(not found)' })"
Write-Host "  JDK 17   : $(if ($jdk17) { $jdk17 } else { '(not found)' })"
Write-Host "  python   : $pythonExe"
Write-Host "  output   : $libs"

if (-not $jdk21) { throw 'JDK 21 not found. Install Temurin 21 or pass -Jdk21 <path>.' }
if (-not $jdk17 -and -not $AutoDownloadJdk) {
    throw 'JDK 17 not found, and GT6CE core builds with a 17 toolchain. Install Temurin 17, pass -Jdk17 <path>, or pass -AutoDownloadJdk to let Gradle download one.'
}
if (-not (Get-Command $pythonExe -ErrorAction SilentlyContinue)) {
    throw "Python not found ('$pythonExe'). GT6CE's prepareNativeTools needs Python 3 on PATH; use -Python <path> if it lives elsewhere."
}
if ($DryRun) { Write-Step 'Dry run: stopping before clone and build.'; return }

# 1. Working copy at the pinned commit.
$fresh = -not (Test-Path (Join-Path $WorkDir '.git'))
if ($fresh) {
    if (Test-Path $WorkDir) { throw "$WorkDir exists but is not a git working copy; remove it or pass -WorkDir." }
    Write-Step "Cloning $Source"
    Invoke-Native 'git clone' 'git' @('-c', 'core.longpaths=true', 'clone', '--no-checkout', $Source, $WorkDir) $repoRoot
}
if (-not (Test-HasCommit $WorkDir $Commit)) {
    Write-Step 'Fetching the pinned commit'
    Invoke-Native 'git fetch' 'git' @('fetch', 'origin') $WorkDir
}
if (-not (Test-HasCommit $WorkDir $Commit)) {
    try { Invoke-Native 'git fetch' 'git' @('fetch', 'origin', $Commit) $WorkDir } catch { }
}
if (-not (Test-HasCommit $WorkDir $Commit)) {
    throw "Commit $Commit is not in $Source. Is it the GT6CE repository, and has the commit been pushed?"
}
if (-not $fresh -and (Get-Git $WorkDir @('status', '--porcelain', '--untracked-files=no'))) {
    Write-Step 'Discarding local changes in the working copy'
    Invoke-Native 'git reset' 'git' @('reset', '--hard') $WorkDir
}
Write-Step 'Checking out the pinned commit'
Invoke-Native 'git checkout' 'git' @('-c', 'core.longpaths=true', 'checkout', '--detach', $Commit) $WorkDir
if ($NoBuild) { Write-Step "Working copy ready at $Commit (-NoBuild)."; return }

# 2. Build, with the same toolchain wiring as CI.
$nodeBuild = Join-Path $WorkDir "multiversion\versions\$Node\build"
foreach ($stale in @((Join-Path $nodeBuild 'libs'), (Join-Path $nodeBuild 'addon-sdk'))) {
    if (Test-Path $stale) { Get-ChildItem $stale -Filter '*.jar' -ErrorAction SilentlyContinue | Remove-Item -Force }
}

$gradlew = Join-Path $WorkDir 'gradlew.bat'
$launcher = if ($jdk17) { $jdk17 } else { $jdk21 }
$fromEnv = if ($jdk17) { 'JAVA17_HOME,JAVA21_HOME' } else { 'JAVA21_HOME' }
$autoDownload = if ($AutoDownloadJdk) { 'true' } else { 'false' }
$pythonArgs = if ($Python) { @("-PpythonExecutable=$Python") } else { @() }

$savedEnv = @{}
foreach ($name in 'JAVA_HOME', 'JAVA17_HOME', 'JAVA21_HOME') {
    $savedEnv[$name] = [Environment]::GetEnvironmentVariable($name, 'Process')
}
try {
    $env:JAVA_HOME = $launcher
    $env:JAVA21_HOME = $jdk21
    if ($jdk17) { $env:JAVA17_HOME = $jdk17 }

    Write-Step 'prepareNativeTools'
    Invoke-Native 'prepareNativeTools' $gradlew (@('--no-daemon', '--console=plain') + $pythonArgs + @('prepareNativeTools')) $WorkDir

    Write-Step "Building the $Node SDK and mod jars"
    $jarArgs = @(
        '--no-daemon', '--console=plain', '--stacktrace',
        "-Dorg.gradle.java.home=$launcher",
        "-Dorg.gradle.java.installations.fromEnv=$fromEnv",
        "-Dorg.gradle.java.installations.auto-download=$autoDownload"
    ) + $pythonArgs + @(":multiversion:${Node}:addonSdkJar", ":multiversion:${Node}:jar")
    Invoke-Native 'GT6CE jar build' $gradlew $jarArgs $WorkDir
} finally {
    foreach ($name in $savedEnv.Keys) {
        [Environment]::SetEnvironmentVariable($name, $savedEnv[$name], 'Process')
    }
}

# 3. Collect the outputs exactly like CI does.
$modJars = @(Get-ChildItem (Join-Path $nodeBuild 'libs') -Filter '*.jar' |
    Where-Object { $_.Name -notmatch '-(sources|javadoc|dev)\.jar$' })
if ($modJars.Count -ne 1) { throw "Expected one GT6CE $Node jar, found: $($modJars.Name -join ', ')" }
$sdkJars = @(Get-ChildItem (Join-Path $nodeBuild 'addon-sdk') -Filter "gt6ce-addon-sdk-$Node-*.jar" |
    Where-Object { $_.Name -notmatch '-(sources|javadoc)\.jar$' })
if ($sdkJars.Count -ne 1) { throw "Expected one GT6CE addon SDK, found: $($sdkJars.Name -join ', ')" }

New-Item -ItemType Directory -Force -Path $libs | Out-Null
Get-ChildItem $libs -Filter '*.jar' | Remove-Item -Force
Copy-Item $modJars[0].FullName (Join-Path $libs $ModJarName)
Copy-Item $sdkJars[0].FullName (Join-Path $libs $sdkJars[0].Name)

# 4. Compare with the recorded checksum. Like CI, a difference is a warning, not a failure.
$actual = (Get-FileHash (Join-Path $libs $ModJarName) -Algorithm SHA256).Hash.ToLowerInvariant()
$shaFile = Join-Path $libs "$ModJarName.sha256"
$expected = ''
if (Test-Path $shaFile) { $expected = ((Get-Content $shaFile -Raw) -split '\s+')[0].ToLowerInvariant() }

Write-Step 'Done'
Write-Host "  SDK      : $($sdkJars[0].Name)"
Write-Host "  mod jar  : $ModJarName"
Write-Host "  sha256   : $actual"
if ($expected -eq $actual) {
    Write-Host '  checksum : matches libs/gt6ce/*.sha256' -ForegroundColor Green
} elseif ($UpdateChecksum) {
    ($actual + '  ' + $ModJarName) | Set-Content -Path $shaFile -Encoding ASCII
    Write-Host '  checksum : recorded value replaced (-UpdateChecksum)' -ForegroundColor Yellow
} else {
    Write-Host "  checksum : differs from the recorded value ($expected)" -ForegroundColor Yellow
    Write-Host '             Jars built on another machine need not be byte-identical; CI only warns.'
    Write-Host '             After bumping build-commit.txt, rerun with -UpdateChecksum.'
}
Write-Host 'Next: .\gradlew.bat build'
