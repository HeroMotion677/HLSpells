package com.divinity.hlspells.items.capabilities.totemcap;

import net.minecraft.util.Hand;

import javax.annotation.Nullable;

public class TotemCap implements ITotemCap {
    private double xPos;
    private double yPos;
    private double zPos;
    private boolean hasDied;
    private Hand hand;

    public TotemCap() {
        xPos = 0.0D;
        yPos = 0.0D;
        zPos = 0.0D;
        hasDied = false;
        hand = null;
    }

    @Override
    public double getXPos() {
        return this.xPos;
    }

    @Override
    public void setXPos(double xPos) {
        this.xPos = xPos;
    }

    @Override
    public double getYPos() {
        return this.yPos;
    }

    @Override
    public void setYPos(double yPos) {
        this.yPos = yPos;
    }

    @Override
    public double getZPos() {
        return this.zPos;
    }

    @Override
    public void setZPos(double zPos) {
        this.zPos = zPos;
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
}
