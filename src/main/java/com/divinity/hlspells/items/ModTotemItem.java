package com.divinity.hlspells.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.divinity.hlspells.events.entity.EntityDiesEvent.displayActivation;

public class ModTotemItem extends Item {
    public ModTotemItem() {
        super(new Item.Properties().tab(ItemGroup.TAB_COMBAT).stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    public static void vanillaTotemBehavior(PlayerEntity entity, ItemStack heldItem, Item animationItem) {
        heldItem.shrink(1);
        entity.setHealth(1.0F);
        entity.removeAllEffects();
        entity.addEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
        entity.addEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
        displayActivation(entity, animationItem, true);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
        entity.playSound(SoundEvents.TOTEM_USE, 1.0F, 1.0F);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(ItemStack itemstack) {
        return false;
    }
}