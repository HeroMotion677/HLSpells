package com.divinity.hlspells.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;

public class SoulBond extends Enchantment {
    public SoulBond(EquipmentSlotType... slots) {
        super(Rarity.VERY_RARE, EnchantmentType.BREAKABLE, slots);
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