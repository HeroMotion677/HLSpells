package com.divinity.hlspells.items.capabilities.totemcap;

import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class TotemCap implements ITotemCap {
    private boolean hasDied;
    private Hand hand;
    private ListNBT playerInventoryNBT;
    private BlockPos blockPos;
    private ListNBT curiosNBT;
    private int curiosSlot;
    private boolean curioDied;

    public TotemCap() {
        hasDied = false;
        hand = null;
        playerInventoryNBT = new ListNBT();
        blockPos = new BlockPos(0, 0, 0);
        curiosNBT = new ListNBT();
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
    public Hand getTotemInHand() {
        return hand;
    }

    @Override
    public void setTotemInHand(@Nullable Hand hand) {
        this.hand = hand;
    }

    @Override
    public ListNBT getInventoryNBT() {
        return playerInventoryNBT;
    }

    @Override
    public void setInventoryNBT(ListNBT playerInventory) {
        this.playerInventoryNBT = playerInventory;
    }

    @Override
    public ListNBT getCuriosNBT() {
        return curiosNBT;
    }

    @Override
    public void setCuriosNBT(ListNBT curiosInv) {
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