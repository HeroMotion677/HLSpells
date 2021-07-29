package com.divinity.hlspells.spells;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.items.SpellBookItem;
import com.divinity.hlspells.spell.SpellBookObject;
import com.divinity.hlspells.spell.SpellType;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.divinity.hlspells.spells.SpellActions.resetEffects;

@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class RunSpells
{
    public static void doCastSpell(PlayerEntity playerEntity, World world, ItemStack itemStack)
    {
        SpellBookObject spellBook = SpellUtils.getSpellBook(itemStack);
        if (spellBook.isEmpty() || spellBook.containsSpell(spellInstance -> spellInstance.getSpell().getCategory() != SpellType.CAST)) return;
        spellBook.runAction(playerEntity, world);
    }

    @SubscribeEvent
    public static void doHeldSpell(TickEvent.PlayerTickEvent event)
    {
        if (event.player == null) return;

        PlayerEntity player = event.player;

        if (SpellBookItem.isHeldActive)
        {
            ItemStack playerItem = player.getItemInHand(player.getUsedItemHand());
            boolean mainPredicate = SpellUtils.getSpellBook(playerItem).containsSpell(sI -> sI.getSpell().getCategory() == SpellType.HELD);

            if (mainPredicate)
                SpellUtils.getSpellBook(playerItem).runAction(player, player.level);
        }

        else
        {
            resetEffects(player);
        }
    }
}