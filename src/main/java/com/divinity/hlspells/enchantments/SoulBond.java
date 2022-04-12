package com.divinity.hlspells.enchantments;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.EquipmentSlot;

import net.minecraft.world.item.enchantment.Enchantment.Rarity;

public class SoulBond extends Enchantment {
    public SoulBond(EquipmentSlot... slots) {
        super(Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE, slots);
    }

    @Override
    protected boolean checkCompatibility(Enchantment enchantment) {
        return enchantment != Enchantments.VANISHING_CURSE && enchantment != Enchantments.BINDING_CURSE;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
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