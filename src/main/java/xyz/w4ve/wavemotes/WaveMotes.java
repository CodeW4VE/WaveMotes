package xyz.w4ve.wavemotes;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Punto de entrada comun (cliente + servidor). Por ahora solo arranca; la
 * logica vive en el cliente ({@code WaveMotesClient}).
 */
public class WaveMotes implements ModInitializer {
	public static final String MOD_ID = "wavemotes";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("[WaveMotes] inicializado");
	}
}
