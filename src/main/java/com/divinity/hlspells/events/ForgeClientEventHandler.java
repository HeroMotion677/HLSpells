package com.divinity.hlspells.events;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.items.spellitems.SpellHoldingItem;
import com.divinity.hlspells.network.NetworkManager;
import net.neoforged.neoforge.network.PacketDistributor;
import com.divinity.hlspells.network.packets.serverbound.WandInputPacket;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.spells.Phasing;
import com.divinity.hlspells.spell.spells.PhasingII;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.client.event.RenderBlockScreenEffectEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import java.util.HashMap;
import java.util.Map;

import static com.divinity.hlspells.events.ModClientEventHandler.WAND_BINDING;


@EventBusSubscriber(modid = HLSpells.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class ForgeClientEventHandler {

    public static final Map<Item, HumanoidModel<LivingEntity>> hatArmorModel = new HashMap<>();

        @SubscribeEvent
        public static void onRenderBlockOnHUD(RenderBlockScreenEffectEvent event) {
            Player player = event.getPlayer();
            if (player != null && player.isUsingItem()) {
                if (SpellUtils.getSpell(player.getUseItem()) instanceof Phasing spell && spell.canUseSpell() || SpellUtils.getSpell(player.getUseItem()) instanceof PhasingII spell2 && spell2.canUseSpell()) {
                    event.setCanceled(true);
                }
            }
        }
        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            {
                LocalPlayer player = Minecraft.getInstance().player;
                if (WAND_BINDING.consumeClick()) {
                    if (player != null && !player.isUsingItem()) {
                        PacketDistributor.sendToServer(new WandInputPacket(WAND_BINDING.getKey().getValue()));
                        for (InteractionHand hand : InteractionHand.values()) {
                            ItemStack carriedItem = player.getItemInHand(hand);
                            if (carriedItem.getItem() instanceof SpellHoldingItem item /*&& !item.isSpellBook()*/) {
                                SpellHolderProvider.get(carriedItem).ifPresent(cap -> {
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
                        if (spell == SpellInit.SPEED.get() || spell == SpellInit.FROST_PATH_II.get() || spell == SpellInit.FROST_PATH.get() || spell == SpellInit.PHASING.get() || spell == SpellInit.PHASING_II.get() || spell == SpellInit.ILLUMINATE.get() || spell == SpellInit.ILLUMINATE_II.get()) {
                            player.input.leftImpulse /= 0.13F;
                            player.input.forwardImpulse /= 0.13F;
                        }
                    }
                }
            }
        }
    }