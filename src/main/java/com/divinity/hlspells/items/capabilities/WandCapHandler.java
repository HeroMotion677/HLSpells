package com.divinity.hlspells.items.capabilities;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.init.ItemInit;
import com.divinity.hlspells.items.WandItem;
import com.divinity.hlspells.network.NetworkManager;
import com.divinity.hlspells.network.packets.WandInputPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketDirection;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;

import static com.divinity.hlspells.HLSpells.*;

@Mod.EventBusSubscriber(modid = MODID)
public class WandCapHandler
{
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event)
    {
        if (event.getObject().getItem() instanceof WandItem)
        {
            event.addCapability(new ResourceLocation(MODID, "wandcap"), new WandItemProvider());
        }
    }

    static int buttonPressTimer = 0;
    @SubscribeEvent
    public static void onClientTick (TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            if (WAND_BINDING.isDown())
            {
                buttonPressTimer++;
                if (buttonPressTimer % 10 == 0)
                {
                    NetworkManager.INSTANCE.sendToServer(new WandInputPacket(WAND_BINDING.getKey().getValue()));
                    buttonPressTimer = 0;
                }
            }

            else
            {
                buttonPressTimer = 0;
            }
        }
    }

}
