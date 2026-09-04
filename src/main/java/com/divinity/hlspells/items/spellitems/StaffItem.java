package com.divinity.hlspells.items.spellitems;

import com.divinity.hlspells.setup.init.EnchantmentInit;
import com.divinity.hlspells.setup.init.ItemInit;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;
import java.util.function.Supplier;

public class StaffItem extends SpellHoldingItem {

    private static final Set<ResourceKey<Enchantment>> ALLOWED_ENCHANTMENTS = Set.of(
            Enchantments.UNBREAKING, Enchantments.MENDING, Enchantments.SMITE, Enchantments.FIRE_ASPECT, EnchantmentInit.SOUL_BOND);

    private final boolean isGemAmethyst;
    private final double castDelay;
    private final double maxCastTime;
    private final Supplier<Ingredient> repairIngredient;

    public StaffItem(Properties properties, double damage, double attackSpeed, boolean canRepair, double castDelay, boolean isGemAmethyst, boolean isFireResistant, double maxCastTime, Supplier<Ingredient> repairIngredient) {
        super(properties.attributes(ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, damage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build()), false);
        this.castDelay = castDelay;
        this.isGemAmethyst = isGemAmethyst;
        this.maxCastTime = maxCastTime;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getEnchantmentValue() {
        return 4;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        if (pToRepair.getItem() == ItemInit.GOLDEN_STAFF.get() || pToRepair.getItem() == ItemInit.GOLDEN_STAFF_AMETHYST.get())
            return pRepair.is(Items.GOLD_INGOT);

        else if (pToRepair.getItem() == ItemInit.WOODEN_STAFF.get() || pToRepair.getItem() == ItemInit.WOODEN_STAFF_AMETHYST.get())
            return pRepair.is(ItemTags.PLANKS);

        else if (pToRepair.getItem() == ItemInit.NETHER_STAFF.get() || pToRepair.getItem() == ItemInit.NETHER_STAFF_AMETHYST.get())
            return pRepair.is(Items.NETHERITE_INGOT);

        return false;
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        for (ResourceKey<Enchantment> allowed : ALLOWED_ENCHANTMENTS) {
            if (enchantment.is(allowed)) {
                return true;
            }
        }
        return enchantment.value().isSupportedItem(stack);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pStack.hurtAndBreak(1, pAttacker, EquipmentSlot.MAINHAND);
        return true;
    }

    public double getCastDelay() {
        return this.castDelay;
    }

    public double getMaxCastTime() {
        return this.maxCastTime;
    }

    public boolean isGemAmethyst() {
        return isGemAmethyst;
    }
}
