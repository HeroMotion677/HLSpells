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
    }
}
