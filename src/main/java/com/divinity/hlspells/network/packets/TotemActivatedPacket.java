package com.divinity.hlspells.network.packets;

import com.divinity.hlspells.setup.client.ClientAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class TotemActivatedPacket {
    private final UUID player;
    private final ItemStack stack;

    public TotemActivatedPacket(UUID player, ItemStack stack) {
        this.player = player;
        this.stack = stack;
    }

    public static void encode(TotemActivatedPacket msg, FriendlyByteBuf buffer) {
        buffer.writeUUID(msg.player);
        buffer.writeItemStack(msg.stack, true);
    }

    public static TotemActivatedPacket decode(FriendlyByteBuf buffer) {
        return new TotemActivatedPacket(buffer.readUUID(), buffer.readItem());
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            context.enqueueWork(() -> ClientAccess.syncTotemActivation(this.player, this.stack));
        }
        context.setPacketHandled(true);
    }
}
