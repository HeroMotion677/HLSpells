package com.divinity.hlspells.network.packets.clientbound;

import com.divinity.hlspells.network.ClientAccess;
import com.divinity.hlspells.network.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.UUID;

public record SpellCluePacket(UUID player, String... spellClues) implements IPacket {

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.player);
        buffer.writeUtf(this.spellClues[0]);
        buffer.writeUtf(this.spellClues[1]);
        buffer.writeUtf(this.spellClues[2]);
    }

    public static SpellCluePacket decode(FriendlyByteBuf buffer) {
        return new SpellCluePacket(buffer.readUUID(), buffer.readUtf(), buffer.readUtf(), buffer.readUtf());
    }

    @Override
    public void handle(ServerPlayer player) {
        ClientAccess.updateSpellClues(this.player, this.spellClues);
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_CLIENT, SpellCluePacket.class, SpellCluePacket::decode);
    }
}
