package com.divinity.hlspells.setup.client;

import com.divinity.hlspells.setup.init.ItemInit;
import com.divinity.hlspells.world.blocks.blockentities.AltarOfAttunementBE;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class AltarItemRenderer implements BlockEntityRenderer<AltarOfAttunementBE> {

    public AltarItemRenderer(BlockEntityRendererProvider.Context pContext) {
        super();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void render(AltarOfAttunementBE tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ItemStack itemToEnchant = tileEntityIn.itemHandler.getStackInSlot(3);
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5F, 1.15F, 0.5F);
        BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(itemToEnchant, tileEntityIn.getLevel(), null, 0);
        float hoverOffset = Mth.sin((tileEntityIn.time + partialTicks) / 10.0F) * 0.1F + 0.1F;
        float modelYScale = model.getTransforms().getTransform(ItemTransforms.TransformType.GROUND).scale.y();
        matrixStackIn.translate(0.0, hoverOffset + 0.25F * modelYScale, 0.0);
        matrixStackIn.mulPose(Vector3f.YP.rotation((tileEntityIn.time + partialTicks) / 20.0F));
        Minecraft.getInstance().getItemRenderer().render(itemToEnchant, ItemTransforms.TransformType.GROUND, false, matrixStackIn, bufferIn, combinedLightIn, OverlayTexture.NO_OVERLAY, model);
        matrixStackIn.popPose();
    }
}
