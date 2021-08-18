package com.divinity.hlspells.enchantments.spells;

import com.divinity.hlspells.enchantments.ISpell;
import com.divinity.hlspells.init.SpellBookInit;
import com.divinity.hlspells.init.SpellInit;
import com.divinity.hlspells.items.SpellBookItem;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class AquaBolt extends Enchantment implements ISpell {
    public AquaBolt(EquipmentSlotType... slots) {
        super(Rarity.COMMON, EnchantmentType.BREAKABLE, slots);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return SpellUtils.getSpellBook(stack) == SpellBookInit.EMPTY.get();
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.getItem() instanceof SpellBookItem || super.canEnchant(stack);
    }

    @Override
    public int getMinCost(int value) {
        return 1;
    }

    @Override
    public int getMaxCost(int value) {
        return 33;
    }

    @Override
    protected boolean checkCompatibility(Enchantment enchantment) {
        return !(enchantment instanceof ISpell);
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
    public String getSpellRegistryName() {
        ResourceLocation registryName = SpellInit.AQUA_BOLT.get().getRegistryName();
        return registryName != null ? registryName.toString() : "null";
    }
}
