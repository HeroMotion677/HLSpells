package com.divinity.hlspells.player.capability;

import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

import javax.annotation.Nullable;

public interface IPlayerCap {

    @Nullable
    Effect getEffect();

    void setEffect(@Nullable Effect effect);

    int getEffectDuration ();

    void setEffectDuration (int duration);

    int getEffectAmplifier();

    void setEffectAmplifier(int amplifier);

    void resetEffect();

    @Nullable
    ListNBT getSoulBondInventoryNBT();

    void setSoulBondInventoryNBT(@Nullable ListNBT playerInventory);
}
