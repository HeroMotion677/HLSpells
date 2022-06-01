package com.divinity.hlspells.events;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.items.spellitems.SpellHoldingItem;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.util.SpellUtils;
import com.divinity.hlspells.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class SpellEvents {

    // This is needed to prevent a loophole where using a held spell then switching to another slot doesn't proc the cooldown
    @SubscribeEvent
    public static void onLivingEquipChange(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getSlot().getType() == EquipmentSlot.Type.HAND) {
                ItemStack next = event.getTo();
                ItemStack previous = event.getFrom();
                if (next.getItem() instanceof SpellHoldingItem item && !item.isSpellBook()) {
                    next.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).filter(cap -> !cap.getSpells().isEmpty()).ifPresent(cap -> {
                        Spell spell = SpellUtils.getSpellByID(cap.getCurrentSpell());
                        player.displayClientMessage(new TextComponent("Spell : " + spell.getTrueDisplayName()).withStyle(ChatFormatting.GOLD), true);
                    });
                }
                if (previous.getItem() instanceof SpellHoldingItem item) {
                    previous.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).filter(cap -> !cap.getSpells().isEmpty()).ifPresent(cap -> {
                        Spell spell = SpellUtils.getSpellByID(cap.getCurrentSpell());
                        if (spell.getSpellType() == SpellAttributes.Type.HELD && item.isWasHolding() && !player.isUsingItem()) {
                            player.getCooldowns().addCooldown(previous.getItem(), ((int) (HLSpells.CONFIG.cooldownDuration.get() * 20)));
                            item.setWasHolding(false);
                        }
                    });
                }
            }
        }
    }

    @SubscribeEvent
    public static void clearEffectsAfterUse(TickEvent.PlayerTickEvent event) {
        if (event.player != null && !event.player.level.isClientSide()) {
            if (event.phase == TickEvent.Phase.END) {
                if (!(event.player.getUseItem().getItem() instanceof SpellHoldingItem)) {
                    Util.clearEffects(event.player);
                }
            }
        }
    }
}
