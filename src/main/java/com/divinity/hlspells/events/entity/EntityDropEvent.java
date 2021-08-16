package com.divinity.hlspells.events.entity;

import com.divinity.hlspells.items.capabilities.totemcap.TotemItemProvider;
import com.divinity.hlspells.network.NetworkManager;
import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.init.EnchantmentInit;
import com.divinity.hlspells.init.ItemInit;
import com.divinity.hlspells.network.packets.TotemPacket;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Iterator;

@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class EntityDropEvent
{
    static int slotNumber;

    @SubscribeEvent
    public static void onEntityDrops(LivingDropsEvent event)
    {
        if (event == null) return;
        if (!(event.getEntity() instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) event.getEntity();
        boolean flag = false;
        for (Iterator<ItemEntity> itemEntityIterator = event.getDrops().iterator(); itemEntityIterator.hasNext();)
        {
            if (itemEntityIterator.next().getItem().getItem() == ItemInit.TOTEM_OF_KEEPING.get())
            {
                itemEntityIterator.remove();
                flag = true;
                break;
            }
        }
        for (Iterator<ItemEntity> itemEntityIterator = event.getDrops().iterator(); itemEntityIterator.hasNext();)
        {
            ItemStack stack = itemEntityIterator.next().getItem();
            if ((EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.SOUL_BOND.get(), stack) > 0)
                    || (flag) || stack.getItem().equals(ItemInit.TOTEM_OF_RETURNING.get()))
            {
                // TOTEM OF RETURNING
                if (EntityDiesEvent.totemActivationFlag)
                {
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
                }

                else if (EntityDiesEvent.totemActivationFlagOff)
                {
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