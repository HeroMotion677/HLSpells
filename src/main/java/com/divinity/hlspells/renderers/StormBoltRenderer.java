package com.divinity.hlspells.renderers;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.entities.InvisibleTargetingEntity;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class StormBoltRenderer extends EntityRenderer<InvisibleTargetingEntity> {
    public StormBoltRenderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    @Override
    public boolean shouldRender(InvisibleTargetingEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return false;
    }

    @Override
    public ResourceLocation getTextureLocation(InvisibleTargetingEntity pEntity) {
        return new ResourceLocation("");
    }
}
