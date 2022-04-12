package com.divinity.hlspells.items.capabilities.totemcap;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;

public interface ITotemCap {

    BlockPos getBlockPos();

    void setBlockPos(BlockPos pos);

    boolean getHasDied();

    void hasDied(boolean value);

    InteractionHand getTotemInHand();

    void setTotemInHand(InteractionHand hand);

    ListTag getInventoryNBT();

    void setInventoryNBT(ListTag playerInventory);

    ListTag getCuriosNBT();

    void setCuriosNBT(ListTag curiosInv);

    int getCuriosSlot();

    void setCuriosSlot(int curiosSlot);

    boolean diedTotemInCurios();

    void setDiedTotemInCurios(boolean inCurios);
}
