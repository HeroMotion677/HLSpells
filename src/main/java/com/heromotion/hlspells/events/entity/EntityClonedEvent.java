package com.heromotion.hlspells.events.entity;

import com.heromotion.hlspells.HLSpells;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class EntityClonedEvent {

    @SubscribeEvent
    public static void onEntityCloned(PlayerEvent.Clone event) {
        if (event == null) return;
        if (!event.isWasDeath()) return;
        event.getPlayer().inventory.replaceWith(event.getOriginal().inventory);
    }
}
