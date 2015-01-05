package com.cdkrot.mechanics.util;

import net.minecraftforge.common.util.ForgeDirection;

public class DirectionalVecs {
    public static final VecI3Base[] list = new VecI3Base[6];

    static {
        ForgeDirection[] dirs = ForgeDirection.VALID_DIRECTIONS;
        for (int i = 0; i < dirs.length; i++) {
            ForgeDirection d = dirs[i];
            list[i] = new VecI3Base(d.offsetX, d.offsetY, d.offsetZ);
        }
    }

    private DirectionalVecs() {
    }

    public static boolean isFacingNegative(VecI3Base base) {
        return (Math.signum(base.x) + Math.signum(base.y) + Math.signum(base.z)) < 0;
    }

}
