<div align="center">

# WaveMotes

**Type `:emote:` in Minecraft chat and pick it from a popup — just like commands.**

[![Minecraft](https://img.shields.io/badge/Minecraft-1.21%2B-brightgreen?logo=minecraft)](https://www.minecraft.net/)
[![Fabric](https://img.shields.io/badge/Loader-Fabric-blue)](https://fabricmc.net/)
[![Requires Fabric API](https://img.shields.io/badge/Requires-Fabric%20API-blue)](https://modrinth.com/mod/fabric-api)
[![Environment](https://img.shields.io/badge/Environment-Client-orange)](#installation)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

English | [Español](README_es.md)

<!--
PREVIEW: replace this note with a GIF of the `:emote:` popup in action.
Save the file as docs/preview.gif and uncomment the line below.
![WaveMotes preview](docs/preview.gif)
-->
*A GIF of the popup in action is coming here soon.*

</div>

---

## What is it?

**WaveMotes** is a lightweight, client-side Fabric mod. Start typing `:` in
chat and a suggestion popup appears — the exact same one you get with `/` commands,
but full of emotes. Pick one and the rendered emote is inserted into your message.

It ships with an embedded emote pack that renders **client-side**, so your emotes show
up even if the server never sends you a resource pack. Optionally, it can also suggest
the **full set of standard Discord emojis** (`:skull:` → 💀, `:fire:` → 🔥, …).

> **One jar, no extra setup.** No malilib, no YACL, no config files to copy by hand.
> Drop it in your `mods` folder and it works.

## Features

- 🗨️ **Popup autocomplete** — type `:` to open the same dropdown as `/` commands, filtered live as you type.
- 🖼️ **Client-side rendering** — the built-in emote textures are bundled in the jar, so they render even with no server resource pack.
- 🔠 **Bigger than text** — emotes are scaled up so they actually read as emotes, not tiny glyphs.
- 😀 **Optional Discord emojis** — 1900+ standard Discord shortcodes (Twemoji textures, **rendered in color in-game**), **off by default**. Enable them all, or add just the ones you want (e.g. only `:skull:`).
- 🎛️ **In-game config screen** — Tweakeroo-style On/Off switches. Open it with `/wm` or bind a key.
- 🪶 **No dependencies** beyond Fabric API. Single jar.

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft **1.21 or newer**.
2. Download [Fabric API](https://modrinth.com/mod/fabric-api).
3. Download **WaveMotes** and drop both jars into your `.minecraft/mods` folder.
4. Launch the game.

This is a **client-side** mod — it does not need to be installed on the server.

## Usage

| Action | How |
| --- | --- |
| Open the emote popup | Type `:` in chat (optionally keep typing to filter, e.g. `:sku`) |
| Insert an emote | Click it, or use the arrow keys + `Tab`/`Enter` like any suggestion |
| Open the config | Run `/wm` (or `/wavemotes`), or bind a key in **Options → Controls → WaveMotes** |

## Configuration

The config screen (and the file at `config/wavemotes.json`) has:

- **Built-in emotes** — On/Off. The embedded pack, rendered client-side.
- **Discord native emojis (all)** — On/Off, **Off by default**. Adds the full standard Discord emoji set to the popup.
- **Add a single emoji** — type a shortcode (e.g. `skull`) and click **Add** to enable just that one without turning on the whole set. **Clear added emojis** wipes that list.

> **About Discord emojis:** these are standard Unicode emoji rendered with bundled
> [Twemoji](https://github.com/jdecked/twemoji) textures, so they show **in color in-game**.
> A few hundred multi-codepoint ones (most country flags, ZWJ sequences like family emoji)
> can't be drawn as a single in-game glyph, so those render as their components locally —
> but they still travel correctly to Discord through a chat bridge. They're off by default
> to keep the picker focused on the built-in pack.

## How it fits: the W4VE emote stack

WaveMotes is the **client half** of a two-part system. It runs fine on its own, but it
was built to sit next to a server side that does the heavy lifting.

- **WaveMotes (this mod, client side):** the `:emote:` popup and client-side rendering.
  *You* see your emotes on any server, even if no resource pack ever reaches you, because
  the textures are embedded in the jar on purpose.
- **W4VE server side (the real workhorse):** makes *everyone* on the server see the emotes,
  even vanilla players with no mod, and bridges them to and from Discord. Three pieces:
  - a **resource pack** with a custom PUA font that draws each emote, pushed to every client;
  - **[Styled Chat](https://modrinth.com/mod/styled-chat)**, so typing `:tet:` in-game turns
    into the emote for *all* players;
  - a patched **[ChatBridge](https://github.com/CodeW4VE/ChatBridge)** that rewrites
    `:name:` ↔ `<:name:id>` between Minecraft and Discord.

Run them together (as on [MineWave](https://w4ve.xyz/)) and you get the full experience:
a Discord-style picker, emotes that *everyone* sees, and a live Discord bridge.

> **The mod still works alone.** Solo, you get the picker and your own emotes rendered
> locally. What you *don't* get without the server side: other players seeing your custom
> emotes, and the Discord bridge.

The server side needs a **dedicated server or VPS** with full file/plugin access. It is
**not** possible on locked-down shared hosts (Shockbyte, Aternos, etc.). It is bespoke
infrastructure (a resource pack tied to a Discord guild, plus its own hosting), not a
one-click download. So far the packaged piece is the chat bridge, **[ChatBridge](https://github.com/CodeW4VE/ChatBridge)**;
the rest lives in the wider [CodeW4VE](https://github.com/CodeW4VE) project.

## Compatibility

- Minecraft **1.21+** (Fabric).
- Requires **Fabric API**.
- Client-side only; safe to use on servers that don't have it.

## Building from source

```bash
# JDK 21 required
export JAVA_HOME=/path/to/jdk-21
./gradlew build
```

The built jar lands in `build/libs/`.

## License

[MIT](LICENSE) © tvtvirus / CodeW4VE.

The bundled emote textures belong to the MineWave community and are included with permission.
Discord emoji textures are from [Twemoji](https://github.com/jdecked/twemoji) (© Twitter / jdecked, licensed [CC-BY 4.0](https://creativecommons.org/licenses/by/4.0/)).
