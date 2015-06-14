package vazkii.pfaeff.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.pfaeff.tile.TileEntityLightSensor;

public class BlockLightSensor extends BlockContainer {
	
	    public BlockLightSensor() {
	        super(Material.wood);
	        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
	        this.setCreativeTab(CreativeTabs.tabRedstone);
	        setBlockName("lightSensor");
	        setHardness(0.2F);
	        setStepSound(soundTypeWood);
	    }
	    
	    @Override
	    public void registerBlockIcons(IIconRegister par1IconRegister) {
	        blockIcon = par1IconRegister.registerIcon("pfaeff:lightsensor_top");
	    }	    
	    
	    public void updateSensorOutput(World world, int i, int j, int k)  {
	    	int lightValue = world.getBlockLightValue(i, j, k);	    	
	    	int redStoneValue = Math.min(Math.max(lightValue, 0), 15);
	    	world.setBlockMetadataWithNotify(i, j, k, redStoneValue, 1 | 2);
	    }	    
	    
	    @Override
	    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
	        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
	    }

	    @Override
	    public int isProvidingWeakPower(IBlockAccess blockAccess, int i, int j, int k, int l) {
	        return blockAccess.getBlockMetadata(i, j, k);
	    }

	    @Override
	    public boolean renderAsNormalBlock() {
	        return false;
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
	    public TileEntity createNewTileEntity(World par1World, int meta) {
	        return new TileEntityLightSensor();
	    }

	    @Override
	    public IIcon getIcon(int par1, int par2) {
	        return par1 == 1 ? blockIcon : Blocks.daylight_detector.getIcon(par1, par2);
	    }
}
