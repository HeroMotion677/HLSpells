package com.divinity.hlspells.player.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class PlayerCapStorage implements Capability.IStorage<IPlayerCap> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<IPlayerCap> capability, IPlayerCap instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        if (instance.getEffect() != null) {
            tag.putInt("effect", Effect.getId(instance.getEffect()));
            tag.putInt("effectDuration", instance.getEffectDuration());
            tag.putInt("effectAmplifier", instance.getEffectAmplifier());
        }
        if (instance.getSoulBondInventoryNBT() != null) {
            tag.put("playerSoulBondInv", instance.getSoulBondInventoryNBT());
        }
        return tag;
    }

    @Override
    public void readNBT(Capability<IPlayerCap> capability, IPlayerCap instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        int effect = tag.getInt("effect");
        ListNBT soulBondNbt = tag.getList("playerSoulBondInv", 0);
        if (effect != 0) {
            instance.setEffect(Effect.byId(tag.getInt("effect")));
            instance.setEffectDuration(tag.getInt("effectDuration"));
            instance.setEffectAmplifier(tag.getInt("effectAmplifier"));
        }
        if (!soulBondNbt.isEmpty()) {
            instance.setSoulBondInventoryNBT(soulBondNbt);
        }
    }
}
