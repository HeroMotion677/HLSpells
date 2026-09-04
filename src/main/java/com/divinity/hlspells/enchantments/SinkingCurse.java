package com.divinity.hlspells.enchantments;

import com.divinity.hlspells.setup.init.EnchantmentInit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import static com.divinity.hlspells.HLSpells.MODID;

@EventBusSubscriber(modid = MODID)
public class SinkingCurse {

    @SubscribeEvent
    public static void onArmorTick(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity entity) {
            for (ItemStack stack : entity.getArmorSlots())  {
                if (EnchantmentInit.getLevel(EnchantmentInit.CURSE_OF_SINKING, stack) > 0) {
                    if (entity.isInWater() && entity.getDeltaMovement().y() > -0.2D) {
                        entity.setDeltaMovement(entity.getDeltaMovement().add(0, -0.2D, 0));
                    }
                }
            }
        }
    }
}
