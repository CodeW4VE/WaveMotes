package xyz.w4ve.wavemotes.client.mixin;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;

import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import xyz.w4ve.wavemotes.client.EmoteCatalog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Hace que al escribir {@code :} (y al ir tecleando {@code :nombre}) aparezca
 * SOLO, sin tener que tabular, el mismo popup de sugerencias que sale con
 * {@code /}, pero con los emotes. Al elegir uno se inserta el glifo (que se ve
 * renderizado por la fuente embebida del mod).
 *
 * Interceptamos en HEAD y, cuando detectamos un token de emote, montamos las
 * sugerencias nosotros y cancelamos la version vanilla de updateCommandInfo,
 * para que su manejo de flags (keepSuggestions, etc.) no nos tape el popup.
 * Si no hay emote, dejamos correr vanilla normal.
 */
@Mixin(CommandSuggestions.class)
public abstract class CommandSuggestionsMixin {
	@Shadow @Final EditBox input;
	@Shadow private CompletableFuture<Suggestions> pendingSuggestions;
	@Shadow public abstract void showSuggestions(boolean narrateFirstSuggestion);

	@Inject(method = "updateCommandInfo", at = @At("HEAD"), cancellable = true)
	private void wavemotes$emoteSuggestions(CallbackInfo ci) {
		String text = this.input.getValue();
		int cursor = this.input.getCursorPosition();
		if (cursor < 1 || cursor > text.length() || text.startsWith("/")) {
			return;
		}

		// El ':' del token, buscado hacia atras desde el cursor.
		int colon = text.lastIndexOf(':', cursor - 1);
		if (colon < 0) {
			return;
		}
		// Solo lo tratamos como emote si el ':' abre palabra (inicio o tras espacio),
		// asi "12:30" o "http://" no disparan el popup.
		if (colon > 0 && text.charAt(colon - 1) != ' ') {
			return;
		}
		String word = text.substring(colon + 1, cursor);
		if (word.indexOf(' ') >= 0) {
			return;
		}

		// word vacio (solo ":") -> muestra todos, igual que "/".
		List<EmoteCatalog.Entry> matches = EmoteCatalog.matching(word);
		if (matches.isEmpty()) {
			return; // que vanilla siga (puede ser otra cosa)
		}

		StringRange range = StringRange.between(colon, cursor);
		List<Suggestion> list = new ArrayList<>(matches.size());
		for (EmoteCatalog.Entry m : matches) {
			list.add(new Suggestion(range, m.insert(), new LiteralMessage(":" + m.name() + ":")));
		}

		this.pendingSuggestions = CompletableFuture.completedFuture(new Suggestions(range, list));
		this.showSuggestions(false);
		ci.cancel();
	}
}
