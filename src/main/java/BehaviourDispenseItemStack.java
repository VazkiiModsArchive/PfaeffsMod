package net.minecraft.src;

public class BehaviourDispenseItemStack extends BehaviorDefaultDispenseItem {
	
	@Override
    protected ItemStack dispenseStack(IBlockSource blockSource, ItemStack itemStack)  {
        EnumFacing facing = BlockDispenser.getFacing(blockSource.getBlockMetadata());
        IPosition position = BlockDispenser.getIPositionFromBlockSource(blockSource);
        doDispense(blockSource.getWorld(), itemStack, 6, facing, position);
        return itemStack;
    }
}
