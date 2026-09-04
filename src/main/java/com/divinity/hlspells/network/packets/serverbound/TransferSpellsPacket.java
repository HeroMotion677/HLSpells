package com.divinity.hlspells.network.packets.serverbound;

import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.setup.init.SoundInit;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.util.SpellUtils;
import com.divinity.hlspells.world.blocks.blockentities.inventory.AltarOfAttunementMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@SuppressWarnings("unused")
public record TransferSpellsPacket() implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<TransferSpellsPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(HLSpells.MODID, "transfer_spells"));

    public static final StreamCodec<RegistryFriendlyByteBuf, TransferSpellsPacket> STREAM_CODEC =
            StreamCodec.unit(new TransferSpellsPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(TransferSpellsPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> transfer((ServerPlayer) context.player()));
    }

    @SuppressWarnings("ConstantConditions")
    private static void transfer(ServerPlayer player) {
        if (player.containerMenu instanceof AltarOfAttunementMenu menu) {
            ItemStack topSlot = menu.blockEntity.itemHandler.getStackInSlot(0);
            ItemStack bottomSlot = menu.blockEntity.itemHandler.getStackInSlot(1);
            var botHolder = SpellHolderProvider.getSpellHolderUnwrap(bottomSlot);
            var topHolder = SpellHolderProvider.getSpellHolderUnwrap(topSlot);
            Spell spell = SpellUtils.getSpell(topSlot);
            if (spell != SpellInit.EMPTY.get() && botHolder != null && topHolder != null) {
                if (SpellUtils.canAddSpell(bottomSlot, spell)) {
                    if (player.isCreative() || player.experienceLevel >= 5 * (spell.rarityAsInt())) {
                        botHolder.addSpell(SpellInit.SPELLS_REGISTRY.getKey(spell).toString());
                        topHolder.removeSpell(SpellInit.SPELLS_REGISTRY.getKey(spell).toString());
                        player.level().playSound(null, player.blockPosition(), SoundInit.ALTAR_TRANSFER.get(), SoundSource.BLOCKS, 0.6F, 0.6F);
                        if (!player.isCreative()) {
                            player.giveExperiencePoints(-(5 * spell.rarityAsInt()));
                        }
                    }
                    else player.level().playSound(null, player.blockPosition(), SoundInit.MISCAST_SOUND.get(), SoundSource.BLOCKS, 0.6F, 0.6F);
                }
                else player.level().playSound(null, player.blockPosition(), SoundInit.MISCAST_SOUND.get(), SoundSource.BLOCKS, 0.6F, 0.6F);
            } else player.level().playSound(null, player.blockPosition(), SoundInit.MISCAST_SOUND.get(), SoundSource.BLOCKS, 0.6F, 0.6F);
        }
    }
}
