package com.divinity.hlspells.renderers;

import com.divinity.hlspells.models.BaseBoltModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ShulkerBulletRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ShulkerBullet;

public class BaseBoltRenderer<T extends Projectile> extends EntityRenderer<T> {
    private final ResourceLocation TEXTURE_LOCATION;
    private final RenderType RENDER_TYPE;
    private final BaseBoltModel<T> model;

    public BaseBoltRenderer(Context manager, ResourceLocation location) {
        super(manager);
        this.model = new BaseBoltModel<>(manager.bakeLayer(BaseBoltModel.LAYER_LOCATION));
        this.TEXTURE_LOCATION = location;
        this.RENDER_TYPE = RenderType.entityTranslucent(location);
    }

    @Override
    protected int getBlockLightLevel(T entity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public void render(T entity, float v1, float v2, PoseStack stack, MultiBufferSource buffer, int v3) {
        stack.pushPose();
        float f = Mth.rotLerp(entity.yRotO, entity.getYRot(), v2);
        float f1 = Mth.lerp(v2, entity.xRotO, entity.getXRot());
        float f2 = (float)entity.tickCount + v2;
        stack.translate(0.0D, (double)0.15F, 0.0D);
        stack.mulPose(Vector3f.YP.rotationDegrees(Mth.sin(f2 * 0.1F) * 180.0F));
        stack.mulPose(Vector3f.XP.rotationDegrees(Mth.cos(f2 * 0.1F) * 180.0F));
        stack.mulPose(Vector3f.ZP.rotationDegrees(Mth.sin(f2 * 0.15F) * 360.0F));
        stack.scale(-0.5F, -0.5F, 0.5F);
        this.model.setupAnim(entity, 0.0F, 0.0F, 0.0F, f, f1);
        VertexConsumer vertexconsumer = buffer.getBuffer(this.model.renderType(TEXTURE_LOCATION));
        this.model.renderToBuffer(stack, vertexconsumer, v3, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        stack.scale(1.5F, 1.5F, 1.5F);
        VertexConsumer vertexconsumer1 = buffer.getBuffer(RENDER_TYPE);
        this.model.renderToBuffer(stack, vertexconsumer1, v3, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.15F);
        stack.popPose();
        super.render(entity, v1, v2, stack, buffer, v3);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TEXTURE_LOCATION;
    }
}
