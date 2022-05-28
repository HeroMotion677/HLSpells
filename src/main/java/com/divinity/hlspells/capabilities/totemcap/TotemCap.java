package com.divinity.hlspells.capabilities.totemcap;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

public class TotemCap implements ITotemCap {

    private boolean hasDied;
    private InteractionHand hand;
    private ListTag playerInventoryNBT;
    private BlockPos blockPos;
    private ListTag curiosNBT;
    private int curiosSlot;
    private boolean curioDied;

    public TotemCap() {
        hasDied = false;
        hand = null;
        playerInventoryNBT = new ListTag();
        blockPos = new BlockPos(0, 0, 0);
        curiosNBT = new ListTag();
        curiosSlot = 0;
    }

    @Override
    public BlockPos getBlockPos() {
        return blockPos;
    }

    @Override
    public void setBlockPos(BlockPos pos) {
        this.blockPos = pos;
    }

    @Override
    public boolean getHasDied() {
        return this.hasDied;
    }

    @Override
    public void hasDied(boolean hasDied) {
        this.hasDied = hasDied;
    }

    @Override
    @Nullable
    public InteractionHand getTotemInHand() {
        return hand;
    }

    @Override
    public void setTotemInHand(@Nullable InteractionHand hand) {
        this.hand = hand;
    }

    @Override
    public ListTag getInventoryNBT() {
        return playerInventoryNBT;
    }

    @Override
    public void setInventoryNBT(ListTag playerInventory) {
        this.playerInventoryNBT = playerInventory;
    }

    @Override
    public ListTag getCuriosNBT() {
        return curiosNBT;
    }

    @Override
    public void setCuriosNBT(ListTag curiosInv) {
        this.curiosNBT = curiosInv;
    }

    @Override
    public int getCuriosSlot() {
        return curiosSlot;
    }

    @Override
    public void setCuriosSlot(int curiosSlot) {
        this.curiosSlot = curiosSlot;
    }

    @Override
    public boolean diedTotemInCurios() {
        return curioDied;
    }

    @Override
    public void setDiedTotemInCurios(boolean inCurios) {
        this.curioDied = inCurios;
    }
}