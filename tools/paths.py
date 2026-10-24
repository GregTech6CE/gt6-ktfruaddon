"""Where the generators in this directory find their inputs.

Every location can be overridden with an environment variable, so the scripts do not depend on where
the repositories were cloned. A missing or mismatched input stops the script with one message that
says what to provide.

  KTFRU_UPSTREAM         kTFRUAddon checkout          default <repo>/kTFRUAddon (git-ignored)
  GT6CE_SOURCE           GT6CE source tree            default <repo>/.gt6ce-src (tools/build-gt6ce.ps1)
  GT6_REF                original GregTech 6 sources  default <repo>/../gt6_source_code, else <GT6CE_SOURCE>/work/gt6-ref
  ALLOW_INPUT_MISMATCH=1 run even when a checkout is not at the pinned commit
"""

from __future__ import annotations

import os
import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]

# The kTFRUAddon commit the committed generated files were taken from (docs/porting-strategy.md).
UPSTREAM_URL = "https://github.com/kuzuanpa/kTFRUAddon"
UPSTREAM_COMMIT = "6404ab6979de9fce832ce0c41171000decf00e69"

# The GT6CE commit the addon is built against; tools/build-gt6ce.ps1 checks it out into .gt6ce-src.
PIN_FILE = ROOT / "libs" / "gt6ce" / "build-commit.txt"


def _from_env(name: str, default: Path) -> Path:
    value = os.environ.get(name)
    return Path(value).expanduser().resolve() if value else default


UPSTREAM = _from_env("KTFRU_UPSTREAM", ROOT / "kTFRUAddon")
GT6_ROOT = _from_env("GT6CE_SOURCE", ROOT / ".gt6ce-src")


def _default_gt6_ref() -> Path:
    # The original 1.7.10 GregTech 6 source, usually cloned next to this repository.
    for candidate in (ROOT.parent / "gt6_source_code", GT6_ROOT / "work" / "gt6-ref"):
        if candidate.is_dir():
            return candidate
    return ROOT.parent / "gt6_source_code"


GT6_REF = _from_env("GT6_REF", _default_gt6_ref())
RUNTIME_JAR = ROOT / "libs" / "gt6ce" / "gt6ce-1.21.1-neoforge.jar"


def _head(checkout: Path) -> str | None:
    try:
        done = subprocess.run(["git", "-C", str(checkout), "rev-parse", "HEAD"],
                              capture_output=True, text=True, check=True)
    except (OSError, subprocess.CalledProcessError):
        return None
    return done.stdout.strip()


def _check_commit(what: str, checkout: Path, expected: str, problems: list[str]) -> None:
    head = _head(checkout)
    if head is None or head == expected or os.environ.get("ALLOW_INPUT_MISMATCH") == "1":
        return
    problems.append(
        f"{what} at {checkout} is at {head[:12]}, but the generated files were taken from {expected[:12]}.\n"
        f"    Check out {expected}, or set ALLOW_INPUT_MISMATCH=1 to run anyway.")


def require_inputs(*, upstream: bool = False, gt6: bool = False, gt6_ref: bool = False,
                   runtime_jar: bool = False) -> None:
    """Stop with one message listing every input that is missing or at the wrong commit."""
    problems: list[str] = []
    if upstream:
        if not UPSTREAM.is_dir():
            problems.append(
                f"kTFRUAddon checkout not found at {UPSTREAM}.\n"
                f"    git clone {UPSTREAM_URL} \"{UPSTREAM}\" && git -C \"{UPSTREAM}\" checkout {UPSTREAM_COMMIT}\n"
                "    or set KTFRU_UPSTREAM to an existing checkout.")
        else:
            _check_commit("kTFRUAddon checkout", UPSTREAM, UPSTREAM_COMMIT, problems)
    if gt6:
        if not (GT6_ROOT / "core" / "src").is_dir():
            problems.append(
                f"GT6CE source tree not found at {GT6_ROOT}.\n"
                "    Run tools\\build-gt6ce.ps1 (add -NoBuild to only check out the pinned commit), "
                "or set GT6CE_SOURCE.")
        elif PIN_FILE.is_file():
            _check_commit("GT6CE source tree", GT6_ROOT, PIN_FILE.read_text(encoding="utf-8").strip(), problems)
    if gt6_ref and not GT6_REF.is_dir():
        problems.append(
            f"original GregTech 6 sources not found at {GT6_REF}.\n"
            "    The extractor reads gregtech/loaders/b/Loader_MultiTileEntities.java from a local copy of the "
            "1.7.10 GregTech 6 source: <GT6_REF>/gregtech6/src/main/java/gregtech/...\n"
            "    Put the copy at <repo>/../gt6_source_code or set GT6_REF.")
    if runtime_jar and not RUNTIME_JAR.is_file():
        problems.append(f"GT6CE runtime jar not found at {RUNTIME_JAR}.\n    Run tools\\build-gt6ce.ps1.")
    if problems:
        sys.exit("cannot run, inputs are missing or at the wrong commit:\n\n" + "\n\n".join(problems))
