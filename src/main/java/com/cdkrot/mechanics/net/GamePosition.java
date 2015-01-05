package com.cdkrot.mechanics.net;

import io.netty.buffer.ByteBuf;

/**
 * Class describes an block position in game
 */
public final class GamePosition {
    public final int worldid, x, y, z;

    public GamePosition(int worldid, int x, int y, int z) {
        this.worldid = worldid;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public GamePosition(ByteBuf in) {
        this(in.readInt(), in.readInt(), in.readInt(), in.readInt());
    }

    public void writeTo(ByteBuf buffer) {
        buffer.writeInt(worldid);
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
    }
}