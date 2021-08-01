package com.divinity.hlspells.models;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.projectile.ArrowEntity;

public class BaseBoltModel<T extends ArrowEntity> extends SegmentedModel<T>
{
    private final ModelRenderer model;

    public BaseBoltModel ()
    {
        this.texWidth = 42;
        this.texHeight = 20;
        this.model = new ModelRenderer(this);
        this.model.texOffs(0, 0).addBox(-4.0F, -4.0F, -1.0F, 8.0F, 8.0F, 2.0F, 0.0F);
        this.model.texOffs(0, 10).addBox(-1.0F, -4.0F, -4.0F, 2.0F, 8.0F, 8.0F, 0.0F);
        this.model.texOffs(20, 0).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 2.0F, 8.0F, 0.0F);
        this.model.setPos(0.0F, 0.0F, 0.0F);
    }

    @Override
    public void setupAnim(T Entity, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_)
    {
        this.model.yRot = p_225597_5_ * ((float)Math.PI / 180F);
        this.model.xRot = p_225597_6_ * ((float)Math.PI / 180F);
    }

    @Override
    public Iterable<ModelRenderer> parts()
    {
        return ImmutableList.of(this.model);
    }
}
