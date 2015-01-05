package com.cdkrot.mechanics.tileentity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import com.cdkrot.mechanics.Mechanics;
import com.cdkrot.mechanics.util.Utility;
import com.cdkrot.mechanics.util.VecI3;
import com.cdkrot.mechanics.util.VecI3Base;


public class TileEntityFanON extends TileEntity implements IInventory{

    private VecI3Base dirvec;
    private VecI3 base;// base
    // private vecd3 power_base;
    private boolean initialized = false;
	public boolean disabled = false;
    private int ePs;// energy per step
	private double ePm;// energy per entity move;

    public void init() {
        if (initialized)
            return;
        initialized = true;
        int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        dirvec = com.cdkrot.mechanics.util.DirectionalVecs.list[meta & 7];
        base = new VecI3(xCoord, yCoord, zCoord);

        ePs = dirvec.y == 0 ? 1 : 3; //need to play with nums to get more realistic effects
		ePm = dirvec.y == 0 ? 30.0 : 48.0;
    }

    @Override
    public void updateEntity() {
        init();
        if (disabled || !Mechanics.fan.updatePowered(worldObj, xCoord, yCoord, zCoord)){
			disabled =true;
			return;
		}
		if (this.stack!=null && this.worldObj.getBlock(base.x+dirvec.x, base.y+dirvec.y, base.z+dirvec.z)==Blocks.air){
			Mechanics.modLogger.info("--");
			EntityItem e = new EntityItem(worldObj, base.x+dirvec.x+0.5, base.y+dirvec.y+0.5, base.z+dirvec.z+0.5, stack);
			worldObj.spawnEntityInWorld(e);
			e.motionX/=3; e.motionY/=3; e.motionZ/=3;
			stack=null;
		}
		goOnAndTrace();
	}

    @SuppressWarnings("unchecked")
    public void goOnAndTrace() {
        VecI3 cur = this.base.clone();
		//cur.add(dirvec);
        int power = 6;
        while (power > 0) {
            AxisAlignedBB selection = Utility.SelectPoolBasingOnVectorAndInc(cur, dirvec);
            Block b = worldObj.getBlock(cur.x+dirvec.x, cur.y+dirvec.y, cur.z+dirvec.z);
            if (b!= Blocks.air)
				return;

			Entity e = Utility.randomFromList((List<Entity>) worldObj.getEntitiesWithinAABB(Entity.class, selection), worldObj.rand);
            if (e != null) {
                e.addVelocity(dirvec.x * power / ePm, dirvec.y * power / ePm, dirvec.z * power / ePm);
                return;
            }
            power -= ePs;
            cur.add(dirvec);// move forward
        }
    }

	//fan-inventory
	private ItemStack stack;

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return slot==0?stack:null;
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (slot==0) {
			ItemStack s = stack;
			s.stackSize = Math.min(amount, stack.stackSize);

			if (s.stackSize==amount)
				stack=null;
			else
				stack.stackSize-=amount;
			return s;
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return getStackInSlot(slot);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		stack=itemStack;
	}

	@Override
	public String getInventoryName() {
		return "";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;//can be used
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemStack) {
		return true;
	}
}
