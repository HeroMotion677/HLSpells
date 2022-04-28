package com.divinity.hlspells.network.packets;

import com.divinity.hlspells.setup.client.ClientAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SpellCluePacket {
    private final UUID player;
    private final String[] spellClues;

    public SpellCluePacket(UUID player, String... clues) {
        this.player = player;
        this.spellClues = clues;
    }

    public static void encode(SpellCluePacket msg, FriendlyByteBuf buffer) {
        buffer.writeUUID(msg.player);
        buffer.writeUtf(msg.spellClues[0]);
        buffer.writeUtf(msg.spellClues[1]);
        buffer.writeUtf(msg.spellClues[2]);
    }

    public static SpellCluePacket decode(FriendlyByteBuf buffer) {
        return new SpellCluePacket(buffer.readUUID(), buffer.readUtf(), buffer.readUtf(), buffer.readUtf());
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            context.enqueueWork(() -> ClientAccess.updateSpellClues(this.player, this.spellClues));
        }
        context.setPacketHandled(true);
    }
}
