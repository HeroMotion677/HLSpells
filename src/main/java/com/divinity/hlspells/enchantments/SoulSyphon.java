package com.divinity.hlspells.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

public class SoulSyphon extends Enchantment {
    public SoulSyphon(EquipmentSlotType... slots) {
        super(Rarity.VERY_RARE, EnchantmentType.WEAPON, slots);
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
