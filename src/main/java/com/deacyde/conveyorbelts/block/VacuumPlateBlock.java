package com.deacyde.conveyorbelts.block;

import com.deacyde.conveyorbelts.blockentity.VacuumPlateBlockEntity;
import com.deacyde.conveyorbelts.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * Vacuum Plate — flat block (2px tall) that pulls dropped items within a 3-block radius
 * onto itself, then onto an adjacent belt.
 */
public class VacuumPlateBlock extends BaseEntityBlock {

    // Flat plate shape — 2/16 of a block tall
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 2, 16);

    public VacuumPlateBlock(BlockBehaviour.Properties props) {
        super(props);
    }

    @Override
    public VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level,
                               BlockPos pos, CollisionContext ctx) {
        return SHAPE;
    }

    @Override public RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }

    @Nullable @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new VacuumPlateBlockEntity(pos, state);
    }

    @Nullable @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                   BlockEntityType<T> type) {
        return level.isClientSide ? null
                : createTickerHelper(type, ModBlockEntities.VACUUM_PLATE_BE.get(), VacuumPlateBlockEntity::tick);
    }
}
