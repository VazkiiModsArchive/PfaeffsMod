package net.minecraft.src;

public class TileEntityChestTrap extends TileEntityChest {
	
	@Override
	public int getBlockMetadata() {
		if (blockMetadata == -1) {
			blockMetadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		}
		return blockMetadata & 7;
	}
    
    @Override
    public void checkForAdjacentChests()
    {
        if (adjacentChestChecked)
        {
            return;
        }
        adjacentChestChecked = true;
        adjacentChestZNeg = null;
        adjacentChestXPos = null;
        adjacentChestXNeg = null;
        adjacentChestZPosition = null;
        if (worldObj.getBlockId(xCoord - 1, yCoord, zCoord) == mod_ChestTrap.getID())
        {
            adjacentChestXNeg = (TileEntityChestTrap)worldObj.getBlockTileEntity(xCoord - 1, yCoord, zCoord);
        }
        if (worldObj.getBlockId(xCoord + 1, yCoord, zCoord) == mod_ChestTrap.getID())
        {
            adjacentChestXPos = (TileEntityChestTrap)worldObj.getBlockTileEntity(xCoord + 1, yCoord, zCoord);
        }
        if (worldObj.getBlockId(xCoord, yCoord, zCoord - 1) == mod_ChestTrap.getID())
        {
            adjacentChestZNeg = (TileEntityChestTrap)worldObj.getBlockTileEntity(xCoord, yCoord, zCoord - 1);
        }
        if (worldObj.getBlockId(xCoord, yCoord, zCoord + 1) == mod_ChestTrap.getID())
        {
        	adjacentChestZPosition = (TileEntityChestTrap)worldObj.getBlockTileEntity(xCoord, yCoord, zCoord + 1);
        }
        if (adjacentChestZNeg != null)
        {
            adjacentChestZNeg.updateContainingBlockInfo();
        }
        if (adjacentChestZPosition != null)
        {
        	adjacentChestZPosition.updateContainingBlockInfo();
        }
        if (adjacentChestXPos != null)
        {
            adjacentChestXPos.updateContainingBlockInfo();
        }
        if (adjacentChestXNeg != null)
        {
            adjacentChestXNeg.updateContainingBlockInfo();
        }
    } 
    
    @Override
	public void onInventoryChanged() {
		super.onInventoryChanged();
		if (worldObj.getBlockId(xCoord, yCoord, zCoord) == mod_ChestTrap.chestTrap.blockID) {
			if (!((BlockChestTrap)mod_ChestTrap.chestTrap).getRedStoneState(worldObj, xCoord, yCoord, zCoord)) {
				((BlockChestTrap)mod_ChestTrap.chestTrap).setStateWithNeighbors(worldObj, xCoord, yCoord, zCoord, true);
				worldObj.playSoundEffect((double)xCoord, (double)yCoord, (double)zCoord, "random.click", 0.3F, 0.6F);
				worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, mod_ChestTrap.chestTrap.blockID, mod_ChestTrap.chestTrap.tickRate(ModLoader.getMinecraftInstance().thePlayer.worldObj));
			}
		}
	}
}
