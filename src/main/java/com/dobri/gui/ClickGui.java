package com.dobri.gui;

import com.dobri.DobriClient;
import com.dobri.module.Module;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class ClickGui extends Screen {
    public ClickGui() { super(Text.literal("DobriClient")); }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        this.renderBackground(ctx, mouseX, mouseY, delta);
        ctx.fill(20, 20, 300, 420, 0xCC101010);
        ctx.drawText(this.textRenderer, "DobriClient", 30, 28, 0xFF00FFAA, false);
        int y = 50;
        for (Module.Category cat : Module.Category.values()) {
            ctx.drawText(this.textRenderer, "- " + cat.name() + " -", 30, y, 0xFFAAAAAA, false);
            y += 14;
            for (Module m : DobriClient.MODULES.modules) {
                if (m.category != cat) continue;
                int color = m.enabled ? 0xFF00FFAA : 0xFFFFFFFF;
                ctx.drawText(this.textRenderer, (m.enabled ? "+ " : "  ") + m.name, 34, y, color, false);
                y += 12;
            }
            y += 6;
        }
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        int y = 50;
        for (Module.Category cat : Module.Category.values()) {
            y += 14;
            for (Module m : DobriClient.MODULES.modules) {
                if (m.category != cat) continue;
                if (mx >= 34 && mx <= 200 && my >= y && my <= y + 12) { m.toggle(); return true; }
                y += 12;
            }
            y += 6;
        }
        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean shouldPause() { return false; }
}
