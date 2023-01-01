package com.divinity.hlspells.network.packets.clientbound;

import com.divinity.hlspells.network.ClientAccess;
import com.divinity.hlspells.network.util.IPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.UUID;

public record TotemActivatedPacket(UUID player, ItemStack stack) implements IPacket {

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.player);
        buffer.writeItemStack(this.stack, true);
    }

    public static TotemActivatedPacket decode(FriendlyByteBuf buffer) {
        return new TotemActivatedPacket(buffer.readUUID(), buffer.readItem());
    }

    public void handle(NetworkEvent.Context context) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            context.enqueueWork(() -> ClientAccess.syncTotemActivation(this.player, this.stack));
        }
        context.setPacketHandled(true);
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_CLIENT, TotemActivatedPacket.class, TotemActivatedPacket::decode);
    }
}
