package com.deacyde.conveyorbelts.init;

import com.deacyde.conveyorbelts.ConveyorBeltsMod;
import com.deacyde.conveyorbelts.block.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ConveyorBeltsMod.MOD_ID);

    // --- Straight Belts (3 speed tiers) ---
    public static final RegistryObject<Block> CONVEYOR_BELT_SLOW = BLOCKS.register("conveyor_belt_slow",
            () -> new ConveyorBeltBlock(0.05f, props()));
    public static final RegistryObject<Block> CONVEYOR_BELT = BLOCKS.register("conveyor_belt",
            () -> new ConveyorBeltBlock(0.10f, props()));
    public static final RegistryObject<Block> CONVEYOR_BELT_FAST = BLOCKS.register("conveyor_belt_fast",
            () -> new ConveyorBeltBlock(0.20f, props()));

    // --- Corner Belts ---
    public static final RegistryObject<Block> CORNER_BELT_SLOW = BLOCKS.register("corner_belt_slow",
            () -> new CornerBeltBlock(0.05f, props()));
    public static final RegistryObject<Block> CORNER_BELT = BLOCKS.register("corner_belt",
            () -> new CornerBeltBlock(0.10f, props()));
    public static final RegistryObject<Block> CORNER_BELT_FAST = BLOCKS.register("corner_belt_fast",
            () -> new CornerBeltBlock(0.20f, props()));

    // --- Slope Belts ---
    public static final RegistryObject<Block> SLOPE_BELT_SLOW = BLOCKS.register("slope_belt_slow",
            () -> new SlopeBeltBlock(0.05f, props()));
    public static final RegistryObject<Block> SLOPE_BELT = BLOCKS.register("slope_belt",
            () -> new SlopeBeltBlock(0.10f, props()));
    public static final RegistryObject<Block> SLOPE_BELT_FAST = BLOCKS.register("slope_belt_fast",
            () -> new SlopeBeltBlock(0.20f, props()));

    // --- Splitter Belt ---
    public static final RegistryObject<Block> SPLITTER_BELT = BLOCKS.register("splitter_belt",
            () -> new SplitterBeltBlock(props()));

    // --- Merger Belt ---
    public static final RegistryObject<Block> MERGER_BELT = BLOCKS.register("merger_belt",
            () -> new MergerBeltBlock(props()));

    // --- Detector Belt ---
    public static final RegistryObject<Block> DETECTOR_BELT = BLOCKS.register("detector_belt",
            () -> new DetectorBeltBlock(props()));

    // --- Conveyor Chest ---
    public static final RegistryObject<Block> CONVEYOR_CHEST = BLOCKS.register("conveyor_chest",
            () -> new ConveyorChestBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(2.5f)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    // --- Vacuum Plate ---
    public static final RegistryObject<Block> VACUUM_PLATE = BLOCKS.register("vacuum_plate",
            () -> new VacuumPlateBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(1.5f)
                    .sound(SoundType.METAL)
                    .noOcclusion()));

    // --- Wrench (tool item registered via ModItems, block here for reference) ---

    private static BlockBehaviour.Properties props() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .strength(1.5f)
                .sound(SoundType.METAL)
                .noOcclusion();
    }
}
