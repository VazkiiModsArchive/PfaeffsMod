package net.minecraft.src;


public class mod_ChestTrap extends BaseMod {
	public static Block chestTrap = null;
	private static int id;
	
	@Override
	public void load() {
		id = PfaeffModPropertyReader.getIntValue("chesttrap_ID", 201);
		// "getIndirectPowerOutput()" is just a wrongly translated "setUnlocalizedName()"
		chestTrap = (new BlockChestTrap(id)).setHardness(2.5F).setStepSound(Block.soundWoodFootstep).getIndirectPowerOutput("chesttrap");   
		
		// TileEntity
		ModLoader.registerTileEntity(TileEntityChestTrap.class, "Chest Trap");		
		
		PfaeffModLogger.writeToLog("Block:\t\tAdded Chest Trap using ID " + chestTrap.blockID, false);				
		
		// Chest Trap
		ModLoader.registerBlock(chestTrap);
		
		ModLoader.addName(chestTrap, "Chest Trap");		
		ModLoader.addRecipe(new ItemStack(chestTrap, 1), new Object[]{"X", "#", Character.valueOf('X'), Item.redstone, Character.valueOf('#'), Block.chest});	
	}

	public static int getID() {
		return id;
	}
	
	@Override
	public String getVersion() {
		return "1.2.3";
	}
}
