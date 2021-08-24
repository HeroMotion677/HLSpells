package com.divinity.hlspells.items.capabilities.totemcap;

import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public interface ITotemCap {

    BlockPos getBlockPos();

    void setBlockPos(BlockPos pos);

    boolean getHasDied();

    void hasDied(boolean value);

    Hand getTotemInHand();

    void setTotemInHand(Hand hand);

    ListNBT getInventoryNBT();

    void setInventoryNBT(ListNBT playerInventory);

    ListNBT getCuriosNBT();

    void setCuriosNBT(ListNBT curiosInv);


    int getCuriosSlot();

    void setCuriosSlot(int curiosSlot);

    boolean diedTotemInCurios();

    void setDiedTotemInCurios(boolean inCurios);
}
