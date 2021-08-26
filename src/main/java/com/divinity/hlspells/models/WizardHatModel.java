package com.divinity.hlspells.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerModelPart;

public class WizardHatModel<T extends LivingEntity> extends BipedModel<T> {

    private final ModelRenderer hat_main;
    private final ModelRenderer cube_r1;
    private final ModelRenderer main;
    private final ModelRenderer rotator;

    public WizardHatModel(float f) {
        super(f, 0.0F, 64, 64);
        texWidth = 64;
        texHeight = 64;

        hat_main = new ModelRenderer(this);
        hat_main.setPos(0.0F, 22.0F, 0.0F);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(0.0F, 0.0F, 0.0F);
        hat_main.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.1309F, 0.0F, 0.0F);
        cube_r1.texOffs(0, 0).addBox(-7.0F, -1.0F, -6.75F, 14.0F, 2.0F, 14.0F, 0.0F, false);

        main = new ModelRenderer(this);
        main.setPos(0.0F, -0.25F, 0.25F);
        hat_main.addChild(main);
        setRotationAngle(main, -0.3054F, 0.0F, 0.0F);
        main.texOffs(0, 16).addBox(-4.5F, -8.75F, -5.5F, 9.0F, 9.0F, 10.0F, 0.0F, false);

        rotator = new ModelRenderer(this);
        rotator.setPos(0.0F, -8.75F, 4.5F);
        main.addChild(rotator);
        setRotationAngle(rotator, -0.3491F, 0.0F, 0.0F);
        rotator.texOffs(31, 28).addBox(-3.5F, 0.0F, 0.0F, 7.0F, 5.0F, 7.0F, 0.0F, false);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder iVertexBuilder, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        hat_main.render(matrixStack, iVertexBuilder, packedLight, packedOverlay);
    }

    private void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}