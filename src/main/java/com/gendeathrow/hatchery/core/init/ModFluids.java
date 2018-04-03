package com.gendeathrow.hatchery.core.init;

import com.gendeathrow.hatchery.fluid.BlockLiquidFertilizer;
import com.gendeathrow.hatchery.fluid.LiquidFertilizer;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@EventBusSubscriber
public class ModFluids 
{ 

	public static Fluid liquidfertilizer = new LiquidFertilizer();
	
//	static {
//		FluidRegistry.addBucketForFluid(liquidfertilizer);
//	}
	
	public static Block blockLiquidFertilizer;
		
	public static ItemStack getFertilizerBucket()
	{
		return UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, liquidfertilizer);
	}
	
	@SubscribeEvent
	public static void itemRegistry(RegistryEvent.Register<Item> event) {
	      if (!FluidRegistry.getBucketFluids().contains(liquidfertilizer)) {
	          FluidRegistry.addBucketForFluid(liquidfertilizer);
	        }
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void registerFluids(RegistryEvent.Register<Block> event) {
		FluidRegistry.registerFluid(liquidfertilizer);
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void blockRegistry(RegistryEvent.Register<Block> event) {
		
		blockLiquidFertilizer = new BlockLiquidFertilizer(liquidfertilizer);
		event.getRegistry().register(blockLiquidFertilizer);
	}
	
}
