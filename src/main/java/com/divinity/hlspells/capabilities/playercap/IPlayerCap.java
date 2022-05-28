package com.divinity.hlspells.capabilities.playercap;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffect;

import javax.annotation.Nullable;
import java.util.Map;

public interface IPlayerCap {

    @Nullable MobEffect getEffect();

    void setEffect(@Nullable MobEffect effect);

    int getEffectDuration();

    void setEffectDuration(int duration);

    int getEffectAmplifier();

    void setEffectAmplifier(int amplifier);

    void resetEffect();

    Map<Integer, ItemStack> getSoulBondItems();

    void addSoulBondItem(int id, ItemStack stack);

    int getSpellTimer();

    void setSpellTimer(int spellTimer);

    int getSpellXpTickCounter();

    void setSpellXpTickCounter(int xpTickCounter);

    int getDurabilityTickCounter();

    void setDurabilityTickCounter(int durabilityTickCounter);
}
