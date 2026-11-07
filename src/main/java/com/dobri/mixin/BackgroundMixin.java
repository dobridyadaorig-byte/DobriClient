package com.dobri.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class BackgroundMixin {

    private static long dobri$start = System.currentTimeMillis();

    @Inject(method = "renderBackground", at = @At("HEAD"), cancellable = true)
    private void dobri$bg(DrawContext ctx, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        long t = System.currentTimeMillis() - dobri$start;
        float p = (float)(Math.sin(t / 900.0) * 0.5 + 0.5);

        int top = 0xFF0A0A12;
        int bot = 0xFF001A12;
        int blend = blend(top, bot, p);

        ctx.fill(0, 0, ctx.getScaledWindowWidth(), ctx.getScaledWindowHeight(), blend);
        ci.cancel();
    }

    private static int blend(int a, int b, float t) {
        int ar = (a >> 16) & 0xFF, ag = (a >> 8) & 0xFF, ab = a & 0xFF;
        int br = (b >> 16) & 0xFF, bg = (b >> 8) & 0xFF, bb = b & 0xFF;
        int r = (int)(ar + (br - ar) * t);
        int g = (int)(ag + (bg - ag) * t);
        int bl = (int)(ab + (bb - ab) * t);
        return 0xFF000000 | (r << 16) | (g << 8) | bl;
    }
}
