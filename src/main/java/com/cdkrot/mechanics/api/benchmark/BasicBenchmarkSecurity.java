package com.cdkrot.mechanics.api.benchmark;

import net.minecraft.entity.player.EntityPlayerMP;

import com.cdkrot.mechanics.tileentity.TileEntityBenchmark;

/**
 * Checks distance (players can't interact with Benchmarks, which are far from
 * them)
 */
public class BasicBenchmarkSecurity implements INetworkBenchmarkProcessor {

    @Override
    public boolean onTextChanged(TileEntityBenchmark tile, String newtext, EntityPlayerMP p) {
        return requestEditor(tile, p); // same as in request editor;
    }

    @Override
    public boolean onBenchmark(TileEntityBenchmark tile, String echotext) {
        return true;
    }

    @Override
    public boolean requestEditor(TileEntityBenchmark tile, EntityPlayerMP p) {
        // 16 blocks maximum.
        return p.posX * p.posX + p.posY * p.posY + p.posZ * p.posZ > 256;
    }

}
