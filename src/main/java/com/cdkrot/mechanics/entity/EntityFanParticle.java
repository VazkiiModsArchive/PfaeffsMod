package com.cdkrot.mechanics.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityFanParticle extends Entity {
    // /UNIMPLEMENTED IDEA of fan particles...
    public EntityFanParticle(World world) {
        super(world);
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        this.posX = nbt.getDouble("x");
        this.posY = nbt.getDouble("y");
        this.posZ = nbt.getDouble("z");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setDouble("x", posX);
        nbt.setDouble("y", posY);
        nbt.setDouble("z", posZ);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (motionX == 0 && motionY == 0 && motionZ == 0) {
            this.setDead();
            return;
        }
    }

    @Override
    public void applyEntityCollision(Entity e) {
        super.applyEntityCollision(e);
        this.setDead();
    }
}
