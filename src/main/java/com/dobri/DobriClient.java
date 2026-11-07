package com.dobri;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import com.dobri.gui.ClickGui;
import com.dobri.module.ModuleManager;
import com.dobri.hud.HudModules;
import com.dobri.misc.PanicMode;
import com.dobri.misc.PanicCommand;

public class DobriClient implements ClientModInitializer {
    public static ModuleManager MODULES = new ModuleManager();
    public static ClickGui GUI = new ClickGui();
    public static KeyBinding openGui;
    public static KeyBinding panicKey;

    @Override
    public void onInitializeClient() {
        openGui = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.dobri.gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "category.dobri"
        ));
        panicKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.dobri.panic", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F6, "category.dobri"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGui.wasPressed()) {
                if (client.currentScreen == null) client.setScreen(GUI);
            }
            while (panicKey.wasPressed()) {
                if (PanicMode.isActive()) PanicMode.unpanic();
                else PanicMode.panic();
            }
            MODULES.onTick();
        });
        HudModules.register();
        PanicCommand.register();
    }
}
