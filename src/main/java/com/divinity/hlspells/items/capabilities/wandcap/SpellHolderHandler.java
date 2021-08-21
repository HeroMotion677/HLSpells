package com.divinity.hlspells.items.capabilities.wandcap;

import com.divinity.hlspells.init.SpellInit;
import com.divinity.hlspells.items.SpellBookItem;
import com.divinity.hlspells.items.WandItem;
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
        ItemStack wandItem = event.getLeft().getItem() instanceof WandItem ? event.getLeft() : null;
        ItemStack spellBook = event.getRight().getItem() instanceof SpellBookItem ? event.getRight() : null;
        if (wandItem != null && spellBook != null) {
            ItemStack transformedItem = wandItem.copy();
            Spell spell = SpellUtils.getSpell(spellBook);
            if (spell != SpellInit.EMPTY.get()) {
                transformedItem.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP)
                        .ifPresent(wandCap -> {
                            if (SpellUtils.canAddSpell(transformedItem.getItem(), wandCap.getSpells())) {
                                wandCap.addSpell(spell.getRegistryName().toString());
                                event.setCost(15);
                                event.setMaterialCost(1);
                                event.setOutput(transformedItem);
                            }
                        });
            }
        }
    }

}
