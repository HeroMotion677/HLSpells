package com.divinity.hlspells.network.packets.serverbound;

import com.divinity.hlspells.capabilities.spellholdercap.ISpellHolder;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.items.spellitems.SpellHoldingItem;
import com.divinity.hlspells.network.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.simple.SimpleChannel;

public record WandInputPacket(int key) implements IPacket {

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.key);
    }

    public static WandInputPacket decode(FriendlyByteBuf buffer) {
        return new WandInputPacket(buffer.readInt());
    }

    @Override
    public void handle(ServerPlayer player) {
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack carriedItem = player.getItemInHand(hand);
            if (carriedItem.getItem() instanceof SpellHoldingItem item && !item.isSpellBook()) {
                carriedItem.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(ISpellHolder::incrementCurrentSpellCycle);
                break;
            }
        }
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_SERVER, WandInputPacket.class, WandInputPacket::decode);
    }
}
