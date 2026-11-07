package com.dobri.mixin;

import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(method = "renderEntities", at = @At("HEAD"))
    private void dobri$glowBegin(CallbackInfo ci) {
        org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_BLEND);
        org.lwjgl.opengl.GL11.glBlendFunc(
            org.lwjgl.opengl.GL11.GL_SRC_ALPHA,
            org.lwjgl.opengl.GL11.GL_ONE
        );
    }

    @Inject(method = "renderEntities", at = @At("RETURN"))
    private void dobri$glowEnd(CallbackInfo ci) {
        org.lwjgl.opengl.GL11.glBlendFunc(
            org.lwjgl.opengl.GL11.GL_SRC_ALPHA,
            org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA
        );
    }
}
