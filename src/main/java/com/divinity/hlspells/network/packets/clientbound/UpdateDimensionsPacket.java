package com.divinity.hlspells.network.packets.clientbound;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.network.ClientAccess;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record UpdateDimensionsPacket(UUID playerUUID) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<UpdateDimensionsPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(HLSpells.MODID, "update_dimensions"));

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateDimensionsPacket> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, UpdateDimensionsPacket::playerUUID,
            UpdateDimensionsPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(UpdateDimensionsPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientAccess.updateDimensions(packet.playerUUID));
    }
}
