package com.divinity.hlspells.player.capability;

import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

import javax.annotation.Nullable;
import java.util.Objects;

public class PlayerCap implements IPlayerCap {

    private Effect effect;
    private int effectDuration;
    private int effectAmplifier;
    private ListNBT nbt;

    public PlayerCap () {
        this.effect = null;
        this.effectDuration = 0;
        this.effectAmplifier = 0;
        nbt = null;
    }

    @Override
    public Effect getEffect() {
        return this.effect;
    }

    @Override
    public void setEffect(Effect effect) {
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
    @Nullable
    public ListNBT getSoulBondInventoryNBT() {
        return this.nbt;
    }

    @Override
    public void setSoulBondInventoryNBT(@Nullable ListNBT nbt) {
        this.nbt = nbt;
    }
}
