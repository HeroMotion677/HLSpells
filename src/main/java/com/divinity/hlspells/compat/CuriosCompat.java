package com.divinity.hlspells.compat;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;

import java.util.Optional;

public class CuriosCompat {
    public static Optional<ImmutableTriple<String, Integer, ItemStack>> getItemInCuriosSlot(LivingEntity entity, Item item) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(item, entity);
    }

    public static Optional<ImmutableTriple<String, Integer, ItemStack>> getStackInCuriosSlot(LivingEntity entity, ItemStack stack) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.equals(stack), entity);
    }

    public static LazyOptional<IItemHandlerModifiable> getCuriosHandler(LivingEntity livingEntity) {
        return CuriosApi.getCuriosHelper().getEquippedCurios(livingEntity);
    }

    public static void sendImc() {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("charm").build());
    }

    public static ListNBT getCuriosInv(PlayerEntity player) {
        ListNBT list = new ListNBT();
        CuriosApi.getCuriosHelper().getCuriosHandler(player)
                .ifPresent(curioHandler -> curioHandler.getCurios().forEach((id, stackHandler) -> {
                    ListNBT list1 = new ListNBT();
                    ListNBT list2 = new ListNBT();

                    for (int i = 0; i < stackHandler.getSlots(); i++) {
                        CompoundNBT stack = new CompoundNBT();
                        stackHandler.getStacks().getStackInSlot(i).save(stack);
                        stack.putInt("Slot", i);
                        list1.add(stack);
                        CompoundNBT cosmeticStack = new CompoundNBT();
                        stackHandler.getCosmeticStacks().getStackInSlot(i).save(cosmeticStack);
                        cosmeticStack.putInt("Slot", i);
                        list2.add(cosmeticStack);
                    }
                    CompoundNBT tag = new CompoundNBT();
                    tag.putString("Identifier", id);
                    tag.put("Stacks", list1);
                    tag.put("CosmeticStacks", list2);
                    list.add(tag);
                }));
        return list;
    }

    public static void restoreCuriosInv(PlayerEntity player, ListNBT curiosNBT) {
        CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(handler -> {
            for (int i = 0; i < curiosNBT.size(); i++) {
                CompoundNBT tag = curiosNBT.getCompound(i);
                String id = tag.getString("Identifier");
                handler.getStacksHandler(id).ifPresent(stacksHandler -> {
                    ListNBT stacks = tag.getList("Stacks", Constants.NBT.TAG_COMPOUND);
                    for (int j = 0; j < stacks.size(); j++) {
                        CompoundNBT compoundnbt = stacks.getCompound(j);
                        int slot = compoundnbt.getInt("Slot");
                        ItemStack itemstack = ItemStack.of(compoundnbt);
                        if (!itemstack.isEmpty()) {
                            stacksHandler.getStacks().setStackInSlot(slot, itemstack);
                            CuriosApi.getCuriosHelper().getCurio(itemstack).ifPresent((curio) -> player.getAttributes()
                                    .addTransientAttributeModifiers(curio.getAttributeModifiers(id)));
                        }
                    }
                    ListNBT cosmeticStacks = tag.getList("CosmeticStacks", Constants.NBT.TAG_COMPOUND);
                    for (int j = 0; j < cosmeticStacks.size(); j++) {
                        CompoundNBT compoundnbt = stacks.getCompound(j);
                        int slot = compoundnbt.getInt("Slot");
                        ItemStack itemstack = ItemStack.of(compoundnbt);
                        if (!itemstack.isEmpty()) {
                            stacksHandler.getCosmeticStacks().setStackInSlot(slot, itemstack);
                        }
                    }
                });
            }
        });
    }
}
