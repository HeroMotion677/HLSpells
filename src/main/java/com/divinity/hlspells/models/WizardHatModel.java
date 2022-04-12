package com.divinity.hlspells.models;

import com.divinity.hlspells.HLSpells;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class WizardHatModel<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(HLSpells.MODID, "wizard_hat"), "main");
    private final ModelPart hat_main;

    public WizardHatModel(ModelPart root) {
        this.hat_main = root.getChild("hat_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition hat_main = partdefinition.addOrReplaceChild("hat_main", CubeListBuilder.create(),
                PartPose.offset(0.0F, -6.0F, 0.0F));
        hat_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 59)
                .addBox(-6.0F, -1F, -6.25F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, 0F, 0F, -0.1309F, 0.0F, 0.0F));
        PartDefinition main = hat_main.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 73)
                .addBox(-4.0F, -7.75F, -4.5F, 8.0F, 8.0F, 9.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0F, -0.25F, -0.5F,-0.3054F, 0.0F, 0.0F));
        main.addOrReplaceChild("rotator", CubeListBuilder.create().texOffs(27, 83)
                .addBox(-2.5F, 0.95F, 0.35F, 5.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation( 0.0F, -8.75F, 4.5F,-0.3491F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        hat_main.render(poseStack, buffer, packedLight, packedOverlay);
    }

    private void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}