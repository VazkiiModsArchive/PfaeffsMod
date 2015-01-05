package net.minecraft.src;

import net.minecraft.client.Minecraft;


public class mod_Allocator extends BaseMod {
	public static Block allocator = null;
	
	public static int guiID = 5477111; 
	
	@Override
	public void load() {				
		int id = PfaeffModPropertyReader.getIntValue("allocator_ID", 200);
		boolean filter = PfaeffModPropertyReader.getBooleanValue("allocator_Filter", true);
		boolean filterSubItems = PfaeffModPropertyReader.getBooleanValue("allocator_SubItemFilter", true);
		boolean newTextures = PfaeffModPropertyReader.getBooleanValue("allocator_NewTextures", true);
		
		// "getIndirectPowerOutput()" is just a wrongly translated "setUnlocalizedName()"
		allocator = (new BlockAllocator(id, filter, filterSubItems, newTextures)).setHardness(3.5F).setStepSound(Block.soundStoneFootstep).getIndirectPowerOutput("allocator"); 
		
		// TileEntity
		ModLoader.registerTileEntity(TileEntityAllocator.class, "Allocator");
		
		// Container
		ModLoader.registerContainerID(this, guiID);		
		
		// Allocator
		ModLoader.registerBlock(allocator);
		ModLoader.addName(allocator, "Allocator");						
		ModLoader.addRecipe(new ItemStack(allocator, 1), new Object[]{"X#X", "X$X", "X#X", Character.valueOf('X'), Block.cobblestone, Character.valueOf('#'), Item.redstone, Character.valueOf('$'), Item.ingotGold});	
		
		PfaeffModLogger.writeToLog("Block:\t\tAdded Allocator using ID " + allocator.blockID, false);
	}
	
	
	@Override
	public GuiContainer getContainerGUI(EntityClientPlayerMP var1, int guiID, int i, int j, int k) {
		if (guiID == this.guiID) {
			World world = var1.worldObj;
			return new GuiAllocator(var1.inventory,	(TileEntityAllocator)world.getBlockTileEntity(i, j, k));
		} else {
			return null;
		}
	}
	
	@Override
	public String getVersion() {
		return "1.2.3";
	}
}
