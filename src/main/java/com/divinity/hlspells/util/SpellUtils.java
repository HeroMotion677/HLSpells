package com.divinity.hlspells.util;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.init.ItemInit;
import com.divinity.hlspells.init.SpellInit;
import com.divinity.hlspells.items.capabilities.spellholdercap.ISpellHolder;
import com.divinity.hlspells.items.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class SpellUtils {

    /**
     * Returns the current active spell if not found return empty
     */
    public static Spell getSpell(ItemStack stack) {
        String id = stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).map(ISpellHolder::getCurrentSpell).orElse(null);
        if (id != null) return getSpellByID(id);
        else return SpellInit.EMPTY.get();
    }

    public static Spell getSpellByID(@Nonnull String id) {
        Spell spell = SpellInit.SPELLS_REGISTRY.get().getValue(new ResourceLocation(id));
        if (spell != null) return spell;
        else return SpellInit.EMPTY.get();
    }

    public static boolean canAddSpell(Item item, List<String> existingSpells) {
        if (item == ItemInit.SPELL_BOOK.get() && existingSpells.isEmpty())
            return true;
        else if (item == ItemInit.WAND.get() && existingSpells.size() < 3)
            return true;
        else return item == ItemInit.STAFF.get() && existingSpells.size() < 6;
    }

    public static boolean checkXpReq(PlayerEntity player, Spell spell) {
        return player.isCreative() || !HLSpells.CONFIG.spellsUseXP.get() || player.totalExperience >= spell.getXpCost();
    }

    public static int getTickDelay(PlayerEntity player, Spell spell) {
        //modify to alter tick delay
        return spell.getTickDelay();
    }

    /**
     * Do own check for configs
     */
    public static void removeXP(PlayerEntity player, Spell spell) {
        int xpToRemove = spell.getXpCost();
        if (spell.getType() == SpellType.CAST) {
            // modify to remove more xp
        }
        player.giveExperiencePoints(-xpToRemove);
    }
}
