package com.divinity.hlspells.setup.client;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.init.EntityInit;
import com.divinity.hlspells.init.ItemInit;
import com.divinity.hlspells.items.SpellHoldingItem;
import com.divinity.hlspells.items.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.network.NetworkManager;
import com.divinity.hlspells.network.packets.WandInputPacket;
import com.divinity.hlspells.renderers.BaseBoltRenderer;
import com.divinity.hlspells.renderers.StormBoltRenderer;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = HLSpells.MODID, value = Dist.CLIENT)
public class ClientSetup {
    public static final KeyBinding WAND_BINDING = new KeyBinding("Wand Cycle", KeyConflictContext.UNIVERSAL, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_G, "HLSpells");
    static boolean buttonPressedFlag;

    public static void init(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemModelsProperties.register(ItemInit.SPELL_BOOK.get(), new ResourceLocation("using"), (stack, world, living) -> {
                if (living instanceof PlayerEntity && living.isUsingItem() && living.getUseItem() == stack) {
                    if ((double) living.getUseItemRemainingTicks() < 72000 && (double) living.getUseItemRemainingTicks() >= 71997) {
                        return 0.2F;
                    } else if ((double) living.getUseItemRemainingTicks() < 71997 && (double) living.getUseItemRemainingTicks() >= 71994) {
                        return 0.4F;
                    } else if ((double) living.getUseItemRemainingTicks() < 71994 && (double) living.getUseItemRemainingTicks() >= 71991) {
                        return 0.6F;
                    } else if ((double) living.getUseItemRemainingTicks() < 71991 && (double) living.getUseItemRemainingTicks() >= 71988) {
                        return 0.8F;
                    } else if ((double) living.getUseItemRemainingTicks() < 71988) {
                        return 1;
                    }
                }
                return 0;
            });
            ItemModelsProperties.register(ItemInit.WAND.get(), new ResourceLocation("pull"), (stack, world, living) -> {
                if (living instanceof PlayerEntity && living.isUsingItem() && living.getUseItem() == stack) {
                    if ((double) living.getUseItemRemainingTicks() < 72000 && (double) living.getUseItemRemainingTicks() >= 71997) {
                        return 0.2F;
                    } else if ((double) living.getUseItemRemainingTicks() < 71997 && (double) living.getUseItemRemainingTicks() >= 71994) {
                        return 0.4F;
                    } else if ((double) living.getUseItemRemainingTicks() < 71994 && (double) living.getUseItemRemainingTicks() >= 71991) {
                        return 0.6F;
                    } else if ((double) living.getUseItemRemainingTicks() < 71991 && (double) living.getUseItemRemainingTicks() >= 71988) {
                        return 0.8F;
                    } else if ((double) living.getUseItemRemainingTicks() < 71988) {
                        return 1;
                    }
                }
                return 0;
            });
            ItemModelsProperties.register(ItemInit.STAFF.get(), new ResourceLocation("pull"), (stack, world, living) -> {
                if (living instanceof PlayerEntity && living.isUsingItem() && living.getUseItem() == stack) {
                    if ((double) living.getUseItemRemainingTicks() < 72000 && (double) living.getUseItemRemainingTicks() >= 71997) {
                        return 0.2F;
                    } else if ((double) living.getUseItemRemainingTicks() < 71997 && (double) living.getUseItemRemainingTicks() >= 71994) {
                        return 0.4F;
                    } else if ((double) living.getUseItemRemainingTicks() < 71994 && (double) living.getUseItemRemainingTicks() >= 71991) {
                        return 0.6F;
                    } else if ((double) living.getUseItemRemainingTicks() < 71991 && (double) living.getUseItemRemainingTicks() >= 71988) {
                        return 0.8F;
                    } else if ((double) living.getUseItemRemainingTicks() < 71988) {
                        return 1;
                    }
                }
                return 0;
            });
        });
        ClientRegistry.registerKeyBinding(WAND_BINDING);
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.STORM_BULLET_ENTITY.get(), StormBoltRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.PIERCING_BOLT_ENTITY.get(), manager -> new BaseBoltRenderer<>(manager, new ResourceLocation(HLSpells.MODID, "textures/entity/bolt/green_bolt.png")));
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.FLAMING_BOLT_ENTITY.get(), manager -> new BaseBoltRenderer<>(manager, new ResourceLocation(HLSpells.MODID, "textures/entity/bolt/orange_bolt.png")));
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.AQUA_BOLT_ENTITY.get(), manager -> new BaseBoltRenderer<>(manager, new ResourceLocation(HLSpells.MODID, "textures/entity/bolt/blue_bolt.png")));
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
                    boolean mainHandWand = mainHand.getItem() instanceof SpellHoldingItem && ((SpellHoldingItem) mainHand.getItem()).isWand();
                    boolean offHandWand = offHand.getItem() instanceof SpellHoldingItem && ((SpellHoldingItem) offHand.getItem()).isWand();
                    if (mainHandWand)
                        stack = mainHand;
                    else if (offHandWand)
                        stack = offHand;
                    if (!stack.isEmpty()) {
                        stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP, null).ifPresent(cap ->
                        {
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

    public static void displayActivation(PlayerEntity playerEntity, ItemStack stack, boolean particleIn) {
        Minecraft.getInstance().gameRenderer.displayItemActivation(stack);
        if (particleIn) {
            Minecraft.getInstance().particleEngine.createTrackingEmitter(playerEntity, ParticleTypes.TOTEM_OF_UNDYING, 30);
        }
    }
}