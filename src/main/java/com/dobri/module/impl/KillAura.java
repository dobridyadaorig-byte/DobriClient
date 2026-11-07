package com.dobri.module.impl;

import com.dobri.DobriClient;
import com.dobri.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;

public class KillAura extends Module {
    public KillAura() { super("KillAura", "Auto-attack nearby players", Category.COMBAT, 0); }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;
        AILab ai = (AILab) DobriClient.MODULES.get("AILab");
        boolean aiMode = ai != null && ai.enabled && ai.isReplaying();

        Entity target = null;
        double best = 3.5;
        for (Entity e : mc.world.getEntities()) {
            if (e == mc.player || !(e instanceof PlayerEntity) || !e.isAlive()) continue;
            double d = mc.player.distanceTo(e);
            if (d < best) { best = d; target = e; }
        }
        if (target != null) {
            if (!aiMode) {
                mc.player.lookAt(net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor.EYES, target.getPos());
            }
            mc.interactionManager.attackEntity(mc.player, target);
            mc.player.swingHand(Hand.MAIN_HAND);
        }
    }
}
