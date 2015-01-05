package vazkii.pfaeff;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import vazkii.pfaeff.client.ContainerAllocator;
import vazkii.pfaeff.client.GuiAllocator;
import vazkii.pfaeff.tile.TileEntityAllocator;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new ContainerAllocator(player.inventory, (TileEntityAllocator) world.getTileEntity(x, y, z));
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new GuiAllocator(player.inventory, (TileEntityAllocator) world.getTileEntity(x, y, z));
	}

}
