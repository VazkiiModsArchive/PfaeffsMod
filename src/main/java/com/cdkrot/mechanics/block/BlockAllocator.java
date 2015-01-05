package com.cdkrot.mechanics.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.cdkrot.mechanics.Mechanics;
import com.cdkrot.mechanics.tileentity.TileEntityAllocator;
import com.cdkrot.mechanics.util.Utility;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAllocator extends BlockContainer {

    @SideOnly(Side.CLIENT)
    public IIcon[] icons;

    /**
     * Constructor
     */
    public BlockAllocator() {
        super(Material.rock);
    }

    @Override
    public TileEntity createNewTileEntity(World w, int i) {
        return new TileEntityAllocator();
    }

    @Override
    public void breakBlock(World world, int i, int j, int k, Block par5, int par6) {
        // drop nothing
        super.breakBlock(world, i, j, k, par5, par6);
    }

    @Override
    public int tickRate(World world) {
        return 4;
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        if (!world.isRemote) {
            // moved to utility
            int meta = Utility.getDefaultDirectionsMeta(world, x, y, z);
            world.setBlockMetadataWithNotify(x, y, z, meta, 2);
        }
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random) {
        if (world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k)) {
            TileEntityAllocator tea = (TileEntityAllocator) world.getTileEntity(i, j, k);
            if (tea != null) {
                tea.allocateItems(world, i, j, k, random);
            } else {
                // can't function without a tile backing this
                // note: I decided to remove blocks with no tile, we may want to
                // instead add a new tile
                world.setBlock(i, j, k, Blocks.air);
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block b) {
        if (b.canProvidePower()) {
            if (world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y + 1, z))
                world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase e, ItemStack stack) {
        int side = Utility.getMetadataForBlockAnyPlaced(x, y, z, e);
        world.setBlockMetadataWithNotify(x, y, z, side, 2);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
        if (!world.isRemote)
            entityplayer.openGui(Mechanics.instance, 0, world, x, y, z);
        return true;
    }

    // ------ Texture handling ------- //

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon(Mechanics.modid + ":pfaeff_topbottom");

        icons = new IIcon[] { iconRegister.registerIcon(Mechanics.modid + ":allocator_sidel"),// 0
        iconRegister.registerIcon(Mechanics.modid + ":allocator_sider"),// 1
        iconRegister.registerIcon(Mechanics.modid + ":allocator_in"),// 2
        iconRegister.registerIcon(Mechanics.modid + ":allocator_out"),// 3
        iconRegister.registerIcon(Mechanics.modid + ":allocator_m_in"),// 4
        iconRegister.registerIcon(Mechanics.modid + ":allocator_m_out"),// 5
        iconRegister.registerIcon(Mechanics.modid + ":allocator_m_sideup"),// 6
        iconRegister.registerIcon(Mechanics.modid + ":allocator_m_sidedown") // 7
        };
    }

    // Soooo dirty item view
    // TODO: check if this changed since 1.6
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metaAlwaysZero) {
        return getIconForTerrain(side, 2);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess iba, int x, int y, int z, int s) {
        return this.getIconForTerrain(s, iba.getBlockMetadata(x, y, z));
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconForTerrain(int side, int meta) {
        if (meta == 0)// facing down
            if (side == meta)
                return icons[5];// out
            else if (side == Facing.oppositeSide[meta])
                return icons[4];// in
            else
                return icons[7];// facedown
        if (meta == 1)
            if (side == meta)
                return icons[4];// in
            else if (side == Facing.oppositeSide[meta])
                return icons[5];// out
            else
                return icons[6];// faceup
        // ===meta!=0; meta!=1===//
        if (side == meta)
            return icons[3];// out
        else if (side == Facing.oppositeSide[meta])
            return icons[2];// in
        else if (side == 0 || side == 1)
            return this.blockIcon;// topbottom
        else
            return icons[1 - (meta % 2)];
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int s) {
        TileEntityAllocator tileentity = (TileEntityAllocator) world.getTileEntity(x, y, z);
        return Container.calcRedstoneFromInventory(tileentity);
    }
}