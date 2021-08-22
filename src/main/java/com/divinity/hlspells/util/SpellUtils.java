package com.divinity.hlspells.util;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.init.ItemInit;
import com.divinity.hlspells.init.SpellInit;
import com.divinity.hlspells.items.capabilities.spellholdercap.ISpellHolder;
import com.divinity.hlspells.items.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.spell.Spell;
import net.minecraft.entity.player.PlayerEntity;
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

    public static String getSpellName(ItemStack stack) {
        return stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).map(ISpellHolder::getCurrentSpell).orElseGet(() -> SpellInit.EMPTY.get().getRegistryName().toString());
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
        else return item == ItemInit.STAFF.get() && existingSpells.size() < 6;
    }

    public static boolean canUseSpell(PlayerEntity player, Spell spell) {
        return player.isCreative() || !HLSpells.CONFIG.spellsUseXP.get() || player.totalExperience >= spell.getXpCost();
    }
}
