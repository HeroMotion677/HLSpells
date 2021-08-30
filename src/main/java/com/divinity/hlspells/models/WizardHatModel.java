package com.divinity.hlspells.models;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class WizardHatModel extends BipedModel<LivingEntity> {
    private ModelRenderer complete;
    private ModelRenderer hatBase;
    private ModelRenderer hatTop;
    private ModelRenderer hangingPart;

    public WizardHatModel(float scale) {
        super(scale, 0, 64, 128);

        complete = new ModelRenderer(this);
        complete.setPos(0.0F, -6.0F, 0.0F);

        hatBase = new ModelRenderer(this);
        hatBase.setPos(0.0F, 0.0F, 0.0F);
        setRotationAngle(hatBase, -0.1309F, 0.0F, 0.0F);
        hatBase.texOffs(0, 59).addBox(-6.0F, -1.0F, -6.25F, 12.0F, 2.0F, 12.0F, 0.0F, false);
        complete.addChild(hatBase);

        hatTop = new ModelRenderer(this);
        hatTop.setPos(0.0F, -0.25F, -0.5F);
        setRotationAngle(hatTop, -0.3054F, 0.0F, 0.0F);
        hatTop.texOffs(0, 73).addBox(-4.0F, -7.75F, -4.5F, 8.0F, 8.0F, 9.0F, 0.0F, false);

        hangingPart = new ModelRenderer(this);
        hangingPart.setPos(0.0F, -8.75F, 4.5F);
        setRotationAngle(hangingPart, -0.3491F, 0.0F, 0.0F);
        hangingPart.texOffs(27, 83).addBox(-2.5F, 0.95F, 0.35F, 5.0F, 4.0F, 7.0F, 0.0F, false);
        hatTop.addChild(hangingPart);

        complete.addChild(hatTop);
        this.head.addChild(this.complete);
    }

    private void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}