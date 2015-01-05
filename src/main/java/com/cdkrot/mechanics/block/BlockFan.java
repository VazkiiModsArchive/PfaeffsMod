package com.cdkrot.mechanics.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.cdkrot.mechanics.Mechanics;
import com.cdkrot.mechanics.tileentity.TileEntityFanON;
import com.cdkrot.mechanics.util.Utility;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

// implementation of Fan idea: a bad one, in fact.
public class BlockFan extends BlockContainer {
    @SideOnly(Side.CLIENT)
    private IIcon icons[];// [zero for inactive] [one for active]

    public BlockFan() {
        super(Material.rock);
        this.setHardness(3.5F).setStepSound(Block.soundTypeStone).setBlockName("mechanics::cdkrotFan");
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return (meta & 8) != 0;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random nullrandom) {
        this.updatePowered(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block b) {
        this.updatePowered(world, x, y, z);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack unused) {
        int suggested = Utility.getMetadataForBlockAnyPlaced(x, y, z, entity);
        boolean ispow = world.isBlockIndirectlyGettingPowered(x, y, z);
        world.setBlockMetadataWithNotify(x, y, z, ispow ? suggested | 8 : suggested, 2);// side+8 : side
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return createNewTileEntity();
    }

    public TileEntity createNewTileEntity() {
        return new TileEntityFanON();
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
            return createNewTileEntity();
    }

    @Override
    public BlockFan setCreativeTab(CreativeTabs ctab) {
        super.setCreativeTab(ctab);
        return this;
    }

    public boolean updatePowered(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z); // if 4'th bit set = powered
        if (world.isBlockIndirectlyGettingPowered(x, y, z)) {
            if ((meta & 8) == 0) {
                world.setBlockMetadataWithNotify(x, y, z, meta | 8, 2);// unpow->pow
				((TileEntityFanON)world.getTileEntity(x,y,z)).disabled=false;
            }
            return true;
        } else {
            if ((meta & 8) != 0) {
                world.setBlockMetadataWithNotify(x, y, z, meta & 7, 2);
				((TileEntityFanON)world.getTileEntity(x,y,z)).disabled=true;// pow->unpow
            }
            return false;
        }
    }

    // -----------TEXTURE------------//

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon(Mechanics.modid + ":pfaeff_topbottom");
        icons = new IIcon[] { iconRegister.registerIcon(Mechanics.modid + ":fanfront"), iconRegister.registerIcon(Mechanics.modid + ":fanfrontactive") };
    }

    // Soooo dirty item view
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int StupidMetaAlwaysZero) {
        return getIconForTerrain(side, 3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {

        return this.getIconForTerrain(par5, par1IBlockAccess.getBlockMetadata(par2, par3, par4));
    }

    @SideOnly(Side.CLIENT)
    private IIcon getIconForTerrain(int side, int meta) {
        return side == (meta & 7) ? ((meta & 8) == 0 ? icons[0] : icons[1]) : this.blockIcon;
    }

    @Override
    public int getMobilityFlag() {
        return 0;
    }
}