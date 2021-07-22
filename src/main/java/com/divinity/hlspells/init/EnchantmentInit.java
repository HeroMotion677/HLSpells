package com.divinity.hlspells.init;


import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.enchantments.SoulBond;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantmentInit
{

    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS,
            HLSpells.MODID);

    public static final RegistryObject<Enchantment> SOUL_BOND = ENCHANTMENTS.register("soul_bond", () -> new SoulBond(EquipmentSlotType.MAINHAND));
}