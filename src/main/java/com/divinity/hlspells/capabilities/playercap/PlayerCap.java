package com.divinity.hlspells.capabilities.playercap;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class PlayerCap implements IPlayerCap, INBTSerializable<CompoundTag> {

    private Holder<MobEffect> effect;
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
    public Holder<MobEffect> getEffect() {
        return this.effect;
    }

    @Override
    public void setEffect(Holder<MobEffect> effect) {
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

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        if (this.effect != null) {
            this.effect.unwrapKey().ifPresent(key -> tag.putString("effect", key.location().toString()));
            tag.putInt("effectDuration", this.effectDuration);
            tag.putInt("effectAmplifier", this.effectAmplifier);
        }
        tag.putInt("soulBondItemsSize", this.soulBondStacks.size());
        ListTag slotsNBT = new ListTag();
        ListTag stacksNBT = new ListTag();
        this.soulBondStacks.keySet().forEach(id -> slotsNBT.add(IntTag.valueOf(id)));
        this.soulBondStacks.values().forEach(stack -> stacksNBT.add(stack.save(provider)));
        tag.put("slotIds", slotsNBT);
        tag.put("stacks", stacksNBT);
        tag.putInt("spellTimer", this.spellTimer);
        tag.putInt("spellXpTickCounter", this.xpTickCounter);
        tag.putInt("durabilityTickCounter", this.durabilityTickCounter);
        tag.putBoolean("phasingActive", this.phasingActive);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        if (nbt.contains("effect")) {
            ResourceLocation id = ResourceLocation.tryParse(nbt.getString("effect"));
            if (id != null) {
                BuiltInRegistries.MOB_EFFECT.getHolder(id).ifPresent(this::setEffect);
            }
            this.effectDuration = nbt.getInt("effectDuration");
            this.effectAmplifier = nbt.getInt("effectAmplifier");
        }
        int soulBondItemsSize = nbt.getInt("soulBondItemsSize");
        ListTag slotsNBT = nbt.getList("slotIds", Tag.TAG_INT);
        ListTag stacksNBT = nbt.getList("stacks", Tag.TAG_COMPOUND);
        for (int i = 0; i < soulBondItemsSize; i++) {
            Tag slot = slotsNBT.get(i);
            Tag stack = stacksNBT.get(i);
            if (slot instanceof IntTag intTag && stack instanceof CompoundTag compoundTag) {
                ItemStack.parse(provider, compoundTag).ifPresent(parsed -> addSoulBondItem(intTag.getAsInt(), parsed));
            }
        }
        this.spellTimer = nbt.getInt("spellTimer");
        this.xpTickCounter = nbt.getInt("spellXpTickCounter");
        this.durabilityTickCounter = nbt.getInt("durabilityTickCounter");
        this.phasingActive = nbt.getBoolean("phasingActive");
    }
}
