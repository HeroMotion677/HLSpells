package com.divinity.hlspells.items.capabilities.totemcap;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class TotemItemStorage implements Capability.IStorage<ITotemCap> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<ITotemCap> capability, ITotemCap instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        tag.put("blockPos", NBTUtil.writeBlockPos(instance.getBlockPos()));
        tag.putBoolean("hasDied", instance.getHasDied());
        int hand = 0;
        Hand totemInHand = instance.getTotemInHand();
        if (totemInHand == Hand.MAIN_HAND) {
            hand = 1;
        } else if (totemInHand == Hand.OFF_HAND) {
            hand = 2;
        }
        tag.putInt("hand", hand);
        tag.put("playerInv", instance.getInventoryNBT());
        tag.put("curiosInv", instance.getCuriosNBT());
        tag.putInt("curiosSlot", instance.getCuriosSlot());
        tag.putBoolean("curiosDied", instance.diedTotemInCurios());
        return tag;
    }

    @Override
    public void readNBT(Capability<ITotemCap> capability, ITotemCap instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.setBlockPos(NBTUtil.readBlockPos(tag.getCompound("blockPos")));
        instance.hasDied(tag.getBoolean("hasDied"));
        int hand = tag.getInt("hand");
        if (hand == 1) {
            instance.setTotemInHand(Hand.MAIN_HAND);
        } else if (hand == 2) {
            instance.setTotemInHand(Hand.OFF_HAND);
        } else {
            instance.setTotemInHand(null);
        }
        instance.setInventoryNBT(tag.getList("playerInv", 0));
        instance.setCuriosNBT(tag.getList("curiosInv", 0));
        instance.setCuriosSlot(tag.getInt("curiosSlot"));
        instance.setDiedTotemInCurios(tag.getBoolean("curiosDied"));
    }
}