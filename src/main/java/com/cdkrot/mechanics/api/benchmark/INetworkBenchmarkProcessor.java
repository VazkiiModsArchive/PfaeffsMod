package com.cdkrot.mechanics.api.benchmark;

import net.minecraft.entity.player.EntityPlayerMP;

import com.cdkrot.mechanics.tileentity.TileEntityBenchmark;

/**
 * Benchmark action preprocessor. Runed in server side.
 */
public interface INetworkBenchmarkProcessor {
    /**
     * Called when text on benchmark changing. Can cancel change event(antispam
     * and such services)
     * 
     * @param tile
     *            - tile {provides original text, x, y, z, world}
     * @param newtext
     *            - text setting
     * @param p
     *            - player. Note since 1.7 it is entityPlayer
     * @return false to cancel event
     */
    public boolean onTextChanged(TileEntityBenchmark tile, String newtext, EntityPlayerMP p);

    /**
     * Called when bencmark is echos text. Can cancel event
     * 
     * @param tile
     *            - tile {world, x, y, z. data}
     * @param echotext
     *            {processed pattern}
     * @return false to cancel event
     */
    public boolean onBenchmark(TileEntityBenchmark tile, String echotext);

    /**
     * check Called when player requesting an editing GUI open (gui is open by
     * client side, but text requires to be send from server). (Note since 1.7
     * player is entityPlayer)
     * 
     * @return false to cancel event
     */
    public boolean requestEditor(TileEntityBenchmark tile, EntityPlayerMP p);
}
