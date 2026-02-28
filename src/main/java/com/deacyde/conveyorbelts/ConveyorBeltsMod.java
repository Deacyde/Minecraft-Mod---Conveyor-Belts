package com.deacyde.conveyorbelts;

import com.deacyde.conveyorbelts.init.ModBlockEntities;
import com.deacyde.conveyorbelts.init.ModBlocks;
import com.deacyde.conveyorbelts.init.ModCreativeTabs;
import com.deacyde.conveyorbelts.init.ModItems;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ConveyorBeltsMod.MOD_ID)
public class ConveyorBeltsMod {

    public static final String MOD_ID = "conveyorbelts";

    public ConveyorBeltsMod(FMLJavaModLoadingContext context) {
        var modBusGroup = context.getModBusGroup();

        ModBlocks.BLOCKS.register(modBusGroup);
        ModItems.ITEMS.register(modBusGroup);
        ModBlockEntities.BLOCK_ENTITIES.register(modBusGroup);
        ModCreativeTabs.CREATIVE_TABS.register(modBusGroup);
    }
}
