package com.deacyde.conveyorbelts.block;

import com.deacyde.conveyorbelts.blockentity.ConveyorBeltBlockEntity;
import com.deacyde.conveyorbelts.init.ModBlockEntities;
import com.deacyde.conveyorbelts.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class CornerBeltBlock extends BaseEntityBlock {

    public enum Turn implements StringRepresentable {
        LEFT("left"), RIGHT("right");
        private final String name;
        Turn(String name) { this.name = name; }
        @Override public String getSerializedName() { return name; }
    }

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<Turn> TURN = EnumProperty.create("turn", Turn.class);

    private final float speed;

    public CornerBeltBlock(float speed, BlockBehaviour.Properties props) {
        super(props);
        this.speed = speed;
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(TURN, Turn.LEFT)
                .setValue(POWERED, false));
    }

    public float getSpeed() { return speed; }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TURN, POWERED);
    }

    @Override
    public BlockState getStateForPlacement(net.minecraft.world.item.context.BlockPlaceContext ctx) {
        return defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection().getOpposite())
                .setValue(POWERED, ctx.getLevel().hasNeighborSignal(ctx.getClickedPos()));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block,
                                BlockPos fromPos, boolean isMoving) {
        level.setBlock(pos, state.setValue(POWERED, level.hasNeighborSignal(pos)), 3);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (player.getItemInHand(hand).is(ModItems.WRENCH.get())) {
            if (!level.isClientSide) {
                // Cycle: rotate facing, or toggle turn direction on sneak
                if (player.isShiftKeyDown()) {
                    Turn newTurn = state.getValue(TURN) == Turn.LEFT ? Turn.RIGHT : Turn.LEFT;
                    level.setBlock(pos, state.setValue(TURN, newTurn), 3);
                } else {
                    level.setBlock(pos, state.setValue(FACING, state.getValue(FACING).getClockWise()), 3);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    /** Returns the direction items enter from based on FACING and TURN. */
    public static Direction getEntryDirection(BlockState state) {
        Direction facing = state.getValue(FACING);
        Turn turn = state.getValue(TURN);
        return turn == Turn.LEFT ? facing.getClockWise() : facing.getCounterClockWise();
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
