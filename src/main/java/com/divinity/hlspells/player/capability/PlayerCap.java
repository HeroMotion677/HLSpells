package com.divinity.hlspells.player.capability;

import com.google.common.collect.Maps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffect;

import java.util.Map;

public class PlayerCap implements IPlayerCap {

    private MobEffect effect;
    private int effectDuration;
    private int effectAmplifier;
    private final Map<Integer, ItemStack> soulBondStacks;

    public PlayerCap () {
        this.effect = null;
        this.effectDuration = 0;
        this.effectAmplifier = 0;
        soulBondStacks = Maps.newHashMap();
    }

    @Override
    public MobEffect getEffect() {
        return this.effect;
    }

    @Override
    public void setEffect(MobEffect effect) {
        this.effect = effect;
    }

    @Override
    public int getEffectDuration() {
        return this.effectDuration;
    }

    @Override
    public void setEffectDuration(int duration) {
        this.effectDuration = duration;
    }

    @Override
    public int getEffectAmplifier() {
        return this.effectAmplifier;
    }

    @Override
    public void setEffectAmplifier(int amplifier) {
        this.effectAmplifier = amplifier;
    }

    @Override
    public void resetEffect () {
        this.effect = null;
        this.effectDuration = 0;
        this.effectAmplifier = 0;
    }

    @Override
    public Map<Integer, ItemStack> getSoulBondItems() {
        return soulBondStacks;
    }

    @Override
    public void addSoulBondItem(int id, ItemStack stack) {
        soulBondStacks.put(id, stack);
    }
}
