package com.divinity.hlspells.util;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.init.ItemInit;
import com.divinity.hlspells.init.SpellInit;
import com.divinity.hlspells.items.capabilities.spellholdercap.ISpellHolder;
import com.divinity.hlspells.items.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

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

    public static boolean checkXpReq(Player player, Spell spell) {
        return player.isCreative() || !HLSpells.CONFIG.spellsUseXP.get() || player.totalExperience >= getXpReq(player, spell);
    }

    public static int getTickDelay(Player player, Spell spell) {
        int tickDelay = spell.getTickDelay();
        if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() == ItemInit.WIZARD_HAT.get()) {
            tickDelay = tickDelay + 4;
        }
        return tickDelay;
    }

    public static int getXpReq(Player player, Spell spell) {
        int xpToRemove = spell.getXpCost();
        if (spell.getType() == SpellType.CAST && player.getItemBySlot(EquipmentSlot.HEAD).getItem() == ItemInit.WIZARD_HAT.get()) {
            xpToRemove *= 0.7;
        }
        return xpToRemove;
    }
}
