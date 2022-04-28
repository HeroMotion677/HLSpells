package com.divinity.hlspells.spells;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.items.SpellHoldingItem;
import com.divinity.hlspells.items.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellTypes;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

/**
 * This class is responsible for firing spell actions
 */
@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class RunSpells {
    static int xpTickCounter;
    static int durabilityTickCounter;

    public static void doCastSpell(Player player, Level world, ItemStack itemStack) {
        if (itemStack.getItem() instanceof SpellHoldingItem && player != null && world != null) {
            itemStack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP)
                    .filter(cap -> !cap.getSpells().isEmpty())
                    .ifPresent(cap -> {
                        Spell spell = SpellUtils.getSpellByID(cap.getCurrentSpell());
                        if (spell.getType() == SpellTypes.CAST && spell.hasCost() && SpellUtils.checkXpReq(player, spell)) {
                            if (spell.getSpellAction().test(player, world)) {
                                if (!player.level.isClientSide() && !player.isCreative())
                                    itemStack.hurt(getSpellHoldingItemCalculation(itemStack), player.getRandom(), (ServerPlayer) player);
                                if (HLSpells.CONFIG.spellsUseXP.get() && !player.isCreative())
                                    player.giveExperiencePoints(-SpellUtils.getXpReq(player, spell));
                            }
                        }
                    });
        }
    }

    @SubscribeEvent
    public static void doHeldSpell(TickEvent.PlayerTickEvent event) {
        if (event.player.isAlive()) {
            Player player = event.player;
            InteractionHand hand = player.getUsedItemHand();
            if (hand == InteractionHand.MAIN_HAND || hand == InteractionHand.OFF_HAND) {
                ItemStack stack = player.getItemInHand(hand);
                //noinspection ConstantConditions
                if (stack.getItem() instanceof SpellHoldingItem && player != null && player.level != null && player.isUsingItem()) {
                    stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).filter(cap -> !cap.getSpells().isEmpty()).ifPresent(cap -> {
                                if (cap.isHeldActive()) {
                                    Spell spell = SpellUtils.getSpellByID(cap.getCurrentSpell());
                                    if (spell.getType() == SpellTypes.HELD && spell.hasCost() && SpellUtils.checkXpReq(player, spell)) {
                                        if (spell.getSpellAction().test(player, player.level)) {
                                            xpTickCounter++;
                                            durabilityTickCounter++;
                                            if (xpTickCounter == SpellUtils.getTickDelay(player, spell) && HLSpells.CONFIG.spellsUseXP.get() && !player.isCreative()) {
                                                player.giveExperiencePoints(-SpellUtils.getXpReq(player, spell));
                                                xpTickCounter = 0;
                                            }
                                            if (durabilityTickCounter == 15 && !player.level.isClientSide() && !player.isCreative()) {
                                                stack.hurt(getSpellHoldingItemCalculation(stack), player.getRandom(), (ServerPlayer) player);
                                                durabilityTickCounter = 0;
                                            }
                                        }
                                    } else {
                                        reset(player);
                                    }
                                } else {
                                    reset(player);
                                }
                            });
                } else {
                    reset(player);
                }
            }
        }
    }

    @SubscribeEvent
    public static void applyEnchantments(AnvilUpdateEvent event) {
        if (event != null) {
            if (event.getRight().getItem() instanceof EnchantedBookItem) {
                ItemStack book = event.getRight();
                boolean hasMending = false;
                for (int i = 0; i < EnchantedBookItem.getEnchantments(book).size(); i++) {
                    if (EnchantedBookItem.getEnchantments(book).getString(i).contains("minecraft:mending")) {
                        hasMending = true;
                    }
                }
                if (hasMending) {
                    if (event.getLeft().getItem() instanceof SpellHoldingItem item && !item.isSpellBook()) {
                        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MENDING, event.getLeft()) == 0) {
                            event.setCost(6);
                            ItemStack copy = event.getLeft().copy();
                            copy.enchant(Enchantments.MENDING, 1);
                            event.setOutput(copy);
                        }
                    }
                }
            }
        }
    }

    private static int getSpellHoldingItemCalculation(ItemStack itemStack) {
        int level = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, itemStack);
        if (level < 1) return 1;
        int random = new Random().nextInt(5);
        return random <= (level - 1) ? 0 : 1;
    }

    public static void reset(Player player) {
        xpTickCounter = 0;
        durabilityTickCounter = 0;
        SpellActions.resetEffects(player);
    }
}