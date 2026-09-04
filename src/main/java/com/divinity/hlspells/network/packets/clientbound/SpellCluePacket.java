package com.divinity.hlspells.network.packets.clientbound;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.network.ClientAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record SpellCluePacket(UUID player, String... spellClues) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SpellCluePacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(HLSpells.MODID, "spell_clue"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SpellCluePacket> STREAM_CODEC = StreamCodec.of(
            (buffer, packet) -> {
                buffer.writeUUID(packet.player);
                buffer.writeUtf(packet.spellClues[0]);
                buffer.writeUtf(packet.spellClues[1]);
                buffer.writeUtf(packet.spellClues[2]);
            },
            buffer -> new SpellCluePacket(buffer.readUUID(), buffer.readUtf(), buffer.readUtf(), buffer.readUtf()));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SpellCluePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientAccess.updateSpellClues(packet.player, packet.spellClues));
    }
}
