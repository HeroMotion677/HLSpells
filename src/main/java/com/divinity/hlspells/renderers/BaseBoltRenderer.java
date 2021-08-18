package com.divinity.hlspells.renderers;

import com.divinity.hlspells.models.BaseBoltModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class BaseBoltRenderer<T extends ArrowEntity> extends EntityRenderer<T> {
    private final ResourceLocation TEXTURE_LOCATION;
    private final RenderType RENDER_TYPE;
    private final BaseBoltModel<T> model = new BaseBoltModel<>();

    public BaseBoltRenderer(EntityRendererManager manager, ResourceLocation location) {
        super(manager);
        this.TEXTURE_LOCATION = location;
        this.RENDER_TYPE = RenderType.entityTranslucent(location);
    }

    @Override
    protected int getBlockLightLevel(T entity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public void render(T entity, float v1, float v2, MatrixStack stack, IRenderTypeBuffer buffer, int v3) {
        stack.pushPose();
        float f = MathHelper.rotLerp(entity.yRotO, entity.yRot, v2);
        float f1 = MathHelper.lerp(v2, entity.xRotO, entity.xRot);
        float f2 = entity.tickCount + v2;
        stack.translate(0.0D, 0.15F, 0.0D);
        stack.mulPose(Vector3f.YP.rotationDegrees(MathHelper.sin(f2 * 0.1F) * 180.0F));
        stack.mulPose(Vector3f.XP.rotationDegrees(MathHelper.cos(f2 * 0.1F) * 180.0F));
        stack.mulPose(Vector3f.ZP.rotationDegrees(MathHelper.sin(f2 * 0.15F) * 360.0F));
        stack.scale(-0.5F, -0.5F, 0.5F);
        this.model.setupAnim(entity, 0.0F, 0.0F, 0.0F, f, f1);
        IVertexBuilder iVertexBuilder = buffer.getBuffer(this.model.renderType(TEXTURE_LOCATION));
        this.model.renderToBuffer(stack, iVertexBuilder, v3, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        stack.scale(1.5F, 1.5F, 1.5F);
        IVertexBuilder iVertexBuilder1 = buffer.getBuffer(RENDER_TYPE);
        this.model.renderToBuffer(stack, iVertexBuilder1, v3, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.15F);
        stack.popPose();
        super.render(entity, v1, v2, stack, buffer, v3);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TEXTURE_LOCATION;
    }
}
