package com.divinity.hlspells.capabilities.totemcap;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("all")
public class TotemItemProvider implements ICapabilitySerializable<CompoundTag> {

    public static Capability<ITotemCap> TOTEM_CAP = CapabilityManager.get(new CapabilityToken<>(){});
    private TotemCap totemCap = null;
    private final LazyOptional<ITotemCap> instance = LazyOptional.of(this::createTotemCap);

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
                tag.putInt("hand", switch (cap.getTotemInHand()) {
                    case MAIN_HAND -> 1;
                    case OFF_HAND -> 2;
                    default -> 0;
                });
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
                cap.setTotemInHand(switch (nbt.getInt("hand")) {
                    case 1 -> InteractionHand.MAIN_HAND;
                    case 2 -> InteractionHand.OFF_HAND;
                    default -> null;
                });
                cap.setInventoryNBT(nbt.getList("playerInv", 0));
                cap.setCuriosNBT(nbt.getList("curiosInv", 0));
                cap.setCuriosSlot(nbt.getInt("curiosSlot"));
                cap.setDiedTotemInCurios(nbt.getBoolean("curiosDied"));
            });
        }
    }

    @Nonnull
    private ITotemCap createTotemCap() {
        return totemCap == null ? new TotemCap() : totemCap;
    }
}
