package com.divinity.hlspells.setup.init;


import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.enchantments.SinkingCurse;
import com.divinity.hlspells.enchantments.SoulBond;
import com.divinity.hlspells.enchantments.SoulSyphon;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EnchantmentInit {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, HLSpells.MODID);

    public static final RegistryObject<Enchantment> SOUL_BOND = ENCHANTMENTS.register("soul_bond", () -> new SoulBond(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> SOUL_SYPHON = ENCHANTMENTS.register("soul_syphon", () -> new SoulSyphon(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> CURSE_OF_SINKING = ENCHANTMENTS.register("curse_of_sinking", () -> new SinkingCurse(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS,  EquipmentSlot.FEET));
}