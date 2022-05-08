package com.divinity.hlspells.items.capabilities.totemcap;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.compat.CuriosCompat;
import com.divinity.hlspells.setup.init.ItemInit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.divinity.hlspells.HLSpells.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class TotemCapHandler {
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack item = event.getObject();
        if (item.getItem() == ItemInit.TOTEM_OF_RETURNING.get() || item.getItem() == ItemInit.TOTEM_OF_KEEPING.get()) {
            event.addCapability(new ResourceLocation(MODID, "totemcap"), new TotemItemProvider());
        }
        if (HLSpells.isCurioLoaded) {
            CuriosCompat.attachCapabilities(event);
        }
    }
}
