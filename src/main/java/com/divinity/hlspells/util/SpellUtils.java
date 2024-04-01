package com.divinity.hlspells.util;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.items.spellitems.StaffItem;
import com.divinity.hlspells.setup.init.ItemInit;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.capabilities.spellholdercap.ISpellHolder;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public final class SpellUtils {

    private SpellUtils() {} // No instances of this class should be created

    /**
     * Returns the current active spell if not found return empty
     */
    public static Spell getSpell(ItemStack stack) {
        String id = stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).map(ISpellHolder::getCurrentSpell).orElse(null);
        if (id != null){
            return getSpellByID(id);
        }
        else return SpellInit.EMPTY.get();
    }

    public static Spell getSpellByID(@Nonnull String id) {
        Spell spell = SpellInit.SPELLS_REGISTRY.get().getValue(new ResourceLocation(id));
        if (spell != null) return spell;
        else return SpellInit.EMPTY.get();
    }

    @SuppressWarnings("all")
    public static boolean canAddSpell(ItemStack item, Spell spell) {
        if (item != ItemStack.EMPTY) {
            List<String> existingSpells = SpellHolderProvider.getSpellHolderUnwrap(item).getSpells();
            Spell currentSpell = getSpell(item);
            String currentSpellName = SpellInit.SPELLS_REGISTRY.get().getKey(currentSpell).toString();
            String otherSpellName = SpellInit.SPELLS_REGISTRY.get().getKey(spell).toString();
            boolean canUpgrade = currentSpell.getUpgrade() != null &&
                  currentSpellName.equals(otherSpellName);
            if (!existingSpells.contains(SpellInit.SPELLS_REGISTRY.get().getKey(spell).toString())) {
                if (currentSpell.getUpgradeableSpellPath() != null && currentSpell.getUpgradeableSpellPath() == spell.getUpgradeableSpellPath()) {
                    return false;
                }
                if (item.getItem() == ItemInit.SPELL_BOOK.get() && existingSpells.isEmpty())
                    return true;
                else if (item.getItem() == ItemInit.WAND.get() && existingSpells.size() < 3)
                    return true;
                else if (item.getItem() == ItemInit.AMETHYST_WAND.get() && existingSpells.size() < 3)
                    return true;
                else return item.getItem() instanceof StaffItem && existingSpells.size() < 6;
            }
            else {
                return canUpgrade;
            }
        }
        return false;
    }

    public static boolean checkXpReq(Player player, Spell spell) {
        return player.isCreative() || !HLSpells.CONFIG.spellsUseXP.get() || player.totalExperience >= getXpReq(player, spell);
    }

    public static int getTickDelay(Player player, Spell spell) {
        int tickDelay = spell.getTickDelay();
        if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() == ItemInit.WIZARD_HAT.get()) {
            tickDelay += 4;
        }
        return tickDelay;
    }

    public static int getXpReq(Player player, Spell spell) {
        int xpToRemove = spell.getXpCost();
        if (spell.getSpellType() == SpellAttributes.Type.CAST && player.getItemBySlot(EquipmentSlot.HEAD).getItem() == ItemInit.WIZARD_HAT.get()) {
            xpToRemove *= 0.7;
        }
        return xpToRemove;
    }
}
