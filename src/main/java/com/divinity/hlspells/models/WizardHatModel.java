package com.divinity.hlspells.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;

public class WizardHatModel extends BipedModel<LivingEntity> {
    private final ModelRenderer hat_main;
    private final ModelRenderer cube_r1;
    private final ModelRenderer main;
    private final ModelRenderer rotator;
    private final ModelRenderer p;

    public WizardHatModel(float f) {
        super(f);;
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

        this.head.addChild(this.hat_main);
    }

    private void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;

    }

    @Override
    public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity instanceof ArmorStandEntity) {
            ArmorStandEntity entityarmorstand = (ArmorStandEntity) entity;
            this.head.xRot = 0.017453292F * entityarmorstand.getHeadPose().getX();
            this.head.yRot = 0.017453292F * entityarmorstand.getHeadPose().getY();
            this.head.zRot = 0.017453292F * entityarmorstand.getHeadPose().getZ();
            this.body.setPos(0.0F, 1.0F, 0.0F);
            this.body.xRot = 0.017453292F * entityarmorstand.getBodyPose().getX();
            this.body.yRot = 0.017453292F * entityarmorstand.getBodyPose().getY();
            this.body.zRot = 0.017453292F * entityarmorstand.getBodyPose().getZ();
            this.leftArm.xRot = 0.017453292F * entityarmorstand.getLeftArmPose().getX();
            this.leftArm.yRot = 0.017453292F * entityarmorstand.getLeftArmPose().getY();
            this.leftArm.zRot = 0.017453292F * entityarmorstand.getLeftArmPose().getZ();
            this.rightArm.xRot = 0.017453292F * entityarmorstand.getRightArmPose().getX();
            this.rightArm.yRot = 0.017453292F * entityarmorstand.getRightArmPose().getY();
            this.rightArm.zRot = 0.017453292F * entityarmorstand.getRightArmPose().getZ();
            this.leftLeg.xRot = 0.017453292F * entityarmorstand.getLeftLegPose().getX();
            this.leftLeg.yRot = 0.017453292F * entityarmorstand.getLeftLegPose().getY();
            this.leftLeg.zRot = 0.017453292F * entityarmorstand.getLeftLegPose().getZ();
            this.leftLeg.setPos(1.9F, 11.0F, 0.0F);
            this.rightLeg.xRot = 0.017453292F * entityarmorstand.getRightLegPose().getX();
            this.rightLeg.yRot = 0.017453292F * entityarmorstand.getRightLegPose().getY();
            this.rightLeg.zRot = 0.017453292F * entityarmorstand.getRightLegPose().getZ();
            this.rightLeg.setPos(-1.9F, 11.0F, 0.0F);
            this.hat.copyFrom(this.head);
            } else {
                super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            }
    }
}