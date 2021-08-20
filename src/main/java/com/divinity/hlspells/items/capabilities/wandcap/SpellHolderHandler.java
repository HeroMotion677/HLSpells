package com.divinity.hlspells.items.capabilities.wandcap;

import com.divinity.hlspells.init.SpellInit;
import com.divinity.hlspells.items.SpellBookItem;
import com.divinity.hlspells.items.WandItem;
import com.divinity.hlspells.network.NetworkManager;
import com.divinity.hlspells.network.packets.WandInputPacket;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.divinity.hlspells.HLSpells.MODID;
import static com.divinity.hlspells.HLSpells.WAND_BINDING;
import static com.divinity.hlspells.items.capabilities.wandcap.SpellHolderStorage.CURRENT_SPELL_VALUE;

@Mod.EventBusSubscriber(modid = MODID)
public class SpellHolderHandler {
    static boolean buttonPressedFlag;
    static ResourceLocation WAND_CAP_ID = new ResourceLocation(MODID, "spell_holder");

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        Item item = event.getObject().getItem();
        if (item instanceof WandItem || item instanceof SpellBookItem) {
            event.addCapability(WAND_CAP_ID, new SpellHolderProvider());
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            if (WAND_BINDING.isDown() && !buttonPressedFlag) {
                if (player != null && !player.isUsingItem()) {
                    NetworkManager.INSTANCE.sendToServer(new WandInputPacket(WAND_BINDING.getKey().getValue()));
                    ItemStack stack = ItemStack.EMPTY;
                    ItemStack mainHand = player.getMainHandItem();
                    ItemStack offHand = player.getOffhandItem();
                    boolean mainHandWand = mainHand.getItem() instanceof WandItem;
                    boolean offHandWand = offHand.getItem() instanceof WandItem;
                    if (mainHandWand)
                        stack = mainHand;
                    else if (offHandWand)
                        stack = offHand;
                    if (!stack.isEmpty()) {
                        stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP, null).ifPresent(cap ->
                        {
                            // Ensures Sync (Temp solution for now, will probably need server -> client packet)
                            cap.setCurrentSpellCycle(CURRENT_SPELL_VALUE);
                            if (!cap.getSpells().isEmpty()) {
                                cap.setCurrentSpellCycle(cap.getCurrentSpellCycle() + 1);
                                Spell spell = SpellUtils.getSpellByID(cap.getCurrentSpell());
                                if (spell != null) {
                                    player.displayClientMessage(new StringTextComponent("Spell : " + spell.getTrueDisplayName()).withStyle(TextFormatting.GOLD), true);
                                }
                            }
                        });
                    }
                }
                buttonPressedFlag = true;
            }
            if (!WAND_BINDING.isDown() && buttonPressedFlag) {
                buttonPressedFlag = false;
            }
        }
    }

    @SubscribeEvent
    public static void anvilUpdateEvent(AnvilUpdateEvent event) {
        ItemStack wandItem = event.getLeft().getItem() instanceof WandItem ? event.getLeft() : null;
        ItemStack spellBook = event.getRight().getItem() instanceof SpellBookItem ? event.getRight() : null;
        if (wandItem != null && spellBook != null) {
            ItemStack transformedItem = wandItem.copy();
            Spell spell = SpellUtils.getSpell(spellBook);
            if (spell != SpellInit.EMPTY.get()) {
                transformedItem.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP)
                        .ifPresent(wandCap -> {
                            if (SpellUtils.canAddSpell(transformedItem.getItem(), wandCap.getSpells()))
                                wandCap.addSpell(spell.getRegistryName().toString());
                        });
            }
            event.setCost(15);
            event.setMaterialCost(1);
            event.setOutput(transformedItem);
        }
    }

}
