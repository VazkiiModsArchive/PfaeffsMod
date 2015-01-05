//package vazkii.pfaeff;
//
//import net.minecraft.item.ItemStack;
//import vazkii.pfaeff.block.BlockJumpPad;
//
//
//public class mod_JumpPad extends BaseMod {
//	public static Block jumpPad = null;
//	
//	@Override
//	public void load() {
//		int id = PfaeffModPropertyReader.getIntValue("jumppad_ID", 205);
//		// "getIndirectPowerOutput()" is just a wrongly translated "setUnlocalizedName()"
//		jumpPad = (new BlockJumpPad(id)).setHardness(1.0F).setStepSound(Block.soundWoodFootstep).getIndirectPowerOutput("jumppad"); 
//		
//		ModLoader.registerBlock(jumpPad);
//		ModLoader.addName(jumpPad, "Jump Pad");				
//		ModLoader.addRecipe(new ItemStack(jumpPad, 4), new Object[]{ "X", "#", Character.valueOf('X'), Item.slimeBall, Character.valueOf('#'), Block.pressurePlatePlanks});		
//		
//		PfaeffModLogger.writeToLog("Block:\t\tAdded Jump Pad using ID " + jumpPad.blockID, false);		
//	}
//	
//	@Override
//	public String getVersion() {
//		return "1.2.3";
//	}
//}
