package com.divinity.hlspells.spells;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.init.SpellInit;
import com.divinity.hlspells.items.SpellBookItem;
import com.divinity.hlspells.items.WandItem;
import com.divinity.hlspells.items.capabilities.WandItemProvider;
import com.divinity.hlspells.items.capabilities.WandItemStorage;
import com.divinity.hlspells.network.packets.WandInputPacket;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellBookObject;
import com.divinity.hlspells.spell.SpellInstance;
import com.divinity.hlspells.spell.SpellType;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;


import static com.divinity.hlspells.spells.SpellActions.resetEffects;
import static com.divinity.hlspells.items.capabilities.WandItemStorage.*;

@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class RunSpells
{
    // TODO: Move this to WandCapHandler.java
    @SubscribeEvent
    public static void anvilUpdateEvent (AnvilUpdateEvent event)
    {
        if (event != null)
        {
            ItemStack wandItem = event.getLeft().getItem() instanceof WandItem ? event.getLeft() : null;
            ItemStack spellBook = event.getRight().getItem() instanceof SpellBookItem ? event.getRight() : null;
            if (wandItem != null && spellBook != null)
            {
                ItemStack transformedItem = wandItem.copy();
                SpellBookObject book = SpellUtils.getSpellBook(spellBook);
                transformedItem.getCapability(WandItemProvider.WAND_CAP, null)
                        .ifPresent(p -> p.addSpell(book.getSpells().stream()
                                .filter(pr -> pr.getSpell().getRegistryName() != null)
                                .map(m -> m.getSpell().getRegistryName().toString())
                                .findFirst()
                                .orElse("null")));
                event.setCost(15);
                event.setMaterialCost(1);
                event.setOutput(transformedItem);
            }
        }
    }

    public static void doCastSpell(PlayerEntity playerEntity, World world, ItemStack itemStack)
    {
        if (!(itemStack.getItem() instanceof WandItem))
        {
            SpellBookObject spellBook = SpellUtils.getSpellBook(itemStack);
            if (spellBook.isEmpty() || spellBook.containsSpell(spellInstance -> spellInstance.getSpell().getCategory() != SpellType.CAST)) return;
            spellBook.runAction(playerEntity, world);
        }

        else if (itemStack.getItem() instanceof WandItem)
        {
            for (RegistryObject<Spell> spell : SpellInit.SPELLS_DEFERRED_REGISTER.getEntries())
            {
                itemStack.getCapability(WandItemProvider.WAND_CAP, null)
                        .filter(iWandCap -> iWandCap.getSpells().size() > 0)
                        .ifPresent(cap -> {
                            ResourceLocation spellLocation = spell.get().getRegistryName();
                            if (spellLocation != null && cap.getSpells().get(cap.getCurrentSpellCycle()).equals(spellLocation.toString()))
                            {
                                if (spell.get().getCategory() == SpellType.CAST)
                                {
                                    spell.get().getSpellAction().accept(playerEntity, playerEntity.level);
                                }
                            }
                        });
            }
        }
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


        else if (WandItem.isWandHeldActive)
        {
            ItemStack playerItem = player.getItemInHand(player.getUsedItemHand());
            for (RegistryObject<Spell> spell : SpellInit.SPELLS_DEFERRED_REGISTER.getEntries())
            {
                playerItem.getCapability(WandItemProvider.WAND_CAP, null)
                        .filter(iWandCap -> iWandCap.getSpells().size() > 0)
                        .ifPresent(cap -> {
                            cap.setCurrentSpellCycle(CURRENT_SPELL_VALUE);
                            ResourceLocation spellLocation = spell.get().getRegistryName();
                            if (spellLocation != null && cap.getSpells().get(cap.getCurrentSpellCycle()).equals(spellLocation.toString()))
                            {
                                if (spell.get().getCategory() == SpellType.HELD)
                                {
                                    spell.get().getSpellAction().accept(player, player.level);
                                }
                            }
                        });
            }
        }

        else
        {
            // TODO: Create individual effect instances and clear them instead of clearing any spell instance of that type on the player
            resetEffects(player);
        }
    }
}