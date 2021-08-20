package com.divinity.hlspells.util;

import com.divinity.hlspells.init.ItemInit;
import com.divinity.hlspells.init.SpellInit;
import com.divinity.hlspells.items.capabilities.wandcap.ISpellHolder;
import com.divinity.hlspells.items.capabilities.wandcap.SpellHolderProvider;
import com.divinity.hlspells.spell.Spell;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;

public class SpellUtils {

    public static Spell getSpell(ItemStack stack) {
        String id = stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).map(ISpellHolder::getCurrentSpell).orElse(null);
        if (id != null) return getSpellByID(id);
        else return SpellInit.EMPTY.get();
    }

    @Nullable
    public static Spell getSpellByID(String id) {
        return SpellInit.SPELLS_REGISTRY.get().getValue(new ResourceLocation(id));
    }

    public static boolean canAddSpell(Item item, List<String> existingSpells) {
        if (item == ItemInit.SPELL_BOOK.get() && existingSpells.isEmpty())
            return true;
        else if (item == ItemInit.WAND.get() && existingSpells.size() < 3)
            return true;
        return false;
    }
}
