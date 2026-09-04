package com.divinity.hlspells.setup.init;

import com.divinity.hlspells.HLSpells;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import it.unimi.dsi.fastutil.objects.Object2IntMap;

public class EnchantmentInit {

    public static final ResourceKey<Enchantment> SOUL_BOND = key("soul_bond");
    public static final ResourceKey<Enchantment> SOUL_SYPHON = key("soul_syphon");
    public static final ResourceKey<Enchantment> CURSE_OF_SINKING = key("curse_of_sinking");

    private static ResourceKey<Enchantment> key(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(HLSpells.MODID, name));
    }

    public static int getLevel(ResourceKey<Enchantment> enchantment, ItemStack stack) {
        return getLevel(enchantment, stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY));
    }

    public static int getStoredLevel(ResourceKey<Enchantment> enchantment, ItemStack stack) {
        return getLevel(enchantment, stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY));
    }

    private static int getLevel(ResourceKey<Enchantment> enchantment, ItemEnchantments enchantments) {
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
            if (entry.getKey().is(enchantment)) {
                return entry.getIntValue();
            }
        }
        return 0;
    }
}
