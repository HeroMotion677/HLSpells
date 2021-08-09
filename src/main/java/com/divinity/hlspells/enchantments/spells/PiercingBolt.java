package com.divinity.hlspells.enchantments.spells;

import com.divinity.hlspells.enchantments.ISpell;
import com.divinity.hlspells.init.SpellInit;
import com.divinity.hlspells.items.SpellBookItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class PiercingBolt extends Enchantment implements ISpell
{
    public PiercingBolt(EquipmentSlotType... slots)
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
    public boolean canApplyAtEnchantingTable(ItemStack stack)
    {
        return stack.getItem() instanceof SpellBookItem;
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
    public String getSpellRegistryName ()
    {
        ResourceLocation registryName = SpellInit.PIERCING_BOLT.get().getRegistryName();
        return registryName != null ? registryName.toString() : "null";
    }
}
