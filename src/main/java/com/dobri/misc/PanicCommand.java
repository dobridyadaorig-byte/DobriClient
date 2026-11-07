package com.dobri.misc;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class PanicCommand {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registry) -> {
            dispatcher.register(ClientCommandManager.literal("panic")
                .then(ClientCommandManager.literal("on").executes(ctx -> { PanicMode.panic(); return 1; }))
                .then(ClientCommandManager.literal("off").executes(ctx -> { PanicMode.unpanic(); return 1; }))
            );
        });
    }
}
