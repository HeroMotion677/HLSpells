package com.divinity.hlspells.items;

import com.divinity.hlspells.HLSpells;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class ModTotemItem extends Item
{
    public ModTotemItem()
    {
        super(new Item.Properties().tab(ItemGroup.TAB_COMBAT).stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(ItemStack itemstack) {
        return true;
    }

    public static void vanillaTotemBehavior(LivingDeathEvent event, LivingEntity entity, ItemStack heldItem, Item animationItem)
    {

        event.setCanceled(true);
        heldItem.shrink(1);
        entity.setHealth(1.0F);
        entity.removeAllEffects();
        entity.addEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
        entity.addEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
        Minecraft.getInstance().particleEngine.createTrackingEmitter(entity, ParticleTypes.TOTEM_OF_UNDYING, 30);
        entity.level.playSound((PlayerEntity) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
        entity.playSound(SoundEvents.TOTEM_USE, 1.0F, 1.0F);
        Minecraft.getInstance().gameRenderer.displayItemActivation(new ItemStack(animationItem));
    }
}