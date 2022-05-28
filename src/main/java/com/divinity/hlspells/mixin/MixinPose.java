package com.divinity.hlspells.mixin;

import com.divinity.hlspells.items.spellitems.SpellHoldingItem;
import com.divinity.hlspells.items.spellitems.StaffItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Keeping this here until PlayerRenderEvent allows you to change the rotation of the player's arm (currently it doesn't, so a mixin is the only way)
@Mixin(HumanoidModel.class)
public class MixinPose {

    @Shadow @Final public ModelPart rightArm;

    @Shadow @Final public ModelPart leftArm;

    @Shadow @Final public ModelPart head;

    @Shadow public HumanoidModel.ArmPose rightArmPose;

    @Shadow public HumanoidModel.ArmPose leftArmPose;

    @Inject(method = "poseRightArm(Lnet/minecraft/world/entity/LivingEntity;)V", at = @At(value = "TAIL"))
    public <T extends LivingEntity> void poseRightArm(T pLivingEntity, CallbackInfo ci) {
        if (this.rightArmPose == HumanoidModel.ArmPose.BOW_AND_ARROW) {
            Item useItem = pLivingEntity.getUseItem().getItem();
            if (useItem instanceof StaffItem) {
                this.rightArm.yRot = -0.4F;
                this.leftArm.yRot = 0.80F;
                this.rightArm.xRot = (-(float) Math.PI / 2F) + 0.2F;
                this.leftArm.xRot = (-(float) Math.PI / 2F) + 0.2F;
            }
            else if (useItem instanceof SpellHoldingItem item && item.isWand()) {
                this.rightArm.yRot = -0.1F + this.head.yRot;
                this.rightArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
                this.leftArm.xRot = 0;
                this.leftArm.yRot = 0;
            }
        }
    }

    @Inject(method = "poseLeftArm(Lnet/minecraft/world/entity/LivingEntity;)V", at = @At(value = "TAIL"))
    public <T extends LivingEntity> void poseLeftArm(T pLivingEntity, CallbackInfo ci) {
        if (this.leftArmPose == HumanoidModel.ArmPose.BOW_AND_ARROW) {
            Item useItem = pLivingEntity.getUseItem().getItem();
            if (useItem instanceof StaffItem) {
                this.rightArm.yRot = -0.4F;
                this.leftArm.yRot = 0.80F;
                this.rightArm.xRot = (-(float) Math.PI / 2F) + 0.2F;
                this.leftArm.xRot = (-(float) Math.PI / 2F) + 0.2F;
            }
            else if (useItem instanceof SpellHoldingItem item && item.isWand()) {
                this.leftArm.yRot = -0.1F + this.head.yRot;
                this.leftArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
                this.rightArm.xRot = 0;
                this.rightArm.yRot = 0;
            }
        }
    }
}
