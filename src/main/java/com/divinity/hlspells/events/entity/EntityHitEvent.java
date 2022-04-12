package com.divinity.hlspells.events.entity;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.init.EnchantmentInit;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class EntityHitEvent {
    @SubscribeEvent
    public static void onEntityHit(LivingHurtEvent event) {
        if (event.getSource().getDirectEntity() instanceof Player player && event.getEntity() != null && !event.getSource().getDirectEntity().level.isClientSide()) {
            if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.SOUL_SYPHON.get(), player.getMainHandItem()) > 0 ||
                    EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.SOUL_SYPHON.get(), player.getOffhandItem()) > 0) {
                if (player.getRandom().nextInt(4) == 1) {
                    player.heal(0.5F);
                }
            }
        }
    }
}
