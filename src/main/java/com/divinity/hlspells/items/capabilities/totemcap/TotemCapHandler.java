package com.divinity.hlspells.items.capabilities.totemcap;

import com.divinity.hlspells.compat.CuriosCompat;
import com.divinity.hlspells.events.entity.EntityDiesEvent;
import com.divinity.hlspells.init.ItemInit;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import static com.divinity.hlspells.HLSpells.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class TotemCapHandler {
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        Item item = event.getObject().getItem();
        if (item == ItemInit.TOTEM_OF_RETURNING.get() || item == ItemInit.TOTEM_OF_KEEPING.get()) {
            event.addCapability(new ResourceLocation(MODID, "totemcap"), new TotemItemProvider());
        }
        if (ModList.get().isLoaded(EntityDiesEvent.CURIOS_ID)) {
            CuriosCompat.attachCapabilities(event);
        }
    }
}
