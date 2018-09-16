package com.gendeathrow.hatchery.item;

import com.gendeathrow.hatchery.Hatchery;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChickenManure extends Item
{
	public ChickenManure()
	{
		super();
		
		this.setTranslationKey("chickenmanure");
		//this.setRegistryName("chickenmanure");
		this.setCreativeTab(Hatchery.hatcheryTabs);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {

        if (!playerIn.canPlayerEdit(pos.offset(facing), facing, playerIn.getHeldItem(hand)))
        {
            return EnumActionResult.FAIL;
        }
        else
        {
                if (ItemDye.applyBonemeal(playerIn.getHeldItem(hand), worldIn, pos))
                {
                    if (!worldIn.isRemote)
                    {
                        worldIn.playEvent(2005, pos, 0);
                    }
                }

            return EnumActionResult.SUCCESS;
        }
        

    }
}
