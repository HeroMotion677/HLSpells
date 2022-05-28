package com.divinity.hlspells.items.totems;

import com.divinity.hlspells.compat.CuriosCompat;
import com.divinity.hlspells.setup.init.ItemInit;
import com.divinity.hlspells.util.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.Event;
import top.theillusivec4.curios.api.SlotResult;

public class EscapingTotem extends Item implements ITotem {

    public EscapingTotem() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public boolean isDisposableOnDeath() {
        return false;
    }

    @Override
    public boolean doesCancelDeath() {
        return true;
    }

    @Override
    public void performAction(Event event, Player player, Level world, ItemStack heldItem, InteractionHand hand, boolean isCurios) {
        if (event instanceof LivingDeathEvent deathEvent) {
            deathEvent.setCanceled(true);
            ItemStack stack = isCurios ? CuriosCompat.getItemInCuriosSlot(player, ItemInit.TOTEM_OF_ESCAPING.get()).map(SlotResult::stack).orElse(ItemStack.EMPTY) : heldItem;
            Util.vanillaTotemBehavior(player, stack, ItemInit.TOTEM_OF_ESCAPING.get());
            player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 200, 0));
            Util.randomTeleport(player);
        }
    }
}
