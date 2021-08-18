package com.divinity.hlspells.events.entity;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.init.ItemInit;
import com.divinity.hlspells.items.capabilities.totemcap.TotemItemProvider;
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
        if (!(event.getEntity() instanceof PlayerEntity)) return;

        PlayerEntity player = (PlayerEntity) event.getEntity();

        // TOTEM OF RETURNING
        for (Iterator<ItemEntity> itemEntityIterator = event.getDrops().iterator(); itemEntityIterator.hasNext(); ) {
            ItemStack stack = itemEntityIterator.next().getItem();
            if (stack.getItem().equals(ItemInit.TOTEM_OF_RETURNING.get())) {
                if (EntityDiesEvent.totemActivationFlag) {
                    itemEntityIterator.remove();
                    stack.getCapability(TotemItemProvider.TOTEM_CAP, null).ifPresent(cap ->
                    {
                        cap.setXPos(player.getX());
                        cap.setYPos(player.getY());
                        cap.setZPos(player.getZ());
                        cap.hasDied(true);
                    });
                    player.inventory.add(player.inventory.selected, stack);
                    EntityDiesEvent.totemActivationFlag = false;
                } else if (EntityDiesEvent.totemActivationFlagOff) {
                    itemEntityIterator.remove();
                    stack.getCapability(TotemItemProvider.TOTEM_CAP, null).ifPresent(cap ->
                    {
                        cap.setXPos(player.getX());
                        cap.setYPos(player.getY());
                        cap.setZPos(player.getZ());
                        cap.hasDied(true);
                    });
                    player.inventory.offhand.set(0, stack);
                    EntityDiesEvent.totemActivationFlagOff = false;
                }
            }
        }
    }
}