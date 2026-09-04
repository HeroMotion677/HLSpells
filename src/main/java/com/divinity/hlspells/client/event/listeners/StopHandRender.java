package com.divinity.hlspells.client.event.listeners;

import com.divinity.hlspells.HLSpells;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RenderArmEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = HLSpells.MODID, value = Dist.CLIENT)
public class StopHandRender {

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event){
        Player player = event.getEntity();
        if(!event.isCanceled()){
            if(player.isInvisible() && player.isInvulnerable()){
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderHand(RenderArmEvent event){
        LocalPlayer player = Minecraft.getInstance().player;
        if(!event.isCanceled() && player != null){
            if(player.isInvisible() && player.isInvulnerable()){
                event.setCanceled(true);
            }
        }
    }
}
