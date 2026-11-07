package com.dobri.module.impl;

import com.dobri.module.Module;

public class Reach extends Module {
    public Reach() { super("Reach", "Extend interaction distance (legit cap 3.1)", Category.COMBAT, 0); }

    public double getExtraReach() {
        return 0.1;
    }
}
