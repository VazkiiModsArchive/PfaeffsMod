package com.cdkrot.mechanics.util;

public class VecF3 {
    public float x;
    public float y;
    public float z;

    public VecF3() {
        x = 0f;
        y = 0f;
        z = 0f;
    }

    public VecF3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public VecF3 multiply(float m) {
        x = m * x;
        y = m * y;
        z = m * z;
        return this;
    }

    public VecF3 add(VecI3 s2) {
        x += s2.x;
        y += s2.y;
        z += s2.z;
        return this;
    }

    public VecF3 add(int x_, int y_, int z_) {
        return this.add(new VecI3(x_, y_, z_));
    }

    @Override
    public VecF3 clone() {
        return new VecF3(x, y, z);
    }

    public String shortDescription() {
        return "Vecf3[" + x + ", " + y + ", " + z + "]";
    }

    public VecF3 incAllByOne() {
        x++;
        y++;
        z++;
        return this;
    }

    public VecD3 toVecD3() {
        return new VecD3(x, y, z);
    }

}
