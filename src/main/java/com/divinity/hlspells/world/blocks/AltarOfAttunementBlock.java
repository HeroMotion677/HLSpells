package com.divinity.hlspells.world.blocks;

import com.divinity.hlspells.setup.init.BlockInit;
import com.divinity.hlspells.world.blocks.blockentities.AltarOfAttunementBE;
import com.divinity.hlspells.world.blocks.blockentities.inventory.AltarOfAttunementMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("deprecation")
public class AltarOfAttunementBlock extends EnchantmentTableBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public AltarOfAttunementBlock(Properties properties) {
        super(properties);
    }

    @Override
    @ParametersAreNonnullByDefault
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new AltarOfAttunementBE(pPos, pState);
    }

    @Override
    @ParametersAreNonnullByDefault
    @NotNull
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return makeShape(pState.getValue(FACING));
    }

    /* BLOCK ENTITY */

    @Override
    @ParametersAreNonnullByDefault
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? createTickerHelper(pBlockEntityType, BlockInit.ALTAR_BE.get(), AltarOfAttunementBE::bookAnimationTick) : null;
    }

    @Override
    @ParametersAreNonnullByDefault
    @NotNull
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.getBlockEntity(pPos) instanceof AltarOfAttunementBE blockEntity) {
            if (pLevel.isClientSide) return InteractionResult.SUCCESS;
            NetworkHooks.openGui((ServerPlayer) pPlayer, blockEntity, pPos);
            if (pPlayer.containerMenu instanceof AltarOfAttunementMenu menu) menu.slotsChanged(null);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof AltarOfAttunementBE be) be.dropContents();
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    @NotNull
    public RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    @NotNull
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override @NotNull public BlockState mirror(BlockState pState, Mirror pMirror) { return pState.rotate(pMirror.getRotation(pState.getValue(FACING))); }

    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) { pBuilder.add(FACING); }

    public static VoxelShape makeShape(Direction property) {
        VoxelShape shape = Shapes.empty();
        VoxelShape topNS = Shapes.join(shape, Shapes.box(-0.1875, 0.75, 0, 1.1875, 1, 1),  BooleanOp.OR); // North and South
        VoxelShape topEW = Shapes.join(shape, Shapes.box(0, 0.75, -0.1875, 1, 1, 1.1875), BooleanOp.OR); // East and West
        if (property == Direction.NORTH || property == Direction.SOUTH) {
            VoxelShape base = Shapes.join(topNS, Shapes.box(0.125, 0.125, 0.125, 0.875, 0.8125, 0.875), BooleanOp.OR);
            return Shapes.join(base, Shapes.box(0.125, 0.3125, 0.125, 0.875, 1, 0.875), BooleanOp.OR);
        }
        VoxelShape base = Shapes.join(topEW, Shapes.box(0.125, 0.125, 0.125, 0.875, 0.8125, 0.875), BooleanOp.OR);
        return Shapes.join(base, Shapes.box(0.125, 0.3125, 0.125, 0.875, 1, 0.875), BooleanOp.OR);
    }
}
