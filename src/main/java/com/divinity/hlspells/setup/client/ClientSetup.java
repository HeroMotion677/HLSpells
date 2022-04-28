package com.divinity.hlspells.setup.client;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.setup.client.screen.AltarOfAttunementScreen;
import com.divinity.hlspells.setup.init.BlockInit;
import com.divinity.hlspells.setup.init.ItemInit;
import com.divinity.hlspells.setup.init.MenuTypeInit;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.items.SpellHoldingItem;
import com.divinity.hlspells.items.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.items.capabilities.totemcap.ITotemCap;
import com.divinity.hlspells.items.capabilities.totemcap.TotemItemProvider;
import com.divinity.hlspells.network.NetworkManager;
import com.divinity.hlspells.network.packets.WandInputPacket;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.util.SpellUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.client.ICurioRenderer;

@Mod.EventBusSubscriber(modid = HLSpells.MODID, value = Dist.CLIENT)
public class ClientSetup {
    public static final KeyMapping WAND_BINDING = new KeyMapping("Wand Cycle", KeyConflictContext.UNIVERSAL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, "HLSpells");
    private static boolean buttonPressedFlag;

    public static void init(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            registerItemModel(ItemInit.SPELL_BOOK.get(), new ResourceLocation("using"), 3, 0.2F, 0.4F, 0.6F, 0.8F, 1F);
            registerItemModel(ItemInit.WAND.get(), new ResourceLocation("pull"), 3, 0.2F, 0.4F, 0.6F, 0.8F, 1F);
            registerItemModel(ItemInit.STAFF.get(), new ResourceLocation("pull"), 3, 0.2F, 0.4F, 0.6F, 0.8F, 1F);

        });
        ItemProperties.register(ItemInit.TOTEM_OF_RETURNING.get(), new ResourceLocation("used"), (stack, world, living, integer) -> {
            if (living instanceof Player) {
                LazyOptional<ITotemCap> totemCap = stack.getCapability(TotemItemProvider.TOTEM_CAP);
                if (totemCap.isPresent()) {
                    return totemCap.map(ITotemCap::getHasDied).orElse(false) ? 1 : 0;
                }
            }
            return 0;
        });
        ClientRegistry.registerKeyBinding(WAND_BINDING);
        // Curios Renderer Registration
        ICurioRenderer renderer = new ICurioRenderer() {
            @Override
            public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                LocalPlayer clientPlayer = Minecraft.getInstance().player;
                if (clientPlayer != null) {
                    ICurioRenderer.translateIfSneaking(matrixStack, clientPlayer);
                    ICurioRenderer.rotateIfSneaking(matrixStack, clientPlayer);
                }
                matrixStack.scale(0.35F, 0.35F, 0.35F);
                matrixStack.translate(0.0F, 0.5F, -0.4F);
                matrixStack.mulPose(Direction.DOWN.getRotation());
                Minecraft.getInstance().getItemRenderer()
                        .renderStatic(stack, ItemTransforms.TransformType.NONE, light, OverlayTexture.NO_OVERLAY,
                                matrixStack, renderTypeBuffer, 1);
            }
        };
        CuriosRendererRegistry.register(ItemInit.TOTEM_OF_RETURNING.get(), () -> renderer);
        CuriosRendererRegistry.register(ItemInit.TOTEM_OF_KEEPING.get(), () -> renderer);
        CuriosRendererRegistry.register(ItemInit.TOTEM_OF_ESCAPING.get(), () -> renderer);
        CuriosRendererRegistry.register(ItemInit.TOTEM_OF_GRIEFING.get(), () -> renderer);
        MenuScreens.register(MenuTypeInit.ALTAR_CONTAINER.get(), AltarOfAttunementScreen::new);
        ItemBlockRenderTypes.setRenderLayer(BlockInit.ALTAR_OF_ATTUNEMENT_BLOCK.get(), RenderType.cutout());
        BlockEntityRenderers.register(BlockInit.ALTAR_BE.get(), AltarItemRenderer::new);
    }

    /**
     * Registers the model of a given item that pulls back like a bow
     *
     * @param item                    The item to register the model of
     * @param location                The resource location of the model predicate
     * @param useItemRemainTickOffset The tick offset at which the item will change models at
     * @param values                  The amount of model increments at which it changes at
     */
    private static void registerItemModel(Item item, ResourceLocation location, int useItemRemainTickOffset, float... values) {
        ItemProperties.register(item, location, (stack, world, living, integer) -> {
            if (living instanceof Player && living.isUsingItem() && living.getUseItem() == stack) {
                int useDuration = item.getUseDuration(item.getDefaultInstance());
                int minUseAmount = useDuration - (useItemRemainTickOffset * (values.length - 1));
                for (int i = 0; i < values.length; i++) {
                    if ((double) living.getUseItemRemainingTicks() < minUseAmount) return values[values.length - 1];
                    else if ((double) living.getUseItemRemainingTicks() < useDuration && (double) living.getUseItemRemainingTicks() >= (useDuration - (useItemRemainTickOffset * (i == 0 ? 1 : i)))) {
                        return values[i];
                    }
                }
            }
            return 0;
        });
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (WAND_BINDING.isDown() && !buttonPressedFlag) {
                if (player != null && !player.isUsingItem()) {
                    NetworkManager.INSTANCE.sendToServer(new WandInputPacket(WAND_BINDING.getKey().getValue()));
                    ItemStack stack = ItemStack.EMPTY;
                    ItemStack mainHand = player.getMainHandItem();
                    ItemStack offHand = player.getOffhandItem();
                    boolean mainHandWand = mainHand.getItem() instanceof SpellHoldingItem sItem && sItem.isWand();
                    boolean offHandWand = offHand.getItem() instanceof SpellHoldingItem item && item.isWand();
                    if (mainHandWand)
                        stack = mainHand;
                    else if (offHandWand)
                        stack = offHand;
                    if (!stack.isEmpty()) {
                        stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP, null).ifPresent(cap -> {
                            if (!cap.getSpells().isEmpty()) {
                                cap.setCurrentSpellCycle(cap.getCurrentSpellCycle() + 1);
                                Spell spell = SpellUtils.getSpellByID(cap.getCurrentSpell());
                                player.displayClientMessage(new TextComponent("Spell : " + spell.getTrueDisplayName()).withStyle(ChatFormatting.GOLD), true);
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

    /**
     * When a spell holding item is used it stops the slowness effect
     */
    @SubscribeEvent
    @SuppressWarnings("ConstantConditions")
    public static void onInput(MovementInputUpdateEvent event) {
        LocalPlayer player = (LocalPlayer) event.getPlayer();
        InteractionHand hand = player.getUsedItemHand();
        // Don't remove this even if it complains if it can be null it can be null
        if (hand != null) {
            ItemStack stack = player.getItemInHand(hand);
            if (player.isUsingItem() && !player.isPassenger() && stack.getItem() instanceof SpellHoldingItem) {
                Spell spell = SpellUtils.getSpell(stack);
                if (spell == SpellInit.SPEED.get() || spell == SpellInit.FROST_PATH.get()) {
                    player.input.leftImpulse /= 0.2F;
                    player.input.forwardImpulse /= 0.2F;
                }
            }
        }
    }
}