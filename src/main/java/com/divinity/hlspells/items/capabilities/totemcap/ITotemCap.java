package com.divinity.hlspells.items.capabilities.totemcap;

import net.minecraft.util.Hand;

public interface ITotemCap {
    double getXPos();

    void setXPos(double xPos);

    double getYPos();

    void setYPos(double yPos);

    double getZPos();

    void setZPos(double zPos);

    boolean getHasDied();

    void hasDied(boolean value);

    Hand getTotemInHand();

    void setTotemInHand(Hand hand);
}
