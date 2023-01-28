package com.divinity.hlspells.network.packets.clientbound;

import com.divinity.hlspells.network.ClientAccess;
import com.divinity.hlspells.network.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.UUID;

public record TotemActivatedPacket(UUID player, ItemStack stack) implements IPacket {

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.player);
        buffer.writeItemStack(this.stack, true);
    }

    public static TotemActivatedPacket decode(FriendlyByteBuf buffer) {
        return new TotemActivatedPacket(buffer.readUUID(), buffer.readItem());
    }

    @Override
    public void handle(ServerPlayer player) {
        ClientAccess.syncTotemActivation(this.player, this.stack);
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_CLIENT, TotemActivatedPacket.class, TotemActivatedPacket::decode);
    }
}
