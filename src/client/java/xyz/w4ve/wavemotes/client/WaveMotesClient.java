package xyz.w4ve.wavemotes.client;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import org.lwjgl.glfw.GLFW;

import xyz.w4ve.wavemotes.WaveMotes;

/**
 * Punto de entrada del cliente:
 *  - carga config + catalogo (emotes propios embebidos + emojis de Discord),
 *  - registra la tecla para abrir la config (sin asignar por defecto),
 *  - registra los comandos {@code /wm} y {@code /wavemotes} para
 *    abrir la pantalla de config sin depender de Mod Menu.
 * El autocompletado vive en el mixin {@code CommandSuggestionsMixin}.
 */
public class WaveMotesClient implements ClientModInitializer {
	private static KeyMapping openConfigKey;

	@Override
	public void onInitializeClient() {
		WaveMotesConfig.load();
		EmoteCatalog.load();

		openConfigKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.wavemotes.config",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN, // sin tecla por defecto; el usuario la asigna en Controles
				"key.categories.wavemotes"
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (openConfigKey.consumeClick()) {
				client.setScreen(new ConfigScreen(client.screen));
			}
		});

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("wm")
					.executes(ctx -> openConfig()));
			dispatcher.register(ClientCommandManager.literal("wavemotes")
					.executes(ctx -> openConfig()));
		});

		WaveMotes.LOGGER.info("[WaveMotes] cliente inicializado");
	}

	private static int openConfig() {
		// El comando corre mientras el chat aun esta abierto; abrimos en el
		// siguiente tick para no pelearnos con el cierre del ChatScreen.
		Minecraft mc = Minecraft.getInstance();
		mc.execute(() -> mc.setScreen(new ConfigScreen(null)));
		return 1;
	}
}
