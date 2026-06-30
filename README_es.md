<div align="center">

# WaveMotes

**Escribe `:emote:` en el chat de Minecraft y elígelo de un popup, igual que los comandos.**

[![Minecraft](https://img.shields.io/badge/Minecraft-1.21%2B-brightgreen?logo=minecraft)](https://www.minecraft.net/)
[![Fabric](https://img.shields.io/badge/Loader-Fabric-blue)](https://fabricmc.net/)
[![Requiere Fabric API](https://img.shields.io/badge/Requiere-Fabric%20API-blue)](https://modrinth.com/mod/fabric-api)
[![Entorno](https://img.shields.io/badge/Entorno-Cliente-orange)](#instalación)
[![Licencia](https://img.shields.io/badge/Licencia-MIT-yellow.svg)](LICENSE)

[English](README.md) | Español

<!--
CAPTURA: reemplaza esta nota por un GIF del popup `:emote:` en acción.
Guarda el archivo como docs/preview.gif y descomenta la línea de abajo.
![Vista previa de WaveMotes](docs/preview.gif)
-->
*Pronto va aquí un GIF del popup en acción.*

</div>

---

## ¿Qué es?

**WaveMotes** es un mod de Fabric ligero y del lado del cliente. Empieza a
escribir `:` en el chat y aparece un popup de sugerencias, el mismo que sale con los
comandos `/`, pero lleno de emotes. Eliges uno y se inserta el emote ya renderizado en
tu mensaje.

Viene con un pack de emotes embebido que se renderiza **del lado del cliente**, así que
tus emotes se ven aunque el servidor nunca te mande un resource pack. Opcionalmente,
también puede sugerir el **set completo de emojis estándar de Discord** (`:skull:` → 💀,
`:fire:` → 🔥, …).

> **Un solo jar, sin configurar nada.** Sin malilib, sin YACL, sin archivos de config
> que copiar a mano. Lo sueltas en tu carpeta `mods` y funciona.

## Características

- 🗨️ **Autocompletado con popup** — escribe `:` para abrir el mismo desplegable que los comandos `/`, filtrando en vivo mientras escribes.
- 🖼️ **Render del lado del cliente** — las texturas del pack embebido van dentro del jar, así que se ven aunque no haya resource pack del servidor.
- 🔠 **Más grandes que el texto** — los emotes se ven a mayor tamaño para que se lean como emotes, no como glifos diminutos.
- 😀 **Emojis de Discord opcionales** — más de 1900 shortcodes estándar de Discord (texturas Twemoji, **se ven a color in-game**), **desactivados por defecto**. Actívalos todos, o añade solo los que quieras (ej. solo `:skull:`).
- 🎛️ **Pantalla de config in-game** — interruptores On/Off estilo Tweakeroo. Ábrela con `/wm` o asígnale una tecla.
- 🪶 **Sin dependencias** más allá de Fabric API. Un único jar.

## Instalación

1. Instala [Fabric Loader](https://fabricmc.net/use/) para Minecraft **1.21 o superior**.
2. Descarga [Fabric API](https://modrinth.com/mod/fabric-api).
3. Descarga **WaveMotes** y suelta ambos jars en tu carpeta `.minecraft/mods`.
4. Abre el juego.

Es un mod **del lado del cliente**: no hace falta instalarlo en el servidor.

## Uso

| Acción | Cómo |
| --- | --- |
| Abrir el popup de emotes | Escribe `:` en el chat (sigue escribiendo para filtrar, ej. `:sku`) |
| Insertar un emote | Haz clic, o usa las flechas + `Tab`/`Enter` como en cualquier sugerencia |
| Abrir la config | Ejecuta `/wm` (o `/wavemotes`), o asigna una tecla en **Opciones → Controles → WaveMotes** |

## Configuración

La pantalla de config (y el archivo en `config/wavemotes.json`) tiene:

- **Emotes propios** — On/Off. El pack embebido, renderizado del lado del cliente.
- **Emojis de Discord (todos)** — On/Off, **Off por defecto**. Añade el set completo de emojis de Discord al popup.
- **Añadir un emoji suelto** — escribe un shortcode (ej. `skull`) y pulsa **Añadir** para activar solo ese sin encender todo el set. **Vaciar emojis añadidos** borra esa lista.

> **Sobre los emojis de Discord:** son emojis Unicode estándar dibujados con texturas
> [Twemoji](https://github.com/jdecked/twemoji) embebidas, así que **se ven a color in-game**.
> Unos cientos multi-codepoint (la mayoría de banderas de países y secuencias ZWJ tipo emoji
> de familia) no se pueden dibujar como un solo glifo en el juego, así que esos salen como sus
> componentes localmente, pero igual llegan bien a Discord por el bridge. Vienen desactivados
> por defecto para que el menú se centre en el pack propio.

## Cómo encaja: el stack de emotes W4VE

WaveMotes es la **mitad cliente** de un sistema de dos partes. Funciona solo, pero está
pensado para ir al lado de un lado servidor que hace el trabajo pesado.

- **WaveMotes (este mod, lado cliente):** el popup `:emote:` y el render del lado del cliente.
  *Tú* ves tus emotes en cualquier servidor, aunque nunca te llegue un resource pack, porque
  las texturas van embebidas en el jar a propósito.
- **Lado servidor W4VE (el verdadero motor):** hace que *todos* en el servidor vean los emotes,
  incluso jugadores vanilla sin mod, y los puentea hacia y desde Discord. Tres piezas:
  - un **resource pack** con una fuente PUA custom que dibuja cada emote, enviado a cada cliente;
  - **[Styled Chat](https://modrinth.com/mod/styled-chat)**, para que escribir `:tet:` in-game
    se convierta en el emote para *todos* los jugadores;
  - un **[ChatBridge](https://github.com/CodeW4VE/ChatBridge)** parcheado que reescribe
    `:name:` ↔ `<:name:id>` entre Minecraft y Discord.

Úsalos juntos (como en [MineWave](https://w4ve.xyz/)) y tienes la experiencia completa:
un selector estilo Discord, emotes que ven *todos*, y un puente vivo con Discord.

> **El mod igual funciona solo.** En solitario tienes el selector y tus propios emotes
> renderizados localmente. Lo que *no* tienes sin el lado servidor: que los demás vean tus
> emotes personalizados, y el puente con Discord.

El lado servidor necesita un **servidor dedicado o VPS** con acceso completo a archivos/plugins.
**No** es posible en hosts compartidos cerrados (Shockbyte, Aternos, etc.). Es infraestructura
a medida (un resource pack atado a un guild de Discord, más su propio hosting), no una descarga
de un clic. Por ahora la pieza empaquetada es el puente de chat, **[ChatBridge](https://github.com/CodeW4VE/ChatBridge)**;
el resto vive en el proyecto más amplio [CodeW4VE](https://github.com/CodeW4VE).

## Compatibilidad

- Minecraft **1.21+** (Fabric).
- Requiere **Fabric API**.
- Solo cliente; es seguro usarlo en servidores que no lo tengan.

## Compilar desde el código

```bash
# Hace falta JDK 21
export JAVA_HOME=/ruta/al/jdk-21
./gradlew build
```

El jar compilado queda en `build/libs/`.

## Licencia

[MIT](LICENSE) © tvtvirus / CodeW4VE.

Las texturas de emotes incluidas pertenecen a la comunidad de MineWave y se incluyen con permiso.
Las texturas de emojis de Discord son de [Twemoji](https://github.com/jdecked/twemoji) (© Twitter / jdecked, licencia [CC-BY 4.0](https://creativecommons.org/licenses/by/4.0/)).
