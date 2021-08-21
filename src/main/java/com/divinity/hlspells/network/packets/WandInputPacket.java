package com.divinity.hlspells.network.packets;

import com.divinity.hlspells.items.SpellHoldingItem;
import com.divinity.hlspells.items.capabilities.spellholdercap.SpellHolderProvider;
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
                ItemStack mainHandStack = player.getMainHandItem();
                ItemStack offHandStack = player.getOffhandItem();
                boolean mainHandWand = mainHandStack.getItem() instanceof SpellHoldingItem && ((SpellHoldingItem) mainHandStack.getItem()).isWand();
                boolean offHandWand = offHandStack.getItem() instanceof SpellHoldingItem && ((SpellHoldingItem) offHandStack.getItem()).isWand();
                if (mainHandWand) {
                    mainHandStack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP, null).ifPresent(p -> p.setCurrentSpellCycle(p.getCurrentSpellCycle() + 1));
                } else if (offHandWand) {
                    offHandStack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP, null).ifPresent(p -> p.setCurrentSpellCycle(p.getCurrentSpellCycle() + 1));
                }
            }
        });
        context.setPacketHandled(true);
    }
}
