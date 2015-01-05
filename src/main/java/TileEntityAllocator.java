package net.minecraft.src;

public class TileEntityAllocator extends TileEntity implements IInventory {

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		if (i == 0) {
			//System.out.println("getStackInSlot(0) = " + allocatorFilterItem + " | pos: " + xCoord + "; " + yCoord + "; " + zCoord + " | remote? " + this.worldObj.isRemote);
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
	public String getInvName() {
		return "Allocator";
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this) {
        	return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5d, (double)yCoord + 0.5d, (double)zCoord + 0.5d) <= 64d;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		NBTTagList items = nbttagcompound.getTagList("Items");
		if (items.tagCount() == 0) {
			return;
		}
		NBTTagCompound item = (NBTTagCompound)items.tagAt(0); // TODO: is it always at 0 ?
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
	
	private ItemStack allocatorFilterItem;

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public boolean isInvNameLocalized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStackValidForSlot(int var1, ItemStack var2) {
		// TODO Auto-generated method stub
		return false;
	}
}
