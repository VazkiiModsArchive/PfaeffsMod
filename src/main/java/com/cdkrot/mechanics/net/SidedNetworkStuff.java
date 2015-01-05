package com.cdkrot.mechanics.net;

import net.minecraft.world.World;

import com.cdkrot.mechanics.Mechanics;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

//Sided network code
public class SidedNetworkStuff {
    // we actualy need this. Java JIT dynamic classloading requires to split
    // this into another method
    @SideOnly(Side.CLIENT)
    public static void requestBenchmarkGUI(World w, int x, int y, int z) {
        PacketRequestBenchmarkText packet = new PacketRequestBenchmarkText();
        packet.pos = new GamePosition(w.provider.dimensionId, x, y, z);
        Mechanics.networkHandler.sendToServer(packet);
    }
}
