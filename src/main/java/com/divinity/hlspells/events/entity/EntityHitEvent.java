package com.divinity.hlspells.events.entity;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.init.EnchantmentInit;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class EntityHitEvent
{
    @SubscribeEvent
    public static void onEntityHit (LivingHurtEvent event)
    {
        if (event.getSource().getDirectEntity() instanceof PlayerEntity && event.getEntity() != null && !event.getSource().getDirectEntity().level.isClientSide())
        {
            PlayerEntity player = (PlayerEntity) event.getSource().getDirectEntity();
            if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.SOUL_SYPHON.get(), player.getMainHandItem()) > 0 ||
                    EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.SOUL_SYPHON.get(), player.getOffhandItem()) > 0)
            {
                if (player.getRandom().nextInt(4) == 1)
                {
                    player.heal(0.5F);
                }
            }
        }
    }
}
