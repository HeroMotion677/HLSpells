package com.divinity.hlspells.events;
import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.capabilities.totemcap.ITotemCap;
import com.divinity.hlspells.capabilities.totemcap.TotemItemProvider;
import com.divinity.hlspells.client.models.BaseBoltModel;
import com.divinity.hlspells.client.models.FireballModel;
import com.divinity.hlspells.client.models.WizardHatModel;
import com.divinity.hlspells.client.other.AltarItemRenderer;
import com.divinity.hlspells.client.renderers.BaseBoltRenderer;
import com.divinity.hlspells.client.renderers.FireballRenderer;
import com.divinity.hlspells.compat.CuriosCompat;
import com.divinity.hlspells.entities.projectile.InvisibleTargetingEntity;
import com.divinity.hlspells.langproviders.EnUsLangProvider;
import com.divinity.hlspells.loot.EvokerLootModifier;
import com.divinity.hlspells.loot.SetSpell;
import com.divinity.hlspells.setup.init.BlockInit;
import com.divinity.hlspells.setup.init.EntityInit;
import com.divinity.hlspells.setup.init.ItemInit;
import com.divinity.hlspells.setup.init.MenuTypeInit;
import com.divinity.hlspells.world.blocks.blockentities.screen.AltarOfAttunementScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import java.util.HashMap;
import java.util.Map;

import static com.divinity.hlspells.HLSpells.LOGGER;
import static com.divinity.hlspells.events.ForgeEventHandler.WAND_BINDING;

@Mod.EventBusSubscriber(modid = HLSpells.MODID, bus = Bus.MOD)
public class ModEventHandler {

    public static final Map<Item, HumanoidModel<LivingEntity>> armorModel = new HashMap<>();

    public static LootItemFunctionType SET_SPELL;

