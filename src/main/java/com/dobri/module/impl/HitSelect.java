package com.dobri.module.impl;

import com.dobri.module.Module;

public class HitSelect extends Module {
    public HitSelect() { super("HitSelect", "Only hit when at optimal knockback timing", Category.COMBAT, 0); }

    public boolean shouldBoost(boolean isHurt, boolean isOnGround) {
        return isHurt && isOnGround;
    }
}
