package xyz.w4ve.wavemotes.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.fabricmc.loader.api.FabricLoader;

import xyz.w4ve.wavemotes.WaveMotes;

import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;

/**
 * Configuracion del mod, persistida en {@code config/wavemotes.json}.
 * Sin dependencias externas: solo Gson (ya viene con Minecraft) + la API de
 * Fabric Loader para localizar la carpeta de config. Esto mantiene el "un
 * solo jar que instalas y funciona".
 */
public final class WaveMotesConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path PATH =
			FabricLoader.getInstance().getConfigDir().resolve("wavemotes.json");

	/** Emotes propios embebidos (los de MineWave). Siempre se renderizan. */
	public boolean builtinEmotesEnabled = true;
	/** Emojis nativos de Discord: TODOS de golpe. OFF por defecto. */
	public boolean discordNativeEnabled = false;
	/** Emojis de Discord sueltos activados a mano (ej. solo "skull"). */
	public Set<String> enabledNativeEmotes = new TreeSet<>();

	private static WaveMotesConfig instance;

	public static WaveMotesConfig get() {
		if (instance == null) {
			load();
		}
		return instance;
	}

	public static void load() {
		try {
			if (Files.exists(PATH)) {
				try (Reader r = Files.newBufferedReader(PATH, StandardCharsets.UTF_8)) {
					WaveMotesConfig c = GSON.fromJson(r, WaveMotesConfig.class);
					instance = (c != null) ? c : new WaveMotesConfig();
				}
			} else {
				instance = new WaveMotesConfig();
				instance.save();
			}
		} catch (Exception e) {
			WaveMotes.LOGGER.error("[WaveMotes] no pude leer la config, uso defaults", e);
			instance = new WaveMotesConfig();
		}
		if (instance.enabledNativeEmotes == null) {
			instance.enabledNativeEmotes = new TreeSet<>();
		}
	}

	public void save() {
		try {
			Files.createDirectories(PATH.getParent());
			try (Writer w = Files.newBufferedWriter(PATH, StandardCharsets.UTF_8)) {
				GSON.toJson(this, w);
			}
		} catch (Exception e) {
			WaveMotes.LOGGER.error("[WaveMotes] no pude guardar la config", e);
		}
	}
}
