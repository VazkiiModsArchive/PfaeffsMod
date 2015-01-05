package com.cdkrot.mechanics.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.cdkrot.mechanics.Mechanics;
import com.cdkrot.mechanics.tileentity.TileEntityLightSensor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockLightSensor extends BlockContainer {
    @SideOnly(Side.CLIENT)
    private IIcon[] iconArray;

    public BlockLightSensor() {
        super(Material.wood);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
        this.setHardness(3.5F);
        this.setStepSound(Block.soundTypeStone);
        this.setBlockName("mechanics::lightsensor");
        this.setTickRandomly(true);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        this.iconArray = new IIcon[] { register.registerIcon(Mechanics.modid + ":lightsensor_top"), register.registerIcon("minecraft:daylight_detector_side") };
    }

    public void updateSensorOutput(World world, int x, int y, int z) {
        int lightValue = world.getBlockLightValue(x, y, z);
        int redStoneValue = Math.min(Math.max(lightValue, 0), 15);
        world.setBlockMetadataWithNotify(x, y, z, redStoneValue, 5);
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess blockAccess, int x, int y, int z, int s) {
        return blockAccess.getBlockMetadata(x, y, z);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    // Should use "getIcon". (now this method called like this in super-class.
    @Override
    public IIcon getIcon(int par1, int par2) {
        return par1 == 1 ? this.iconArray[0] : this.iconArray[1];
    }

    @Override
    public int tickRate(World w) {
        return 1;
    }

    @Override
    public void onNeighborBlockChange(World w, int x, int y, int z, Block b) {
        this.updateTick(w, x, y, z, null);
        this.updateSensorOutput(w, x, y, z);
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int par5, float par6, float par7, float par8, int par9) {
        this.updateSensorOutput(world, x, y, z);
        return par9;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return createNewTileEntity();
    }

    public TileEntity createNewTileEntity() {
        return new TileEntityLightSensor();
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random r) {
        if (world.getTileEntity(x, y, z) == null)
            world.setTileEntity(x, y, z, this.createNewTileEntity());
    }

    @Override
    public BlockLightSensor setCreativeTab(CreativeTabs ctab) {
        super.setCreativeTab(ctab);
        return this;
    }
}
