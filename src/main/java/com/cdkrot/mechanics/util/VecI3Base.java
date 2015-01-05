package com.cdkrot.mechanics.util;

public class VecI3Base {
    public int x;
    public int y;
    public int z;

    public VecI3Base() {
        x = 0;
        y = 0;
        z = 0;
    }

    public VecI3Base(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public VecI3Base clone() {
        return new VecI3Base(x, y, z);
    }

    public VecI3 cloneAsVecI3() {
        return new VecI3(x, y, z);
    }
}
