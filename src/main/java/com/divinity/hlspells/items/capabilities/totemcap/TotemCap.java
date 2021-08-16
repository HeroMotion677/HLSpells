package com.divinity.hlspells.items.capabilities.totemcap;

public class TotemCap implements ITotemCap
{
    private double xPos;
    private double yPos;
    private double zPos;
    private boolean hasDied;

    public TotemCap()
    {
        xPos = 0.0D;
        yPos = 0.0D;
        zPos = 0.0D;
        hasDied = false;
    }

    @Override
    public double getXPos()
    {
        return this.xPos;
    }

    @Override
    public double getYPos()
    {
        return this.yPos;
    }

    @Override
    public double getZPos()
    {
        return this.zPos;
    }

    @Override
    public boolean getHasDied()
    {
        return this.hasDied;
    }

    @Override
    public void hasDied(boolean hasDied)
    {
        this.hasDied = hasDied;
    }

    @Override
    public void setXPos(double xPos)
    {
        this.xPos = xPos;
    }

    @Override
    public void setYPos(double yPos)
    {
        this.yPos = yPos;
    }

    @Override
    public void setZPos(double zPos)
    {
        this.zPos = zPos;
    }
}
