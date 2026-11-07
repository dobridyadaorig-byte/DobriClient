package com.dobri.module.impl;

import com.dobri.module.Module;
import net.minecraft.client.MinecraftClient;
import java.util.*;

public class AILab extends Module {
    public AILab() { super("AILab", "Record & replay human-like aim", Category.COMBAT, 0); }

    private final List<float[]> samples = new ArrayList<>();
    private boolean recording, replaying;
    private int replayIndex = 0;

    public void startRecord() { samples.clear(); recording = true; }
    public void stopRecord() { recording = false; }
    public void startReplay() { replaying = true; replayIndex = 0; }
    public void stopReplay() { replaying = false; }
    public boolean isReplaying() { return replaying; }
    public int sampleCount() { return samples.size(); }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;

        if (recording) samples.add(new float[]{mc.player.getYaw(), mc.player.getPitch()});

        if (replaying && !samples.isEmpty()) {
            float[] s = samples.get(replayIndex % samples.size());
            mc.player.setYaw(s[0]);
            mc.player.setPitch(s[1]);
            replayIndex++;
        }
    }
}
