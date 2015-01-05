package com.cdkrot.mechanics.network;

import com.cdkrot.mechanics.entity.EntityFanParticle;
import com.cdkrot.mechanics.gui.RendererFanParticle;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void doInit() {
        RenderingRegistry.registerEntityRenderingHandler(EntityFanParticle.class, new RendererFanParticle());
    }
}