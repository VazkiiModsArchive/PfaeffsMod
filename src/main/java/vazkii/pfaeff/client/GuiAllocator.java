package vazkii.pfaeff.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import vazkii.pfaeff.tile.TileEntityAllocator;

public class GuiAllocator extends GuiContainer {
	
	private static final ResourceLocation resource = new ResourceLocation("pfaeff", "textures/gui/allocator.png");

    public GuiAllocator(InventoryPlayer invPlayer, TileEntityAllocator tileentityallocator) {
        super(new ContainerAllocator(invPlayer, tileentityallocator));
        allocatorInv = tileentityallocator;
    }	
	
    protected void drawGuiContainerForegroundLayer()
    {
        fontRendererObj.drawString("Allocator", 60, 6, 0x404040);
        fontRendererObj.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
    }    
    
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(resource);
        int j1 = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j1, k, 0, 0, xSize, ySize);
	}
	
	private TileEntityAllocator allocatorInv;
}
