package com.deacyde.conveyorbelts.blockentity;

import com.deacyde.conveyorbelts.block.*;
import com.deacyde.conveyorbelts.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ConveyorBeltBlockEntity extends BlockEntity {

    /** Splitter alternates which side gets the next item. */
    private boolean splitterToggle = false;

    public ConveyorBeltBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CONVEYOR_BELT_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ConveyorBeltBlockEntity be) {
        if (level.isClientSide()) return;

        Block block = state.getBlock();

        // All belt types except Detector stop when powered by redstone
        if (!(block instanceof DetectorBeltBlock) &&
                state.hasProperty(BlockStateProperties.POWERED) &&
                state.getValue(BlockStateProperties.POWERED)) {
            return;
        }

        if (block instanceof ConveyorBeltBlock cb) {
            be.moveStraight(level, pos, state.getValue(ConveyorBeltBlock.FACING), cb.getSpeed());

        } else if (block instanceof CornerBeltBlock cb) {
            be.moveCorner(level, pos, state, cb.getSpeed());

        } else if (block instanceof SlopeBeltBlock sb) {
            be.moveSlope(level, pos, state, sb.getSpeed());

        } else if (block instanceof SplitterBeltBlock) {
            be.moveSplitter(level, pos, state);

        } else if (block instanceof MergerBeltBlock) {
            be.moveStraight(level, pos, state.getValue(MergerBeltBlock.FACING), 0.10f);

        } else if (block instanceof DetectorBeltBlock) {
            be.tickDetector(level, pos, state);
        }
    }

    // -----------------------------------------------------------------------
    // Straight movement
    // -----------------------------------------------------------------------

    private void moveStraight(Level level, BlockPos pos, Direction facing, float speed) {
        List<ItemEntity> items = getItemsAbove(level, pos);
        double dx = facing.getStepX() * speed;
        double dz = facing.getStepZ() * speed;
        for (ItemEntity item : items) {
            item.setDeltaMovement(dx, Math.max(item.getDeltaMovement().y, -0.08), dz);
            item.hurtMarked = true;
        }
    }

    // -----------------------------------------------------------------------
    // Corner movement
    // -----------------------------------------------------------------------

    private void moveCorner(Level level, BlockPos pos, BlockState state, float speed) {
        Direction exitDir = state.getValue(CornerBeltBlock.FACING);
        Direction entryDir = CornerBeltBlock.getEntryDirection(state);

        double centerX = pos.getX() + 0.5;
        double centerZ = pos.getZ() + 0.5;

        for (ItemEntity item : getItemsAbove(level, pos)) {
            double ix = item.getX(), iz = item.getZ();
            // Measure how far item is from center along the entry axis
            double distOnEntryAxis = entryDir.getAxis() == Direction.Axis.X
                    ? Math.abs(ix - centerX)
                    : Math.abs(iz - centerZ);

            if (distOnEntryAxis > 0.18) {
                // Still on entry leg — push toward center along entry axis only
                double dx = entryDir.getAxis() == Direction.Axis.X
                        ? Math.signum(centerX - ix) * speed : 0;
                double dz = entryDir.getAxis() == Direction.Axis.Z
                        ? Math.signum(centerZ - iz) * speed : 0;
                item.setDeltaMovement(dx, Math.max(item.getDeltaMovement().y, -0.08), dz);
            } else {
                // Past center — push in exit direction
                item.setDeltaMovement(
                        exitDir.getStepX() * speed,
                        Math.max(item.getDeltaMovement().y, -0.08),
                        exitDir.getStepZ() * speed);
            }
            item.hurtMarked = true;
        }
    }

    // -----------------------------------------------------------------------
    // Slope movement
    // -----------------------------------------------------------------------

    private void moveSlope(Level level, BlockPos pos, BlockState state, float speed) {
        Direction facing = state.getValue(SlopeBeltBlock.FACING);

        // Extended AABB — items may be at +1 block height while climbing
        AABB area = new AABB(
                pos.getX() - 0.1, pos.getY() + 0.4, pos.getZ() - 0.1,
                pos.getX() + 1.1, pos.getY() + 2.2, pos.getZ() + 1.1
        );
        List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, area);

        for (ItemEntity item : items) {
            item.setDeltaMovement(
                    facing.getStepX() * speed,
                    speed * 0.75,   // climb upward
                    facing.getStepZ() * speed);
            item.hurtMarked = true;
        }
    }

    // -----------------------------------------------------------------------
    // Splitter movement — alternates items left / right of FACING
    // -----------------------------------------------------------------------

    private void moveSplitter(Level level, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(SplitterBeltBlock.FACING);
        float speed = 0.10f;

        double centerX = pos.getX() + 0.5;
        double centerZ = pos.getZ() + 0.5;

        // Input direction is opposite of FACING
        Direction inputDir = facing.getOpposite();

        for (ItemEntity item : getItemsAbove(level, pos)) {
            double ix = item.getX(), iz = item.getZ();

            // How far past center in the input→exit axis?
            double progressAlongInput = inputDir.getAxis() == Direction.Axis.X
                    ? (ix - centerX) * -inputDir.getStepX()
                    : (iz - centerZ) * -inputDir.getStepZ();

            if (progressAlongInput < 0.1) {
                // Still on input side — push straight toward center
                item.setDeltaMovement(
                        facing.getStepX() * speed,
                        Math.max(item.getDeltaMovement().y, -0.08),
                        facing.getStepZ() * speed);
            } else {
                // Past center — split left or right
                Direction outDir = splitterToggle ? facing.getClockWise() : facing.getCounterClockWise();
                splitterToggle = !splitterToggle; // alternate next item
                item.setDeltaMovement(
                        outDir.getStepX() * speed,
                        Math.max(item.getDeltaMovement().y, -0.08),
                        outDir.getStepZ() * speed);
            }
            item.hurtMarked = true;
        }
    }

    // -----------------------------------------------------------------------
    // Detector — straight belt + redstone signal update
    // -----------------------------------------------------------------------

    private void tickDetector(Level level, BlockPos pos, BlockState state) {
        // Move items straight in FACING direction
        Direction facing = state.getValue(DetectorBeltBlock.FACING);
        List<ItemEntity> items = getItemsAbove(level, pos);

        double dx = facing.getStepX() * 0.10;
        double dz = facing.getStepZ() * 0.10;
        for (ItemEntity item : items) {
            item.setDeltaMovement(dx, Math.max(item.getDeltaMovement().y, -0.08), dz);
            item.hurtMarked = true;
        }

        // Update DETECTING state if it changed
        boolean hasItems = !items.isEmpty();
        boolean currently = state.getValue(DetectorBeltBlock.DETECTING);
        if (hasItems != currently) {
            level.setBlock(pos, state.setValue(DetectorBeltBlock.DETECTING, hasItems), 3);
            level.updateNeighborsAt(pos, state.getBlock());
        }
    }

    // -----------------------------------------------------------------------
    // Helper
    // -----------------------------------------------------------------------

    private static List<ItemEntity> getItemsAbove(Level level, BlockPos pos) {
        AABB area = new AABB(
                pos.getX() + 0.05, pos.getY() + 0.5, pos.getZ() + 0.05,
                pos.getX() + 0.95, pos.getY() + 1.5, pos.getZ() + 0.95
        );
        return level.getEntitiesOfClass(ItemEntity.class, area);
    }
}
