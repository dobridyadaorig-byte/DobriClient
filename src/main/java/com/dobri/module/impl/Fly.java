package com.dobri.module.impl;

import com.dobri.module.Module;
import net.minecraft.client.MinecraftClient;

public class Fly extends Module {
    public Fly() { super("Fly", "Creative-style flight", Category.MOVEMENT, 0); }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null) {
            mc.player.getAbilities().allowFlying = true;
            mc.player.getAbilities().flying = true;
        }
    }
    @Override
    public void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null && !mc.player.isCreative()) {
            mc.player.getAbilities().allowFlying = false;
            mc.player.getAbilities().flying = false;
        }
    }
}
