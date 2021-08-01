package com.divinity.hlspells.items.capabilities;

import com.divinity.hlspells.init.ItemInit;
import com.divinity.hlspells.items.WandItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import static com.divinity.hlspells.HLSpells.*;

@Mod.EventBusSubscriber(modid = MODID)
public class WandCapHandler
{
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event)
    {
        if (event.getObject().getItem() instanceof WandItem)
        {
            event.addCapability(new ResourceLocation(MODID, "wandCap"), new WandItemProvider());
        }
    }
}
