package com.divinity.hlspells.items.capabilities;

import com.divinity.hlspells.spell.Spell;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.List;

public class WandItemStorage implements Capability.IStorage<IWandCap>
{
    @Nullable
    @Override
    public INBT writeNBT(Capability<IWandCap> capability, IWandCap instance, Direction side)
    {
        CompoundNBT tag = new CompoundNBT();
        instance.getSpells().forEach(s -> tag.putString(s, s));
        return tag;
    }

    @Override
    public void readNBT(Capability<IWandCap> capability, IWandCap instance, Direction side, INBT nbt)
    {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.getSpells().forEach(s -> instance.addSpell(tag.getString(s)));
    }
}
