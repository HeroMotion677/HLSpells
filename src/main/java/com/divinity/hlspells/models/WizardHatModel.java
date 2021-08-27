package com.divinity.hlspells.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.ArmorItem;

public class WizardHatModel<T extends LivingEntity> extends BipedModel<T> {
    private final ModelRenderer hat_main;
    private final ModelRenderer cube_r1;
    private final ModelRenderer main;
    private final ModelRenderer rotator;
    private final ModelRenderer p;

    public WizardHatModel(float f) {
        super(f);
        texWidth = 64;
        texHeight = 64;

        hat_main = new ModelRenderer(this);
        hat_main.setPos(0.0F, -6.0F, 0.0F);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(0.0F, 0.0F, 0.0F);
        hat_main.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.1309F, 0.0F, 0.0F);
        cube_r1.texOffs(0, 0).addBox(-6.0F, -1.0F, -6.25F, 12.0F, 2.0F, 12.0F, 0.0F, false);

        main = new ModelRenderer(this);
        main.setPos(0.0F, -0.25F, -0.5F);
        hat_main.addChild(main);
        setRotationAngle(main, -0.3054F, 0.0F, 0.0F);
        main.texOffs(0, 14).addBox(-3.5F, -6.75F, -4.5F, 7.0F, 7.0F, 9.0F, 0.0F, false);

        rotator = new ModelRenderer(this);
        rotator.setPos(0.0F, -8.75F, 4.5F);
        main.addChild(rotator);
        setRotationAngle(rotator, -0.3491F, 0.0F, 0.0F);
        rotator.texOffs(25, 23).addBox(-2.5F, 2.0F, 0.5F, 5.0F, 4.0F, 7.0F, 0.0F, false);

        p = new ModelRenderer(this);
        p.setPos(0.0F, 24.0F, 0.0F);
        p.texOffs(20, 0).addBox(-8.0F, -22.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
        p.texOffs(14, 0).addBox(-4.0F, -30.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        p.texOffs(19, 0).addBox(-4.0F, -10.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
        p.texOffs(19, 0).addBox(0.0F, -10.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
        p.texOffs(20, 0).addBox(4.0F, -22.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
        p.texOffs(14, 0).addBox(-4.0F, -22.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder iVertexBuilder, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        hat_main.render(matrixStack, iVertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
    }

    private void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}