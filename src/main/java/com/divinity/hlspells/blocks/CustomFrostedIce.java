package com.divinity.hlspells.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.FrostedIceBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CustomFrostedIce extends FrostedIceBlock {
    public CustomFrostedIce(Properties p_i48394_1_) {
        super(p_i48394_1_);
    }

    @Override
    protected void melt(BlockState pState, World pLevel, BlockPos pPos) {
        pLevel.removeBlock(pPos, false);
    }
}
