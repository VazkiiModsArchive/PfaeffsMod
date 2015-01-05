package net.minecraft.src;

import java.util.Random;


public class BlockJumpPad extends Block {
	
	protected BlockJumpPad(int i) {
		super(i, Material.ground);
				
		// 1/4 height
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
		
		this.setCreativeTab(CreativeTabs.tabTransport);
	}
	
	/*
	 * Needed to be rendered correctly
	 */
	@Override
    public boolean isOpaqueCube() {
        return false;
    }	      
    
    /*
     * Only placeable on solid blocks
     */
	@Override
    public boolean canPlaceBlockAt(World world, int i, int j, int k) {
    	return world.isBlockOpaqueCube(i, j - 1, k);
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
