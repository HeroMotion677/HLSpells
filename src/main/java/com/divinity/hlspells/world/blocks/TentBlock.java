package com.divinity.hlspells.world.blocks;

import com.divinity.hlspells.setup.init.SoundInit;
import com.divinity.hlspells.world.blocks.blockentities.TentBE;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
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

    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_COMPOSTER;
    final double MAX_XP = 1400;
    public TentBlock(Properties p_49795_) {

        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, Integer.valueOf(0)));
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
        return makeShape();
    }

    public static VoxelShape makeShape(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.3125, 0, 0.3125, 0.6875, 0.125, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.34374999999999994, 0.1875, 0.34375, 0.65625, 0.5, 0.65625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.375, 0.125, 0.375, 0.625, 0.1875, 0.625), BooleanOp.OR);

        return shape;
    }

    @Override
    @NotNull
    public RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(LEVEL);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TentBE(pPos, pState);
    }
}
