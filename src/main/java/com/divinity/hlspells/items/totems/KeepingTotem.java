package com.divinity.hlspells.items.totems;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.compat.CuriosCompat;
import com.divinity.hlspells.events.TotemEvents;
import com.divinity.hlspells.capabilities.totemcap.TotemItemProvider;
import com.divinity.hlspells.setup.init.ItemInit;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.Event;

public class KeepingTotem extends Item implements ITotem {

    public KeepingTotem() {
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
                    if (hand == InteractionHand.MAIN_HAND) cap.setTotemInHand(InteractionHand.MAIN_HAND);
                    else if (hand == InteractionHand.OFF_HAND) cap.setTotemInHand(InteractionHand.OFF_HAND);
                    cap.setInventoryNBT(player.inventory.save(new ListTag()));
                    if (HLSpells.isCurioLoaded) cap.setCuriosNBT(CuriosCompat.getCuriosInv(player));
                });
            }
            else {
                CuriosCompat.getItemInCuriosSlot(player, ItemInit.TOTEM_OF_KEEPING.get()).ifPresent(map -> {
                    ItemStack stack = map.stack();
                    stack.getCapability(TotemItemProvider.TOTEM_CAP).ifPresent(cap -> {
                        cap.hasDied(true);
                        cap.setTotemInHand(InteractionHand.MAIN_HAND);
                        cap.setInventoryNBT(player.inventory.save(new ListTag()));
                        cap.setCuriosNBT(CuriosCompat.getCuriosInv(player));
                        cap.setCuriosSlot(map.slotContext().index());
                        cap.setDiedTotemInCurios(true);
                    });
                });
            }
            TotemEvents.soulBond = false;
        }
    }
}
