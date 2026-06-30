# Modrinth — publishing draft (DO NOT auto-publish)

This is a ready-to-paste draft for the Modrinth project page. Nothing here is
published yet; copy it into the Modrinth UI when you decide to go live.

## Project metadata

| Field | Value |
| --- | --- |
| Name | WaveMotes |
| Slug | `wavemotes` |
| Summary | Type `:emote:` in chat and pick it from a popup, like commands. Client-side rendering + optional Discord emojis. One jar. |
| Environments | Client: **required** · Server: **unsupported** |
| Loaders | Fabric |
| Minecraft versions | 1.21, 1.21.x (and newer 1.21+) |
| Categories | Utility, Social, Decoration |
| License | MIT |
| Dependencies | Fabric API (required) |
| Links | Source: https://github.com/CodeW4VE/WaveMotes · Issues: same repo · Homepage: https://w4ve.xyz/ |

## Description (long, paste into Modrinth body)

# WaveMotes

**Type `:` in chat and a popup appears — the same one you get with `/` commands, but
full of emotes.** Pick one and the rendered emote drops straight into your message.

## ✨ Features

- **Popup autocomplete.** Type `:`, keep typing to filter (`:sku` → `:skull:`). Same UX as command suggestions — nothing new to learn.
- **Renders client-side.** The built-in emote textures ship inside the jar, so they show up even when the server doesn't send a resource pack.
- **Bigger than text.** Emotes are scaled up so they actually read as emotes.
- **Optional Discord emojis.** 1900+ standard Discord shortcodes, **off by default**. Turn them all on, or whitelist just the ones you want (e.g. only `:skull:`).
- **In-game config.** Tweakeroo-style On/Off switches. Open with `/wm` or bind a key.
- **One jar, no bloat.** Only needs Fabric API. No malilib, no YACL.

## 🎮 How to use

1. Type `:` in chat → popup opens.
2. Filter by typing, pick with the mouse or `Tab`/`Enter`.
3. Configure with `/wm` (or bind a key under Options → Controls).

## ⚙️ Discord emojis

1900+ standard Discord shortcodes, drawn in color in-game with bundled
[Twemoji](https://github.com/jdecked/twemoji) textures. They're off by default to keep the
picker focused on the built-in pack. Enable them all, or whitelist individual ones from the
config screen. A few hundred multi-codepoint ones (most flags, family/ZWJ sequences) render
as their components locally but still travel correctly to Discord.

## 📏 Emote size

Every emote shares one size, so changing it is a single find-and-replace. The jar is a zip:
open `assets/minecraft/font/default.json` and change every `"height": 16` (bigger = bigger
emotes); `"ascent": 11` moves them up/down (rule of thumb: `ascent ≈ height − 5`). Restart and
done. Building from source? `python tools/set_emote_size.py 20`.

## 🖥️ Want everyone to see your emotes?

The client mod renders emotes for **you** on any server. If you want **other players** to
see your custom emotes, that needs a server-side setup (resource pack + chat bridge) on a
**dedicated server or VPS** — it can't be done on locked-down shared hosts like Shockbyte
or Aternos. That server-side piece is part of the [CodeW4VE](https://github.com/CodeW4VE)
project.

## 📦 Requirements

- Minecraft 1.21+
- Fabric Loader
- Fabric API

---

*Built-in emote textures belong to the MineWave community, included with permission. Discord emoji textures from [Twemoji](https://github.com/jdecked/twemoji) (CC-BY 4.0). Mod under MIT.*

## Gallery checklist (before publishing)

- [ ] Screenshot/GIF of the `:` popup in action
- [ ] Screenshot of an emote rendered in chat (bigger than text)
- [ ] Screenshot of the config screen
- [ ] Set an icon (the mod's `icon.png` can be reused)
