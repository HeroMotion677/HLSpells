package com.divinity.hlspells.events;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.capabilities.playercap.PlayerCapProvider;
import com.divinity.hlspells.capabilities.totemcap.TotemItemProvider;
import com.divinity.hlspells.compat.CuriosCompat;
import com.divinity.hlspells.setup.init.ItemInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.divinity.hlspells.HLSpells.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class CapabilityEvents {

    @SubscribeEvent
    public static void onAttachPlayerCap(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player && !event.getObject().getCapability(PlayerCapProvider.PLAYER_CAP).isPresent()) {
            event.addCapability(new ResourceLocation(MODID, "playereffectcap"), new PlayerCapProvider());
        }
    }

    @SubscribeEvent
    public static void onAttachItemStackCap(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack item = event.getObject();
        if (item.getItem() == ItemInit.TOTEM_OF_RETURNING.get() || item.getItem() == ItemInit.TOTEM_OF_KEEPING.get()) {
            event.addCapability(new ResourceLocation(MODID, "totemcap"), new TotemItemProvider());
        }
        if (HLSpells.isCurioLoaded) CuriosCompat.attachCapabilities(event);
    }
}
