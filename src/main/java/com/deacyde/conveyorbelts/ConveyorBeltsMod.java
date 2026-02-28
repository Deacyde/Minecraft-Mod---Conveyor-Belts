package com.deacyde.conveyorbelts;

import com.deacyde.conveyorbelts.init.ModBlockEntities;
import com.deacyde.conveyorbelts.init.ModBlocks;
import com.deacyde.conveyorbelts.init.ModCreativeTabs;
import com.deacyde.conveyorbelts.init.ModItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ConveyorBeltsMod.MOD_ID)
public class ConveyorBeltsMod {

    public static final String MOD_ID = "conveyorbelts";

    public ConveyorBeltsMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModCreativeTabs.CREATIVE_TABS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }
}
