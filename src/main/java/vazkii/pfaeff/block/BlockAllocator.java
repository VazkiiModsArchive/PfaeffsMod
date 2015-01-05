package vazkii.pfaeff.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.BlockJukebox.TileEntityJukebox;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.pfaeff.PfaeffsMod;
import vazkii.pfaeff.tile.TileEntityAllocator;


public class BlockAllocator extends BlockContainer {
	
	private final IBehaviorDispenseItem dispenseBehaviour = new BehaviorDefaultDispenseItem();
	/**
	 * Constructor
	 * 
	 */
	public BlockAllocator(boolean allowFiltering, boolean subItemFiltering, boolean newTextures) {
		super(Material.rock);
		
		this.allowFiltering = allowFiltering;
		this.subItemFiltering = subItemFiltering;
		this.newTextures = newTextures;
		
		this.setCreativeTab(CreativeTabs.tabRedstone);
		setBlockName("allocator");
	}
	
	@Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("pfaeff_side");
        iconFront = iconRegister.registerIcon("allocator_front");
        iconBack = iconRegister.registerIcon("allocator_back");
        iconLeft = iconRegister.registerIcon("allocator_sidel");
        iconRight = iconRegister.registerIcon("allocator_sider");
        iconTopBottom = iconRegister.registerIcon("pfaeff_topbottom");        
    }	
    
    /**
     * Returns orientation along the x-axis (-1, 0, 1)
     */
    private int getDirectionX(World world, int i, int j, int k) {
    	int m = world.getBlockMetadata(i, j, k); 	
    	int dx = 0;
    	if (m == 4) {
    		dx = -1;
    	}
    	if (m == 5) {
    		dx = 1;
    	}       	
    	return dx;
    }
    
    /**
     * Returns orientation along the z-axis (-1, 0, 1)
     */  
    private int getDirectionZ(World world, int i, int j, int k) {
    	int m = world.getBlockMetadata(i, j, k); 	
    	int dz = 0;
    	if (m == 2) {
    		dz = -1;
    	}
    	if (m == 3) {
    		dz = 1;
    	}  	
    	return dz;
    }      	
	
	@Override
	public TileEntity createNewTileEntity(World var1, int meta) {
		return new TileEntityAllocator();
	}	
	
	@Override
	public void breakBlock(World world, int i, int j, int k, Block block, int meta) {
		TileEntityAllocator allocator = (TileEntityAllocator) world.getTileEntity(i, j, k);
		if (allocator != null) {
			ItemStack itemStack = allocator.getStackInSlot(0);
			if (itemStack != null) {
				EntityItem entityItem = new EntityItem(world, i, j, k, new ItemStack(itemStack.getItem(), 1, itemStack.getItemDamage()));

				if (itemStack.hasTagCompound()) {
					entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
				}

				float var15 = 0.05F;
				entityItem.motionX = (double) ((float) world.rand.nextGaussian() * var15);
				entityItem.motionY = (double) ((float) world.rand.nextGaussian() * var15 + 0.2F);
				entityItem.motionZ = (double) ((float) world.rand.nextGaussian() * var15);
				world.spawnEntityInWorld(entityItem);
			}
		}		
		
		super.breakBlock(world, i, j, k, block, meta);
	}

	@Override
    public int quantityDropped(Random random) {
        return 1;
    }         
    
	@Override
    public int tickRate(World world) {
        return 4;
    }    
    
	@Override
    public void onBlockAdded(World world, int i, int j, int k) {
        super.onBlockAdded(world, i, j, k);
        setAllocatorDefaultDirection(world, i, j, k);
    }

	private void setAllocatorDefaultDirection(World world, int i, int j, int k) {
		if (!world.isRemote) {
			Block front = world.getBlock(i, j, k - 1);
			Block back = world.getBlock(i, j, k + 1);
			Block left = world.getBlock(i - 1, j, k);
			Block right = world.getBlock(i + 1, j, k);
			byte val = 3;

			if (front.isOpaqueCube() && !back.isOpaqueCube()) {
				val = 3;
			}

			if (back.isOpaqueCube() && !front.isOpaqueCube()) {
				val = 2;
			}

			if (left.isOpaqueCube() && !right.isOpaqueCube()) {
				val = 5;
			}

			if (right.isOpaqueCube() && !left.isOpaqueCube()) {
				val = 4;
			}

			world.setBlockMetadataWithNotify(i, j, k, val, 2);
		}
	}   
    
	int getOpposite(int i) {
		return ForgeDirection.OPPOSITES[i];
	}
	
    @Override
	public IIcon getIcon(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		// Top and Bottom
		if (l == 1) {
			return iconTopBottom;
		}
		if (l == 0) {
			return iconTopBottom;
		}
		int i1 = iblockaccess.getBlockMetadata(i, j, k);

		// Output
		if (l == i1) {
			return iconBack;
		}

		// Input
		if (l == getOpposite(i1)) {
			return iconFront;
		}

		// Sides
		if (newTextures) {
			if (Math.abs(l - i1) == 2) {
				if ((i1 == 2) || (i1 == 3)) {
					return iconRight;
				} else {
					return iconLeft;
				}
			}
			if (Math.abs(l - getOpposite(i1)) == 2) {
				if ((i1 == 2) || (i1 == 3)) {
					return iconLeft;
				} else {
					return iconRight;
				}
			}
		}

		return this.blockIcon;
	}
    
    /**
     * For the item view
     */
    @Override
    public IIcon getIcon(int i, int m) {
    	switch (i) {
    	case 0: {
    		return iconTopBottom;
    	}
    	case 1: {
    		return iconTopBottom;
    	}
    	case 2: {
    		return iconBack;
    	}
    	case 3: {
    		return iconFront;
    	}
    	case 4: {
    		if (newTextures) {
    			return iconRight;	
    		}
    	}
    	case 5: {
    		if (newTextures) {
    			return iconLeft;	
    		}
    	}
    	}
    	return this.blockIcon;
    }    
    
    /**
     * Returns true, if the item is allowed to pass
     * 
     * @param world
     * @param i
     * @param j
     * @param k
     * @param itemID	item to check
     * @return
     */
    private boolean passesFilter(World world, int i, int j, int k, ItemStack item) {
    	if (!allowFiltering) {
    		return true;
    	}
    	TileEntityAllocator tileentityallocator = (TileEntityAllocator)world.getTileEntity(i, j, k);	
    	
    	// Item in slot 0 is the reference item
    	ItemStack filterItem = tileentityallocator.getStackInSlot(0);
    	
    	if (filterItem == null) {
    		return true;
    	}    	
    	boolean filterSubItems = true;
    	boolean record = false;
    	if (subItemFiltering) {    		
    		filterSubItems = ((!item.getItem().getHasSubtypes()) || (filterItem.getItemDamage() == item.getItemDamage()));
    		record = ((item.getItem() instanceof ItemRecord) && (filterItem.getItem() instanceof ItemRecord));
    	}
    	return ((filterItem.getItem() == item.getItem()) && filterSubItems) || record;
    }
    
    /**
     * Returns the container (IIventory) at position (i,j,k) if it exists.
     */
    protected IInventory containerAtPos(World world, int i, int j, int k) {
    	TileEntity tile = world.getTileEntity(i, j, k);
    	if (!(tile instanceof IInventory)) {
    		return null;
    	}
    	return getDoubleChest(world, i, j, k);
    } 
    
    /**
     * Checks if there is a "blocking" cube at (i,j,k)
     * 
     * @param world
     * @param i
     * @param j
     * @param k
     * @return
     */
    protected boolean blockingCubeAtPos(World world, int i, int j, int k) {
    	Block block = world.getBlock(i, j, k);
    	boolean isOpaque = block.isOpaqueCube();
    	return isOpaque;
    }

    /**
     * Finds a free item slot for the item
     */
    private int getFirstFreeInventorySlotOfKind(IInventory inventory, ItemStack item) {
    	int inventorySize = inventory.getSizeInventory();
    	
    	// Only use the first two slots as the input of the furnace,
    	// because it doesn't make sense to have items being put in the last slot
    	if (inventory instanceof TileEntityFurnace) {
    		inventorySize--;
    	}    	
    	for (int i = 0; i < inventorySize; i++) {
    		boolean canStack = false;
    		// Check if stacking is possible
			if ((inventory.getStackInSlot(i) != null)
					&& (inventory.getStackInSlot(i).getItem() == item.getItem()) &&
					((!item.getItem().getHasSubtypes()) || (inventory.getStackInSlot(i).getItemDamage() == item.getItemDamage()))) {	
					canStack = (inventory.getStackInSlot(i).stackSize <= (item.getMaxStackSize() - item.stackSize));
			}
    		if ((inventory.getStackInSlot(i) == null) || canStack) {
    			return i;
    		}
    	}
    	return -1;
    }    
    
    /**
     * Returns a random item from the container, using the same rule as the dispenser
     */
	public int getRandomItemFromContainer(IInventory inventory, Random rand, World world, int i1, int j1, int k1) {
		if (inventory == null) {
			return -1;
		}		
		int i = -1;
		int j = 1;
		
		int startAt = 0;
		
		// Only use the last slot, if it is a furnace,
		// because it doesn't make sense to take something out of the first slots as they are inputs
		if (inventory instanceof TileEntityFurnace) {
			startAt = 2;
		}		
		for (int k = startAt; k < inventory.getSizeInventory(); k++) {
			if ((inventory.getStackInSlot(k) != null) && passesFilter(world, i1, j1, k1, inventory.getStackInSlot(k)) && (rand.nextInt(j) == 0)) {
				i = k;
				j++;
			}
		}
		return i;
	}
    
	/**
	 * Puts an item in the container slot at the index
	 */
    private void putItemInContainer(IInventory inventory, ItemStack item, int index) {
    	if (item == null) {
    		return;
    	}
    	if (index >= 0) {
    		ItemStack stack = inventory.getStackInSlot(index);
    		if (stack != null) {
    			// Item already there, increase stacksize
    			stack.stackSize += item.stackSize;					// TODO: when stack is full, search for new position
    			inventory.setInventorySlotContents(index, stack);
    		} else {
    			// Add a new item
    			inventory.setInventorySlotContents(index, item);
    		}
    	}
    }

    /**
     * Spits out an item (like the dropper, but the whole stack)
     */    
	protected void dispense(World world, int i, int j, int k, ItemStack item) {
		BlockSourceImpl blockImpl = new BlockSourceImpl(world, i, j, k);
		TileEntityAllocator allocator = (TileEntityAllocator) blockImpl.getBlockTileEntity();

		
		if (allocator != null) {
			int meta = world.getBlockMetadata(i, j, k) & 7;
			IInventory hopper = TileEntityHopper.func_145893_b(world,
					(double) (i + Facing.offsetsXForSide[meta]),
					(double) (j + Facing.offsetsYForSide[meta]),
					(double) (k + Facing.offsetsZForSide[meta]));
			
			ItemStack stack = this.dispenseBehaviour.dispense(blockImpl, item);
			if (stack != null && stack.stackSize == 0) {
				stack = null;
			}
		}
	}
        
    /**
     * Handles the item output. Returns true, if item was successfully put out.
     */
    private boolean outputItem(World world, int i, int j, int k, int dx, int dz, ItemStack item, Random random) {
    	IInventory outputContainer = containerAtPos(world, i + dx, j, k + dz);    	
    	
    	if (outputContainer == null) {
    		// Search for storage minecarts and such
			List entities = world.getEntitiesWithinAABB(IInventory.class, AxisAlignedBB.getBoundingBox((double)(i + dx), (double)j, (double)(k + dz), (double)(i + dx + 1), (double)(j + 1), (float)(k + dz + 1)));
			if (entities.size() > 0) {
				// Prevent the allocator from using non-storage minecarts
				if (!((entities.get(0) instanceof  EntityMinecart) && (entities.get(0)) instanceof IInventory)) {
					outputContainer = (IInventory)(entities.get(0));   
				}
			}
    	}
    	
		if (outputContainer == null) {
			// Jukebox
			if (world.getBlock(i + dx, j, k + dz) == Blocks.jukebox) {
				TileEntityJukebox tJukeBox = (TileEntityJukebox)world.getTileEntity(i + dx, j, k + dz);
				if ((item.getItem() instanceof ItemRecord) && (tJukeBox.func_145856_a() == null)) {					
					ItemRecord record = (ItemRecord)item.getItem();
					record.onItemUse(item, null, world, i + dx, j, k + dz, 0, 0, 0, 0);
					((BlockJukebox) Blocks.jukebox).func_149926_b(world, i + dx, j, k + dz, item);
					return true;
				} else {
					return false;
				}
			}			
			if ((!blockingCubeAtPos(world, i + dx, j, k + dz))) {
				dispense(world, i, j, k, item);
				return true;
			}
		} else {
			int index = getFirstFreeInventorySlotOfKind(outputContainer, item);
			if (index >= 0) {
				putItemInContainer(outputContainer, item, index);
				return true;
			}
		}    	
		return false;
    }        
    
    /**
     * Returns the IIventory of a large chest, if there is one at (i, j, k)
     */
    private IInventory getDoubleChest(World world, int i, int j, int k) {
    	TileEntity tile = world.getTileEntity(i, j, k);
		if (!(tile instanceof TileEntityChest)) {
			if (tile instanceof IInventory) {
				return (IInventory)tile;
			} else {
				return null;
			}
		}
		
		Block cblock = world.getBlock(i, j, k);
    	IInventory chest1 = (IInventory)(world.getTileEntity(i, j, k)); 
    	if (world.getBlock(i + 1, j, k) == cblock) {
    		IInventory chest2 = (IInventory)(world.getTileEntity(i + 1, j, k));
        	return new InventoryLargeChest("", chest1, chest2);
    	}
    	if (world.getBlock(i - 1, j, k) == cblock) {
    		IInventory chest2 = (IInventory)(world.getTileEntity(i - 1, j, k));
    		return new InventoryLargeChest("", chest2, chest1);
    	}    
    	if (world.getBlock(i, j, k + 1) == cblock) {
    		IInventory chest2 = (IInventory)(world.getTileEntity(i, j, k + 1));
    		return new InventoryLargeChest("", chest1, chest2);
    	}    
    	if (world.getBlock(i, j, k - 1) == cblock) {
    		IInventory chest2 = (IInventory)(world.getTileEntity(i, j, k - 1));
    		return new InventoryLargeChest("", chest2, chest1);
    	}        	
    	return chest1;
    }
    
    /**
     * Handles all the item input/output
     */
    private void allocateItems(World world, int i, int j, int k, Random random) {    	
    	int dx = getDirectionX(world, i, j, k);
    	int dz = getDirectionZ(world, i, j, k);	
    	   	
    	IInventory inputContainer = containerAtPos(world, i - dx, j, k - dz);     
    	
    	List entities = null;
    	
    	if (inputContainer == null) {
    		entities = world.getEntitiesWithinAABB(IInventory.class, AxisAlignedBB.getBoundingBox((double)(i - dx), (double)j, (double)(k - dz), (double)(i - dx + 1), (double)(j + 1), (float)(k - dz + 1)));		
    		// Search for storage minecarts and such
			if (entities.size() > 0) {
				if (!((entities.get(0) instanceof  EntityMinecart) && (entities.get(0)) instanceof IInventory)) {
					inputContainer = (IInventory) entities.get(0);
				}
			}
    	}
    	
    	// No Input-Container
    	if (inputContainer == null) {
			// Jukebox
			if (world.getBlock(i - dx, j, k - dz) == Blocks.jukebox) {
				TileEntityJukebox tJukeBox = (TileEntityJukebox) world.getTileEntity(i - dx, j, k - dz);
				if (tJukeBox != null) {
					if (passesFilter(world, i, j, k, tJukeBox.func_145856_a())) {
						if (outputItem(world, i, j, k, dx, dz, tJukeBox.func_145856_a(), random)) {
		                    world.playAuxSFX(1005, i - dx, j, k - dz, 0);
		                    world.playRecord((String)null, i - dx, j, k - dz);							
							tJukeBox.func_145857_a(null);
							//tJukeBox.onInventoryChanged();
							world.setBlockMetadataWithNotify(i - dx, j, k - dz, 0, 4);
						}
					}
				}
			} else {    		
	    		entities = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox((double)(i - dx), (double)j, (double)(k - dz), (double)(i - dx + 1), (double)(j + 1), (float)(k - dz + 1)));		

	    		// Find all EntityItems that would pass
	    		ArrayList<EntityItem> items = new ArrayList<EntityItem>();
	    		for (int l = 0; l < entities.size(); l++) {
	        		if (entities.get(l) instanceof EntityItem) {
	        			EntityItem item = (EntityItem)entities.get(l);  
	        			if ((!item.isDead) && (passesFilter(world, i, j, k, item.getEntityItem()))) {
	        				items.add(item);
	        			}
	        		}	        		
	        	}
	    		
	    		// Suck in a random one
	    		if (items.size() > 0) {
		    		int index = world.rand.nextInt(items.size());
					if (outputItem(world, i, j, k, dx, dz, items.get(index).getEntityItem(), random)) {
						items.get(index).setDead();
					}	    		
	    		}
			}
        // Input-Container
    	} else {
    		int itemIndex = getRandomItemFromContainer(inputContainer, random, world, i, j, k);	
    		if (itemIndex >= 0) {
    			ItemStack item = inputContainer.getStackInSlot(itemIndex);
    			if (outputItem(world, i, j, k, dx, dz, item, random)) {
    				inputContainer.decrStackSize(itemIndex, item.stackSize); 
    			}
    		}
    	}    	
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random) {
        if(world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k)) {
        	allocateItems(world, i, j, k, random);		        	
        }
    }    

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, Block b) {
        if(b.canProvidePower()) {
            if (world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k)) {
                world.scheduleBlockUpdate(i, j, k, this, tickRate(world));
            }
        }
    }    
    
    @Override
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLivingBase entityliving, ItemStack stack) {
        int l = MathHelper.floor_double((double)((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        
		if (l == 0) {
			world.setBlockMetadataWithNotify(i, j, k, 2, 4);
		}
		if (l == 1) {
			world.setBlockMetadataWithNotify(i, j, k, 5, 4);
		}
		if (l == 2) {
			world.setBlockMetadataWithNotify(i, j, k, 3, 4);
		}
		if (l == 3) {
			world.setBlockMetadataWithNotify(i, j, k, 4, 4);
		}
    }      
    
    @Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		if (!allowFiltering) {
			return false;
		}
		TileEntityAllocator tileentityallocator = (TileEntityAllocator) world.getTileEntity(i, j, k);
		if (tileentityallocator != null) {
			entityplayer.openGui(PfaeffsMod.instance, 0, world, i, j, k);
		}
		return false;
	}  
    
    // Icons    
    private IIcon iconTopBottom;
    private IIcon iconLeft;
    private IIcon iconRight;
    private IIcon iconFront;
    private IIcon iconBack;       
    
    private final boolean allowFiltering;
    private final boolean subItemFiltering;
    private final boolean newTextures;
    
    private final IBehaviorDispenseItem dispenserBehaviour = new BehaviorDefaultDispenseItem();
}
