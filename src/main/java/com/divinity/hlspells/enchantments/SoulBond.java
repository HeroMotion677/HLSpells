package com.divinity.hlspells.enchantments;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.setup.init.EnchantmentInit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(modid = HLSpells.MODID)
public class SoulBond {

    @SubscribeEvent
    public static void onEntityHit(LivingDamageEvent.Pre event) {
        if (event.getSource().getDirectEntity() instanceof Player player && !player.level().isClientSide()) {
            for (InteractionHand hand : InteractionHand.values()) {
                if (EnchantmentInit.getLevel(EnchantmentInit.SOUL_SYPHON, player.getItemInHand(hand)) > 0) {
                    if (player.getRandom().nextInt(4) == 1) player.heal(0.5F);
                    break;
                }
            }
        }
    }
}
