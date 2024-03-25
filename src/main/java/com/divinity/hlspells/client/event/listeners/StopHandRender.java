package com.divinity.hlspells.client.event.listeners;

import com.divinity.hlspells.HLSpells;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HLSpells.MODID, value = Dist.CLIENT)
public class StopHandRender {

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event){
        Player player = event.getPlayer();
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
