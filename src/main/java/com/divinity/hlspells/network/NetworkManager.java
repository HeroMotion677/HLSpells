package com.divinity.hlspells.network;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.network.packets.clientbound.SpellCluePacket;
import com.divinity.hlspells.network.packets.clientbound.TotemActivatedPacket;
import com.divinity.hlspells.network.packets.serverbound.TransferSpellsPacket;
import com.divinity.hlspells.network.packets.serverbound.WandInputPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(modid = HLSpells.MODID, bus = Bus.MOD)
public class NetworkManager {

    public static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(HLSpells.MODID, "main"), () -> NetworkManager.PROTOCOL_VERSION,
            NetworkManager.PROTOCOL_VERSION::equals, NetworkManager.PROTOCOL_VERSION::equals);
    private static int index = 0;

    @SubscribeEvent
    public static void registerNetworkStuff(FMLCommonSetupEvent event) {
        INSTANCE.registerMessage(index++, WandInputPacket.class, WandInputPacket::encode, WandInputPacket::decode, WandInputPacket::handle);
        INSTANCE.registerMessage(index++, TotemActivatedPacket.class, TotemActivatedPacket::encode, TotemActivatedPacket::decode, TotemActivatedPacket::handle);
        INSTANCE.registerMessage(index++, SpellCluePacket.class, SpellCluePacket::encode, SpellCluePacket::decode, SpellCluePacket::handle);
        INSTANCE.registerMessage(index++, TransferSpellsPacket.class, TransferSpellsPacket::encode, TransferSpellsPacket::decode, TransferSpellsPacket::handle);
    }
}