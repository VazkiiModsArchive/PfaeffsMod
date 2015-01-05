package com.cdkrot.mechanics.gui;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RendererFanParticle extends Render {
    @Override
    public void doRender(Entity entity, double x, double y, double z, float f, float f1) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y, (float) z);
        GL11.glDisable(GL11.GL_LIGHTING_BIT);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        Tessellator t = Tessellator.instance;
        t.setColorRGBA(255, 255, 255, 255);
        t.startDrawingQuads();
        final float d = 0.05f;
        // 1
        t.addVertex(-d, -d, -d);
        t.addVertex(-d, -d, +d);
        t.addVertex(-d, +d, +d);
        t.addVertex(-d, +d, -d);
        // 2
        t.addVertex(+d, +d, +d);
        t.addVertex(+d, +d, -d);
        t.addVertex(+d, -d, -d);
        t.addVertex(+d, -d, +d);
        // 3
        t.addVertex(-d, -d, -d);
        t.addVertex(-d, -d, +d);
        t.addVertex(+d, -d, +d);
        t.addVertex(+d, -d, -d);
        // 4
        t.addVertex(-d, +d, -d);
        t.addVertex(-d, +d, +d);
        t.addVertex(+d, +d, +d);
        t.addVertex(+d, +d, -d);
        // 5
        t.addVertex(-d, -d, +d);
        t.addVertex(-d, +d, +d);
        t.addVertex(+d, +d, +d);
        t.addVertex(+d, -d, +d);
        // 6
        t.addVertex(-d, -d, -d);
        t.addVertex(-d, +d, -d);
        t.addVertex(+d, +d, -d);
        t.addVertex(+d, -d, -d);

        GL11.glEnable(GL11.GL_LIGHTING_BIT);
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
