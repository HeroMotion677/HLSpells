package com.divinity.hlspells.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TotemPacket {

    private final ItemStack itemStack;

    public TotemPacket(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static TotemPacket decode(PacketBuffer buf) {
        return new TotemPacket(buf.readItem());
    }

    public void encode(PacketBuffer buf) {
        buf.writeItem(itemStack);
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() ->
        {
            Minecraft.getInstance().gameRenderer.displayItemActivation(itemStack);
        });
        context.get().setPacketHandled(true);
    }
}