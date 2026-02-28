package com.deacyde.conveyorbelts.block;

import com.deacyde.conveyorbelts.blockentity.ConveyorBeltBlockEntity;
import com.deacyde.conveyorbelts.init.ModBlockEntities;
import com.deacyde.conveyorbelts.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * Detector belt — functions as a straight belt AND emits a redstone signal (strength 15)
 * when at least one item is present on top of it.
 */
public class DetectorBeltBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    /** True when items are currently on the belt (emits redstone). */
    public static final BooleanProperty DETECTING = BooleanProperty.create("detecting");

    public DetectorBeltBlock(BlockBehaviour.Properties props) {
        super(props);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(DETECTING, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, DETECTING);
    }

    @Override
    public BlockState getStateForPlacement(net.minecraft.world.item.context.BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (player.getItemInHand(hand).is(ModItems.WRENCH.get())) {
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(FACING, state.getValue(FACING).getClockWise()), 3);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    // --- Redstone emission ---
    @Override public boolean isSignalSource(BlockState state) { return true; }

    @Override
    public int getSignal(BlockState state, net.minecraft.world.level.BlockGetter level,
                         BlockPos pos, Direction dir) {
        return state.getValue(DETECTING) ? 15 : 0;
    }

    @Override
    public int getDirectSignal(BlockState state, net.minecraft.world.level.BlockGetter level,
                               BlockPos pos, Direction dir) {
        return getSignal(state, level, pos, dir);
    }

    @Override public RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }

    @Nullable @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ConveyorBeltBlockEntity(pos, state);
    }

    @Nullable @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                   BlockEntityType<T> type) {
        return level.isClientSide ? null
                : createTickerHelper(type, ModBlockEntities.CONVEYOR_BELT_BE.get(), ConveyorBeltBlockEntity::tick);
    }
}
