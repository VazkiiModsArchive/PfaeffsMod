package com.cdkrot.mechanics.tileentity;

import net.minecraft.tileentity.TileEntity;

import com.cdkrot.mechanics.block.BlockLightSensor;

public class TileEntityLightSensor extends TileEntity {
    @Override
    public void updateEntity() {
        if (this.worldObj.getTotalWorldTime() % 60 == 0) {
            if (this.blockType != null && this.blockType instanceof BlockLightSensor) {
                ((BlockLightSensor) this.blockType).updateSensorOutput(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            } else
                this.invalidate();
        }
    }
}
