package com.divinity.hlspells.items.capabilities.wandcap;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class SpellHolderStorage implements Capability.IStorage<ISpellHolder> {
    public static final String CURRENT_SPELL_CYCLE_NBT = "currentSpellCycle";
    public static final String SPELL_NBT = "Spell ";
    //TODO Ensures Sync (Temp solution for now, will probably need server -> client packet)
    public static int CURRENT_SPELL_VALUE;

    @Nullable
    @Override
    public INBT writeNBT(Capability<ISpellHolder> capability, ISpellHolder instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("spellsSize", instance.getSpells().size());
        for (int i = 0; i < instance.getSpells().size(); i++) {
            tag.putString(SPELL_NBT + i, instance.getSpells().get(i));
        }
        tag.putInt(CURRENT_SPELL_CYCLE_NBT, instance.getCurrentSpellCycle());
        tag.putBoolean("isHeld", instance.isHeldActive());
        return tag;
    }

    @Override
    public void readNBT(Capability<ISpellHolder> capability, ISpellHolder instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        for (int i = 0; i < tag.getInt("spellsSize"); i++) {
            instance.addSpell(tag.getString(SPELL_NBT + i));
        }
        instance.setCurrentSpellCycle(tag.getInt(CURRENT_SPELL_CYCLE_NBT));
        CURRENT_SPELL_VALUE = tag.getInt(CURRENT_SPELL_CYCLE_NBT);
        instance.setHeldActive(tag.getBoolean("isHeld"));
    }
}
