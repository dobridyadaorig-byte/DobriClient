package com.dobri.misc;

import com.dobri.DobriClient;
import com.dobri.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class PanicMode {
    private static boolean active = false;
    private static final List<String> savedEnabled = new ArrayList<>();

    public static boolean isActive() { return active; }

    public static void panic() {
        if (active) return;
        active = true;

        savedEnabled.clear();
        for (Module m : DobriClient.MODULES.modules) {
            if (m.enabled) { savedEnabled.add(m.name); m.toggle(); }
        }

        MinecraftClient mc = MinecraftClient.getInstance();
        mc.execute(() -> {
            if (mc.currentScreen != null) mc.setScreen(null);
            if (mc.player != null) {
                mc.player.sendMessage(Text.literal("[Dobri] panic mode ON"), false);
            }
        });

        cleanLogs();
        hideJar();
    }

    public static void unpanic() {
        if (!active) return;
        active = false;

        for (Module m : DobriClient.MODULES.modules) {
            if (savedEnabled.contains(m.name) && !m.enabled) m.toggle();
        }
        savedEnabled.clear();

        MinecraftClient mc = MinecraftClient.getInstance();
        mc.execute(() -> {
            if (mc.player != null) {
                mc.player.sendMessage(Text.literal("[Dobri] panic mode OFF"), false);
            }
        });

        restoreJar();
    }

    private static void cleanLogs() {
        Path runDir = Paths.get(".").toAbsolutePath().normalize();
        try {
            Path latest = runDir.resolve("logs/latest.log");
            if (Files.exists(latest)) Files.writeString(latest, "");

            Path debug = runDir.resolve("logs/debug.log");
            if (Files.exists(debug)) Files.delete(debug);

            Path fabricDir = runDir.resolve("logs/fabric");
            if (Files.exists(fabricDir)) {
                try (var stream = Files.list(fabricDir)) {
                    stream.forEach(p -> { try { Files.delete(p); } catch (IOException ignored) {} });
                }
            }
        } catch (IOException ignored) {}
    }

    private static final String JAR_PREFIX = "dobriclient";
    private static final String HIDDEN_SUFFIX = ".disabled";

    private static void hideJar() {
        Path mods = Paths.get("mods").toAbsolutePath().normalize();
        if (!Files.exists(mods)) return;
        try (var stream = Files.list(mods)) {
            stream.filter(p -> {
                String n = p.getFileName().toString().toLowerCase();
                return n.startsWith(JAR_PREFIX) && n.endsWith(".jar");
            }).forEach(p -> {
                try {
                    Files.move(p, p.resolveSibling(p.getFileName() + HIDDEN_SUFFIX),
                        StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ignored) {}
            });
        } catch (IOException ignored) {}
    }

    private static void restoreJar() {
        Path mods = Paths.get("mods").toAbsolutePath().normalize();
        if (!Files.exists(mods)) return;
        try (var stream = Files.list(mods)) {
            stream.filter(p -> {
                String n = p.getFileName().toString().toLowerCase();
                return n.startsWith(JAR_PREFIX) && n.endsWith(".jar" + HIDDEN_SUFFIX);
            }).forEach(p -> {
                try {
                    String name = p.getFileName().toString();
                    String newName = name.substring(0, name.length() - HIDDEN_SUFFIX.length());
                    Files.move(p, p.resolveSibling(newName),
                        StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ignored) {}
            });
        } catch (IOException ignored) {}
    }
}
