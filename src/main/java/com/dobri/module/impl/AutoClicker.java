package com.dobri.module.impl;

import com.dobri.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Hand;

import java.util.Random;

public class AutoClicker extends Module {
    public AutoClicker() { super("AutoClicker", "Simulate mouse clicks", Category.COMBAT, 0); }

    private final Random rng = new Random();
    public int cpsMin = 8;
    public int cpsMax = 12;
    private long nextClick = 0;

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;
        long now = System.currentTimeMillis();
        if (now < nextClick) return;

        mc.player.swingHand(Hand.MAIN_HAND);
        int cps = cpsMin + rng.nextInt(Math.max(1, cpsMax - cpsMin + 1));
        nextClick = now + (1000 / Math.max(1, cps));
    }
}
