package com.divinity.hlspells.setup.client;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.setup.init.EntityInit;
import com.divinity.hlspells.setup.init.ItemInit;
import com.divinity.hlspells.models.BaseBoltModel;
import com.divinity.hlspells.models.WizardHatModel;
import com.divinity.hlspells.renderers.BaseBoltRenderer;
import com.divinity.hlspells.renderers.StormBoltRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.VexRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = HLSpells.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RenderRegistry {

    public static final Map<Item, HumanoidModel<LivingEntity>> armorModel = new HashMap<>();

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BaseBoltModel.LAYER_LOCATION, BaseBoltModel::createBodyLayer);
        event.registerLayerDefinition(WizardHatModel.LAYER_LOCATION, WizardHatModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerModelLayers(EntityRenderersEvent.AddLayers event) {
        EntityRendererProvider.Context context = new EntityRendererProvider.Context(Minecraft.getInstance().getEntityRenderDispatcher(),
                Minecraft.getInstance().getItemRenderer(), Minecraft.getInstance().getResourceManager(),
                Minecraft.getInstance().getEntityModels(), Minecraft.getInstance().font);
        WizardHatModel<LivingEntity> model = new WizardHatModel<>(context.bakeLayer(WizardHatModel.LAYER_LOCATION));
        armorModel.put(ItemInit.WIZARD_HAT.get(), model);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.INVISIBLE_TARGETING_ENTITY.get(), StormBoltRenderer::new);
        event.registerEntityRenderer(EntityInit.PIERCING_BOLT_ENTITY.get(), ctx -> new BaseBoltRenderer<>(ctx, new ResourceLocation(HLSpells.MODID, "textures/entity/bolt/green_bolt.png")));
        event.registerEntityRenderer(EntityInit.FLAMING_BOLT_ENTITY.get(), ctx -> new BaseBoltRenderer<>(ctx, new ResourceLocation(HLSpells.MODID, "textures/entity/bolt/orange_bolt.png")));
        event.registerEntityRenderer(EntityInit.AQUA_BOLT_ENTITY.get(), ctx -> new BaseBoltRenderer<>(ctx, new ResourceLocation(HLSpells.MODID, "textures/entity/bolt/blue_bolt.png")));
        event.registerEntityRenderer(EntityInit.SUMMONED_VEX_ENTITY.get(), VexRenderer::new);
    }
}
