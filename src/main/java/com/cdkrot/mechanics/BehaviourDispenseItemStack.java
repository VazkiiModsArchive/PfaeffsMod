package com.cdkrot.mechanics;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

// Needed to dispense items
public class BehaviourDispenseItemStack extends BehaviorDefaultDispenseItem {
    @Override
    protected ItemStack dispenseStack(IBlockSource blockSource, ItemStack itemStack) {
        EnumFacing facing = EnumFacing.getFront(blockSource.getBlockMetadata() % 6);
        IPosition position = BlockDispenser.func_149939_a(blockSource);// temporary solution
        doDispense(blockSource.getWorld(), itemStack, 6, facing, position);
        return itemStack;
    }
}
