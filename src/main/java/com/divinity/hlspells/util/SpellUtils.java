package com.divinity.hlspells.util;

import com.divinity.hlspells.init.SpellInit;
import com.divinity.hlspells.spell.Spell;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class SpellUtils {

    public static Spell getSpell(ItemStack stack) {
        CompoundNBT nbt = stack.getTag();
        if (nbt != null) {
            return getSpellByID(nbt.getString("SpellBook"));
        } else {
            return SpellInit.EMPTY.get();
        }
    }

    public static ItemStack setSpell(ItemStack stack, Spell spell) {
        ResourceLocation resourcelocation = SpellInit.SPELLS_REGISTRY.get().getKey(spell);
        if (spell == SpellInit.EMPTY.get()) {
            stack.removeTagKey("SpellBook");
        } else if (resourcelocation != null) {
            stack.getOrCreateTag().putString("SpellBook", resourcelocation.toString());
        }
        return stack;
    }

    @Nullable
    public static Spell getSpellByID(String id) {
        return SpellInit.SPELLS_REGISTRY.get().getValue(new ResourceLocation(id));
    }
}
