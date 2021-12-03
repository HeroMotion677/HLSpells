package com.divinity.hlspells.compat;

import com.divinity.hlspells.items.ModTotemItem;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.InterModComms;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class CuriosCompat {
    public static Optional<ImmutableTriple<String, Integer, ItemStack>> getItemInCuriosSlot(LivingEntity entity, Item item) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(item, entity);
    }

    public static Optional<ICurioStacksHandler> getStackHandler(LivingEntity entity) {
        return CuriosApi.getCuriosHelper().getCuriosHandler(entity).map(iCuriosItemHandler -> iCuriosItemHandler.getStacksHandler("charm")).orElse(Optional.empty());
    }

    public static void sendImc() {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("charm").build());
    }

    public static ListNBT getCuriosInv(PlayerEntity player) {
        ListNBT list = new ListNBT();
        LazyOptional<ICuriosItemHandler> optional = CuriosApi.getCuriosHelper().getCuriosHandler(player);
        if (optional.isPresent()) {
            list = optional.orElseGet(null).saveInventory(false);
        }
        return list;
    }

    public static void restoreCuriosInv(PlayerEntity player, ListNBT curiosNBT) {
        CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(handler -> handler.loadInventory(curiosNBT));
    }

    public static void attachCapabilities(AttachCapabilitiesEvent<ItemStack> evt) {
        if (evt.getObject().getItem() instanceof ModTotemItem) {
            ICurio curio = new ICurio() {

                @Override
                public boolean canEquipFromUse(SlotContext ctx) {
                    return true;
                }

                @Override
                public boolean canRender(String identifier, int index, LivingEntity livingEntity) {
                    return true;
                }

                @Override
                public void render(String identifier, int index, MatrixStack matrixStack,
                                   IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity,
                                   float limbSwing,
                                   float limbSwingAmount, float partialTicks, float ageInTicks,
                                   float netHeadYaw,
                                   float headPitch) {
                    ICurio.RenderHelper.translateIfSneaking(matrixStack, livingEntity);
                    ICurio.RenderHelper.rotateIfSneaking(matrixStack, livingEntity);
                    matrixStack.scale(0.35F, 0.35F, 0.35F);
                    matrixStack.translate(0.0F, 0.5F, -0.4F);
                    matrixStack.mulPose(Direction.DOWN.getRotation());
                    Minecraft.getInstance().getItemRenderer()
                            .renderStatic(evt.getObject(), ItemCameraTransforms.TransformType.NONE, light, OverlayTexture.NO_OVERLAY,
                                    matrixStack, renderTypeBuffer);
                }
            };
            ICapabilityProvider provider = new ICapabilityProvider() {
                private final LazyOptional<ICurio> curioOpt = LazyOptional.of(() -> curio);

                @Nonnull
                @Override
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
                                                         @Nullable Direction side) {
                    return CuriosCapability.ITEM.orEmpty(cap, curioOpt);
                }
            };
            evt.addCapability(CuriosCapability.ID_ITEM, provider);
        }
    }
}
