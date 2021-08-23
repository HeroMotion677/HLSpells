package com.divinity.hlspells.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Optional;

public class CuriosCompat {
    public static Optional<ImmutableTriple<String, Integer, ItemStack>> getItemInCuriosSlot(LivingEntity entity, Item item) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(item, entity);
    }
}
