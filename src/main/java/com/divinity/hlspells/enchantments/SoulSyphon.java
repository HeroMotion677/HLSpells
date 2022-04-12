package com.divinity.hlspells.enchantments;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

import net.minecraft.world.item.enchantment.Enchantment.Rarity;

public class SoulSyphon extends Enchantment {
    public SoulSyphon(EquipmentSlot... slots) {
        super(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, slots);
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.getItem() instanceof SwordItem || stack.getItem() instanceof AxeItem || super.canEnchant(stack);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return stack.getItem() instanceof SwordItem || stack.getItem() instanceof AxeItem || super.canApplyAtEnchantingTable(stack);
    }

    @Override
    protected boolean checkCompatibility(Enchantment enchantment) {
        return enchantment != Enchantments.VANISHING_CURSE && enchantment != Enchantments.BINDING_CURSE;
    }

    @Override
    public boolean isTreasureOnly() {
        return false;
    }

    @Override
    public boolean isCurse() {
        return false;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return true;
    }
}
