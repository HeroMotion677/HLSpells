package com.divinity.hlspells.items.capabilities.totemcap;

public interface ITotemCap
{
    double getXPos();

    double getYPos();

    double getZPos();

    boolean getHasDied();

    void hasDied(boolean value);

    void setXPos(double xPos);

    void setYPos(double yPos);

    void setZPos(double zPos);
}
