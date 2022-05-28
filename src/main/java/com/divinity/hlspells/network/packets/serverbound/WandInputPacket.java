package com.divinity.hlspells.network.packets.serverbound;

import com.divinity.hlspells.capabilities.spellholdercap.ISpellHolder;
import com.divinity.hlspells.items.spellitems.SpellHoldingItem;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class WandInputPacket {

    private final int key;

    public WandInputPacket(int key) {
        this.key = key;
    }

    public static void encode(WandInputPacket message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.key);
    }

    public static WandInputPacket decode(FriendlyByteBuf buffer) {
        return new WandInputPacket(buffer.readInt());
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                ItemStack mainHandStack = player.getMainHandItem();
                ItemStack offHandStack = player.getOffhandItem();
                boolean mainHandWand = mainHandStack.getItem() instanceof SpellHoldingItem item && item.isWand();
                boolean offHandWand = offHandStack.getItem() instanceof SpellHoldingItem item && item.isWand();
                if (mainHandWand) {
                    mainHandStack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP, null).ifPresent(ISpellHolder::incrementCurrentSpellCycle);
                }
                else if (offHandWand) {
                    offHandStack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP, null).ifPresent(ISpellHolder::incrementCurrentSpellCycle);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
