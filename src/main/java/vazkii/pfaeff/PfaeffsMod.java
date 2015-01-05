package vazkii.pfaeff;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.pfaeff.block.BlockAllocator;
import vazkii.pfaeff.block.BlockJumpPad;
import vazkii.pfaeff.block.BlockLightSensor;
import vazkii.pfaeff.tile.TileEntityAllocator;
import vazkii.pfaeff.tile.TileEntityLightSensor;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "pfaeffsmod", name = "Pfaeff's Mod", version="1.0")
public class PfaeffsMod {

	Block allocator, jumpPad, lightSensor;
	
	@Instance("pfaeffsmod")
	public static PfaeffsMod instance;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		allocator = new BlockAllocator(true, true, true);
		jumpPad = new BlockJumpPad();
		lightSensor = new BlockLightSensor();
		
		GameRegistry.registerBlock(allocator, "allocator");
		GameRegistry.registerBlock(jumpPad, "jumpPad");
		GameRegistry.registerBlock(lightSensor, "lightSensor");
		
		GameRegistry.registerTileEntity(TileEntityAllocator.class, "pfaeff:allocator");
		GameRegistry.registerTileEntity(TileEntityLightSensor.class, "pfaeff:lightSensor");
		
		GameRegistry.addRecipe(new ItemStack(jumpPad, 4), 
				"X", "#", 
				'X', Items.slime_ball, 
				'#', Blocks.wooden_pressure_plate);
		
		GameRegistry.addRecipe(new ItemStack(allocator, 1),
				"X#X", "X$X", "X#X", 
				'X', Blocks.cobblestone, 
				'#', Items.redstone, 
				'$', Items.gold_ingot);
		
		GameRegistry.addRecipe(new ItemStack(lightSensor, 1), 
				"G", "Q", "#", 
				'G', Items.slime_ball,
				'Q', Items.quartz,
				'#', Blocks.wooden_pressure_plate);
		
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
	}
	
}
