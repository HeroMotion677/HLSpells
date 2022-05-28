package com.divinity.hlspells.items.totems;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;

public interface ITotem {

    boolean isDisposableOnDeath();

    boolean doesCancelDeath();

    void performAction(Event event, Player player, Level world, ItemStack stack, InteractionHand hand, boolean isCurios);
}
