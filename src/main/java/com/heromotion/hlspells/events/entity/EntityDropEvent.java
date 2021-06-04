package com.heromotion.hlspells.events.entity;

import com.heromotion.hlspells.HLSpells;
import com.heromotion.hlspells.init.EnchantmentInit;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;

@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class EntityDropEvent {

    @SubscribeEvent
    public static void onEntityDrops(LivingDropsEvent event) {
        if (event == null) return;
        if (!(event.getEntity() instanceof PlayerEntity)) return;
        for (Iterator<ItemEntity> itemEntityIterator = event.getDrops().iterator(); itemEntityIterator.hasNext(); ) {
            ItemStack stack = itemEntityIterator.next().getItem();
            if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.SOUL_BOND.get(), stack) > 0) {
                itemEntityIterator.remove();
                ((PlayerEntity) event.getEntity()).inventory.add(stack);
            }
        }
    }
}
