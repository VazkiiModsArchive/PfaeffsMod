package com.cdkrot.mechanics.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import com.cdkrot.mechanics.api.benchmark.BenchmarkRegistry;
import com.cdkrot.mechanics.gui.GuiBenchmark;
import com.cdkrot.mechanics.tileentity.TileEntityBenchmark;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketBenchmarkIO extends BasicPacket {
    public String text;
    public GamePosition pos;

    // this packet contains two things: gameposition and text
    // if it is sent from client to server this is a request to update text in
    // benchmark
    // if it is sent to client this is a response to text request.
    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        pos.writeTo(buffer);
        ByteBufUtils.writeUTF8String(buffer, text);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        pos = new GamePosition(buffer);
        text = ByteBufUtils.readUTF8String(buffer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientSide() {
        Minecraft.getMinecraft().displayGuiScreen(new GuiBenchmark(pos, text));
    }

    @Override
    public void handleServerSide(EntityPlayerMP player) {
        TileEntityBenchmark tile = (TileEntityBenchmark) MinecraftServer.getServer().worldServers[pos.worldid].getTileEntity(pos.x, pos.y, pos.z);
        if (BenchmarkRegistry.instance.onTextChanged(tile, text, player))
            tile.s = text;
    }
}
