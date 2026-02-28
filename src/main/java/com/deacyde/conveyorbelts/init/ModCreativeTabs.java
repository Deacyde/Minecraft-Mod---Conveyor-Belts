package com.deacyde.conveyorbelts.init;

import com.deacyde.conveyorbelts.ConveyorBeltsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ConveyorBeltsMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> CONVEYOR_TAB = CREATIVE_TABS.register("conveyor_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.conveyorbelts.conveyor_tab"))
                    .icon(() -> new ItemStack(ModItems.CONVEYOR_BELT_ITEM.get()))
                    .displayItems((params, output) -> {
                        // Belts
                        output.accept(ModItems.CONVEYOR_BELT_SLOW_ITEM.get());
                        output.accept(ModItems.CONVEYOR_BELT_ITEM.get());
                        output.accept(ModItems.CONVEYOR_BELT_FAST_ITEM.get());
                        // Corners
                        output.accept(ModItems.CORNER_BELT_SLOW_ITEM.get());
                        output.accept(ModItems.CORNER_BELT_ITEM.get());
                        output.accept(ModItems.CORNER_BELT_FAST_ITEM.get());
                        // Slopes
                        output.accept(ModItems.SLOPE_BELT_SLOW_ITEM.get());
                        output.accept(ModItems.SLOPE_BELT_ITEM.get());
                        output.accept(ModItems.SLOPE_BELT_FAST_ITEM.get());
                        // Specials
                        output.accept(ModItems.SPLITTER_BELT_ITEM.get());
                        output.accept(ModItems.MERGER_BELT_ITEM.get());
                        output.accept(ModItems.DETECTOR_BELT_ITEM.get());
                        output.accept(ModItems.CONVEYOR_CHEST_ITEM.get());
                        output.accept(ModItems.VACUUM_PLATE_ITEM.get());
                        // Tool
                        output.accept(ModItems.WRENCH.get());
                    })
                    .build());
}
