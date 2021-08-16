package com.divinity.hlspells.items.capabilities.wandcap;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class WandItemStorage implements Capability.IStorage<IWandCap>
{
    public static int CURRENT_SPELL_VALUE;

    @Nullable
    @Override
    public INBT writeNBT(Capability<IWandCap> capability, IWandCap instance, Direction side)
    {
        CompoundNBT tag = new CompoundNBT();
        for (int i = 0; i < instance.getSpells().size(); i++)
        {
            tag.putString("Spell " + i, instance.getSpells().get(i));
        }
        tag.putInt("currentSpellCycle", instance.getCurrentSpellCycle());
        return tag;
    }

    @Override
    public void readNBT(Capability<IWandCap> capability, IWandCap instance, Direction side, INBT nbt)
    {
        CompoundNBT tag = (CompoundNBT) nbt;
        for (int i = 0; i < 3; i++)
        {
            if (!tag.getString("Spell " + i).equals(""))
            {
                instance.addSpell(tag.getString("Spell " + i));
            }
        }
        instance.setCurrentSpellCycle(tag.getInt("currentSpellCycle"));
        CURRENT_SPELL_VALUE = tag.getInt("currentSpellCycle");
    }
}
