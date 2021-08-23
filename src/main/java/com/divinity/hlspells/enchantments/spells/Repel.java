package com.divinity.hlspells.enchantments.spells;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.enchantments.ISpell;
import com.divinity.hlspells.init.ItemInit;
import com.divinity.hlspells.init.SpellInit;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class Repel extends Enchantment implements ISpell {
    public Repel(EquipmentSlotType... slots) {
        super(Rarity.VERY_RARE, EnchantmentType.BREAKABLE, slots);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return SpellUtils.getSpell(stack) == SpellInit.EMPTY.get();
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.getItem() == ItemInit.SPELL_BOOK.get() || super.canEnchant(stack);
    }

    @Override
    public int getMinCost(int value) {
        return 20;
    }

    @Override
    public int getMaxCost(int value) {
        return 33;
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
        ResourceLocation registryName = SpellInit.REPEL.get().getRegistryName();
        return registryName != null ? registryName.toString() : "null";
    }

    @Override
    public boolean isDiscoverable() {
        return !HLSpells.CONFIG.lootOnlyMode.get();
    }
}
