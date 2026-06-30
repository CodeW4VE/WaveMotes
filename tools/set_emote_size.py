#!/usr/bin/env python3
"""Resize every WaveMotes emote in one shot.

All emotes share a single size, so this just rewrites the `height` and `ascent`
of every glyph provider in the bundled font.

    height  how many pixels tall each emote is drawn (bigger = bigger emotes)
    ascent  vertical baseline offset (moves emotes up/down); good default is height - 5

Usage:
    python tools/set_emote_size.py <height> [ascent]

Examples:
    python tools/set_emote_size.py 20        # bigger emotes, ascent auto = 15
    python tools/set_emote_size.py 16 11     # the defaults

Then rebuild:  ./gradlew build
"""
import json
import sys
from pathlib import Path

FONT = Path(__file__).resolve().parent.parent / \
    "src/main/resources/assets/minecraft/font/default.json"


def main() -> int:
    if len(sys.argv) < 2:
        print(__doc__)
        return 1

    height = int(sys.argv[1])
    ascent = int(sys.argv[2]) if len(sys.argv) > 2 else height - 5

    if ascent > height:
        print(f"warning: ascent ({ascent}) > height ({height}); "
              "the emote may render clipped or oddly placed.")

    data = json.loads(FONT.read_text(encoding="utf-8"))
    providers = data.get("providers", [])
    changed = 0
    for p in providers:
        if p.get("type") == "bitmap" and "height" in p and "ascent" in p:
            p["height"] = height
            p["ascent"] = ascent
            changed += 1

    FONT.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n",
                    encoding="utf-8")
    print(f"set {changed} emotes to height={height}, ascent={ascent}")
    print(f"updated {FONT}")
    print("now rebuild:  ./gradlew build")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
