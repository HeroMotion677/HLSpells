package com.divinity.hlspells.network.packets;

import com.divinity.hlspells.items.WandItem;
import com.divinity.hlspells.items.capabilities.wandcap.WandItemProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class WandInputPacket {
    private int key;

    public WandInputPacket() {
    }

    public WandInputPacket(int key) {
        this.key = key;
    }

    public static void encode(WandInputPacket message, PacketBuffer buffer) {
        buffer.writeInt(message.key);
    }

    public static WandInputPacket decode(PacketBuffer buffer) {
        return new WandInputPacket(buffer.readInt());
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            if (player != null) {
                boolean mainHandWand = player.getMainHandItem().getItem() instanceof WandItem;
                boolean offHandWand = player.getOffhandItem().getItem() instanceof WandItem;
                if (mainHandWand) {
                    ItemStack wandItem = player.getMainHandItem();
                    wandItem.getCapability(WandItemProvider.WAND_CAP, null).ifPresent(p -> p.setCurrentSpellCycle(p.getCurrentSpellCycle() + 1));
                } else if (offHandWand) {
                    ItemStack wandItem = player.getOffhandItem();
                    wandItem.getCapability(WandItemProvider.WAND_CAP, null).ifPresent(p -> p.setCurrentSpellCycle(p.getCurrentSpellCycle() + 1));
                }
            }
        });
        context.setPacketHandled(true);
    }
}
