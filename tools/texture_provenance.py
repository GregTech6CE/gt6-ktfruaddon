"""Count texture matches against kTFRUAddon and GT6CE (PNG bytes; LF-normalized metadata).

The numbers in NOTICE come from here. Read-only: it prints a table and the files that match neither source.
"""

from __future__ import annotations

import hashlib
from collections import Counter
from pathlib import Path

import paths

SUFFIXES = {".png", ".mcmeta"}
OURS = paths.ROOT / "src" / "main" / "resources" / "assets" / "gt6ktfruaddon" / "textures"
UPSTREAM_ASSETS = paths.UPSTREAM / "src" / "main" / "resources" / "assets"
GT6CE_ASSETS = paths.GT6_ROOT / "core" / "src" / "main" / "resources" / "assets"


def digest(path: Path) -> str:
    data = path.read_bytes()
    if path.suffix == ".mcmeta":
        data = data.replace(b"\r\n", b"\n")
    return hashlib.sha256(data).hexdigest()


def texture_files(root: Path) -> list[Path]:
    return sorted(path for path in root.rglob("*") if path.is_file() and path.suffix in SUFFIXES)


def main() -> None:
    upstream = {digest(path) for path in texture_files(UPSTREAM_ASSETS)}
    gt6ce = {digest(path) for path in texture_files(GT6CE_ASSETS)}
    groups: dict[str, Counter] = {}
    unmatched: list[str] = []
    for path in texture_files(OURS):
        relative = path.relative_to(OURS)
        group = "/".join(relative.parts[:2]) if len(relative.parts) > 2 else relative.parts[0]
        value = digest(path)
        in_upstream, in_gt6ce = value in upstream, value in gt6ce
        if in_upstream and in_gt6ce:
            kind = "both"
        elif in_upstream:
            kind = "upstream only"
        elif in_gt6ce:
            kind = "gt6ce only"
        else:
            kind = "neither"
            unmatched.append(relative.as_posix())
        groups.setdefault(group, Counter())[kind] += 1
        groups.setdefault("TOTAL", Counter())[kind] += 1
    print(f"{'directory':34s}{'files':>6s}{'upstream only':>15s}{'gt6ce only':>12s}{'both':>6s}{'neither':>9s}")
    for group in sorted(groups, key=lambda name: (name == "TOTAL", name)):
        count = groups[group]
        print(f"{group:34s}{sum(count.values()):6d}{count['upstream only']:15d}{count['gt6ce only']:12d}"
              f"{count['both']:6d}{count['neither']:9d}")
    print("\nin neither source:")
    for name in unmatched:
        print("  " + name)


if __name__ == "__main__":
    paths.require_inputs(upstream=True, gt6=True)
    main()
