package xyz.w4ve.wavemotes.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Pantalla de configuracion estilo Tweakeroo: switches On/Off, un campo para
 * agregar emojis de Discord sueltos y la LISTA de los agregados, cada uno con
 * su boton para borrarlo (paginada). Solo widgets vanilla (sin malilib/YACL).
 * Se abre con {@code /wm} o con la tecla configurable.
 */
public class ConfigScreen extends Screen {
	private static final int PER_PAGE = 8; // 2 columnas x 4 filas

	private final Screen parent;
	private final WaveMotesConfig cfg = WaveMotesConfig.get();

	private EditBox shortcodeBox;
	private int page = 0;
	private int listTop;
	private Component feedback = Component.empty();
	private int feedbackColor = 0xFFFFFF;

	public ConfigScreen(Screen parent) {
		super(Component.translatable("screen.wavemotes.title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		int w = 240;
		int x = this.width / 2 - w / 2;
		int y = 36;

		addRenderableWidget(CycleButton.onOffBuilder(cfg.builtinEmotesEnabled)
				.create(x, y, w, 20, Component.translatable("option.wavemotes.builtin"),
						(btn, val) -> cfg.builtinEmotesEnabled = val));
		y += 23;

		addRenderableWidget(CycleButton.onOffBuilder(cfg.discordNativeEnabled)
				.create(x, y, w, 20, Component.translatable("option.wavemotes.discord_all"),
						(btn, val) -> cfg.discordNativeEnabled = val));
		y += 27;

		// Agregar un shortcode suelto (ej. :skull:).
		shortcodeBox = new EditBox(this.font, x, y, w - 64, 20,
				Component.translatable("option.wavemotes.add"));
		shortcodeBox.setHint(Component.literal(":skull:"));
		shortcodeBox.setMaxLength(64);
		addRenderableWidget(shortcodeBox);
		addRenderableWidget(Button.builder(Component.translatable("option.wavemotes.add_btn"), b -> addShortcode())
				.bounds(x + w - 60, y, 60, 20).build());
		y += 26;

		// Cabecera de la lista (el texto lo pinta render()).
		this.listTop = y + 12;

		List<String> added = new ArrayList<>(cfg.enabledNativeEmotes);
		int pages = Math.max(1, (added.size() + PER_PAGE - 1) / PER_PAGE);
		this.page = Math.max(0, Math.min(this.page, pages - 1));

		int colW = (w - 4) / 2;
		for (int i = 0; i < PER_PAGE; i++) {
			int idx = page * PER_PAGE + i;
			if (idx >= added.size()) break;
			final String name = added.get(idx);
			int bx = x + (i % 2) * (colW + 4);
			int by = this.listTop + (i / 2) * 22;
			String glyph = EmoteCatalog.discordInsert(name);
			Component label = Component.literal((glyph != null ? glyph + " " : "") + ":" + name + ": ✕");
			addRenderableWidget(Button.builder(label, b -> {
				cfg.enabledNativeEmotes.remove(name);
				setFeedback(Component.translatable("msg.wavemotes.removed", name), 0xFFAA00);
				this.rebuildWidgets();
			}).bounds(bx, by, colW, 20).build());
		}

		int navY = this.listTop + 4 * 22 + 4;
		if (pages > 1) {
			addRenderableWidget(Button.builder(Component.literal("◀"), b -> {
				this.page--;
				this.rebuildWidgets();
			}).bounds(x, navY, 30, 20).build());
			addRenderableWidget(Button.builder(Component.literal("▶"), b -> {
				this.page++;
				this.rebuildWidgets();
			}).bounds(x + 34, navY, 30, 20).build());
		}
		if (!added.isEmpty()) {
			addRenderableWidget(Button.builder(Component.translatable("option.wavemotes.clear_list"), b -> {
				cfg.enabledNativeEmotes.clear();
				this.page = 0;
				setFeedback(Component.translatable("msg.wavemotes.cleared"), 0xFFAA00);
				this.rebuildWidgets();
			}).bounds(x + w - 100, navY, 100, 20).build());
		}

		addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> onClose())
				.bounds(x, this.height - 28, w, 20).build());
	}

	private void addShortcode() {
		String raw = shortcodeBox.getValue().trim().toLowerCase(Locale.ROOT).replace(":", "");
		if (raw.isEmpty()) {
			return;
		}
		if (EmoteCatalog.discordHas(raw)) {
			boolean isNew = cfg.enabledNativeEmotes.add(raw);
			setFeedback(Component.translatable(
					isNew ? "msg.wavemotes.added" : "msg.wavemotes.already", raw), 0x55FF55);
			shortcodeBox.setValue("");
			this.rebuildWidgets();
		} else {
			setFeedback(Component.translatable("msg.wavemotes.notfound", raw), 0xFF5555);
		}
	}

	private void setFeedback(Component text, int color) {
		this.feedback = text;
		this.feedbackColor = color;
	}

	@Override
	public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
		super.render(g, mouseX, mouseY, delta);
		g.drawCenteredString(this.font, this.title, this.width / 2, 16, 0xFFFFFF);

		int x = this.width / 2 - 120;
		Component header = Component.translatable("msg.wavemotes.whitelist", cfg.enabledNativeEmotes.size());
		g.drawString(this.font, header, x, this.listTop - 11, 0xAAAAAA, false);

		if (!this.feedback.getString().isEmpty()) {
			g.drawCenteredString(this.font, this.feedback, this.width / 2, this.height - 42, this.feedbackColor);
		}
	}

	@Override
	public void onClose() {
		cfg.save();
		if (this.minecraft != null) {
			this.minecraft.setScreen(parent);
		}
	}
}
