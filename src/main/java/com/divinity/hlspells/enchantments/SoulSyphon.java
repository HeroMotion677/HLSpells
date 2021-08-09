package com.divinity.hlspells.enchantments;

import com.divinity.hlspells.init.SpellInit;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ResourceLocation;

public class SoulSyphon extends Enchantment
{
    public SoulSyphon(EquipmentSlotType... slots)
    {
        super(Rarity.VERY_RARE, EnchantmentType.WEAPON, slots);
    }

    @Override
    public boolean canEnchant(ItemStack stack)
    {
        return stack.getItem() instanceof SwordItem || stack.getItem() instanceof AxeItem || super.canEnchant(stack);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack)
    {
        return stack.getItem() instanceof SwordItem || stack.getItem() instanceof AxeItem || super.canApplyAtEnchantingTable(stack);
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
