package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class GuiAllocator extends GuiContainer {

    public GuiAllocator(InventoryPlayer invPlayer, TileEntityAllocator tileentityallocator) {
        super(new ContainerAllocator(invPlayer, tileentityallocator));
        allocatorInv = tileentityallocator;
    }	
	
    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Allocator", 60, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
    }    
    
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture("/gui/allocator.png");
        int j1 = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j1, k, 0, 0, xSize, ySize);
	}
	
	private TileEntityAllocator allocatorInv;
}
