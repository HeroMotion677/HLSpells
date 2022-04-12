package com.divinity.hlspells.items.capabilities.spellholdercap;

//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.Tag;
//import net.minecraft.core.Direction;
//import net.minecraftforge.common.capabilities.Capability;
//
//import javax.annotation.Nullable;
//
//public class SpellHolderStorage implements Capability.IStorage<ISpellHolder> {
//    public static final String CURRENT_SPELL_CYCLE_NBT = "currentSpellCycle";
//    public static final String SPELL_NBT = "Spell ";
//
//    @Nullable
//    @Override
//    public Tag writeNBT(Capability<ISpellHolder> capability, ISpellHolder instance, Direction side) {
//        CompoundTag tag = new CompoundTag();
//        tag.putInt("spellsSize", instance.getSpells().size());
//        if (instance.getSpells() != null) {
//            for (int i = 0; i < instance.getSpells().size(); i++) {
//                tag.putString(SPELL_NBT + i, instance.getSpells().get(i));
//            }
//        }
//        tag.putInt(CURRENT_SPELL_CYCLE_NBT, instance.getCurrentSpellCycle());
//        tag.putBoolean("isHeld", instance.isHeldActive());
//        return tag;
//    }
//
//    @Override
//    public void readNBT(Capability<ISpellHolder> capability, ISpellHolder instance, Direction side, Tag nbt) {
//        CompoundTag tag = (CompoundTag) nbt;
//        for (int i = 0; i < tag.getInt("spellsSize"); i++) {
//            instance.addSpell(tag.getString(SPELL_NBT + i));
//        }
//        instance.setCurrentSpellCycle(tag.getInt(CURRENT_SPELL_CYCLE_NBT));
//        instance.setHeldActive(tag.getBoolean("isHeld"));
//    }
//}
