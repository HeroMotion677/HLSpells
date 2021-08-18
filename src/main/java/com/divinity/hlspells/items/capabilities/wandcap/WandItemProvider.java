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
public class WandItemProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(IWandCap.class)
    public static final Capability<IWandCap> WAND_CAP = null;

    private LazyOptional<IWandCap> instance = LazyOptional.of(WAND_CAP::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == WAND_CAP ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return WAND_CAP.getStorage().writeNBT(WAND_CAP, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be Empty!")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        WAND_CAP.getStorage().readNBT(WAND_CAP, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be Empty!")), null, nbt);
    }
}
