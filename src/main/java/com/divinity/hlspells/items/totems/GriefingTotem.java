package com.divinity.hlspells.items.totems;

import com.divinity.hlspells.compat.CuriosCompat;
import com.divinity.hlspells.setup.init.ItemInit;
import com.divinity.hlspells.util.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.Event;

public class GriefingTotem extends Item implements ITotem  {

    public GriefingTotem() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public boolean isDisposableOnDeath() {
        return true;
    }

    @Override
    public boolean doesCancelDeath() {
        return false;
    }

    @Override
    public void performAction(Event event, Player player, Level world, ItemStack stack, InteractionHand hand, boolean isCurios) {
        if (event instanceof LivingDeathEvent) {
            if (!isCurios) player.setItemInHand(hand, ItemStack.EMPTY);
            else CuriosCompat.getItemInCuriosSlot(player, ItemInit.TOTEM_OF_GRIEFING.get()).ifPresent(map -> map.stack().shrink(1));
            world.explode(player, player.getX(), player.getY(), player.getZ(), 5.0F, Explosion.BlockInteraction.BREAK);
            Util.displayActivation(player, ItemInit.TOTEM_OF_GRIEFING.get());
        }
    }
}
