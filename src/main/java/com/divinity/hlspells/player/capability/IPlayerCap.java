package com.divinity.hlspells.player.capability;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffect;

import javax.annotation.Nullable;
import java.util.Map;

public interface IPlayerCap {

    @Nullable
    MobEffect getEffect();

    void setEffect(@Nullable MobEffect effect);

    int getEffectDuration();

    void setEffectDuration(int duration);

    int getEffectAmplifier();

    void setEffectAmplifier(int amplifier);

    void resetEffect();

    Map<Integer, ItemStack> getSoulBondItems();

    void addSoulBondItem(int id, ItemStack stack);
}
