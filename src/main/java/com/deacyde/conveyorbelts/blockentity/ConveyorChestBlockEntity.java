package com.deacyde.conveyorbelts.blockentity;

import com.deacyde.conveyorbelts.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class ConveyorChestBlockEntity extends BlockEntity implements MenuProvider, net.minecraft.world.Container {

    private final NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
    private int ejectTimer = 0;
    private LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> new InvWrapper(this));

    public ConveyorChestBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CONVEYOR_CHEST_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ConveyorChestBlockEntity be) {
        if (level.isClientSide()) return;
        be.ejectTimer++;
        if (be.ejectTimer < 20) return;
        be.ejectTimer = 0;

        Direction facing = state.getValue(com.deacyde.conveyorbelts.block.ConveyorChestBlock.FACING);
        BlockPos targetPos = pos.relative(facing);
        net.minecraft.world.level.block.entity.BlockEntity targetBe = level.getBlockEntity(targetPos);
        if (targetBe == null) return;

        targetBe.getCapability(ForgeCapabilities.ITEM_HANDLER, facing.getOpposite()).ifPresent(handler -> {
            for (int i = 0; i < be.items.size(); i++) {
                ItemStack stack = be.items.get(i);
                if (stack.isEmpty()) continue;
                ItemStack remainder = net.minecraftforge.items.ItemHandlerHelper.insertItemStacked(handler, stack, false);
                be.items.set(i, remainder);
                be.setChanged();
                if (remainder.isEmpty()) break;
            }
        });
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        ContainerHelper.saveAllItems(output, items);
        output.putInt("eject_timer", ejectTimer);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        ContainerHelper.loadAllItems(input, items);
        ejectTimer = input.getIntOr("eject_timer", 0);
    }

    @Override public Component getDisplayName() { return Component.translatable("container.conveyor_chest"); }
    @Nullable @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return ChestMenu.threeRows(id, inv, this);
    }

    @Override public int getContainerSize() { return items.size(); }
    @Override public boolean isEmpty() { return items.stream().allMatch(ItemStack::isEmpty); }
    @Override public ItemStack getItem(int slot) { return items.get(slot); }
    @Override public ItemStack removeItem(int slot, int amount) {
        ItemStack result = net.minecraft.world.ContainerHelper.removeItem(items, slot, amount);
        if (!result.isEmpty()) setChanged();
        return result;
    }
    @Override public ItemStack removeItemNoUpdate(int slot) { return net.minecraft.world.ContainerHelper.takeItem(items, slot); }
    @Override public void setItem(int slot, ItemStack stack) { items.set(slot, stack); if (stack.getCount() > getMaxStackSize()) stack.setCount(getMaxStackSize()); setChanged(); }
    @Override public boolean stillValid(Player player) { return true; }
    @Override public void clearContent() { items.clear(); }

    @Nonnull @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) return itemHandler.cast();
        return super.getCapability(cap, side);
    }

    @Override public void invalidateCaps() { super.invalidateCaps(); itemHandler.invalidate(); }
    @Override public void reviveCaps() { super.reviveCaps(); itemHandler = LazyOptional.of(() -> new InvWrapper(this)); }
}
