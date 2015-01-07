package vazkii.pfaeff.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityAllocator extends TileEntity implements IInventory {

	private ItemStack allocatorFilterItem;

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		if (i == 0) {
			return allocatorFilterItem;
		}
		return null;
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (i != 0) {
			return null;
		}
		if (allocatorFilterItem != null) {
			if (allocatorFilterItem.stackSize <= j) {
                ItemStack itemstack = allocatorFilterItem;
                allocatorFilterItem = null;
                return itemstack;
			}
            ItemStack itemstack = allocatorFilterItem.splitStack(j);
            if(allocatorFilterItem.stackSize == 0) {
            	allocatorFilterItem = null;
            }
            return itemstack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		if (i != 0) {
			return;
		}
		allocatorFilterItem = itemstack;
        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
        	itemstack.stackSize = getInventoryStackLimit();
        }
	}

	@Override
	public String getInventoryName() {
		return "Allocator";
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        if(worldObj.getTileEntity(xCoord, yCoord, zCoord) != this) {
        	return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5d, (double)yCoord + 0.5d, (double)zCoord + 0.5d) <= 64d;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		NBTTagList items = nbttagcompound.getTagList("Items", 10);
		if (items.tagCount() == 0) {
			return;
		}
		NBTTagCompound item = (NBTTagCompound) items.getCompoundTagAt(0); // TODO: is it always at 0 ?
		int slot = item.getByte("Slot") & 0xff;
		if (slot == 0) {
			allocatorFilterItem = ItemStack.loadItemStackFromNBT(item);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {	
		super.writeToNBT(nbttagcompound);
		NBTTagList items = new NBTTagList();
		if (allocatorFilterItem != null) {
			NBTTagCompound item = new NBTTagCompound();
			item.setByte("Slot", (byte)0);
			allocatorFilterItem.writeToNBT(item);
			items.appendTag(item);
		}
		nbttagcompound.setTag("Items", items);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if ((allocatorFilterItem != null) && (i == 0)) {
			return allocatorFilterItem;
		} else {
			return null;
		}
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public void openInventory() { } // TODO: Should something be here?

	@Override
	public void closeInventory() { } // TODO: Should something be here?

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return true;
	}
}
