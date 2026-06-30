package xyz.w4ve.wavemotes.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * Integracion OPCIONAL con Mod Menu: pone el boton de configuracion (el
 * engranaje) en la entrada del mod dentro de Mod Menu, abriendo {@link ConfigScreen}.
 *
 * Mod Menu es solo {@code modCompileOnly}: no se empaqueta ni es dependencia de
 * runtime. Este entrypoint "modmenu" unicamente lo carga Mod Menu cuando esta
 * presente; si el jugador no lo tiene, Fabric lo ignora y no pasa nada.
 */
public class ModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return ConfigScreen::new;
	}
}
