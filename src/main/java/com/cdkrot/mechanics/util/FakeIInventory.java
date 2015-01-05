package com.cdkrot.mechanics.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * A fake IInventory to hold your items in transactions. Flexible size.
 * 
 * @author Kenzie Togami
 *
 */
public class FakeIInventory implements IInventory {
    public ItemStack[] slots = null;

    public FakeIInventory(int size) {
        slots = new ItemStack[size];
    }

    @Override
    public int getSizeInventory() {
        return slots.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return slots[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amt) {
        ItemStack inSlot = slots[slot], out = null;
        slots[slot] = null;
        if (inSlot != null) {
            if (inSlot.stackSize <= amt) {
                out = inSlot.copy();
                inSlot = null;
            } else {
                out = inSlot.splitStack(amt);
            }
        }
        slots[slot] = inSlot;
        return out;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return getStackInSlot(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        slots[slot] = stack;
    }

    @Override
    public String getInventoryName() {
        return "Fake Inventory";
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
    public void markDirty() {
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1) {
        return true;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int var1, ItemStack var2) {
        return true;
    }

}
