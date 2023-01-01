package com.divinity.hlspells.mixin;

import com.divinity.hlspells.items.spellitems.SpellHoldingItem;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.util.SpellUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class MixinItemRenderer {

    @Shadow protected abstract void applyItemArmTransform(PoseStack pMatrixStack, HumanoidArm pHand, float pEquippedProg);

    @Shadow public abstract void renderItem(LivingEntity pLivingEntity, ItemStack pItemStack, ItemTransforms.TransformType pTransformType, boolean pLeftHand, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pCombinedLight);

    @Shadow protected abstract void applyItemArmAttackTransform(PoseStack pMatrixStack, HumanoidArm pHand, float pSwingProgress);

    @Inject(method = "renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "HEAD"), cancellable = true)
    public void renderArmWithItem(AbstractClientPlayer pPlayer, float pPartialTicks, float pPitch, InteractionHand pHand, float pSwingProgress, ItemStack pStack, float pEquippedProgress, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pCombinedLight, CallbackInfo ci) {
        if (!pPlayer.isScoping()) {
            boolean flag = pHand == InteractionHand.MAIN_HAND;
            HumanoidArm humanoidarm = flag ? pPlayer.getMainArm() : pPlayer.getMainArm().getOpposite();
            boolean flag3 = humanoidarm == HumanoidArm.RIGHT;
            if (pStack.getItem() instanceof SpellHoldingItem item) {
                if (SpellUtils.getSpell(pPlayer.getUseItem()) == SpellInit.PHASING_II.get()) {
                    ci.cancel();
                }
                else if (!item.isSpellBook()) {
                    pMatrixStack.pushPose();
                    if (pPlayer.isUsingItem() && pPlayer.getUseItemRemainingTicks() > 0 && pPlayer.getUsedItemHand() == pHand) {
                        this.applyItemArmTransform(pMatrixStack, humanoidarm, 0);
                    }
                    else if (pPlayer.isAutoSpinAttack()) {
                        this.applyItemArmTransform(pMatrixStack, humanoidarm, pEquippedProgress);
                        int j = flag3 ? 1 : -1;
                        pMatrixStack.translate((float) j * -0.4F, 0.8F, 0.3F);
                        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees((float) j * 65.0F));
                        pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees((float) j * -85.0F));
                    }
                    else {
                        float f5 = -0.4F * Mth.sin(Mth.sqrt(pSwingProgress) * (float) Math.PI);
                        float f6 = 0.2F * Mth.sin(Mth.sqrt(pSwingProgress) * ((float) Math.PI * 2F));
                        float f10 = -0.2F * Mth.sin(pSwingProgress * (float) Math.PI);
                        int l = flag3 ? 1 : -1;
                        pMatrixStack.translate((float) l * f5, f6, f10);
                        this.applyItemArmTransform(pMatrixStack, humanoidarm, pEquippedProgress);
                        this.applyItemArmAttackTransform(pMatrixStack, humanoidarm, pSwingProgress);
                    }
                    this.renderItem(pPlayer, pStack, flag3 ? ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !flag3, pMatrixStack, pBuffer, pCombinedLight);
                    pMatrixStack.popPose();
                    ci.cancel();
                }
            }
        }
    }
}
