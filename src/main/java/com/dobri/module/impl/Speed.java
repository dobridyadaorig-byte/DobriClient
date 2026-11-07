package com.dobri.module.impl;

import com.dobri.module.Module;
import net.minecraft.client.MinecraftClient;

public class Speed extends Module {
    public Speed() { super("Speed", "Go fast", Category.MOVEMENT, 0); }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null && mc.player.isOnGround()) {
            double yaw = Math.toRadians(mc.player.getYaw());
            mc.player.addVelocity(-Math.sin(yaw) * 0.4, 0, Math.cos(yaw) * 0.4);
            mc.player.velocityModified = true;
        }
    }
}
