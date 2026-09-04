package com.divinity.hlspells.network.packets.clientbound;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.network.ClientAccess;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record TotemActivatedPacket(UUID player, ItemStack stack) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<TotemActivatedPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(HLSpells.MODID, "totem_activated"));

    public static final StreamCodec<RegistryFriendlyByteBuf, TotemActivatedPacket> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, TotemActivatedPacket::player,
            ItemStack.OPTIONAL_STREAM_CODEC, TotemActivatedPacket::stack,
            TotemActivatedPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(TotemActivatedPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientAccess.syncTotemActivation(packet.player, packet.stack));
    }
}
