package com.cdkrot.mechanics.tileentity;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockSourceImpl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.world.World;

import com.cdkrot.mechanics.BehaviourDispenseItemStack;
import com.cdkrot.mechanics.Mechanics;
import com.cdkrot.mechanics.api.allocator.AllocatorRegistry;
import com.cdkrot.mechanics.util.DirectionalVecs;
import com.cdkrot.mechanics.util.VecI3Base;

public class TileEntityAllocator extends TileEntity implements IInventory, ISidedInventory {
    public ItemStack allocatorFilterItems[] = new ItemStack[16];

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if (amount > 0) {
            ItemStack itemstack = allocatorFilterItems[slot];
            allocatorFilterItems[slot] = null;
            return itemstack;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemstack) {
        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
            itemstack.stackSize = getInventoryStackLimit();
        allocatorFilterItems[slot] = itemstack;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D; // EXperimental
    }

    @Override
    // TODO: rewrite NBT IO code...
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagList items = (NBTTagList) nbt.getTag("Items");
        for (int i = 0; (i < items.tagCount() && i < getSizeInventory()); i++) {
            allocatorFilterItems[i] = ItemStack.loadItemStackFromNBT(items.getCompoundTagAt(i));// TODO:
                                                                                                // experimental
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        NBTTagList items = new NBTTagList();
        for (int i = 0; i < getSizeInventory(); i++) {
            if (allocatorFilterItems[i] != null)
                items.appendTag(allocatorFilterItems[i].writeToNBT(new NBTTagCompound()));
            nbttagcompound.setTag("Items", items);
        }
    }

    @Override
    public ItemStack getStackInSlot(int s) {
        return allocatorFilterItems[s];
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public int getSizeInventory() {
        return 16;
    }

    @Override
    public void markDirty() {
        Mechanics.modLogger.info("Marked dirty");
    }

    @Override
    public String getInventoryName() {
        return Mechanics.allocator.getLocalizedName();
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValidForSlot(int s, ItemStack is) {
        return true;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int s) {
        return getStackInSlot(s);
    }

    // ==========================================================//
    // Start of item io algorithms. //
    // ==========================================================//
    // TODO rewrite

    private final BehaviourDispenseItemStack dispenser = new BehaviourDispenseItemStack();

    /**
     * Returns true, if the item is allowed to pass.
     */
    private boolean passesFilter(ItemStack item) {
        if (allocatorFilterItems == null)
            return true;
        boolean t = true;
        for (ItemStack filter_ : allocatorFilterItems) {
            if (filter_ == null)
                continue;
            t = false;
            if ((item.getItem() == filter_.getItem()) && (item.getItemDamage() == filter_.getItemDamage()))
                if (ItemStack.areItemStackTagsEqual(item, filter_))
                    return true;
        }
        return t;
    }

    /**
     * Returns the container (IIventory) at position (x,y,z) if it exists.
     */
    private IInventory containerAtPos(World world, int x, int y, int z) {
        IInventory inv = AllocatorRegistry.instance.getIInventoryFor(world, x, y, z);

        if (inv != null)
            return inv;

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IInventory)
            return (IInventory) tile;
        else
            return null;
    }

    /**
     * INPUT! Returns an item (index) from the container, using the same rule as
     * the dispenser
     */
    private int getItemIndexFromContainer(IInventory inventory, int side) {
        if (inventory == null)
            return -1;

        if (inventory instanceof ISidedInventory) {
            // TODO: check.
            int list[] = ((ISidedInventory) inventory).getAccessibleSlotsFromSide(side);

            for (int k = 0; k < list.length; k++) {
                ItemStack s = inventory.getStackInSlot(list[k]);
                if ((s != null) && passesFilter(s)) {
                    return list[k];
                }
            }
        } else {
            for (int k = 0; k < inventory.getSizeInventory(); k++) {
                ItemStack s = inventory.getStackInSlot(k);
                if ((s != null) && passesFilter(s)) {
                    return k;
                }
            }
        }
        return -1;
    }

    /**
     * Spits out an item (like the dropper, but the whole stack)
     */
    private void dispense(World world, int i, int j, int k, ItemStack item) {
        BlockSourceImpl blockImpl = new BlockSourceImpl(world, i, j, k);
        // Inventory fetcher for hopper, probably not needed
        //
        // int meta = world.getBlockMetadata(i, j, k) & 7;
        // IInventory hopper = TileEntityHopper.func_145893_b(world,
        // (double) (i + Facing.offsetsXForSide[meta]),
        // (double) (j + Facing.offsetsYForSide[meta]),
        // (double) (k + Facing.offsetsZForSide[meta]));

        // TODO: use stack
        ItemStack stack = dispenser.dispense(blockImpl, item);
        // if (stack != null && stack.stackSize == 0)
        // stack = null;
        // removed this, because the code above did nothing anyways. so
        // strange code
    }

    /**
     * Handles all the item input/output
     */
    @SuppressWarnings("unchecked")
    // TODO: remove the need for suppression
    public void allocateItems(World world, int x, int y, int z, Random random) {
        // NB: front is the out, back is the in
        int front = getBlockMetadata(), back = Facing.oppositeSide[front];
        VecI3Base d = DirectionalVecs.list[front];
        int xoff = x - d.x, yoff = y - d.y, zoff = z - d.z;
        int invxoff = x + d.x, invyoff = y + d.y, invzoff = z + d.z;
        IInventory input = containerAtPos(world, xoff, yoff, zoff);
        IInventory output = containerAtPos(world, invxoff, invyoff, invzoff);

        if (input == null) {
            List<Entity> entities = (List<Entity>) world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(xoff, yoff, zoff, xoff + 1, yoff + 1, zoff + 1));
            List<IInventory> invs = AllocatorRegistry.instance.getIInventoryAllInFor(entities, true);
            // TODO: entity inventories should be caught by other way
            // [REFACTORING].

            if (invs.size() > 0) {
                input = invs.get(random.nextInt(invs.size()));
            } else {
                return;// no input.
            }
        }

        if (output == null) {
            List<Entity> entities = (List<Entity>) world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(invxoff, invyoff, invzoff, invxoff + 1, invyoff + 1, invzoff + 1));
            List<IInventory> invs = AllocatorRegistry.instance.getIInventoryAllInFor(entities, false);
            if (invs.size() > 0) {
                output = invs.get(random.nextInt(invs.size()));
            } else if (world.getBlock(invxoff, invyoff, invzoff).isOpaqueCube()) {
                return;
            }
        }
        // TODO: inline
        int itemIndex = getItemIndexFromContainer(input, front);

