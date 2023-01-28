package com.divinity.hlspells.network;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.network.packets.clientbound.SpellCluePacket;
import com.divinity.hlspells.network.packets.clientbound.TotemActivatedPacket;
import com.divinity.hlspells.network.packets.clientbound.UpdateDimensionsPacket;
import com.divinity.hlspells.network.packets.serverbound.TransferSpellsPacket;
import com.divinity.hlspells.network.packets.serverbound.WandInputPacket;
import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.List;
import java.util.function.BiConsumer;

@Mod.EventBusSubscriber(modid = HLSpells.MODID, bus = Bus.MOD)
public class NetworkManager {

    private static final String PROTOCOL_VERSION = "1";
    private static int index = 0;
    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(HLSpells.MODID, "main"), () -> NetworkManager.PROTOCOL_VERSION,
            NetworkManager.PROTOCOL_VERSION::equals, NetworkManager.PROTOCOL_VERSION::equals);

    @SubscribeEvent
    public static void registerNetworkStuff(FMLCommonSetupEvent event) {
        registerPackets();
    }

    private static void registerPackets() {
        List<BiConsumer<SimpleChannel, Integer>> packets = ImmutableList.<BiConsumer<SimpleChannel, Integer>>builder()
                .add(SpellCluePacket::register)
                .add(TotemActivatedPacket::register)
                .add(UpdateDimensionsPacket::register)
                .add(TransferSpellsPacket::register)
                .add(WandInputPacket::register)
                .build();
        packets.forEach(consumer -> consumer.accept(INSTANCE, index++));
    }
}