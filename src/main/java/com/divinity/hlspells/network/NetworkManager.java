package com.divinity.hlspells.network;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.network.packets.clientbound.SpellCluePacket;
import com.divinity.hlspells.network.packets.clientbound.TotemActivatedPacket;
import com.divinity.hlspells.network.packets.clientbound.UpdateDimensionsPacket;
import com.divinity.hlspells.network.packets.serverbound.TransferSpellsPacket;
import com.divinity.hlspells.network.packets.serverbound.WandInputPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = HLSpells.MODID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkManager {

    private static final String PROTOCOL_VERSION = "1";

    @SubscribeEvent
    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
        registrar.playToClient(SpellCluePacket.TYPE, SpellCluePacket.STREAM_CODEC, SpellCluePacket::handle);
        registrar.playToClient(TotemActivatedPacket.TYPE, TotemActivatedPacket.STREAM_CODEC, TotemActivatedPacket::handle);
        registrar.playToClient(UpdateDimensionsPacket.TYPE, UpdateDimensionsPacket.STREAM_CODEC, UpdateDimensionsPacket::handle);
        registrar.playToServer(TransferSpellsPacket.TYPE, TransferSpellsPacket.STREAM_CODEC, TransferSpellsPacket::handle);
        registrar.playToServer(WandInputPacket.TYPE, WandInputPacket.STREAM_CODEC, WandInputPacket::handle);
    }
}
