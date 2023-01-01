package com.divinity.hlspells.network.packets.serverbound;

import com.divinity.hlspells.capabilities.spellholdercap.ISpellHolder;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.items.spellitems.SpellHoldingItem;
import com.divinity.hlspells.network.util.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public record WandInputPacket(int key) implements IPacket {

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.key);
    }

    public static WandInputPacket decode(FriendlyByteBuf buffer) {
        return new WandInputPacket(buffer.readInt());
    }

    public void handle(NetworkEvent.Context context) {
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

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_SERVER, WandInputPacket.class, WandInputPacket::decode);
    }
}
