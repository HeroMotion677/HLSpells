package com.divinity.hlspells.items.capabilities.wandcap;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("all")
public class SpellHolderProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(ISpellHolder.class)
    public static final Capability<ISpellHolder> SPELL_HOLDER_CAP = null;

    private LazyOptional<ISpellHolder> instance = LazyOptional.of(SPELL_HOLDER_CAP::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == SPELL_HOLDER_CAP ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return SPELL_HOLDER_CAP.getStorage().writeNBT(SPELL_HOLDER_CAP, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be Empty!")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        SPELL_HOLDER_CAP.getStorage().readNBT(SPELL_HOLDER_CAP, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be Empty!")), null, nbt);
    }
}
