package com.divinity.hlspells.compat;

import com.divinity.hlspells.items.totems.ITotem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.InterModComms;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.client.ICurioRenderer;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public final class CuriosCompat {

    private CuriosCompat() {} // To prevent any accidental instances from being created

    public static Optional<SlotResult> getItemInCuriosSlot(LivingEntity entity, Item item) {
        return CuriosApi.getCuriosInventory(entity).flatMap(handler -> handler.findFirstCurio(item));
    }

    public static Optional<SlotResult> getItemInCuriosSlot(LivingEntity entity, Predicate<ItemStack> filter) {
        return CuriosApi.getCuriosInventory(entity).flatMap(handler -> handler.findFirstCurio(filter));
    }

    public static Optional<ICurioStacksHandler> getStackHandler(LivingEntity entity) {
        return CuriosApi.getCuriosInventory(entity).flatMap(handler -> handler.getStacksHandler("charm"));
    }

    public static void sendImc() {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("charm").build());
    }

    public static ListTag getCuriosInv(Player player) {
        return CuriosApi.getCuriosInventory(player).map(handler -> handler.saveInventory(false)).orElseGet(ListTag::new);
    }

    public static void restoreCuriosInv(Player player, ListTag curiosNBT) {
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> handler.loadInventory(curiosNBT));
    }

    public static void renderCuriosTotems(List<DeferredItem<Item>> totemList) {
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
                Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.NONE, light, OverlayTexture.NO_OVERLAY, matrixStack, renderTypeBuffer, null, 1);
            }
        };
        totemList.forEach(totem -> CuriosRendererRegistry.register(totem.get(), () -> renderer));
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event, List<DeferredItem<Item>> totemList) {
        totemList.forEach(totem -> event.registerItem(CuriosCapability.ITEM, (stack, context) -> {
            if (!(stack.getItem() instanceof ITotem)) {
                return null;
            }
            return new ICurio() {
                @Override public ItemStack getStack() { return stack; }
                @Override public boolean canEquipFromUse(SlotContext ctx) { return true; }
            };
        }, totem.get()));
    }
}
