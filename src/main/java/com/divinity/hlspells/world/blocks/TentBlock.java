package com.divinity.hlspells.world.blocks;

import com.divinity.hlspells.setup.init.SoundInit;
import com.divinity.hlspells.world.blocks.blockentities.AltarOfAttunementBE;
import com.divinity.hlspells.world.blocks.blockentities.TentBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

public class TentBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public TentBlock(Properties properties) {
        super(properties);
    }


    @Override
    @ParametersAreNonnullByDefault
    @NotNull
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            // Real work must happen on the server; the client only needs to know the interaction succeeded.
            return InteractionResult.sidedSuccess(true);
        }


        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        if (blockentity instanceof TentBE tent && pPlayer instanceof ServerPlayer sp) {
            // On the server, pPlayer is expected to be ServerPlayer.
            tent.setPrevBed(sp.getRespawnPosition()); // can be null
            sp.setRespawnPosition(pLevel.dimension(), tent.getBlockPos(), 0, true, true);

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        if (blockentity instanceof TentBE tent) {
            // Readable on both sides (client gets this via BE sync).
            BlockPos bed = tent.getBed();

            // Only the server can actually change the player's respawn point.
            if (!pLevel.isClientSide && pPlayer instanceof ServerPlayer serverPlayer) {
                serverPlayer.setRespawnPosition(pLevel.dimension(), bed, 0, true, false);
            }
        }
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    @Override
    @ParametersAreNonnullByDefault
    @NotNull
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return makeShape(pState.getValue(FACING));
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




    public VoxelShape makeShape(Direction property){
        VoxelShape shape = Shapes.empty();
        if (property == Direction.SOUTH) {
            shape = Shapes.join(shape, Shapes.box(0, 0, -1, 1, 0.125, 1), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.4375, 0.125, 0.875, 0.5625, 1, 1), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.4375, 0.125, -1, 0.5625, 1, -0.875), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.4375, 0.875, -0.875, 0.5625, 1, 0.875), BooleanOp.OR);
        }
        else if (property == Direction.EAST) {

        shape = Shapes.join(shape, Shapes.box(-1, 0, 0, 1, 0.125, 1), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.875, 0.125, 0.4375, 1, 1, 0.5625), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(-1, 0.125, 0.4375, -0.875, 1, 0.5625), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(-0.875, 0.875, 0.4375, 0.875, 1, 0.5625), BooleanOp.OR);
        }
        else if (property == Direction.NORTH) {
            shape = Shapes.join(shape, Shapes.box(0, 0, 0, 1, 0.125, 2), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.4375, 0.125, 0, 0.5625, 1, 0.125), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.4375, 0.125, 1.875, 0.5625, 1, 2), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.4375, 0.875, 0.125, 0.5625, 1, 1.875), BooleanOp.OR);
        }
        else{
            //WEST
            shape = Shapes.join(shape, Shapes.box(0, 0, 0, 2, 0.125, 1), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(1.875, 0.125, 0.4375, 2, 1, 0.5625), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0, 0.125, 0.4375, 0.125, 1, 0.5625), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.125, 0.875, 0.4375, 1.875, 1, 0.5625), BooleanOp.OR);
        }
        return shape;
    }


    @Override
    @NotNull
    public RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TentBE(pPos, pState);
    }
}
