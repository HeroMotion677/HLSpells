package com.divinity.hlspells.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TotemPacket {

    private final ItemStack itemStack;
    private final boolean particleIn;

    public TotemPacket(ItemStack itemStack, boolean particleIn) {
        this.itemStack = itemStack;
        this.particleIn = particleIn;
    }

    public static void encode(TotemPacket message, PacketBuffer buf) {
        buf.writeItem(message.itemStack);
        buf.writeBoolean(message.particleIn);
    }

    public static TotemPacket decode(PacketBuffer buf) {
        return new TotemPacket(buf.readItem(), buf.readBoolean());
    }

    public static void whenThisPacketIsReceived(TotemPacket message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Minecraft.getInstance().gameRenderer.displayItemActivation(message.itemStack);
            ClientPlayerEntity player = Minecraft.getInstance().player;
            if (player != null  && message.particleIn) {
                Minecraft.getInstance().particleEngine.createTrackingEmitter(player, ParticleTypes.TOTEM_OF_UNDYING, 30);
            }
        });
        context.get().setPacketHandled(true);
    }
}
