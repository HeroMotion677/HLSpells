package com.divinity.hlspells.items.capabilities.totemcap;

import com.divinity.hlspells.player.capability.IPlayerCap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("all")
public class TotemItemProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<ITotemCap> TOTEM_CAP = CapabilityManager.get(new CapabilityToken<>(){});
    private TotemCap totemCap = null;
    private final LazyOptional<ITotemCap> instance = LazyOptional.of(this::createTotemCap);

    @Nonnull
    private ITotemCap createTotemCap() {
        return totemCap == null ? new TotemCap() : totemCap;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        return cap == TOTEM_CAP ? instance.cast() : LazyOptional.empty();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return getCapability(cap);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (instance.isPresent()) {
            instance.ifPresent(cap -> {
                tag.put("blockPos", NbtUtils.writeBlockPos(cap.getBlockPos()));
                tag.putBoolean("hasDied", cap.getHasDied());
                int hand = 0;
                InteractionHand totemInHand = cap.getTotemInHand();
                if (totemInHand == InteractionHand.MAIN_HAND) {
                    hand = 1;
                } else if (totemInHand == InteractionHand.OFF_HAND) {
                    hand = 2;
                }
                tag.putInt("hand", hand);
                tag.put("playerInv", cap.getInventoryNBT());
                tag.put("curiosInv", cap.getCuriosNBT());
                tag.putInt("curiosSlot", cap.getCuriosSlot());
                tag.putBoolean("curiosDied", cap.diedTotemInCurios());
            });
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (instance.isPresent()) {
            instance.ifPresent(cap -> {
                cap.setBlockPos(NbtUtils.readBlockPos(nbt.getCompound("blockPos")));
                cap.hasDied(nbt.getBoolean("hasDied"));
                int hand = nbt.getInt("hand");
                if (hand == 1) {
                    cap.setTotemInHand(InteractionHand.MAIN_HAND);
                } else if (hand == 2) {
                    cap.setTotemInHand(InteractionHand.OFF_HAND);
                } else {
                    cap.setTotemInHand(null);
                }
                cap.setInventoryNBT(nbt.getList("playerInv", 0));
                cap.setCuriosNBT(nbt.getList("curiosInv", 0));
                cap.setCuriosSlot(nbt.getInt("curiosSlot"));
                cap.setDiedTotemInCurios(nbt.getBoolean("curiosDied"));
            });
        }
    }
}
