package com.divinity.hlspells.network.packets.serverbound;

import com.divinity.hlspells.capabilities.spellholdercap.ISpellHolder;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.items.spellitems.SpellHoldingItem;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.TorchBlock;
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
        var context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                for (InteractionHand hand : InteractionHand.values()) {
                    ItemStack carriedItem = player.getItemInHand(hand);
                    if (carriedItem.getItem() instanceof SpellHoldingItem item && !item.isSpellBook()) {
                        carriedItem.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(ISpellHolder::incrementCurrentSpellCycle);
                        break;
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}
