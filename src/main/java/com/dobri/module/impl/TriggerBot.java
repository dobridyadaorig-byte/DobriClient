package com.dobri.module.impl;

import com.dobri.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;

import java.util.Random;

public class TriggerBot extends Module {
    public TriggerBot() { super("TriggerBot", "Auto-hit when crosshair on target", Category.COMBAT, 0); }

    private final Random rng = new Random();
    private int delay = 0;
    public int minDelay = 2;
    public int maxDelay = 6;
    public double range = 3.0;
    public double missChance = 0.05;

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.currentScreen != null) return;
        if (delay > 0) { delay--; return; }

        Entity target = mc.crosshairTarget instanceof Entity e ? e : null;
        if (target == null || !(target instanceof LivingEntity) || !target.isAlive()) return;
        if (mc.player.distanceTo(target) > range) return;
        if (rng.nextDouble() < missChance) return;

        mc.interactionManager.attackEntity(mc.player, target);
        mc.player.swingHand(Hand.MAIN_HAND);
        delay = minDelay + rng.nextInt(Math.max(1, maxDelay - minDelay + 1));
    }
}
