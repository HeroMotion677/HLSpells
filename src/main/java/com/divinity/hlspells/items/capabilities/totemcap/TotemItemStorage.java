package com.divinity.hlspells.items.capabilities.totemcap;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
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
        return tag;
    }

    @Override
    public void readNBT(Capability<ITotemCap> capability, ITotemCap instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.setXPos(tag.getDouble("xPos"));
        instance.setYPos(tag.getDouble("yPos"));
        instance.setZPos(tag.getDouble("zPos"));
        instance.hasDied(tag.getBoolean("hasDied"));
    }
}
