package com.divinity.hlspells.items.spellitems;

import com.divinity.hlspells.setup.init.EnchantmentInit;
import com.divinity.hlspells.setup.init.ItemInit;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.function.Supplier;

public class StaffItem extends SpellHoldingItem {

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    private final boolean isGemAmethyst;
    private final double castDelay;

    private final double maxCastTime;

    public StaffItem(Properties properties, double damage, double attackSpeed, boolean canRepair, double castDelay, boolean isGemAmethyst, boolean isFireResistant, double maxCastTime, Supplier<Ingredient> repairIngredient) {
        super(properties, false);
        this.castDelay = castDelay;
        this.isGemAmethyst = isGemAmethyst;
        this.maxCastTime = maxCastTime;
        this.repairIngredient = repairIngredient;


        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", damage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    public int getEnchantmentValue() {
        return 1;
    }


    private final Supplier<Ingredient> repairIngredient;

    public Ingredient getRepairIngredient() {

        return this.repairIngredient.get();
    }

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
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getAttributeModifiers(slot, stack);
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        if (EnchantedBookItem.getEnchantments(book).size() > 0) {
            for (int i = 0; i < EnchantedBookItem.getEnchantments(book).size(); i++) {
                CompoundTag tag = EnchantedBookItem.getEnchantments(book).getCompound(i);
                ResourceLocation enchantment = EnchantmentHelper.getEnchantmentId(tag);
                if (enchantment != null) {
                    switch (enchantment.toString()) {
                        case "minecraft:mending":
                            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MENDING, stack) >= 1) {
                                return stack.getItem() instanceof StaffItem;
                            }
                        case "minecraft:unbreaking":
                            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack) <= EnchantmentHelper.getEnchantmentLevel(tag)) {
                                if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack) != 3) {
                                    return stack.getItem() instanceof StaffItem;
                                }
                            }
                        case "hlspells:soul_bond":
                            if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.SOUL_BOND.get(), stack) >= 0) {
                                return stack.getItem() instanceof StaffItem;
                            }
                            break;
                    }
                }
            }
        }
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pStack.hurtAndBreak(1, pAttacker, livingEntity -> livingEntity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
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
