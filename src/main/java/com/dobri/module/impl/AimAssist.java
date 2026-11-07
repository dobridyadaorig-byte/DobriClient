package com.dobri.module.impl;

import com.dobri.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class AimAssist extends Module {
    public AimAssist() { super("AimAssist", "Smooth aim toward nearest target", Category.COMBAT, 0); }

    public double range = 4.0;
    public float strength = 0.25f;
    public float smoothness = 0.35f;
    private final Random rng = new Random();

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;

        Entity target = null;
        double best = range;
        for (Entity e : mc.world.getEntities()) {
            if (e == mc.player || !(e instanceof LivingEntity) || !e.isAlive()) continue;
            double d = mc.player.distanceTo(e);
            if (d < best) { best = d; target = e; }
        }
        if (target == null) return;

        Vec3d eye = mc.player.getEyePos();
        Vec3d targetEye = target.getEyePos();
        double dx = targetEye.x - eye.x, dy = targetEye.y - eye.y, dz = targetEye.z - eye.z;
        double horizontal = Math.sqrt(dx*dx + dz*dz);

        float targetYaw = (float)(Math.toDegrees(Math.atan2(dz, dx))) - 90f;
        float targetPitch = (float)(-Math.toDegrees(Math.atan2(dy, horizontal)));

        float currentYaw = mc.player.getYaw();
        float currentPitch = mc.player.getPitch();

        float dYaw = MathHelper.wrapDegrees(targetYaw - currentYaw);
        float dPitch = targetPitch - currentPitch;

        float jitterYaw = (rng.nextFloat() - 0.5f) * 0.6f;
        float jitterPitch = (rng.nextFloat() - 0.5f) * 0.3f;

        float newYaw = currentYaw + dYaw * strength + jitterYaw;
        float newPitch = MathHelper.clamp(currentPitch + dPitch * strength + jitterPitch, -90f, 90f);

        mc.player.setYaw(MathHelper.lerp(smoothness, currentYaw, newYaw));
        mc.player.setPitch(MathHelper.lerp(smoothness, currentPitch, newPitch));
    }
}
