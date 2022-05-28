package com.divinity.hlspells.items.totems;

import com.divinity.hlspells.compat.CuriosCompat;
import com.divinity.hlspells.capabilities.totemcap.TotemItemProvider;
import com.divinity.hlspells.setup.init.ItemInit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.Event;

public class ReturningTotem extends Item implements ITotem {

    public ReturningTotem() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public boolean isDisposableOnDeath() {
        return false;
    }

    @Override
    public boolean doesCancelDeath() {
        return false;
    }

    @Override
    public void performAction(Event event, Player player, Level world, ItemStack heldItem, InteractionHand hand, boolean isCurios) {
        if (event instanceof LivingDeathEvent) {
            if (!isCurios) {
                heldItem.getCapability(TotemItemProvider.TOTEM_CAP).ifPresent(cap -> {
                    cap.hasDied(true);
                    cap.setBlockPos(player.blockPosition());
                    if (hand == InteractionHand.MAIN_HAND) cap.setTotemInHand(InteractionHand.MAIN_HAND);
                    else if (hand == InteractionHand.OFF_HAND) cap.setTotemInHand(InteractionHand.OFF_HAND);
                });
            }
            else {
                CuriosCompat.getItemInCuriosSlot(player, ItemInit.TOTEM_OF_RETURNING.get()).ifPresent(map -> {
                    ItemStack stack = map.stack();
                    stack.getCapability(TotemItemProvider.TOTEM_CAP).ifPresent(cap -> {
                        cap.hasDied(true);
                        cap.setBlockPos(player.blockPosition());
                        cap.setTotemInHand(InteractionHand.MAIN_HAND);
                        cap.setCuriosSlot(map.slotContext().index());
                        cap.setDiedTotemInCurios(true);
                    });
                });
            }
        }
    }
}
