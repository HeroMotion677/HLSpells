package com.divinity.hlspells.enchantments.spells;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.enchantments.ISpell;
import com.divinity.hlspells.init.ItemInit;
import com.divinity.hlspells.init.SpellInit;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.item.enchantment.Enchantment.Rarity;

public class Pull extends Enchantment implements ISpell {
    public Pull(EquipmentSlot... slots) {
        super(Rarity.COMMON, EnchantmentCategory.BREAKABLE, slots);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return stack.getItem() == ItemInit.SPELL_BOOK.get() && SpellUtils.getSpell(stack) == SpellInit.EMPTY.get();
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.getItem() == ItemInit.SPELL_BOOK.get() && SpellUtils.getSpell(stack) == SpellInit.EMPTY.get();
    }

    @Override
    public int getMinCost(int value) {
        return 1;
    }

    @Override
    public int getMaxCost(int value) {
        return 10;
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
        return false;
    }

    @Override
    protected boolean checkCompatibility(Enchantment enchantment) {
        return !(enchantment instanceof ISpell) && super.checkCompatibility(enchantment);
    }

    @Override
    public String getSpellRegistryName() {
        ResourceLocation registryName = SpellInit.PULL.get().getRegistryName();
        return registryName != null ? registryName.toString() : "null";
    }

    @Override
    public boolean isDiscoverable() {
        return !HLSpells.CONFIG.lootOnlyMode.get();
    }
}
