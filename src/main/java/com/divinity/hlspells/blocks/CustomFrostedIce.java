package com.divinity.hlspells.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.FrostedIceBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class CustomFrostedIce extends FrostedIceBlock {
    public CustomFrostedIce(Properties properties) {
        super(properties);
    }

    // Overridden to make the ice turn into air instead of water
    @Override
    protected void melt(BlockState pState, World pLevel, BlockPos pPos) {
        pLevel.removeBlock(pPos, false);
    }

    // Overridden to remove the check for light level
    @Override
    public void tick(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRand) {
        if ((pRand.nextInt(3) == 0 || this.fewerNeigboursThan(pLevel, pPos, 4)) && this.slightlyMelt(pState, pLevel, pPos)) {
            BlockPos.Mutable mutablePos = new BlockPos.Mutable();
            for (Direction direction : Direction.values()) {
                mutablePos.setWithOffset(pPos, direction);
                BlockState blockstate = pLevel.getBlockState(mutablePos);
                if (blockstate.is(this) && !this.slightlyMelt(blockstate, pLevel, mutablePos)) {
                    pLevel.getBlockTicks().scheduleTick(mutablePos, this, MathHelper.nextInt(pRand, 20, 40));
                }
            }
        } else {
            pLevel.getBlockTicks().scheduleTick(pPos, this, MathHelper.nextInt(pRand, 20, 40));
        }
    }

    // Below methods are copied from super class as they are private
    private boolean slightlyMelt(BlockState pState, World pLevel, BlockPos pPos) {
        int i = pState.getValue(AGE);
        if (i < 3) {
            pLevel.setBlock(pPos, pState.setValue(AGE, i + 1), 2);
            return false;
        } else {
            this.melt(pState, pLevel, pPos);
            return true;
        }
    }

    private boolean fewerNeigboursThan(IBlockReader pLevel, BlockPos pPos, int pNeighborsRequired) {
        int i = 0;
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
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