    @SubscribeEvent
    public static void commonSetup (FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // We need to initialize this here during CommonSetup because this event fires after the RegistryEvents have been fired
            // Initializing any sooner would result in the game crashing
            SET_SPELL = register("set_spell", new SetSpell.Serializer());
        });
    }

    @SubscribeEvent
    public static void registerModifierSerializers(final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().registerAll(new EvokerLootModifier.Serializer().setRegistryName(new ResourceLocation(HLSpells.MODID, "evoker_modifier")));
    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        LOGGER.info("Gathering data providers!");
        DataGenerator generator = event.getGenerator();
        if (event.includeClient()) {
            generator.addProvider(new EnUsLangProvider(generator));
        }
    }

    @SuppressWarnings("all")
    private static LootItemFunctionType register(String id, LootItemConditionalFunction.Serializer<? extends LootItemFunction> serializer) {
        return Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(HLSpells.MODID, id), new LootItemFunctionType(serializer));
    }

    @Mod.EventBusSubscriber(modid = HLSpells.MODID, value = Dist.CLIENT, bus = Bus.MOD)
    public static class ClientEventHandler {
        @SubscribeEvent
        public static void init(final FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                registerItemModel(ItemInit.SPELL_BOOK.get(), new ResourceLocation("using"), 3, 0.2F, 0.4F, 0.6F, 0.8F, 1F);
                registerItemModel(ItemInit.WAND.get(), new ResourceLocation("pull"), 3, 0.2F, 0.4F, 0.6F, 0.8F, 1F);
                registerItemModel(ItemInit.AMETHYST_WAND.get(), new ResourceLocation("pull"), 3, 0.2F, 0.4F, 0.6F, 0.8F, 1F);
                ItemInit.STAFFS.forEach(staff -> registerItemModel(staff.get(), new ResourceLocation("pull"), 3, 0.2F, 0.4F, 0.6F, 0.8F, 1F));
                ItemProperties.register(ItemInit.TOTEM_OF_RETURNING.get(), new ResourceLocation("used"), (stack, world, living, integer) -> {
                    if (living instanceof Player) {
                        var totemCap = stack.getCapability(TotemItemProvider.TOTEM_CAP);
                        if (totemCap.isPresent())
                            return totemCap.map(ITotemCap::getHasDied).orElse(false) ? 1 : 0;
                    }
                    return 0;
                });
            });
            ClientRegistry.registerKeyBinding(WAND_BINDING);
            // Curios Renderer Registration
            if (HLSpells.isCurioLoaded) {
                CuriosCompat.renderCuriosTotems(ItemInit.TOTEMS);
            }
            MenuScreens.register(MenuTypeInit.ALTAR_CONTAINER.get(), AltarOfAttunementScreen::new);
            ItemBlockRenderTypes.setRenderLayer(BlockInit.ALTAR_OF_ATTUNEMENT_BLOCK.get(), RenderType.cutout());
            BlockEntityRenderers.register(BlockInit.ALTAR_BE.get(), ctx -> new AltarItemRenderer());
        }

        @SubscribeEvent
        public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(BaseBoltModel.LAYER_LOCATION, BaseBoltModel::createBodyLayer);
            event.registerLayerDefinition(WizardHatModel.LAYER_LOCATION, WizardHatModel::createBodyLayer);
            event.registerLayerDefinition(FireballModel.LAYER_LOCATION, FireballModel::createBodyLayer);
        }

        @SubscribeEvent
        public static void registerModelLayers(EntityRenderersEvent.AddLayers event) {
            EntityRendererProvider.Context context = new EntityRendererProvider.Context(
                    Minecraft.getInstance().getEntityRenderDispatcher(),
                    Minecraft.getInstance().getItemRenderer(),
                    Minecraft.getInstance().getResourceManager(),
                    Minecraft.getInstance().getEntityModels(),
                    Minecraft.getInstance().font);
            var model = new WizardHatModel<>(context.bakeLayer(WizardHatModel.LAYER_LOCATION));
            armorModel.put(ItemInit.WIZARD_HAT.get(), model);
        }

        @SuppressWarnings("all")
        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(EntityInit.INVISIBLE_TARGETING_ENTITY.get(), ctx -> new EntityRenderer<>(ctx) {
                @Override public boolean shouldRender(InvisibleTargetingEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) { return false; }
                @Override public ResourceLocation getTextureLocation(InvisibleTargetingEntity pEntity) { return new ResourceLocation(""); }
            });
            event.registerEntityRenderer(EntityInit.PIERCING_BOLT_ENTITY.get(), ctx -> new BaseBoltRenderer<>(ctx, getBoltLocation("textures/entity/bolt/green_bolt.png")));
            event.registerEntityRenderer(EntityInit.FLAMING_BOLT_ENTITY.get(), ctx -> new BaseBoltRenderer<>(ctx, getBoltLocation("textures/entity/bolt/orange_bolt.png")));
            event.registerEntityRenderer(EntityInit.AQUA_BOLT_ENTITY.get(), ctx -> new BaseBoltRenderer<>(ctx, getBoltLocation("textures/entity/bolt/blue_bolt.png")));
            event.registerEntityRenderer(EntityInit.FREEZING_BOLT_ENTITY.get(), ctx -> new BaseBoltRenderer<>(ctx, getBoltLocation("textures/entity/bolt/white_bolt.png")));
            event.registerEntityRenderer(EntityInit.CHORUS_BOLT_ENTITY.get(), ctx -> new BaseBoltRenderer<>(ctx, getBoltLocation("textures/entity/bolt/purple_bolt.png")));
            event.registerEntityRenderer(EntityInit.FIREBALL.get(), ctx -> new FireballRenderer<>(ctx, getBoltLocation("textures/entity/fireball/fireball1.png")));
            event.registerEntityRenderer(EntityInit.FIREBALL2.get(), ctx -> new FireballRenderer<>(ctx, getBoltLocation("textures/entity/fireball/fireball2.png")));
            event.registerEntityRenderer(EntityInit.KNOCKBACK_BOLT_ENTITY.get(), ShulkerBulletRenderer::new);
            event.registerEntityRenderer(EntityInit.SUMMONED_VEX_ENTITY.get(), VexRenderer::new);
            event.registerEntityRenderer(EntityInit.SUMMONED_SKELETON_ENTITY.get(), SkeletonRenderer::new);
            event.registerEntityRenderer(EntityInit.WITHER_SKULL_ENTITY.get(), WitherSkullRenderer::new);
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

        private static ResourceLocation getBoltLocation(String location) {
            return new ResourceLocation(HLSpells.MODID, location);
        }
    }
}
