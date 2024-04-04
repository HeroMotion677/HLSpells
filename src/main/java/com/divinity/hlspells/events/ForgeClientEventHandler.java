package com.divinity.hlspells.events;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.items.spellitems.SpellHoldingItem;
import com.divinity.hlspells.network.NetworkManager;
import com.divinity.hlspells.network.packets.serverbound.WandInputPacket;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.spells.Phasing;
import com.divinity.hlspells.spell.spells.PhasingII;
import com.divinity.hlspells.util.SpellUtils;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RenderBlockScreenEffectEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.lwjgl.glfw.GLFW;


@Mod.EventBusSubscriber(modid = HLSpells.MODID, value = Dist.CLIENT, bus = Bus.FORGE)
public class ForgeClientEventHandler {

    public static final KeyMapping WAND_BINDING = new KeyMapping("Next Spell", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, "HLSpells");

        @SubscribeEvent
        public static void onRenderBlockOnHUD(RenderBlockScreenEffectEvent event) {
            Player player = event.getPlayer();
            if (player != null && player.isUsingItem()) {
                if (SpellUtils.getSpell(player.getUseItem()) instanceof Phasing spell && spell.canUseSpell() || SpellUtils.getSpell(player.getUseItem()) instanceof PhasingII spell2 && spell2.canUseSpell()) {
                    event.setCanceled(true);
                }
            }
        }
//
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                LocalPlayer player = Minecraft.getInstance().player;
                if (WAND_BINDING.consumeClick()) {
                    if (player != null && !player.isUsingItem()) {
                        NetworkManager.INSTANCE.sendToServer(new WandInputPacket(WAND_BINDING.getKey().getValue()));
                        for (InteractionHand hand : InteractionHand.values()) {
                            ItemStack carriedItem = player.getItemInHand(hand);
                            if (carriedItem.getItem() instanceof SpellHoldingItem item && !item.isSpellBook()) {
                                carriedItem.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(cap -> {
                                    if (!cap.getSpells().isEmpty()) {
                                        cap.incrementCurrentSpellCycle();
                                        Spell spell = SpellUtils.getSpellByID(cap.getCurrentSpell());
                                        player.displayClientMessage(Component.literal(spell.getTrueDisplayName()).withStyle(ChatFormatting.AQUA), true);
                                    }
                                });
                                break;
                            }
                        }
                    }
                }
            }
        }

        /**
         * When a spell holding item is used it stops the slowness effect
         */
        @SubscribeEvent
        @SuppressWarnings("ConstantConditions")
        public static void onInput(MovementInputUpdateEvent event) {
            if (event.getEntity() instanceof LocalPlayer player) {
                InteractionHand hand = player.getUsedItemHand();
                // Don't remove this even if it complains. If it can be null, it can be null
                if (hand != null) {
                    ItemStack stack = player.getItemInHand(hand);
                    if (player.isUsingItem() && !player.isPassenger() && stack.getItem() instanceof SpellHoldingItem) {
                        Spell spell = SpellUtils.getSpell(stack);
                        if (spell == SpellInit.SPEED.get() || spell == SpellInit.FROST_PATH_II.get() || spell == SpellInit.FROST_PATH.get() || spell == SpellInit.PHASING.get() || spell == SpellInit.PHASING_II.get()) {
                            player.input.leftImpulse /= 0.2F;
                            player.input.forwardImpulse /= 0.2F;
                        }
                    }
                }
            }
        }
    }


