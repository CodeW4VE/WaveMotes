# Changelog

## 1.0.3

- Slightly smaller default emote size (height 16 → 14) for a tighter fit next to chat text. You can still set any size yourself: see the README or run `tools/set_emote_size.py`.

## 1.0.2

- Typing `:` again opens the full picker right away (1.0.1 wrongly required a letter first). 
- The bare `:` list now shows only your own emotes; the 1900 optional Discord emojis appear only once you start typing a name (e.g. `:sku`), so the picker stays clean even with Discord turned on.

## 1.0.1

- Fix: pressing Tab on an empty chat box no longer pops up the full emote list, so vanilla player-name completion works again. Emote suggestions now need at least one character after the `:` (e.g. `:sku`).
- Emote size is now a one-line change: see the README, or run `tools/set_emote_size.py`.

## 1.0.0

First public release.

- `:` popup autocomplete in chat, mirroring the vanilla command suggestion UI.
- Built-in emote pack rendered client-side (textures embedded in the jar, no server resource pack needed).
- Emotes scaled up to render larger than chat text.
- Optional Discord native emojis (1900+ shortcodes) with bundled Twemoji textures, rendered in color in-game; off by default; enable all or whitelist individual ones.
- In-game config screen (vanilla widgets, no extra dependencies), openable via `/wm` or a bindable key.
- Minecraft 1.21+ on Fabric. Requires Fabric API. Client-side only.
