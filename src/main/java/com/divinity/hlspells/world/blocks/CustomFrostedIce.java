package com.divinity.hlspells.world.blocks;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.FrostedIceBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import java.util.Random;

import javax.annotation.ParametersAreNonnullByDefault;

public class CustomFrostedIce extends FrostedIceBlock {

    public CustomFrostedIce(Properties properties) {
        super(properties);
    }

    // Overridden to remove the check for light level
    @Override
    @ParametersAreNonnullByDefault
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRand) {
        if ((pRand.nextInt(3) == 0 || this.fewerNeighboursThan(pLevel, pPos, 4)) && this.slightlyMelt(pState, pLevel, pPos)) {
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
            for (Direction direction : Direction.values()) {
                mutablePos.setWithOffset(pPos, direction);
                BlockState blockstate = pLevel.getBlockState(mutablePos);
                if (blockstate.is(this) && !this.slightlyMelt(blockstate, pLevel, mutablePos)) {
                    pLevel.scheduleTick(mutablePos, this, Mth.nextInt(pRand, 20, 40));
                }
            }
        }
        else {
            pLevel.scheduleTick(pPos, this, Mth.nextInt(pRand, 20, 40));
        }
    }

    // Overridden to make the ice turn into air instead of water
    @Override
    @ParametersAreNonnullByDefault
    protected void melt(BlockState pState, Level pLevel, BlockPos pPos) {
        pLevel.removeBlock(pPos, false);
    }

    // Below methods are copied from super class as they are private
    private boolean slightlyMelt(BlockState pState, Level pLevel, BlockPos pPos) {
        int i = pState.getValue(AGE);
        if (i < 3) {
            pLevel.setBlock(pPos, pState.setValue(AGE, i + 1), 2);
            return false;
        }
        else {
            this.melt(pState, pLevel, pPos);
            return true;
        }
    }

    @SuppressWarnings("all")
    private boolean fewerNeighboursThan(BlockGetter pLevel, BlockPos pPos, int pNeighborsRequired) {
        int i = 0;
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
        for (Direction direction : Direction.values()) {
            blockpos$mutable.setWithOffset(pPos, direction);
            if (pLevel.getBlockState(blockpos$mutable).is(this)) {
                ++i;
                if (i >= pNeighborsRequired) {
                    return false;
                }
            }
        }
        return true;
    }
}
