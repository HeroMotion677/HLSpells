package com.divinity.hlspells.items.armor.material;


import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static com.divinity.hlspells.HLSpells.MODID;

@SuppressWarnings("deprecation")
public enum WizardArmorMaterial implements ArmorMaterial {

    WIZHAT("wizhat", 8, new int[]{0, 0, 0, 1}, 22, SoundEvents.ARMOR_EQUIP_LEATHER, 0F, 0.0F, () -> {
        return Ingredient.of(Items.LEATHER);
    });
    private static final int[] HEALTH_PER_SLOT = new int[]{2, 2, 2, 20};
    private final String name;
    private final int durabilityMultiplier;
    private final int[] slotProtections;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    WizardArmorMaterial(String name, int maxDamageFactor, int[] damageReductionAmount, int enchantability, SoundEvent sound, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterial) {
        this.name = name;
        this.durabilityMultiplier = maxDamageFactor;
        this.slotProtections = damageReductionAmount;
        this.enchantmentValue = enchantability;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = new LazyLoadedValue<>(repairMaterial);
    }

    @Override @NotNull public SoundEvent getEquipSound() {
        return this.sound;
    }

    @Override @NotNull public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override @NotNull public String getName() {
        return MODID + ":" + this.name;
    }

    @Override public int getDurabilityForSlot(EquipmentSlot type) { return HEALTH_PER_SLOT[type.getIndex()] * this.durabilityMultiplier; }

    @Override public int getDefenseForSlot(EquipmentSlot type) {
        return this.slotProtections[type.getIndex()];
    }

    @Override public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override public float getToughness() {
        return this.toughness;
    }

    @Override public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
