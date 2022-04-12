package com.divinity.hlspells.renderers;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.init.EntityInit;
import com.divinity.hlspells.models.BaseBoltModel;
import com.divinity.hlspells.models.WizardHatModel;
import net.minecraft.client.renderer.entity.VexRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HLSpells.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RenderRegistry {

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BaseBoltModel.LAYER_LOCATION, BaseBoltModel::createBodyLayer);
        event.registerLayerDefinition(WizardHatModel.LAYER_LOCATION, WizardHatModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.INVISIBLE_TARGETING_ENTITY.get(), StormBoltRenderer::new);
        event.registerEntityRenderer(EntityInit.PIERCING_BOLT_ENTITY.get(), manager -> new BaseBoltRenderer<>(manager, new ResourceLocation(HLSpells.MODID, "textures/entity/bolt/green_bolt.png")));
        event.registerEntityRenderer(EntityInit.FLAMING_BOLT_ENTITY.get(), manager -> new BaseBoltRenderer<>(manager, new ResourceLocation(HLSpells.MODID, "textures/entity/bolt/orange_bolt.png")));
        event.registerEntityRenderer(EntityInit.AQUA_BOLT_ENTITY.get(), manager -> new BaseBoltRenderer<>(manager, new ResourceLocation(HLSpells.MODID, "textures/entity/bolt/blue_bolt.png")));
        event.registerEntityRenderer(EntityInit.SUMMONED_VEX_ENTITY.get(), VexRenderer::new);
    }
}
