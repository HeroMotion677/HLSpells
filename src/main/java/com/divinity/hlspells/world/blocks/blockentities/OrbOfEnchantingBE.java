package com.divinity.hlspells.world.blocks.blockentities;

import com.divinity.hlspells.setup.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class OrbOfEnchantingBE extends BlockEntity {

    private int xp = 0;
    private final int MAX_XP = 1400;
    public OrbOfEnchantingBE(BlockPos pPos, BlockState pBlockState) {
        super(BlockInit.ORB_BE.get(), pPos, pBlockState);
    }

    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.xp = pTag.getInt("xp");
    }

    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (!(this.getXP() == 0)) {
            pTag.putInt("xp", this.getXP());
        }
    }

    public int getXP(){
        return this.xp;
    }
    public void addXP(int xp){
        this.xp += xp;
        this.setChanged();
    }
    public void removeXP(int xp){
        this.xp -= xp;
        this.setChanged();
    }
    public boolean isOrbFull(){
        return xp > MAX_XP;
    }

    @Override
    public BlockEntityType<?> getType() {
        return BlockInit.ORB_BE.get();
    }
}
