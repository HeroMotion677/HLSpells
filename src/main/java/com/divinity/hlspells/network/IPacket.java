package com.divinity.hlspells.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Function;
import java.util.function.Supplier;

public interface  IPacket {

    void encode(FriendlyByteBuf packetBuf);

    /**
     * @param player Nullable, if the packet is clientbound. Never null otherwise.
     */
    void handle(ServerPlayer player);

    default boolean isOnNetworkThread() {
        return false;
    }

    static <T extends IPacket> void register(SimpleChannel channel, int id, NetworkDirection direction, Class<T> packetClass, Function<FriendlyByteBuf, T> readFunc) {
        register(channel.messageBuilder(packetClass, id, direction), readFunc);
    }

    private static boolean handleOnThread(IPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        var context = contextSupplier.get();
        if (!packet.isOnNetworkThread()) {
            context.enqueueWork(() -> handleSided(packet, context));
        }
        else handleSided(packet, context);
        return true;
    }

    private static void handleSided(IPacket packet, NetworkEvent.Context context) {
        if (context.getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                packet.handle(null);
            }
        }
        else if (context.getDirection() == NetworkDirection.PLAY_TO_SERVER) {
            ServerPlayer player = context.getSender();
            if (player != null) {
                packet.handle(player);
            }
        }
    }

    static <T extends IPacket> void register(SimpleChannel.MessageBuilder<T> builder, Function<FriendlyByteBuf, T> decoder) {
        builder.encoder(IPacket::encode)
               .decoder(decoder)
               .consumer(IPacket::handleOnThread)
               .add();
    }
}
