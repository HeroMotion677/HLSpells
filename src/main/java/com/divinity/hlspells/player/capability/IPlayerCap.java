package com.divinity.hlspells.player.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;

import javax.annotation.Nullable;
import java.util.Map;

public interface IPlayerCap {

    @Nullable
    Effect getEffect();

    void setEffect(@Nullable Effect effect);

    int getEffectDuration();

    void setEffectDuration(int duration);

    int getEffectAmplifier();

    void setEffectAmplifier(int amplifier);

    void resetEffect();

    Map<Integer, ItemStack> getSoulBondItems();

    void addSoulBondItem(int id, ItemStack stack);
}
