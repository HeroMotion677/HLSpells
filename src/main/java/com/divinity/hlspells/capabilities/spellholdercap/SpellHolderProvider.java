package com.divinity.hlspells.capabilities.spellholdercap;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("all")
public class SpellHolderProvider implements ICapabilitySerializable<CompoundTag> {

    public static final String CURRENT_SPELL_CYCLE_NBT = "currentSpellCycle";
    public static final String SPELL_NBT = "Spell ";
    public static Capability<ISpellHolder> SPELL_HOLDER_CAP = CapabilityManager.get(new CapabilityToken<>(){});
    private SpellHolder spellHolder = null;
    private final LazyOptional<ISpellHolder> instance = LazyOptional.of(this::createSpellCap);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        return cap == SPELL_HOLDER_CAP ? instance.cast() : LazyOptional.empty();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return getCapability(cap);
    }

    @Override
    public CompoundTag serializeNBT() {
        if (instance.isPresent()) {
	        return instance.orElseThrow(RuntimeException::new).serializeNBT();
        }
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (instance.isPresent()) {
            instance.orElseThrow(RuntimeException::new).deserializeNBT(nbt);
        }
    }

    @Nonnull
    private ISpellHolder createSpellCap() {
        return spellHolder == null ? new SpellHolder() : spellHolder;
    }

    @Nullable
    public static ISpellHolder getSpellHolderUnwrap(ItemStack stack) {
        return stack.getCapability(SPELL_HOLDER_CAP).orElse(null);
    }
}