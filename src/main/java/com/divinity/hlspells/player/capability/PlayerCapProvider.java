package com.divinity.hlspells.player.capability;

import com.divinity.hlspells.items.capabilities.totemcap.ITotemCap;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerCapProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject(IPlayerCap.class)
    public static final Capability<IPlayerCap> PLAYER_CAP = null;

    private LazyOptional<IPlayerCap> instance = LazyOptional.of(PLAYER_CAP::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == PLAYER_CAP ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return PLAYER_CAP.getStorage().writeNBT(PLAYER_CAP, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be Empty!")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        PLAYER_CAP.getStorage().readNBT(PLAYER_CAP, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be Empty!")), null, nbt);
    }
}
