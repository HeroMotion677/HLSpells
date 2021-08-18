package com.divinity.hlspells.items.capabilities.totemcap;

import com.divinity.hlspells.init.ItemInit;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.divinity.hlspells.HLSpells.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class TotemCapHandler {
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() == ItemInit.TOTEM_OF_RETURNING.get()) {
            event.addCapability(new ResourceLocation(MODID, "totemcap"), new TotemItemProvider());
        }
    }
}
