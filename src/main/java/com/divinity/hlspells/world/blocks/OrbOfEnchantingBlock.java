package com.divinity.hlspells.world.blocks;

import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.setup.init.SoundInit;
import com.divinity.hlspells.world.blocks.blockentities.OrbOfEnchantingBE;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
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

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import org.jetbrains.annotations.Nullable;

public class OrbOfEnchantingBlock extends BaseEntityBlock {

    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_COMPOSTER;
    final double MAX_XP = 1400;
    public OrbOfEnchantingBlock(Properties p_49795_) {

        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, Integer.valueOf(0)));
    }

    @Override
    @ParametersAreNonnullByDefault
    @NotNull
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        if(blockentity instanceof OrbOfEnchantingBE) {
            OrbOfEnchantingBE orb = (OrbOfEnchantingBE) blockentity;
            if (pPlayer.isCrouching()) {
                if (pPlayer.totalExperience >= 1 && orb.getXP() < MAX_XP) {
                    pPlayer.giveExperiencePoints(-10);
                    orb.addXP(5);
                    changeState(pState, pLevel, pPos);
                    pLevel.playSound(null, pPos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.NEUTRAL, 0.25F, 0.2F);
                    return InteractionResult.SUCCESS;
                } else {
                    pPlayer.level().playSound(null, pPlayer.blockPosition(), SoundInit.MISCAST_SOUND.get(), SoundSource.NEUTRAL, 0.4F, 0.2F);
                    return InteractionResult.FAIL;
                }
            } else {
                if (orb.getXP() >= 1) {
                    orb.removeXP(5);;
                    changeState(pState, pLevel, pPos);
                    if (pPlayer.isHolding(Items.GLASS_BOTTLE)) {
                        pPlayer.getItemInHand(InteractionHand.MAIN_HAND).setCount(pPlayer.getItemInHand(InteractionHand.MAIN_HAND).getCount() - 1);
                        pPlayer.addItem(new ItemStack(Items.EXPERIENCE_BOTTLE));
                    } else {
                        pPlayer.giveExperiencePoints(10);
                    }
                    pLevel.playSound(null, pPos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.NEUTRAL, 0.5F, 0.5F);
                    return InteractionResult.SUCCESS;
                } else {
                    pPlayer.level().playSound(null, pPlayer.blockPosition(), SoundInit.MISCAST_SOUND.get(), SoundSource.NEUTRAL, 0.4F, 0.3F);
                    return InteractionResult.FAIL;
                }
            }
        }
        return InteractionResult.PASS;
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

    protected void changeState(BlockState pState, Level pLevel, BlockPos pPos){
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        if(blockentity instanceof OrbOfEnchantingBE) {
            OrbOfEnchantingBE orb = (OrbOfEnchantingBE) blockentity;
            switch ((int) Math.floor(orb.getXP() / 175)) {
                case (1):
                    pLevel.setBlock(pPos, pState.setValue(LEVEL, Integer.valueOf(1)), 2);
                    break;
                case (2):
                    pLevel.setBlock(pPos, pState.setValue(LEVEL, Integer.valueOf(2)), 2);
                    break;
                case (3):
                    pLevel.setBlock(pPos, pState.setValue(LEVEL, Integer.valueOf(3)), 2);
                    break;
                case (4):
                    pLevel.setBlock(pPos, pState.setValue(LEVEL, Integer.valueOf(4)), 2);
                    break;
                case (5):
                    pLevel.setBlock(pPos, pState.setValue(LEVEL, Integer.valueOf(5)), 2);
                    break;
                case (6):
                    pLevel.setBlock(pPos, pState.setValue(LEVEL, Integer.valueOf(6)), 2);
                    break;
                case (7):
                    pLevel.setBlock(pPos, pState.setValue(LEVEL, Integer.valueOf(7)), 2);
                    break;
                case (8):
                    pLevel.setBlock(pPos, pState.setValue(LEVEL, Integer.valueOf(8)), 2);
                    break;
                default:
                    pLevel.setBlock(pPos, pState.setValue(LEVEL, Integer.valueOf(0)), 2);
                    break;
            }
        }

    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new OrbOfEnchantingBE(pPos, pState);
    }
}
