package com.deacyde.conveyorbelts.blockentity;

import com.deacyde.conveyorbelts.block.ConveyorBeltBlock;
import com.deacyde.conveyorbelts.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Vacuum Plate — every 5 ticks, scans a 3-block radius for dropped items and
 * pulls them toward the plate's center. Items that land on the plate are then
 * nudged onto the nearest adjacent belt.
 */
public class VacuumPlateBlockEntity extends BlockEntity {

    private static final double VACUUM_RADIUS = 3.5;
    private static final double PULL_SPEED = 0.18;
    private int tickCounter = 0;

    public VacuumPlateBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.VACUUM_PLATE_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, VacuumPlateBlockEntity be) {
        if (level.isClientSide()) return;

        be.tickCounter++;

        // Pull nearby items every 5 ticks (4 times/sec) for efficiency
        if (be.tickCounter % 5 == 0) {
            be.vacuumItems(level, pos);
        }

        // Every tick, nudge items that are on the plate onto an adjacent belt
        be.ejectToAdjacentBelt(level, pos);
    }

    private void vacuumItems(Level level, BlockPos pos) {
        AABB scanArea = new AABB(
                pos.getX() - VACUUM_RADIUS, pos.getY() - 1, pos.getZ() - VACUUM_RADIUS,
                pos.getX() + 1 + VACUUM_RADIUS, pos.getY() + VACUUM_RADIUS + 1, pos.getZ() + 1 + VACUUM_RADIUS
        );

        List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, scanArea);
        double cx = pos.getX() + 0.5;
        double cy = pos.getY() + 0.1;
        double cz = pos.getZ() + 0.5;

        for (ItemEntity item : items) {
            Vec3 toCenter = new Vec3(cx - item.getX(), cy - item.getY(), cz - item.getZ());
            double dist = toCenter.length();
            if (dist < 0.3) continue; // already at center

            Vec3 pull = toCenter.normalize().scale(Math.min(PULL_SPEED, dist));
            item.setDeltaMovement(pull.x, pull.y, pull.z);
            item.hurtMarked = true;
        }
    }

    private void ejectToAdjacentBelt(Level level, BlockPos pos) {
        // Look for items right on the plate (very close to top surface)
        AABB onPlate = new AABB(
                pos.getX() + 0.1, pos.getY() + 0.05, pos.getZ() + 0.1,
                pos.getX() + 0.9, pos.getY() + 0.6, pos.getZ() + 0.9
        );
        List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, onPlate);
        if (items.isEmpty()) return;

        // Find the first adjacent belt block
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos neighbor = pos.relative(dir);
            BlockState neighborState = level.getBlockState(neighbor);
            if (neighborState.getBlock() instanceof ConveyorBeltBlock) {
                double dx = dir.getStepX() * 0.15;
                double dz = dir.getStepZ() * 0.15;
                for (ItemEntity item : items) {
                    item.setDeltaMovement(dx, 0.02, dz);
                    item.hurtMarked = true;
                }
                return;
            }
        }

        // No adjacent belt — just hold items in place on the plate
        for (ItemEntity item : items) {
            item.setDeltaMovement(0, 0, 0);
            item.hurtMarked = true;
        }
    }
}
