package vazkii.pfaeff.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vazkii.pfaeff.tile.TileEntityAllocator;

public class ContainerAllocator extends Container {
	
	public ContainerAllocator(IInventory invPlayer, TileEntityAllocator tileentityallocator) {
		allocator = tileentityallocator;
		
        addSlotToContainer(new Slot(tileentityallocator, 0, 80, 36));
        
		for (int i = 0; i < 3; i++) {
			for (int k = 0; k < 9; k++) {
				addSlotToContainer(new Slot(invPlayer, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
			}
		}
		for (int j = 0; j < 9; j++) {
			addSlotToContainer(new Slot(invPlayer, j, 8 + j * 18, 142));
		}	
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return allocator.isUseableByPlayer(entityplayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1) {		
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(par1);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (par1 == 0) {
				if (!mergeItemStack(itemstack1, 0, 36, true)) {
					return null;
				}
			} else {
				Slot filterSlot =(Slot)inventorySlots.get(0);
				ItemStack filter = filterSlot.getStack();
				if ((filter == null) || (filterSlot.getStack().stackSize == 0)) {
					filterSlot.putStack(slot.decrStackSize(1));
				}
			}
			
			if (itemstack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize != itemstack.stackSize) {
				slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
			} else {
				return null;
			}
		}

		return itemstack;
	}
	
	private TileEntityAllocator allocator;
}
