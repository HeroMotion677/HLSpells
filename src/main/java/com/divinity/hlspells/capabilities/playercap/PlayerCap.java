package com.divinity.hlspells.capabilities.playercap;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffect;

import java.util.HashMap;
import java.util.Map;

public class PlayerCap implements IPlayerCap {

    private MobEffect effect;
    private int effectDuration;
    private int effectAmplifier;
    private final Map<Integer, ItemStack> soulBondStacks;
    private int spellTimer;
    private int xpTickCounter;
    private int durabilityTickCounter;
    private boolean phasingActive;

    public PlayerCap() {
        this.effect = null;
        this.effectDuration = 0;
        this.effectAmplifier = 0;
        this.soulBondStacks = new HashMap<>();
        this.spellTimer = 0;
        this.xpTickCounter = 0;
        this.durabilityTickCounter = 0;
        this.phasingActive = false;
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

    @Override
    public int getSpellTimer() {
        return this.spellTimer;
    }

    @Override
    public void setSpellTimer(int spellTimer) {
        this.spellTimer = spellTimer;
    }

    @Override
    public int getSpellXpTickCounter() {
        return this.xpTickCounter;
    }

    @Override
    public void setSpellXpTickCounter(int xpTickCounter) {
        this.xpTickCounter = xpTickCounter;
    }

    @Override
    public int getDurabilityTickCounter() {
        return this.durabilityTickCounter;
    }

    @Override
    public void setDurabilityTickCounter(int durabilityTickCounter) {
        this.durabilityTickCounter = durabilityTickCounter;
    }

    @Override
    public boolean getPhasingActive() {
        return this.phasingActive;
    }

    @Override
    public void setPhasingActive(boolean phasingActive) {
        this.phasingActive = phasingActive;
    }
}
