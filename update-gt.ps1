# Download the latest successful NeoForge build of GT6CE into libs\gt6ce\
# Compilation uses only the loader SDK. The full mod jar is the development runtime.
$repo = 'SaltNya/GregTech6'
$run = gh run list -R $repo --workflow "Dual-platform build" --branch main --status success --limit 1 --json databaseId,headSha | ConvertFrom-Json
if (-not $run) { throw 'No successful GT6CE build found' }
$dest = Join-Path $PSScriptRoot 'libs\gt6ce-download'
Remove-Item -Recurse -Force $dest -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force -Path $dest | Out-Null
gh run download $run[0].databaseId -R $repo -n gt6ce-1.21.1-neoforge -D $dest
if ($LASTEXITCODE -ne 0) { throw 'Download failed' }

$keep = @(
    'gt6ce-1.21.1-neoforge.jar',
    'gt6ce-1.21.1-neoforge.jar.sha256',
    'build-commit.txt'
)
$sdk = Get-ChildItem $dest -Filter 'gt6ce-addon-sdk-1.21.1-neoforge-*.jar'
if ($sdk.Count -ne 1) { throw 'The GT6CE artifact must contain exactly one NeoForge addon SDK' }
$target = Join-Path $PSScriptRoot 'libs\gt6ce'
New-Item -ItemType Directory -Force -Path $target | Out-Null
Get-ChildItem $target -File | Remove-Item -Force
foreach ($name in $keep) {
    $file = Join-Path $dest $name
    if (-not (Test-Path $file)) { throw "Missing $name in the GT6CE artifact" }
    Copy-Item $file (Join-Path $target $name)
}
Copy-Item $sdk[0].FullName (Join-Path $target $sdk[0].Name)

$mod = Join-Path $target 'gt6ce-1.21.1-neoforge.jar'
$sum = Get-Content (Join-Path $target 'gt6ce-1.21.1-neoforge.jar.sha256')
$expected = ($sum -split '\s+')[0]
$actual = (Get-FileHash $mod -Algorithm SHA256).Hash.ToLower()
if ($expected -ne $actual) { throw "GT6CE jar checksum mismatch: expected $expected, got $actual" }
Write-Host "GT6CE updated to commit $(Get-Content (Join-Path $target 'build-commit.txt'))"
Write-Host "SDK: $($sdk[0].Name)"
Remove-Item -Recurse -Force $dest
