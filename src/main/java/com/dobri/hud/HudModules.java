package com.dobri.hud;

import com.dobri.DobriClient;
import com.dobri.module.Module;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;

import java.util.Collection;

public class HudModules {
    public static void register() {
        HudRenderCallback.EVENT.register(HudModules::render);
    }

    private static void render(DrawContext ctx, float tickDelta) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.options.hudHidden) return;

        renderWatermark(ctx, mc);
        renderPotionHud(ctx, mc);
        renderArmorHud(ctx, mc);
        renderArrayList(ctx, mc);
    }

    private static void renderWatermark(DrawContext ctx, MinecraftClient mc) {
        double t = Util.getMillis() / 1000.0;
        float pulse = (float) (Math.sin(t * 2) * 0.5 + 0.5);
        int r = (int) (0x00 + (0xFF - 0x00) * pulse);
        int g = (int) (0xFF - 0x40 * pulse);
        int b = 0xAA;
        int color = 0xFF000000 | (r << 16) | (g << 8) | b;

        String text = "DobriClient";
        int x = 4, y = 4;
        ctx.fill(x - 2, y - 2, x + mc.textRenderer.getWidth(text) + 2, y + 10, 0x88000000);
        ctx.drawText(mc.textRenderer, text, x, y, color, true);

        String fps = mc.getCurrentFps() + " fps";
        ctx.drawText(mc.textRenderer, fps, x, y + 12, 0xFFAAAAAA, true);
    }

    private static void renderPotionHud(DrawContext ctx, MinecraftClient mc) {
        Collection<StatusEffectInstance> effects = mc.player.getStatusEffects();
        if (effects.isEmpty()) return;

        int x = 4;
        int y = 40;

        for (StatusEffectInstance e : effects) {
            String name = e.getEffectType().value().getName().getString();
            int dur = e.getDuration() / 20;
            String time = dur > 9999 ? "inf" : dur + "s";

            String line = name + " " + time;
            int w = mc.textRenderer.getWidth(line);
            int color = dur < 15 ? 0xFFFF4444 : 0xFF44FF44;

            ctx.fill(x - 2, y - 2, x + w + 4, y + 10, 0x88000000);
            ctx.drawText(mc.textRenderer, line, x, y, color, true);
            y += 14;
        }
    }

    private static void renderArmorHud(DrawContext ctx, MinecraftClient mc) {
        int x = 4;
        int y = mc.getWindow().getScaledHeight() - 60;

        ItemStack[] armor = {
            mc.player.getInventory().getArmorStack(3),
            mc.player.getInventory().getArmorStack(2),
            mc.player.getInventory().getArmorStack(1),
            mc.player.getInventory().getArmorStack(0)
        };

        for (ItemStack s : armor) {
            if (s.isEmpty()) continue;
            String name = s.getItem().getName().getString();
            int dur = s.getMaxDamage() - s.getDamage();
            String line = name + " " + dur;
            ctx.fill(x - 2, y - 2, x + mc.textRenderer.getWidth(line) + 4, y + 10, 0x88000000);
            ctx.drawText(mc.textRenderer, line, x, y, 0xFFFFFFFF, true);
            y += 14;
        }
    }

    private static void renderArrayList(DrawContext ctx, MinecraftClient mc) {
        int screenW = mc.getWindow().getScaledWidth();
        int y = 4;

        for (Module m : DobriClient.MODULES.modules) {
            if (!m.enabled) continue;
            String text = m.name;
            int w = mc.textRenderer.getWidth(text);
            int x = screenW - w - 4;

            ctx.fill(x - 2, y - 2, screenW - 2, y + 10, 0x88000000);
            ctx.drawText(mc.textRenderer, text, x, y, 0xFF00FFAA, true);
            y += 12;
        }
    }
}
