package com.gendeathrow.hatchery.block.nest;

import java.util.Random;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.core.init.ModBlocks;
import com.gendeathrow.hatchery.core.theoneprobe.TOPInfoProvider;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EggNestBlock extends Block implements ITileEntityProvider, TOPInfoProvider
{
    public static final PropertyBool hasEgg = PropertyBool.create("hasegg");
    
    protected static final AxisAlignedBB noEgg_AABB = new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.15, 0.8125D);
    protected static final AxisAlignedBB withEgg_AABB = new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.25, 0.8125D);
    protected static final AxisAlignedBB[] BOUNDING_BOXES = new AxisAlignedBB[] {noEgg_AABB, withEgg_AABB};    

	protected String name;
	
	public EggNestBlock() 
	{
		super(Material.LEAVES);
		this.name = "nest";
		this.setTranslationKey("nest"); 
		this.setCreativeTab(Hatchery.hatcheryTabs);	
		this.setHardness(.2f);
		this.setDefaultState(this.blockState.getBaseState().withProperty(hasEgg, false));
	}
	 
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		 if(worldIn.getTileEntity(pos)  != null && worldIn.getTileEntity(pos) instanceof EggNestTileEntity)
		 {
			 ItemStack stack = ((EggNestTileEntity)worldIn.getTileEntity(pos)).getEgg();
			 if(!stack.isEmpty())
				 Block.spawnAsEntity(worldIn, pos, stack);
		 }
		 super.breakBlock(worldIn, pos, state);
	 }
	 
    @Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(ModBlocks.nest);
    }
	 
	@Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }
    
	@Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BOUNDING_BOXES[state.getValue(hasEgg) ? 1 : 0];
    }
    
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) 
	{
	    list.add(new ItemStack(ModBlocks.nest, 1, 0)); 
	}
	
	@Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
    	return true;
    }
	
	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		ItemStack heldItem = playerIn.getHeldItem(hand);
		EggNestTileEntity te = ((EggNestTileEntity)worldIn.getTileEntity(pos));
		
		if(!worldIn.isRemote)
		{
			if(doesHaveEgg(state))
			{
    				ItemStack egg = te.removeEgg();
    				worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY() + .5d, pos.getZ(), egg));
         			EggNestBlock.removeEgg(worldIn, state, pos);
         			return true;
			}
			else if(!heldItem.isEmpty() && heldItem.getItem() instanceof ItemEgg)
			{
				te.insertEgg(heldItem);
				EggNestBlock.addEgg(worldIn, state, pos);
		        if (!playerIn.capabilities.isCreativeMode) {
		            heldItem.shrink(1);
		        }
		        return true;
			}
		}

		return true;
    }
	
	@Override
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
    	return false;
    }
    
    public static boolean doesHaveEgg(IBlockState state)
    {
    	if(state.getBlock() != ModBlocks.nest) return false;
    	
    	return state.getValue(hasEgg);
    }
    
    public static void setEggState(World worldIn, IBlockState state, BlockPos pos, boolean egg) {
    	worldIn.setBlockState(pos,state.withProperty(hasEgg, egg));
    }
    
	@Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
    
    public static void addEgg(World worldIn, IBlockState state, BlockPos pos)
    {
    	worldIn.setBlockState(pos,state.withProperty(hasEgg, true));
    }
    
    public static void removeEgg(World worldIn, IBlockState state, BlockPos pos)
    {
    	worldIn.setBlockState(pos, state.withProperty(hasEgg, false));
    }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new EggNestTileEntity();
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	  
	@Override
	public boolean isFullBlock(IBlockState state)
	{
		return false;
	}
	
	@Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	
	@Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {hasEgg});
    }
    
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
    	if(meta == 1)
    	{
    		return this.getDefaultState().withProperty(hasEgg, true);
    	}
    	else return this.getDefaultState().withProperty(hasEgg, false);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
	@Override
    public int getMetaFromState(IBlockState state)
    {
    	if (((boolean)state.getValue(hasEgg)))
    	{
    		return 1;
    	}
        return 0;
    }

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) 
	{
		
		
		TileEntity te = world.getTileEntity(data.getPos());
		
		if(te instanceof EggNestTileEntity)
		{
			EggNestTileEntity hte = (EggNestTileEntity) te;
			
			if(hte.getEgg() != null)
			{
				float percentage = hte.getPercentage();
				probeInfo.text(TextFormatting.YELLOW + "Hatching: "+ TextFormatting.GREEN + percentage +"%");
				probeInfo.text(TextFormatting.YELLOW + hte.getEgg().getDisplayName());
			}
			else 
				probeInfo.text(TextFormatting.RED + "Not Hatching");
		}
	}

}
