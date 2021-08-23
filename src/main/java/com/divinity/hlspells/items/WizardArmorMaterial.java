package com.divinity.hlspells.items;


import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

import static com.divinity.hlspells.HLSpells.MODID;

public enum WizardArmorMaterial implements IArmorMaterial {
    WIZHAT("wizhat", 5, new int[] {0, 0, 0, 0}, 22, SoundEvents.ARMOR_EQUIP_LEATHER, 0F, 0.0F, () -> {
        return Ingredient.of(Items.LEATHER);
    });
    private static final int[] HEALTH_PER_SLOT = new int[]{1, 1, 1, 11};
    private final String name;
    private final int durabilityMultiplier;
    private final int[] slotProtections;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyValue<Ingredient> repairIngredient;

    WizardArmorMaterial(String nameIn, int maxDamageFactor, int[] damageReductionAmountIn, int enchantabilityIn, SoundEvent soundIn, float toughnessIn, float knockbackResistanceIn, Supplier<Ingredient> repairMaterialIn) {
        this.name = nameIn;
        this.durabilityMultiplier = maxDamageFactor;
        this.slotProtections = damageReductionAmountIn;
        this.enchantmentValue = enchantabilityIn;
        this.sound = soundIn;
        this.toughness = toughnessIn;
        this.knockbackResistance = knockbackResistanceIn;
        this.repairIngredient = new LazyValue<>(repairMaterialIn);
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlotType type) {
        return HEALTH_PER_SLOT[type.getIndex()] * this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForSlot(EquipmentSlotType type) {
        return this.slotProtections[type.getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.sound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return MODID + ":" + this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
