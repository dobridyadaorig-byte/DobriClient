package com.dobri.module;

import com.dobri.module.impl.*;
import java.util.*;

public class ModuleManager {
    public final List<Module> modules = new ArrayList<>();
    public ModuleManager() {
        modules.add(new KillAura());
        modules.add(new ESP());
        modules.add(new Speed());
        modules.add(new Fly());
        modules.add(new AILab());
        modules.add(new TriggerBot());
        modules.add(new AutoClicker());
        modules.add(new HitSelect());
        modules.add(new Reach());
        modules.add(new AimAssist());
    }
    public void onTick() { for (Module m : modules) if (m.enabled) m.onTick(); }
    public Module get(String name) {
        for (Module m : modules) if (m.name.equalsIgnoreCase(name)) return m;
        return null;
    }
}
