# Changelog

## 1.0.1

- Fix: pressing Tab on an empty chat box no longer pops up the full emote list, so vanilla player-name completion works again. Emote suggestions now need at least one character after the `:` (e.g. `:sku`).
- Emote size is now a one-line change: see the README, or run `tools/set_emote_size.py`.

## 1.0.0

First public release.

- `:` popup autocomplete in chat, mirroring the vanilla command suggestion UI.
- Built-in emote pack rendered client-side (textures embedded in the jar — no server resource pack needed).
- Emotes scaled up to render larger than chat text.
- Optional Discord native emojis (1900+ shortcodes) with bundled Twemoji textures, rendered in color in-game; off by default; enable all or whitelist individual ones.
- In-game config screen (vanilla widgets, no extra dependencies), openable via `/wm` or a bindable key.
- Minecraft 1.21+ on Fabric. Requires Fabric API. Client-side only.
