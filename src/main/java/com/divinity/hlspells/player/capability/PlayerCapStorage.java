package com.divinity.hlspells.player.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.Effect;
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
        tag.putInt("soulBondItemsSize", instance.getSoulBondItems().size());
        ListNBT slotsNBT = new ListNBT();
        ListNBT stacksNBT = new ListNBT();
        instance.getSoulBondItems().keySet().forEach(id -> slotsNBT.add(IntNBT.valueOf(id)));
        instance.getSoulBondItems().values().forEach(stack -> stacksNBT.add(stack.save(new CompoundNBT())));
        tag.put("slotIds", slotsNBT);
        tag.put("stacks", stacksNBT);
        return tag;
    }

    @Override
    public void readNBT(Capability<IPlayerCap> capability, IPlayerCap instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        int effect = tag.getInt("effect");
        if (effect != 0) {
            instance.setEffect(Effect.byId(tag.getInt("effect")));
            instance.setEffectDuration(tag.getInt("effectDuration"));
            instance.setEffectAmplifier(tag.getInt("effectAmplifier"));
        }
        int soulBondItemsSize = tag.getInt("soulBondItemsSize");
        ListNBT slotsNBT = tag.getList("slotIds", 0);
        ListNBT stacksNBT = tag.getList("stacks", 0);
        for (int i = 0; i < soulBondItemsSize; i++) {
            INBT slot = slotsNBT.get(i);
            INBT stack = stacksNBT.get(i);
            if (slot instanceof IntNBT && stack instanceof CompoundNBT) {
                instance.addSoulBondItem(((IntNBT) slot).getAsInt(), ItemStack.of(((CompoundNBT) stack)));
            }
        }
    }
}
