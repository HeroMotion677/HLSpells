package com.divinity.hlspells.spells;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.items.SpellHoldingItem;
import com.divinity.hlspells.items.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellType;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * This class is responsible for firing spell actions
 */
@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class RunSpells {
    static int tick;

    public static void doCastSpell(PlayerEntity player, World world, ItemStack itemStack) {
        if (itemStack.getItem() instanceof SpellHoldingItem) {
            itemStack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP, null)
                    .filter(cap -> !cap.getSpells().isEmpty())
                    .ifPresent(cap -> {
                        Spell spell = SpellUtils.getSpellByID(cap.getCurrentSpell());
                        if (spell.getType() == SpellType.CAST && spell.hasCost() && SpellUtils.checkXpReq(player, spell)) {
                            spell.getSpellAction().accept(player, world);
                            if (HLSpells.CONFIG.spellsUseXP.get())
                                player.giveExperiencePoints(-spell.getXpCost());
                        }
                    });
        }
    }

    @SubscribeEvent
    public static void doHeldSpell(TickEvent.PlayerTickEvent event) {
        if (event.player.isAlive()) {
            PlayerEntity player = event.player;
            Hand hand = player.getUsedItemHand();
            if (hand == Hand.MAIN_HAND || hand == Hand.OFF_HAND) {
                ItemStack stack = player.getItemInHand(hand);
                if (stack.getItem() instanceof SpellHoldingItem) {
                    stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP)
                            .filter(cap -> !cap.getSpells().isEmpty())
                            .ifPresent(cap -> {
                                if (cap.isHeldActive()) {
                                    Spell spell = SpellUtils.getSpellByID(cap.getCurrentSpell());
                                    if (spell.getType() == SpellType.HELD && spell.hasCost() && SpellUtils.checkXpReq(player, spell)) {
                                        spell.getSpellAction().accept(player, player.level);
                                        tick++;
                                        if (tick == spell.getTickDelay() && HLSpells.CONFIG.spellsUseXP.get()) {
                                            player.giveExperiencePoints(-spell.getXpCost());
                                            tick = 0;
                                        }
                                    }
                                } else {
                                    tick = 0;
                                    SpellActions.resetEffects(player);
                                }
                            });
                } else {
                    tick = 0;
                    SpellActions.resetEffects(player);
                }
            }
        }
    }
}