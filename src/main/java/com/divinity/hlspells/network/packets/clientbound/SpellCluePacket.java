package com.divinity.hlspells.network.packets.clientbound;

import com.divinity.hlspells.network.ClientAccess;
import com.divinity.hlspells.network.util.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.UUID;

public record SpellCluePacket(UUID player, String... spellClues) implements IPacket {

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.player);
        buffer.writeUtf(this.spellClues[0]);
        buffer.writeUtf(this.spellClues[1]);
        buffer.writeUtf(this.spellClues[2]);
    }

    public static SpellCluePacket decode(FriendlyByteBuf buffer) {
        return new SpellCluePacket(buffer.readUUID(), buffer.readUtf(), buffer.readUtf(), buffer.readUtf());
    }

    public void handle(NetworkEvent.Context context) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            context.enqueueWork(() -> ClientAccess.updateSpellClues(this.player, this.spellClues));
        }
        context.setPacketHandled(true);
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_CLIENT, SpellCluePacket.class, SpellCluePacket::decode);
    }
}
