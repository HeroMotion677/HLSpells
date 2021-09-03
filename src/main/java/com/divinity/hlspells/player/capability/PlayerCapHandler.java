package com.divinity.hlspells.player.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.divinity.hlspells.HLSpells.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class PlayerCapHandler {

    @SubscribeEvent
    public static void onAttachCapabilities (AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(MODID, "playereffectcap"), new PlayerCapProvider());
        }
    }
}
