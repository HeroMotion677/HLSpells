package com.divinity.hlspells.spells;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.init.SpellInit;
import com.divinity.hlspells.items.SpellBookItem;
import com.divinity.hlspells.items.WandItem;
import com.divinity.hlspells.items.capabilities.WandItemProvider;
import com.divinity.hlspells.spell.SpellBookObject;
import com.divinity.hlspells.spell.SpellType;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.world.World;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.divinity.hlspells.spells.SpellActions.resetEffects;

@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class RunSpells
{
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
                        .ifPresent(p -> p.addSpell(book.getSpells().stream().filter(pr -> pr.getSpell().getRegistryName() != null).map(m -> m.getSpell().getRegistryName().toString())
                        .findFirst()
                        .orElse("null")));
                event.setCost(1);
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
            try {
                itemStack.getCapability(WandItemProvider.WAND_CAP, null)
                        .filter(iWandCap -> iWandCap.getCurrentSpellCycle() <= iWandCap.getSpells().size())
                        .ifPresent(cap -> {
                            SpellInit.SPELLS_DEFERRED_REGISTER.getEntries().stream()
                                    .filter(f -> f.get().getRegistryName() != null)
                                    .filter(f -> cap.containsSpell(f.get().getRegistryName().toString()))
                                    .filter(f -> cap.getSpells().get(cap.getCurrentSpellCycle()).equals(f.get().getRegistryName().toString()))
                                    .filter(f -> f.get().getCategory() == SpellType.CAST)
                                    .forEach(p -> p.get().getSpellAction().accept(playerEntity, world));
                        });
            } catch (Exception e) {
                e.printStackTrace();
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

        else
        {
            resetEffects(player);
        }
    }
}