package com.deacyde.conveyorbelts.init;

import com.deacyde.conveyorbelts.ConveyorBeltsMod;
import com.deacyde.conveyorbelts.blockentity.ConveyorBeltBlockEntity;
import com.deacyde.conveyorbelts.blockentity.ConveyorChestBlockEntity;
import com.deacyde.conveyorbelts.blockentity.VacuumPlateBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ConveyorBeltsMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<ConveyorBeltBlockEntity>> CONVEYOR_BELT_BE =
            BLOCK_ENTITIES.register("conveyor_belt_be", () ->
                    BlockEntityType.Builder.of(ConveyorBeltBlockEntity::new,
                            ModBlocks.CONVEYOR_BELT_SLOW.get(),
                            ModBlocks.CONVEYOR_BELT.get(),
                            ModBlocks.CONVEYOR_BELT_FAST.get(),
                            ModBlocks.CORNER_BELT_SLOW.get(),
                            ModBlocks.CORNER_BELT.get(),
                            ModBlocks.CORNER_BELT_FAST.get(),
                            ModBlocks.SLOPE_BELT_SLOW.get(),
                            ModBlocks.SLOPE_BELT.get(),
                            ModBlocks.SLOPE_BELT_FAST.get(),
                            ModBlocks.SPLITTER_BELT.get(),
                            ModBlocks.MERGER_BELT.get(),
                            ModBlocks.DETECTOR_BELT.get()
                    ).build(null));

    public static final RegistryObject<BlockEntityType<ConveyorChestBlockEntity>> CONVEYOR_CHEST_BE =
            BLOCK_ENTITIES.register("conveyor_chest_be", () ->
                    BlockEntityType.Builder.of(ConveyorChestBlockEntity::new,
                            ModBlocks.CONVEYOR_CHEST.get()
                    ).build(null));

    public static final RegistryObject<BlockEntityType<VacuumPlateBlockEntity>> VACUUM_PLATE_BE =
            BLOCK_ENTITIES.register("vacuum_plate_be", () ->
                    BlockEntityType.Builder.of(VacuumPlateBlockEntity::new,
                            ModBlocks.VACUUM_PLATE.get()
                    ).build(null));
}
