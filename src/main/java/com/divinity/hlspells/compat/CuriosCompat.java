package com.divinity.hlspells.compat;

import com.divinity.hlspells.items.totems.ITotem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.curios.api.*;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.client.ICurioRenderer;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public final class CuriosCompat {

    private CuriosCompat() {} // To prevent any accidental instances from being created

    public static Optional<SlotResult> getItemInCuriosSlot(LivingEntity entity, Item item) {
        return CuriosApi.getCuriosHelper().findFirstCurio(entity, item);
    }

    public static Optional<SlotResult> getItemInCuriosSlot(LivingEntity entity, Predicate<ItemStack> filter) {
        return CuriosApi.getCuriosHelper().findFirstCurio(entity, filter);
    }

    public static Optional<ICurioStacksHandler> getStackHandler(LivingEntity entity) {
        return CuriosApi.getCuriosHelper().getCuriosHandler(entity).map(iCuriosItemHandler -> iCuriosItemHandler.getStacksHandler("charm")).orElse(Optional.empty());
    }

    public static void sendImc() {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("charm").build());
    }

    @SuppressWarnings("ConstantConditions")
    public static ListTag getCuriosInv(Player player) {
        ListTag list = new ListTag();
        var optional = CuriosApi.getCuriosHelper().getCuriosHandler(player);
        if (optional.isPresent()) {
            // If optional is present the orElseGet won't be called anyways, so it's fine to pass null here
            list = optional.orElseGet(null).saveInventory(false);
        }
        return list;
    }

    public static void restoreCuriosInv(Player player, ListTag curiosNBT) {
        CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(handler -> handler.loadInventory(curiosNBT));
    }

    public static void renderCuriosTotems(List<RegistryObject<Item>> totemList) {
        ICurioRenderer renderer = new ICurioRenderer() {
            @Override
            public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                LocalPlayer clientPlayer = Minecraft.getInstance().player;
                if (clientPlayer != null) {
                    ICurioRenderer.translateIfSneaking(matrixStack, clientPlayer);
                    ICurioRenderer.rotateIfSneaking(matrixStack, clientPlayer);
                }
                matrixStack.scale(0.35F, 0.35F, 0.35F);
                matrixStack.translate(0.0F, 0.5F, -0.4F);
                matrixStack.mulPose(Direction.DOWN.getRotation());
                Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.NONE, light, OverlayTexture.NO_OVERLAY, matrixStack, renderTypeBuffer, 1);
            }
        };
        totemList.forEach(totem -> CuriosRendererRegistry.register(totem.get(), () -> renderer));
    }

    public static void attachCapabilities(AttachCapabilitiesEvent<ItemStack> evt) {
        if (evt.getObject().getItem() instanceof ITotem) {
            ICurio curio = new ICurio() {
                @Override public ItemStack getStack() { return evt.getObject(); }
                @Override public boolean canEquipFromUse(SlotContext ctx) { return true; }
            };
            ICapabilityProvider provider = new ICapabilityProvider() {
                private final LazyOptional<ICurio> instance = LazyOptional.of(() -> curio);
                @Nonnull @Override public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) { return CuriosCapability.ITEM.orEmpty(cap, instance); }
                @Nonnull @Override public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) { return this.getCapability(cap); }
            };
            evt.addCapability(CuriosCapability.ID_ITEM, provider);
        }
    }
}
