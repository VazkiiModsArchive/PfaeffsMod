package vazkii.pfaeff.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;


public class BlockJumpPad extends Block {
	
	public BlockJumpPad() {
		super(Material.ground);
				
		// 1/4 height
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
		
		this.setCreativeTab(CreativeTabs.tabTransport);
		setBlockName("jumpPad");
		setHardness(0.6F);
		setStepSound(soundTypeGrass);
	}
	
	/*
	 * Needed to be rendered correctly
	 */
	@Override
    public boolean isOpaqueCube() {
        return false;
    }	     
	
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon("pfaeff:jumppad");
	}
    
    /*
     * Only placeable on solid blocks
     */
	@Override
    public boolean canPlaceBlockAt(World world, int i, int j, int k) {
    	return world.isSideSolid(i, j - 1, k, ForgeDirection.UP);
    }    
    
    /*
     * Launches "entity" into the air
     */
    public void jump(Entity entity) {
    	// Only items and players are allowed
    	if (((entity instanceof EntityPlayer) || (entity instanceof EntityItem)) && (entity.motionY < 1)) {
    		entity.motionY = 0;
    		entity.fallDistance = 0;
    		entity.addVelocity(0, 1, 0); 
    	}	
    }
    
    @Override
    public void onEntityWalking(World world, int i, int j, int k, Entity entity) {
    	// Trigger jump
    	jump(entity);
    }    
    
    @Override
    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
    	if (entity.posY > j) {
    		jump(entity);
    	}
    }
	
}
