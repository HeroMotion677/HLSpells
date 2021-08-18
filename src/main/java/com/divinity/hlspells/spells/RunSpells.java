package com.divinity.hlspells.spells;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.init.SpellBookInit;
import com.divinity.hlspells.items.SpellBookItem;
import com.divinity.hlspells.items.WandItem;
import com.divinity.hlspells.items.capabilities.wandcap.WandItemProvider;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellBookObject;
import com.divinity.hlspells.spell.SpellType;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.divinity.hlspells.items.capabilities.wandcap.WandItemStorage.CURRENT_SPELL_VALUE;
import static com.divinity.hlspells.spells.SpellActions.resetEffects;

@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class RunSpells {
    static int placeHolder;
    static int placeHolderWand;

    // TODO: Move this to WandCapHandler.java
    @SubscribeEvent
    public static void anvilUpdateEvent(AnvilUpdateEvent event) {
        if (event != null) {
            ItemStack wandItem = event.getLeft().getItem() instanceof WandItem ? event.getLeft() : null;
            ItemStack spellBook = event.getRight().getItem() instanceof SpellBookItem ? event.getRight() : null;
            if (wandItem != null && spellBook != null) {
                ItemStack transformedItem = wandItem.copy();
                if (SpellUtils.getSpellBook(spellBook) != SpellBookInit.EMPTY.get()) {
                    SpellBookObject book = SpellUtils.getSpellBook(spellBook);
                    transformedItem.getCapability(WandItemProvider.WAND_CAP, null)
                            .ifPresent(p -> p.addSpell(book.getSpells().stream()
                                    .filter(pr -> pr.getSpell().getRegistryName() != null)
                                    .map(m -> m.getSpell().getRegistryName().toString())
                                    .findFirst()
                                    .orElse("null")));
                }
                event.setCost(15);
                event.setMaterialCost(1);
                event.setOutput(transformedItem);
            }
        }
    }

    public static void doCastSpell(PlayerEntity player, World world, ItemStack itemStack) {
        if (itemStack.getItem() instanceof WandItem) {
            itemStack.getCapability(WandItemProvider.WAND_CAP, null)
                    .filter(cap -> !cap.getSpells().isEmpty())
                    .ifPresent(cap -> {
                        Spell spell = Spell.byId(cap.getCurrentSpell());
                        // Ensures Sync (Temp solution for now, will probably need server -> client packet)
                        cap.setCurrentSpellCycle(CURRENT_SPELL_VALUE);
                        if (spell != null && spell.getType() == SpellType.CAST && spell.hasCost()) {
                            if (player.totalExperience >= spell.getXpCost() || player.isCreative()) {
                                spell.getSpellAction().accept(player, player.level);
                                player.giveExperiencePoints(-spell.getXpCost());
                            }
                        }
                    });
        } else if (itemStack.getItem() instanceof SpellBookItem) {
            SpellBookObject spellBook = SpellUtils.getSpellBook(itemStack);
            if (spellBook.isEmpty() || spellBook.containsSpell(spellInstance -> spellInstance.getSpell().getType() != SpellType.CAST))
                return;
            if (spellBook.getSingletonSpell() != null && (player.isCreative() || (spellBook.getSingletonSpell().hasCost() && player.totalExperience >= spellBook.getSingletonSpell().getXpCost()))) {
                spellBook.runAction(player, world);
                player.giveExperiencePoints(-spellBook.getSingletonSpell().getXpCost());
            }
        }
    }

    @SubscribeEvent
    public static void doHeldSpell(TickEvent.PlayerTickEvent event) {
        if (!event.player.isAlive()) return;

        PlayerEntity player = event.player;
        if (SpellBookItem.isHeldActive) {
            Hand playerHand = player.getUsedItemHand();
            if (playerHand == Hand.MAIN_HAND || playerHand == Hand.OFF_HAND) {
                ItemStack playerItem = player.getItemInHand(playerHand);
                SpellBookObject spellBook = SpellUtils.getSpellBook(playerItem);
                boolean mainPredicate = spellBook.containsSpell(sI -> sI.getSpell().getType() == SpellType.HELD);

                if (mainPredicate && spellBook.getSingletonSpell() != null && spellBook.getSingletonSpell().hasCost()) {
                    if (player.isCreative() || (spellBook.getSingletonSpell().hasCost() && player.totalExperience >= spellBook.getSingletonSpell().getXpCost())) {
                        spellBook.runAction(player, player.level);
                        placeHolder++;
                        if (placeHolder == spellBook.getSingletonSpell().getTickDelay()) {
                            player.giveExperiencePoints(-spellBook.getSingletonSpell().getXpCost());
                            placeHolder = 0;
                        }
                    }
                }
            } else {
                SpellBookItem.isHeldActive = false;
                placeHolder = 0;
            }
        } else if (WandItem.isWandHeldActive) {
            Hand playerHand = player.getUsedItemHand();
            if (playerHand == Hand.MAIN_HAND || playerHand == Hand.OFF_HAND) {
                ItemStack playerItem = player.getItemInHand(playerHand);
                playerItem.getCapability(WandItemProvider.WAND_CAP, null)
                        .filter(iWandCap -> !iWandCap.getSpells().isEmpty()).ifPresent(cap -> {
                    Spell spell = Spell.byId(cap.getCurrentSpell());
                    cap.setCurrentSpellCycle(CURRENT_SPELL_VALUE); // Ensures Sync (Temp solution for now, will probably need server -> client packet)
                    if (spell != null && spell.getType() == SpellType.HELD && spell.hasCost()) {
                        if (player.totalExperience >= spell.getXpCost() || player.isCreative()) {
                            spell.getSpellAction().accept(player, player.level);
                            placeHolderWand++;
                            if (placeHolderWand == spell.getTickDelay()) {
                                player.giveExperiencePoints(-spell.getXpCost());
                                placeHolderWand = 0;
                            }
                        }
                    }
                });
            } else {
                WandItem.isWandHeldActive = false;
                placeHolderWand = 0;
            }
        } else {
            // TODO: Create individual effect instances and clear them instead of clearing any spell instance of that type on the player
            resetEffects(player);
            placeHolderWand = 0;
            placeHolder = 0;
        }
    }
}