package com.divinity.hlspells.spells;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.init.SpellBookInit;
import com.divinity.hlspells.items.SpellBookItem;
import com.divinity.hlspells.spell.SpellBookObject;
import com.divinity.hlspells.spell.SpellType;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.divinity.hlspells.spells.SpellActions.resetEffects;

@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class RunSpells
{
    public static void doCastSpell(PlayerEntity playerEntity, World world, ItemStack itemStack)
    {
        SpellBookObject spellBook = SpellUtils.getSpellBook(itemStack);

        if (spellBook.isEmpty() || spellBook.containsSpell(spellInstance -> spellInstance.getSpell().getCategory() == SpellType.CAST)) {
            return;
        }

        spellBook.runAction(playerEntity, world);
    }

    @SubscribeEvent
    public void activeSpells(TickEvent.PlayerTickEvent event) {
        if (event.player == null) {
            return;
        }
        if (SpellBookItem.isHeldActive) {
            if (event.player.getMainHandItem().getItem() instanceof SpellBookItem) {
                SpellUtils.getSpellBook(event.player.getMainHandItem()).runAction(event.player, event.player.level);
            }
            if (event.player.getOffhandItem().getItem() instanceof SpellBookItem) {
                SpellUtils.getSpellBook(event.player.getOffhandItem()).runAction(event.player, event.player.level);
            }

        } else {
            resetEffects(event.player);
        }
    }
}
