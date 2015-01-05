package com.cdkrot.mechanics.net;

import com.cdkrot.mechanics.Mechanics;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;

/**
 * Packet pipeline class. Directs all registered packet data to be handled by
 * the packets themselves. original author: sirgingalot, (and cpw) added some
 * accuracy fixes (such as EntityPlayerMP..)
 */
@ChannelHandler.Sharable
public class PacketTransformer extends MessageToMessageCodec<FMLProxyPacket, BasicPacket> {

    private EnumMap<Side, FMLEmbeddedChannel> channels;
    private List<Class<? extends BasicPacket>> packets = new LinkedList<Class<? extends BasicPacket>>();
    private boolean isPostInitialised = false;// a lock

    /**
     * Register your packet with the pipeline. Discriminators are automatically
     * set.
     */
    public void registerPacket(Class<? extends BasicPacket> packet_class) {
        if ((this.packets.size() > 256) || (this.packets.contains(packet_class)) || (this.isPostInitialised)) {
            throw new RuntimeException("Something gone wrong....");
        }

        this.packets.add(packet_class);
    }

    // In line encoding of the packet, including discriminator setting
    @Override
    protected void encode(ChannelHandlerContext ctx, BasicPacket msg, List<Object> out) throws Exception {
        ByteBuf buffer = Unpooled.buffer();
        Class<? extends BasicPacket> clazz = msg.getClass();
        if (!this.packets.contains(msg.getClass()))
            throw new NullPointerException("No Packet Registered for: " + msg.getClass().getCanonicalName());

        byte discriminator = (byte) this.packets.indexOf(clazz);
        buffer.writeByte(discriminator);
        msg.encodeInto(ctx, buffer);
        FMLProxyPacket proxyPacket = new FMLProxyPacket(buffer.copy(), ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get());
        out.add(proxyPacket);
    }

    // In line decoding and handling of the packet
    @Override
    protected void decode(ChannelHandlerContext ctx, FMLProxyPacket msg, List<Object> out) throws Exception {
        ByteBuf payload = msg.payload();
        byte discriminator = payload.readByte();
        Class<? extends BasicPacket> clazz = this.packets.get(discriminator);
        if (clazz == null)
            throw new NullPointerException("No packet registered for discriminator: " + discriminator);

        BasicPacket pkt = clazz.newInstance();
        pkt.decodeInto(ctx, payload.slice());

        switch (FMLCommonHandler.instance().getEffectiveSide()) {
        case CLIENT:
            pkt.handleClientSide();
            break;

        case SERVER:
            pkt.handleServerSide(((NetHandlerPlayServer) ctx.channel().attr(NetworkRegistry.NET_HANDLER).get()).playerEntity);
            break;

        default:
        }

        out.add(pkt);
    }

    public void initalise() {
        this.channels = NetworkRegistry.INSTANCE.newChannel("mechanics:neo", this);
    }

    // Checks that packet discriminators are common between server and client by
    // using logical sorting
    public void postInitialise() {
        if (this.isPostInitialised)
            return;

        this.isPostInitialised = true;
        Collections.sort(this.packets, new Comparator<Class<? extends BasicPacket>>() {
            @Override
            public int compare(Class<? extends BasicPacket> clazz1, Class<? extends BasicPacket> clazz2) {
                int r = String.CASE_INSENSITIVE_ORDER.compare(clazz1.getCanonicalName(), clazz2.getCanonicalName());
                if (r == 0)
                    r = clazz1.getCanonicalName().compareTo(clazz2.getCanonicalName());

                return r;
            }
        });
    }

    // NOTE-FOR-FUTURE: for complicated situations it is better to use forge api
    // directly, not wrappers by sirgingalot.

    public void sendToAll(BasicPacket message) {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    public void sendTo(BasicPacket message, EntityPlayerMP player) {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    public void sendToAllAround(BasicPacket message, NetworkRegistry.TargetPoint point) {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    public void sendToDimension(BasicPacket message, int dimensionId) {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    public void sendToServer(BasicPacket message) {
        this.channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        this.channels.get(Side.CLIENT).writeAndFlush(message);
    }
}