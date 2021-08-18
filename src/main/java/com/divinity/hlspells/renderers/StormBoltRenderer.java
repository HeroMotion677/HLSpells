package com.divinity.hlspells.renderers;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.entities.InvisibleTargetingEntity;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class StormBoltRenderer extends EntityRenderer<InvisibleTargetingEntity> {
    public StormBoltRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    public boolean shouldRender(InvisibleTargetingEntity pLivingEntity, ClippingHelper pCamera, double pCamX, double pCamY, double pCamZ) {
        return false;
    }

    @Override
    public ResourceLocation getTextureLocation(InvisibleTargetingEntity pEntity) {
        return new ResourceLocation(HLSpells.MODID, "textures");
    }
}
