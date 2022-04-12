package com.divinity.hlspells.compat;

import com.divinity.hlspells.items.ModTotemItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.InterModComms;
import top.theillusivec4.curios.api.*;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import javax.annotation.Nonnull;
import java.util.Optional;

public class CuriosCompat {
    public static Optional<SlotResult> getItemInCuriosSlot(LivingEntity entity, Item item) {
        return CuriosApi.getCuriosHelper().findFirstCurio(entity, item);
    }

    public static Optional<ICurioStacksHandler> getStackHandler(LivingEntity entity) {
        return CuriosApi.getCuriosHelper().getCuriosHandler(entity).map(iCuriosItemHandler -> iCuriosItemHandler.getStacksHandler("charm")).orElse(Optional.empty());
    }

    public static void sendImc() {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("charm").build());
    }

    public static ListTag getCuriosInv(Player player) {
        ListTag list = new ListTag();
        LazyOptional<ICuriosItemHandler> optional = CuriosApi.getCuriosHelper().getCuriosHandler(player);
        if (optional.isPresent()) {
            list = optional.orElseGet(null).saveInventory(false);
        }
        return list;
    }

    public static void restoreCuriosInv(Player player, ListTag curiosNBT) {
        CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(handler -> handler.loadInventory(curiosNBT));
    }

    public static void attachCapabilities(AttachCapabilitiesEvent<ItemStack> evt) {
        if (evt.getObject().getItem() instanceof ModTotemItem) {
            ICurio curio = new ICurio() {
                @Override
                public ItemStack getStack() {
                    return evt.getObject();
                }

                @Override
                public boolean canEquipFromUse(SlotContext ctx) {
                    return true;
                }
            };
            ICapabilityProvider provider = new ICapabilityProvider() {
                private final LazyOptional<ICurio> instance = LazyOptional.of(() -> curio);

                @Nonnull
                @Override
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
                    return CuriosCapability.ITEM.orEmpty(cap, instance);
                }
                @Nonnull
                @Override
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
                    return getCapability(cap);
                }
            };
            evt.addCapability(CuriosCapability.ID_ITEM, provider);
        }
    }
}
