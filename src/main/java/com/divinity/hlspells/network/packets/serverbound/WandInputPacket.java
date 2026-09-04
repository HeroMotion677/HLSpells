package com.divinity.hlspells.network.packets.serverbound;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.capabilities.spellholdercap.ISpellHolder;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.items.spellitems.SpellHoldingItem;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record WandInputPacket(int key) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<WandInputPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(HLSpells.MODID, "wand_input"));

    public static final StreamCodec<RegistryFriendlyByteBuf, WandInputPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, WandInputPacket::key,
            WandInputPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(WandInputPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack carriedItem = player.getItemInHand(hand);
                if (carriedItem.getItem() instanceof SpellHoldingItem item && !item.isSpellBook()) {
                    SpellHolderProvider.get(carriedItem).ifPresent(ISpellHolder::incrementCurrentSpellCycle);
                    break;
                }
            }
        });
    }
}
