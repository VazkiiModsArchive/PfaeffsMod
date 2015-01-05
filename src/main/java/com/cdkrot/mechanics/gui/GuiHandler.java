package com.cdkrot.mechanics.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.cdkrot.mechanics.tileentity.TileEntityAllocator;

import cpw.mods.fml.common.network.IGuiHandler;

//forge-style GUI handler
//used to manage IInvs based guis
public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (id == 0) {
            if (tileEntity instanceof TileEntityAllocator)
                return new ContainerAllocator(player.inventory, (TileEntityAllocator) tileEntity);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (id == 0) {
            if (tileEntity instanceof TileEntityAllocator)
                return new GuiAllocator(player.inventory, (TileEntityAllocator) tileEntity);
        }
        return null;
    }
}
