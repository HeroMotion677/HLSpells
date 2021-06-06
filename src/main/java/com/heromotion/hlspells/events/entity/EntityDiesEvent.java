package com.heromotion.hlspells.events.entity;

import com.heromotion.hlspells.HLSpells;
import com.heromotion.hlspells.init.ItemInit;
import com.heromotion.hlspells.network.NetworkManager;
import com.heromotion.hlspells.network.packets.TotemPacket;

import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.world.Explosion;

import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class EntityDiesEvent {

    @SubscribeEvent
    public static void onEntityDies(LivingDeathEvent event) {
        if (event == null) return;

        if (event.getEntity() instanceof PlayerEntity) {
            if (!event.getSource().isBypassInvul()) {
                PlayerEntity player = (PlayerEntity) event.getEntity();

                if (!(player.getMainHandItem().getItem().equals(ItemInit.TOTEM_OF_GRIEFING.get())
                        || player.getOffhandItem().getItem().equals(ItemInit.TOTEM_OF_GRIEFING.get())
                        || player.getMainHandItem().getItem().equals(ItemInit.TOTEM_OF_RETURNING.get())
                        || player.getOffhandItem().getItem().equals(ItemInit.TOTEM_OF_RETURNING.get()))) return;

                Item item = (player.getMainHandItem().getItem().equals(ItemInit.TOTEM_OF_GRIEFING.get())
                        || player.getOffhandItem().getItem().equals(ItemInit.TOTEM_OF_GRIEFING.get()))
                        ? ItemInit.TOTEM_OF_GRIEFING.get() : ItemInit.TOTEM_OF_RETURNING.get();
                Hand hand = (player.getMainHandItem().getItem().equals(item) ? Hand.MAIN_HAND : Hand.OFF_HAND);

                if (item.equals(ItemInit.TOTEM_OF_GRIEFING.get())) {
                    player.setItemInHand(hand, ItemStack.EMPTY);
                    player.inventory.setChanged();
                    player.level.explode(player, player.getX(), player.getY(), player.getZ(), 10.0F, Explosion.Mode.BREAK);
                    player.level.broadcastEntityEvent(player, (byte) 35);
                    NetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new TotemPacket(new ItemStack(item)));

                } else if (item.equals(ItemInit.TOTEM_OF_RETURNING.get())) {
                    player.getItemInHand(hand).getOrCreateTag().putString("registryKey", player.level.dimension().location().toString());
                    player.getItemInHand(hand).getOrCreateTag().putDouble("dX", player.getX());
                    player.getItemInHand(hand).getOrCreateTag().putDouble("dY", player.getY());
                    player.getItemInHand(hand).getOrCreateTag().putDouble("dZ", player.getZ());
                }
            }
        }
    }
}
