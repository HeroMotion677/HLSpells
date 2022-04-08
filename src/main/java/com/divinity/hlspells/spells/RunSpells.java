package com.divinity.hlspells.spells;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.items.SpellHoldingItem;
import com.divinity.hlspells.items.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellType;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.UnbreakingEnchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

/**
 * This class is responsible for firing spell actions
 */
@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class RunSpells {
    static int xpTickCounter;
    static int durabilityTickCounter;

    public static void doCastSpell(PlayerEntity player, World world, ItemStack itemStack) {
        if (itemStack.getItem() instanceof SpellHoldingItem) {
            itemStack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP)
                    .filter(cap -> !cap.getSpells().isEmpty())
                    .ifPresent(cap -> {
                        Spell spell = SpellUtils.getSpellByID(cap.getCurrentSpell());
                        if (spell.getType() == SpellType.CAST && spell.hasCost() && SpellUtils.checkXpReq(player, spell)) {
                            if (spell.getSpellAction().test(player, world)) {
                                if (!player.level.isClientSide() && !player.isCreative())
                                    itemStack.hurt(getSpellHoldingItemCalculation(itemStack), player.getRandom(), (ServerPlayerEntity) player);
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
            PlayerEntity player = event.player;
            Hand hand = player.getUsedItemHand();
            if (hand == Hand.MAIN_HAND || hand == Hand.OFF_HAND) {
                ItemStack stack = player.getItemInHand(hand);
                if (stack.getItem() instanceof SpellHoldingItem && player.isUsingItem()) {
                    stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP)
                            .filter(cap -> !cap.getSpells().isEmpty())
                            .ifPresent(cap -> {
                                if (cap.isHeldActive()) {
                                    Spell spell = SpellUtils.getSpellByID(cap.getCurrentSpell());
                                    if (spell.getType() == SpellType.HELD && spell.hasCost() && SpellUtils.checkXpReq(player, spell)) {
                                        if (spell.getSpellAction().test(player, player.level)) {
                                            xpTickCounter++;
                                            durabilityTickCounter++;
                                            if (xpTickCounter == SpellUtils.getTickDelay(player, spell) && HLSpells.CONFIG.spellsUseXP.get() && !player.isCreative()) {
                                                player.giveExperiencePoints(-SpellUtils.getXpReq(player, spell));
                                                xpTickCounter = 0;
                                            }
                                            if (durabilityTickCounter == 15 && !player.level.isClientSide() && !player.isCreative()) {
                                                stack.hurt(getSpellHoldingItemCalculation(stack), player.getRandom(), (ServerPlayerEntity) player);
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

    private static int getSpellHoldingItemCalculation(ItemStack itemStack) {
        int level = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, itemStack);
        if (level < 1) return 1;
        int random = new Random().nextInt(5);
        return random <= (level - 1) ? 0 : 1;
    }

    public static void reset(PlayerEntity player) {
        xpTickCounter = 0;
        durabilityTickCounter = 0;
        SpellActions.resetEffects(player);
    }
}