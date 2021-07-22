package com.divinity.hlspells.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;

public class SoulBond extends Enchantment {

    public SoulBond(EquipmentSlotType... slots)
    {
        super(Enchantment.Rarity.UNCOMMON, EnchantmentType.BREAKABLE, slots);
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    protected boolean checkCompatibility(Enchantment ench)
    {
        if (ench == Enchantments.BINDING_CURSE)
            return false;
        return ench != Enchantments.VANISHING_CURSE;
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