package com.divinity.hlspells.items.capabilities.spellholdercap;

import com.divinity.hlspells.init.ItemInit;
import com.divinity.hlspells.init.SpellInit;
import com.divinity.hlspells.items.SpellHoldingItem;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.divinity.hlspells.HLSpells.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class SpellHolderHandler {

    @SubscribeEvent
    public static void anvilUpdateEvent(AnvilUpdateEvent event) {
        ItemStack wandItem = event.getLeft().getItem() instanceof SpellHoldingItem && ((SpellHoldingItem) event.getLeft().getItem()).isWand() ? event.getLeft() : null;
        ItemStack spellBook = event.getRight().getItem() == ItemInit.SPELL_BOOK.get() ? event.getRight() : null;
        if (wandItem != null && spellBook != null) {
            ItemStack transformedItem = wandItem.copy();
            Spell spell = SpellUtils.getSpell(spellBook);
            if (spell != SpellInit.EMPTY.get()) {
                transformedItem.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(wandCap -> {
                    if (SpellUtils.canAddSpell(transformedItem.getItem(), wandCap.getSpells())) {
                        wandCap.addSpell(spell.getRegistryName().toString());
                        event.setCost(15);
                        event.setOutput(transformedItem);
                    } else {
                        int currentSpellId = wandCap.getCurrentSpellCycle();
                        String currentSpell = wandCap.getCurrentSpell();
                        wandCap.getSpells().add(currentSpellId, spell.getRegistryName().toString());
                        wandCap.removeSpell(currentSpell);
                        event.setCost(15);
                        event.setOutput(transformedItem);
                    }
                });
            }
        }
    }
}