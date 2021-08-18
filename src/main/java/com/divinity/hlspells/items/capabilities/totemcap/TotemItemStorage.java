package com.divinity.hlspells.items.capabilities.totemcap;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class TotemItemStorage implements Capability.IStorage<ITotemCap> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<ITotemCap> capability, ITotemCap instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        tag.putDouble("xPos", instance.getXPos());
        tag.putDouble("yPos", instance.getYPos());
        tag.putDouble("zPos", instance.getZPos());
        tag.putBoolean("hasDied", instance.getHasDied());
        int hand = 0;
        Hand totemInHand = instance.getTotemInHand();
        if (totemInHand == Hand.MAIN_HAND) {
            hand = 1;
        } else if (totemInHand == Hand.OFF_HAND) {
            hand = 2;
        }
        tag.putInt("hand", hand);
        return tag;
    }

    @Override
    public void readNBT(Capability<ITotemCap> capability, ITotemCap instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.setXPos(tag.getDouble("xPos"));
        instance.setYPos(tag.getDouble("yPos"));
        instance.setZPos(tag.getDouble("zPos"));
        instance.hasDied(tag.getBoolean("hasDied"));
        int hand = tag.getInt("hand");
        if (hand == 1) {
            instance.setTotemInHand(Hand.MAIN_HAND);
        } else if (hand == 2) {
            instance.setTotemInHand(Hand.OFF_HAND);
        } else {
            instance.setTotemInHand(null);
        }
    }
}
