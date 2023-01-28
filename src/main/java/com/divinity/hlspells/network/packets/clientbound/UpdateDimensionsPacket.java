package com.divinity.hlspells.network.packets.clientbound;

import com.divinity.hlspells.network.ClientAccess;
import com.divinity.hlspells.network.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.UUID;

public record UpdateDimensionsPacket(UUID playerUUID) implements IPacket {

    @Override
    public void encode(FriendlyByteBuf packetBuf) {
        packetBuf.writeUUID(playerUUID);
    }

    public static UpdateDimensionsPacket decode(FriendlyByteBuf packetBuf) {
        return new UpdateDimensionsPacket(packetBuf.readUUID());
    }

    @Override
    public void handle(ServerPlayer player) {
        ClientAccess.updateDimensions(playerUUID);
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_CLIENT, UpdateDimensionsPacket.class, UpdateDimensionsPacket::decode);
    }
}