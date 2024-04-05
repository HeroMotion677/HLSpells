package com.divinity.hlspells.client.renderers;
import com.divinity.hlspells.client.models.FireballModel;
import com.divinity.hlspells.events.ModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class FireballRenderer<T extends Projectile> extends EntityRenderer<T> {

    private final ResourceLocation TEXTURE_LOCATION;
    private final RenderType RENDER_TYPE;
    private final FireballModel<T> model;

    public FireballRenderer(Context manager, ResourceLocation location) {
        super(manager);
        this.model = new FireballModel<>(manager.bakeLayer(ModelLayers.FIRE_BALL_LAYER));
        this.TEXTURE_LOCATION = location;
        this.RENDER_TYPE = RenderType.entityTranslucent(location);
    }

    @Override
    public void render(T entity, float v1, float v2, PoseStack stack, MultiBufferSource buffer, int v3) {
        stack.pushPose(); // Needed
        float f = Mth.rotLerp(entity.yRotO, entity.getYRot(), v2);
        float f1 = Mth.lerp(v2, entity.xRotO, entity.getXRot());
        float f2 = (float) entity.tickCount + v2;
        //
        stack.translate(0.0D, 0.0F, 0.0D);
        stack.mulPose(Vector3f.YP.rotationDegrees(Mth.sin(f2 * 0.1F) * 180.0F));
        stack.mulPose(Vector3f.XP.rotationDegrees(Mth.cos(f2 * 0.1F) * 180.0F));
        stack.mulPose(Vector3f.ZP.rotationDegrees(Mth.sin(f2 * 0.15F) * 360.0F));
        stack.scale(-0.5F, -0.5F, 0.5F);
        //
        this.model.setupAnim(entity, 0.0F, 0.0F, 0.0F, f, f1); // Needed
        VertexConsumer vertexConsumer = buffer.getBuffer(this.model.renderType(TEXTURE_LOCATION)); // Needed
        this.model.renderToBuffer(stack, vertexConsumer, v3, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        stack.scale(5F, 5F, 5F);

        stack.popPose(); // Needed
        super.render(entity, v1, v2, stack, buffer, v3);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected int getBlockLightLevel(T entity, BlockPos blockPos) {
        return 15;
    }

    @Override
    @NotNull
    public ResourceLocation getTextureLocation(@NotNull T entity) {
        return this.TEXTURE_LOCATION;
    }
}