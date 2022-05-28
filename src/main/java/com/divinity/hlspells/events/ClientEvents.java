package com.divinity.hlspells.events;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.capabilities.totemcap.ITotemCap;
import com.divinity.hlspells.capabilities.totemcap.TotemItemProvider;
import com.divinity.hlspells.compat.CuriosCompat;
import com.divinity.hlspells.entities.InvisibleTargetingEntity;
import com.divinity.hlspells.items.spellitems.SpellHoldingItem;
import com.divinity.hlspells.client.models.BaseBoltModel;
import com.divinity.hlspells.client.models.WizardHatModel;
import com.divinity.hlspells.network.NetworkManager;
import com.divinity.hlspells.network.packets.serverbound.WandInputPacket;
import com.divinity.hlspells.client.renderers.BaseBoltRenderer;
import com.divinity.hlspells.client.other.AltarItemRenderer;
import com.divinity.hlspells.world.blocks.blockentities.screen.AltarOfAttunementScreen;
import com.divinity.hlspells.setup.init.*;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.util.SpellUtils;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.VexRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = HLSpells.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class ClientEvents {

    public static final KeyMapping WAND_BINDING = new KeyMapping("Wand Cycle", KeyConflictContext.UNIVERSAL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, "HLSpells");
    public static boolean buttonPressedFlag;
    public static final Map<Item, HumanoidModel<LivingEntity>> armorModel = new HashMap<>();

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            registerItemModel(ItemInit.SPELL_BOOK.get(), new ResourceLocation("using"), 3, 0.2F, 0.4F, 0.6F, 0.8F, 1F);
            registerItemModel(ItemInit.WAND.get(), new ResourceLocation("pull"), 3, 0.2F, 0.4F, 0.6F, 0.8F, 1F);
            registerItemModel(ItemInit.AMETHYST_WAND.get(), new ResourceLocation("pull"), 3, 0.2F, 0.4F, 0.6F, 0.8F, 1F);
            ItemInit.STAFFS.forEach(staff -> registerItemModel(staff.get(), new ResourceLocation("pull"), 3, 0.2F, 0.4F, 0.6F, 0.8F, 1F));
            ItemProperties.register(ItemInit.TOTEM_OF_RETURNING.get(), new ResourceLocation("used"), (stack, world, living, integer) -> {
                if (living instanceof Player) {
                    LazyOptional<ITotemCap> totemCap = stack.getCapability(TotemItemProvider.TOTEM_CAP);
                    if (totemCap.isPresent()) return totemCap.map(ITotemCap::getHasDied).orElse(false) ? 1 : 0;
                }
                return 0;
            });
        });
        ClientRegistry.registerKeyBinding(WAND_BINDING);
        // Curios Renderer Registration
        if (HLSpells.isCurioLoaded) CuriosCompat.renderCuriosTotems(ItemInit.TOTEMS);
        MenuScreens.register(MenuTypeInit.ALTAR_CONTAINER.get(), AltarOfAttunementScreen::new);
        ItemBlockRenderTypes.setRenderLayer(BlockInit.ALTAR_OF_ATTUNEMENT_BLOCK.get(), RenderType.cutout());
        BlockEntityRenderers.register(BlockInit.ALTAR_BE.get(), ctx -> new AltarItemRenderer());
    }

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BaseBoltModel.LAYER_LOCATION, BaseBoltModel::createBodyLayer);
        event.registerLayerDefinition(WizardHatModel.LAYER_LOCATION, WizardHatModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerModelLayers(EntityRenderersEvent.AddLayers event) {
        EntityRendererProvider.Context context = new EntityRendererProvider.Context(
                Minecraft.getInstance().getEntityRenderDispatcher(),
                Minecraft.getInstance().getItemRenderer(),
                Minecraft.getInstance().getResourceManager(),
                Minecraft.getInstance().getEntityModels(),
                Minecraft.getInstance().font);
        WizardHatModel<LivingEntity> model = new WizardHatModel<>(context.bakeLayer(WizardHatModel.LAYER_LOCATION));
        armorModel.put(ItemInit.WIZARD_HAT.get(), model);
    }

    @SuppressWarnings("all")
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.INVISIBLE_TARGETING_ENTITY.get(), ctx -> new EntityRenderer<>(ctx) {
            @Override public boolean shouldRender(InvisibleTargetingEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) { return false; }
            @Override public ResourceLocation getTextureLocation(InvisibleTargetingEntity pEntity) { return new ResourceLocation(""); }
        });
        event.registerEntityRenderer(EntityInit.PIERCING_BOLT_ENTITY.get(), ctx -> new BaseBoltRenderer<>(ctx, new ResourceLocation(HLSpells.MODID, "textures/entity/bolt/green_bolt.png")));
        event.registerEntityRenderer(EntityInit.FLAMING_BOLT_ENTITY.get(), ctx -> new BaseBoltRenderer<>(ctx, new ResourceLocation(HLSpells.MODID, "textures/entity/bolt/orange_bolt.png")));
        event.registerEntityRenderer(EntityInit.AQUA_BOLT_ENTITY.get(), ctx -> new BaseBoltRenderer<>(ctx, new ResourceLocation(HLSpells.MODID, "textures/entity/bolt/blue_bolt.png")));
        event.registerEntityRenderer(EntityInit.SUMMONED_VEX_ENTITY.get(), VexRenderer::new);
    }

    /**
     * Registers the model of a given item that pulls back like a bow
     *
     * @param item                    The item to register the model of
     * @param location                The resource location of the model predicate
     * @param useItemRemainTickOffset The tick offset at which the item will change models at
     * @param values                  The amount of model increments at which it changes at
     */
    @SuppressWarnings("all")
    private static void registerItemModel(Item item, ResourceLocation location, int useItemRemainTickOffset, float... values) {
        ItemProperties.register(item, location, (stack, world, living, seed) -> {
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

    @Mod.EventBusSubscriber(modid = HLSpells.MODID, value = Dist.CLIENT, bus = Bus.FORGE)
    public static class ForgeEvents {

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
                        if (mainHandWand) stack = mainHand;
                        else if (offHandWand) stack = offHand;
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
                if (!WAND_BINDING.isDown() && buttonPressedFlag) buttonPressedFlag = false;
            }
        }

        /**
         * When a spell holding item is used it stops the slowness effect
         */
        @SubscribeEvent
        @SuppressWarnings("ConstantConditions")
        public static void onInput(MovementInputUpdateEvent event) {
            if (event.getPlayer() instanceof LocalPlayer player) {
                InteractionHand hand = player.getUsedItemHand();
                // Don't remove this even if it complains. If it can be null, it can be null
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
    }
}
