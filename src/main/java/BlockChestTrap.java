package net.minecraft.src;

import java.util.Random;


public class BlockChestTrap extends BlockChest {
	protected BlockChestTrap(int i) {
		super(i, 2);
		
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}
	  
	@Override
	public int tickRate(World world) {
		return 20;
	}    
	
	@Override
    public boolean isOpaqueCube() {
        return false;
    }
	
	@Override
    public boolean canProvidePower() {
        return true;
    }	
	
	@Override
    public boolean renderAsNormalBlock() {
        return false; // Has to be false, for the Chest Trap to work
    }
    
	@Override
	public int isProvidingWeakPower(IBlockAccess iblockaccess, int i, int j, int k, int l) {

		if (!getRedStoneState(iblockaccess, i, j, k)) {
			return 0;
		} else {
			int power = ((TileEntityChestTrap) iblockaccess.getBlockTileEntity(i, j, k)).numUsingPlayers;
			return MathHelper.clamp_int(power, 0, 15);
		}
	}
    
	@Override
    public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int i, int j, int k, int l) {
		return l == 1 ? this.isProvidingWeakPower(par1IBlockAccess, i, j, k, l) : 0;
    }	    
    
    private void notifyNeighbours(World world, int i, int j, int k) {
        world.notifyBlocksOfNeighborChange(i, j, k, blockID);
        world.notifyBlocksOfNeighborChange(i, j-1, k, blockID);    	
    }
    
    private boolean neighborChestAt(World world, int i, int j, int k) {
    	return world.getBlockId(i, j, k) == blockID;
    }
    
    private void setState(World world, int i, int j, int k, int s) {
    	setRedStoneState(world, i, j, k, s);    	
    	notifyNeighbours(world, i, j, k);
  //      world.markBlocksDirty(i, j, k, i, j, k);    
    }
    
    public void setStateWithNeighbors(World world, int i, int j, int k, boolean state) {
    	int s = 0;
    	if (state) {
    		s = 1;
    	}
    	setState(world, i, j, k, s);
    	if (neighborChestAt(world, i+1, j, k)) {
    		setState(world, i+1, j, k, s);	
    	}
    	if (neighborChestAt(world, i-1, j, k)) {
    		setState(world, i-1, j, k, s);	
    	}
    	if (neighborChestAt(world, i, j, k+1)) {
    		setState(world, i, j, k+1, s);	
    	}
    	if (neighborChestAt(world, i, j, k-1)) {
    		setState(world, i, j, k-1, s);	
    	}
    }
    
    @Override
    public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int metaData) {
        if(getRedStoneState(world, i, j, k)) {
        	notifyNeighbours(world, i, j, k);
        }
        super.onBlockDestroyedByPlayer(world, i, j, k, metaData);
    }    
    
    @Override
    public void updateTick(World world, int i, int j, int k, Random random) {
    	setStateWithNeighbors(world, i, j, k, false);
    }   
    
    public boolean getRedStoneState(IBlockAccess iblockaccess, int i, int j, int k) {
    	int rState = (iblockaccess.getBlockMetadata(i, j, k) & 8) >> 3;
    	return rState > 0;
    }
    
    private void setRedStoneState(World world, int i, int j, int k, int s) {
    	int cState = world.getBlockMetadata(i, j, k) & 7;    	 
    	world.setBlockMetadataWithNotify(i, j, k, (s << 3) | cState, 4);    	
    }
    
	@Override
	public TileEntity createNewTileEntity(World w) {
		return new TileEntityChestTrap();
	}	
    
    @Override
    // Kids, don't try this at home ^^
    public void unifyAdjacentChests(World world, int i, int j, int k) {
    	// Store old state
    	int rState = world.getBlockMetadata(i, j, k);
    	// Set new temporary state
    	world.setBlockMetadataWithNotify(i, j, k, rState & 7, 0);
    	super.unifyAdjacentChests(world, i, j, k);
    	// See what has changed
    	int changedState = world.getBlockMetadata(i, j, k);    	
    	int newState = (rState & 8) | changedState;
    	// Restore old state
    	world.setBlockMetadataWithNotify(i, k, k, newState, 4);
    }
}
