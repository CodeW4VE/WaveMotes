package xyz.w4ve.wavemotes.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import xyz.w4ve.wavemotes.WaveMotes;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Catalogo de emotes/emojis para el autocompletado del chat. Dos fuentes:
 * <ul>
 *   <li><b>Emotes propios</b> ({@code emotes.json}): nombre -&gt; glifo PUA.
 *       Las texturas van embebidas en el jar, asi que se renderizan SIEMPRE,
 *       aunque no llegue el resource pack del servidor.</li>
 *   <li><b>Emojis de Discord</b> ({@code discord_emojis.json}): shortcode -&gt;
 *       caracter(es) Unicode. Son emojis estandar; viajan como texto (Discord
 *       los pinta al pasar por el bridge). OFF por defecto.</li>
 * </ul>
 * El filtrado respeta {@link WaveMotesConfig} en vivo.
 */
public final class EmoteCatalog {
	private EmoteCatalog() {}

	/** Una entrada lista para sugerir: nombre, texto a insertar y si es de Discord. */
	public record Entry(String name, String insert, boolean discord) {}

	// Emotes propios: nombre -> glifo PUA (String para soportar codepoints altos).
	private static final Map<String, String> BUILTIN = new LinkedHashMap<>();
	private static final List<String> BUILTIN_NAMES = new ArrayList<>();
	// Emojis de Discord: shortcode (minuscula) -> Unicode.
	private static final Map<String, String> DISCORD = new LinkedHashMap<>();
	private static final List<String> DISCORD_NAMES = new ArrayList<>();

	/** Tope de sugerencias para no inflar el popup al escribir solo ":". */
	private static final int MAX_SUGGESTIONS = 100;

	public static void load() {
		BUILTIN.clear();
		BUILTIN_NAMES.clear();
		DISCORD.clear();
		DISCORD_NAMES.clear();
		loadBuiltin();
		loadDiscord();
		WaveMotes.LOGGER.info(
				"[WaveMotes] catalogo cargado: {} emotes propios + {} emojis de Discord",
				BUILTIN_NAMES.size(), DISCORD_NAMES.size());
	}

	private static void loadBuiltin() {
		try (InputStream in = EmoteCatalog.class.getResourceAsStream("/assets/wavemotes/emotes.json")) {
			if (in == null) {
				WaveMotes.LOGGER.error("[WaveMotes] no encontre emotes.json");
				return;
			}
			JsonObject root = JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
			JsonObject emotes = root.getAsJsonObject("emotes");
			for (Map.Entry<String, JsonElement> e : emotes.entrySet()) {
				String glyph = new String(Character.toChars(e.getValue().getAsInt()));
				BUILTIN.put(e.getKey(), glyph);
				BUILTIN_NAMES.add(e.getKey());
			}
			BUILTIN_NAMES.sort(String.CASE_INSENSITIVE_ORDER);
		} catch (Exception ex) {
			WaveMotes.LOGGER.error("[WaveMotes] error cargando emotes propios", ex);
		}
	}

	private static void loadDiscord() {
		try (InputStream in = EmoteCatalog.class.getResourceAsStream("/assets/wavemotes/discord_emojis.json")) {
			if (in == null) {
				WaveMotes.LOGGER.warn("[WaveMotes] no encontre discord_emojis.json");
				return;
			}
			JsonObject root = JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
			for (Map.Entry<String, JsonElement> e : root.entrySet()) {
				DISCORD.put(e.getKey(), e.getValue().getAsString());
				DISCORD_NAMES.add(e.getKey());
			}
			DISCORD_NAMES.sort(String.CASE_INSENSITIVE_ORDER);
		} catch (Exception ex) {
			WaveMotes.LOGGER.error("[WaveMotes] error cargando emojis de Discord", ex);
		}
	}

	/** True si el shortcode existe en el set de Discord (para validar en la GUI). */
	public static boolean discordHas(String shortcode) {
		return DISCORD.containsKey(shortcode.toLowerCase(Locale.ROOT));
	}

	/** Texto a insertar (glifo/emoji) de un shortcode de Discord, o null. */
	public static String discordInsert(String shortcode) {
		return DISCORD.get(shortcode.toLowerCase(Locale.ROOT));
	}

	public static int builtinSize() {
		return BUILTIN_NAMES.size();
	}

	public static int discordSize() {
		return DISCORD_NAMES.size();
	}

	/**
	 * Sugerencias para un prefijo, respetando la config:
	 * emotes propios si estan activos; emojis de Discord si estan TODOS activos
	 * o el shortcode esta en la whitelist. Case-insensitive.
	 * Con prefijo VACIO (solo ":") la lista trae unicamente los emotes propios
	 * (+ los de Discord del whitelist); el set completo de Discord aparece solo
	 * al teclear al menos un caracter, para no inundar la lista limpia.
	 */
	public static List<Entry> matching(String prefix) {
		WaveMotesConfig cfg = WaveMotesConfig.get();
		String p = prefix.toLowerCase(Locale.ROOT);
		List<Entry> out = new ArrayList<>();

		if (cfg.builtinEmotesEnabled) {
			for (String n : BUILTIN_NAMES) {
				if (n.toLowerCase(Locale.ROOT).startsWith(p)) {
					out.add(new Entry(n, BUILTIN.get(n), false));
					if (out.size() >= MAX_SUGGESTIONS) {
						return out;
					}
				}
			}
		}

		boolean allAllowed = cfg.discordNativeEnabled;
		boolean typedPrefix = !p.isEmpty();
		for (String n : DISCORD_NAMES) {
			// Con ":" a secas (prefijo vacio) NO volcamos los ~1900 emojis de Discord:
			// la lista limpia muestra solo los emotes propios. Los de Discord aparecen
			// al teclear algo (":sku"). Los del whitelist (anadidos a proposito) si
			// salen siempre.
			boolean wanted = cfg.enabledNativeEmotes.contains(n) || (allAllowed && typedPrefix);
			if (!wanted) {
				continue;
			}
			if (n.startsWith(p)) {
				out.add(new Entry(n, DISCORD.get(n), true));
				if (out.size() >= MAX_SUGGESTIONS) {
					return out;
				}
			}
		}
		return out;
	}
}
