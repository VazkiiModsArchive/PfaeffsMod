package com.cdkrot.mechanics.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import com.cdkrot.mechanics.Mechanics;
import com.cdkrot.mechanics.api.benchmark.BenchmarkRegistry;
import com.cdkrot.mechanics.tileentity.TileEntityBenchmark;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

// packet from client to server
public class PacketRequestBenchmarkText extends BasicPacket {
    public GamePosition pos;

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        pos.writeTo(buffer);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        pos = new GamePosition(buffer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientSide() {
        throw new UnsupportedOperationException("This packet is unapplicable on client side");
    }

    @Override
    public void handleServerSide(EntityPlayerMP player) {
        TileEntityBenchmark tile = (TileEntityBenchmark) MinecraftServer.getServer().worldServers[pos.worldid].getTileEntity(pos.x, pos.y, pos.z);
        if (BenchmarkRegistry.instance.requestEditor(tile, player)) {
            PacketBenchmarkIO packet = new PacketBenchmarkIO();
            packet.pos = pos;
            packet.text = tile.s;

            Mechanics.networkHandler.sendTo(packet, player);
        }
    }
}
