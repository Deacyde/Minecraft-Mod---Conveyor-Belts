package com.deacyde.conveyorbelts.blockentity;

import com.deacyde.conveyorbelts.block.ConveyorChestBlock;
import com.deacyde.conveyorbelts.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.stream.IntStream;

public class ConveyorChestBlockEntity extends BlockEntity implements WorldlyContainer, MenuProvider {

    private static final int SIZE = 27;
    // Eject one item every 20 ticks (1 second)
    private static final int EJECT_INTERVAL = 20;

    private final NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);
    private int ejectTimer = 0;

    public ConveyorChestBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CONVEYOR_CHEST_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ConveyorChestBlockEntity be) {
        if (level.isClientSide) return;
        be.ejectTimer++;
        if (be.ejectTimer >= EJECT_INTERVAL) {
            be.ejectTimer = 0;
            be.tryEject(level, pos, state);
        }
    }

    private void tryEject(Level level, BlockPos pos, BlockState state) {
        for (int i = 0; i < items.size(); i++) {
            if (!items.get(i).isEmpty()) {
                // Take exactly 1 item from stack
                ItemStack ejected = items.get(i).copyWithCount(1);
                items.get(i).shrink(1);
                if (items.get(i).isEmpty()) items.set(i, ItemStack.EMPTY);

                Direction facing = state.getValue(ConveyorChestBlock.FACING);
                double spawnX = pos.getX() + 0.5 + facing.getStepX() * 0.6;
                double spawnY = pos.getY() + 0.6;
                double spawnZ = pos.getZ() + 0.5 + facing.getStepZ() * 0.6;

                ItemEntity entity = new ItemEntity(level, spawnX, spawnY, spawnZ, ejected,
                        facing.getStepX() * 0.1, 0, facing.getStepZ() * 0.1);
                entity.setDefaultPickUpDelay();
                level.addFreshEntity(entity);
                setChanged();
                break;
            }
        }
    }

    // --- WorldlyContainer (Hopper compatibility) ---

    @Override
    public int[] getSlotsForFace(Direction side) {
        // Accept from top and all horizontal sides; bottom is not used
        if (side == Direction.DOWN) return new int[0];
        return IntStream.range(0, SIZE).toArray();
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return dir != Direction.DOWN;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return false; // items only leave via belt ejection, not hoppers extracting
    }

    // --- Container ---
    @Override public int getContainerSize() { return SIZE; }
    @Override public boolean isEmpty() { return items.stream().allMatch(ItemStack::isEmpty); }
    @Override public ItemStack getItem(int slot) { return items.get(slot); }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        setChanged();
        return ContainerHelper.removeItem(items, slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) stack.setCount(getMaxStackSize());
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override public void clearContent() { items.clear(); }

    // --- MenuProvider (open 3×9 chest GUI) ---

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.conveyorbelts.conveyor_chest");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return ChestMenu.threeRows(id, inventory, this);
    }

    // --- NBT ---

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, items);
        tag.putInt("eject_timer", ejectTimer);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        ContainerHelper.loadAllItems(tag, items);
        ejectTimer = tag.getInt("eject_timer");
    }
}
