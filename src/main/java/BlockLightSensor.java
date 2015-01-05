package net.minecraft.src;

import java.util.Random;

public class BlockLightSensor extends BlockContainer {
	
    	private Icon[] iconArray = new Icon[2];	

	    public BlockLightSensor(int id) {
	        super(id, Material.wood);
	        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
	        this.setCreativeTab(CreativeTabs.tabRedstone);
	    }
	    
	    public void registerIcons(IconRegister par1IconRegister) {
	        this.iconArray[0] = par1IconRegister.registerIcon("lightsensor_top");
	        this.iconArray[1] = par1IconRegister.registerIcon("daylightDetector_side");
	    }	    
	    
	    public void updateSensorOutput(World world, int i, int j, int k)  {
	    	int lightValue = world.getBlockLightValue(i, j, k);	    	
	    	int redStoneValue = Math.min(Math.max(lightValue, 0), 15);
	    	world.setBlockMetadataWithNotify(i, j, k, redStoneValue, 4);
	    }	    

	    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
	        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
	    }

	    public int isProvidingWeakPower(IBlockAccess blockAccess, int i, int j, int k, int l) {
	        return blockAccess.getBlockMetadata(i, j, k);
	    }

	    public boolean renderAsNormalBlock() {
	        return false;
	    }

	    public boolean isOpaqueCube() {
	        return false;
	    }

	    public boolean canProvidePower() {
	        return true;
	    }
	    
	    public TileEntity createNewTileEntity(World par1World) {
	        return new TileEntityLightSensor();
	    }

	    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2) {
	        return par1 == 1 ? this.iconArray[0] : this.iconArray[1];
	    }
}
