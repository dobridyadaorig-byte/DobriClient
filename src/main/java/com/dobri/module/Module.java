package com.dobri.module;

public abstract class Module {
    public String name, desc;
    public int key;
    public boolean enabled;
    public Category category;

    public Module(String name, String desc, Category cat, int key) {
        this.name = name; this.desc = desc; this.category = cat; this.key = key;
    }
    public void toggle() { enabled = !enabled; if (enabled) onEnable(); else onDisable(); }
    public void onEnable() {}
    public void onDisable() {}
    public void onTick() {}
    public enum Category { COMBAT, MOVEMENT, RENDER, MISC }
}
