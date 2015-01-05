package com.cdkrot.mechanics.api.allocator;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;

public interface IInventoryProviderEntity {
	/**
	 * Return inventory as IInventory or as ISidedInventory
	 */
    IInventory createIInventory(Entity entity);
}
