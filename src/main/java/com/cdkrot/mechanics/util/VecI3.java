package com.cdkrot.mechanics.util;

public class VecI3 extends VecI3Base {
    public VecI3() {
        super();
    }

    public VecI3(int x, int y, int z) {
        super(x, y, z);
    }

    public VecI3 multiply(int m) {
        x = m * x;
        y = m * y;
        z = m * z;
        return this;
    }

    public VecI3 add(VecI3Base vec) {
        x += vec.x;
        y += vec.y;
        z += vec.z;
        return this;
    }

    public VecI3 add(int x_, int y_, int z_) {
        return this.add(new VecI3(x_, y_, z_));
    }

    @Override
    public String toString() {
        return "Veci3: " + x + " " + y + " " + z;
    }

    public VecI3 incAllByOne() {
        x++;
        y++;
        z++;
        return this;
    }

    public VecF3 mutliplyf(float f) {
        return new VecF3(x * f, y * f, z * f);
    }

    public VecF3 multiply3f(float x_, float y_, float z_) {
        return new VecF3(x * x_, y * y_, z * z_);
    }

    public VecI3 substract(VecI3 v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        return this;
    }

    @Override
    public VecI3 clone() {
        return new VecI3(x, y, z);
    }
}
