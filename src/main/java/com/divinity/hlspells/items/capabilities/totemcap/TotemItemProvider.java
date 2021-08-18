package com.divinity.hlspells.items.capabilities.totemcap;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("all")
public class TotemItemProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(ITotemCap.class)
    public static final Capability<ITotemCap> TOTEM_CAP = null;

    private LazyOptional<ITotemCap> instance = LazyOptional.of(TOTEM_CAP::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == TOTEM_CAP ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return TOTEM_CAP.getStorage().writeNBT(TOTEM_CAP, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be Empty!")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        TOTEM_CAP.getStorage().readNBT(TOTEM_CAP, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be Empty!")), null, nbt);
    }
}
