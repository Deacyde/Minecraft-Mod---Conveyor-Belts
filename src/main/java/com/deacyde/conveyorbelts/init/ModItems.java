package com.deacyde.conveyorbelts.init;

import com.deacyde.conveyorbelts.ConveyorBeltsMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ConveyorBeltsMod.MOD_ID);

    // Block items
    public static final RegistryObject<Item> CONVEYOR_BELT_SLOW_ITEM = registerBlockItem("conveyor_belt_slow", ModBlocks.CONVEYOR_BELT_SLOW);
    public static final RegistryObject<Item> CONVEYOR_BELT_ITEM      = registerBlockItem("conveyor_belt", ModBlocks.CONVEYOR_BELT);
    public static final RegistryObject<Item> CONVEYOR_BELT_FAST_ITEM = registerBlockItem("conveyor_belt_fast", ModBlocks.CONVEYOR_BELT_FAST);

    public static final RegistryObject<Item> CORNER_BELT_SLOW_ITEM   = registerBlockItem("corner_belt_slow", ModBlocks.CORNER_BELT_SLOW);
    public static final RegistryObject<Item> CORNER_BELT_ITEM        = registerBlockItem("corner_belt", ModBlocks.CORNER_BELT);
    public static final RegistryObject<Item> CORNER_BELT_FAST_ITEM   = registerBlockItem("corner_belt_fast", ModBlocks.CORNER_BELT_FAST);

    public static final RegistryObject<Item> SLOPE_BELT_SLOW_ITEM    = registerBlockItem("slope_belt_slow", ModBlocks.SLOPE_BELT_SLOW);
    public static final RegistryObject<Item> SLOPE_BELT_ITEM         = registerBlockItem("slope_belt", ModBlocks.SLOPE_BELT);
    public static final RegistryObject<Item> SLOPE_BELT_FAST_ITEM    = registerBlockItem("slope_belt_fast", ModBlocks.SLOPE_BELT_FAST);

    public static final RegistryObject<Item> SPLITTER_BELT_ITEM      = registerBlockItem("splitter_belt", ModBlocks.SPLITTER_BELT);
    public static final RegistryObject<Item> MERGER_BELT_ITEM        = registerBlockItem("merger_belt", ModBlocks.MERGER_BELT);
    public static final RegistryObject<Item> DETECTOR_BELT_ITEM      = registerBlockItem("detector_belt", ModBlocks.DETECTOR_BELT);
    public static final RegistryObject<Item> CONVEYOR_CHEST_ITEM     = registerBlockItem("conveyor_chest", ModBlocks.CONVEYOR_CHEST);
    public static final RegistryObject<Item> VACUUM_PLATE_ITEM       = registerBlockItem("vacuum_plate", ModBlocks.VACUUM_PLATE);

    // Wrench tool — used to rotate belt blocks
    public static final RegistryObject<Item> WRENCH = ITEMS.register("wrench",
            () -> new Item(new Item.Properties().stacksTo(1)));

    private static RegistryObject<Item> registerBlockItem(String name, Supplier<? extends net.minecraft.world.level.block.Block> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