        if (itemIndex < 0)
            return; // no item
        ItemStack stack = input.getStackInSlot(itemIndex).copy();
        if (!canExtractFromInventoryAtSlot(input, stack, itemIndex, front)) {
            // illegal
            return;
        }
        if (output == null) {
            dispense(world, invxoff, invyoff, invzoff, stack);
            stack = null;
        } else {
            stack = moveStackToInv(output, stack, back);
        }
        input.setInventorySlotContents(itemIndex, stack);
    }

    private static ItemStack moveStackToInv(IInventory inv, ItemStack stack, int side) {
        if (inv instanceof ISidedInventory && side > -1) {
            ISidedInventory isidedinventory = (ISidedInventory) inv;
            int[] slots = isidedinventory.getAccessibleSlotsFromSide(side);

            for (int l = 0; l < slots.length && stack != null && stack.stackSize > 0; ++l) {
                stack = tryStackMove(inv, stack, slots[l], side);
            }
        } else {
            int size = inv.getSizeInventory();

            for (int k = 0; k < size && stack != null && stack.stackSize > 0; ++k) {
                stack = tryStackMove(inv, stack, k, side);
            }
        }

        if (stack != null && stack.stackSize == 0) {
            stack = null;
        }

        return stack;
    }

    /**
     * Attempts to move the given stack into the given inventory at the given
     * slot and side.
     */
    private static ItemStack tryStackMove(IInventory inv, ItemStack stack, int slot, int side) {
        ItemStack stackInSlot = inv.getStackInSlot(slot);

        if (canInsertIntoInventoryAtSlot(inv, stack, slot, side)) {
            boolean save = false;

            if (stackInSlot == null) {
                int max = Math.min(stack.getMaxStackSize(), inv.getInventoryStackLimit());
                if (max >= stack.stackSize) {
                    inv.setInventorySlotContents(slot, stack);
                    stack = null;
                } else {
                    inv.setInventorySlotContents(slot, stack.splitStack(max));
                }
                save = true;
            } else if (canStackItems(stackInSlot, stack)) {
                int max = Math.min(stack.getMaxStackSize(), inv.getInventoryStackLimit());
                if (max > stackInSlot.stackSize) {
                    int l = Math.min(stack.stackSize, max - stackInSlot.stackSize);
                    stack.stackSize -= l;
                    stackInSlot.stackSize += l;
                    save = l > 0;
                }
            }

            if (save) {
                inv.markDirty();
            }
        }

        return stack;
    }

    /**
     * Checks for insertion into the given inventory at the slot specified, and,
     * if inv implements ISidedInventory, the given side.
     */
    private static boolean canInsertIntoInventoryAtSlot(IInventory inv, ItemStack stack, int slot, int side) {
        return (inv.isItemValidForSlot(slot, stack) && (!(inv instanceof ISidedInventory) || ((ISidedInventory) inv).canInsertItem(slot, stack, side)));
    }

    /**
     * Checks for extraction into the given inventory at the slot specified,
     * and, if inv implements ISidedInventory, the given side.
     */
    private static boolean canExtractFromInventoryAtSlot(IInventory inv, ItemStack stack, int slot, int side) {
        return (!(inv instanceof ISidedInventory) || ((ISidedInventory) inv).canExtractItem(slot, stack, side));
    }

    /**
     * This compares two item stacks, and ensures that a's size is under it's
     * max and that it's item data (id, meta, tags) matches b's.
     */
    private static boolean canStackItems(ItemStack a, ItemStack b) {
        // old code, I optimized it so it isn't so obfuscated
        // a.getItem() != b.getItem() ? false : (a.getItemDamage() !=
        // b.getItemDamage() ? false : (a.stackSize > a.getMaxStackSize() ?
        // false : ItemStack.areItemStackTagsEqual(a, b)))
        return a.getItem() == b.getItem() && a.getItemDamage() == b.getItemDamage() && a.stackSize <= a.getMaxStackSize() && ItemStack.areItemStackTagsEqual(a, b);
    }

    // ==== ISidedInventory

    @Override
    public int[] getAccessibleSlotsFromSide(int i) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, int i2) {
        return false;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemStack, int i2) {
        return false;
    }
}
