package com.cdkrot.mechanics.api.allocator;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;

public interface IInventoryProvider {
    /**
	 * Return inventory as IInventory or as ISidedInventory
     */
    IInventory createIInventory(World w, int x, int y, int z, Block b);
}
