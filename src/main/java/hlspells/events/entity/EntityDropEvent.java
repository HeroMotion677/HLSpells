package com.heromotion.hlspells.events.entity;

import com.heromotion.hlspells.HLSpells;
import com.heromotion.hlspells.init.EnchantmentInit;
import com.heromotion.hlspells.init.ItemInit;
import com.heromotion.hlspells.network.packets.TotemPacket;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkManager;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Iterator;

@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class EntityDropEvent {

    @SubscribeEvent
    public static void onEntityDrops(LivingDropsEvent event) {
        if (event == null) return;
        if (!(event.getEntity() instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) event.getEntity();
        boolean flag = false;
        for (Iterator<ItemEntity> itemEntityIterator = event.getDrops().iterator(); itemEntityIterator.hasNext();) {
            if (itemEntityIterator.next().getItem().getItem() == ItemInit.TOTEM_OF_KEEPING.get()) {
                itemEntityIterator.remove();
                flag = true;
                break;
            }
        }
        for (Iterator<ItemEntity> itemEntityIterator = event.getDrops().iterator(); itemEntityIterator.hasNext(); ) {
            ItemStack stack = itemEntityIterator.next().getItem();
            if ((EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.SOUL_BOND.get(), stack) > 0)
                    || (flag) || stack.getItem().equals(ItemInit.TOTEM_OF_RETURNING.get())) {
                itemEntityIterator.remove();
                player.inventory.add(stack);
            }
        }
        if (flag)
        {
            player.level.broadcastEntityEvent(player, (byte) 35);
            com.heromotion.hlspells.network.NetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new TotemPacket(new ItemStack(ItemInit.TOTEM_OF_KEEPING.get())));
        }
    }
}