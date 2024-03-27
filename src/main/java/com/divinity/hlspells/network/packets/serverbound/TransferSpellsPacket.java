package com.divinity.hlspells.network.packets.serverbound;

import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.network.IPacket;
import com.divinity.hlspells.setup.init.SoundInit;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.util.SpellUtils;
import com.divinity.hlspells.world.blocks.blockentities.inventory.AltarOfAttunementMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.simple.SimpleChannel;

@SuppressWarnings("unused")
public record TransferSpellsPacket() implements IPacket {

    public TransferSpellsPacket() {}

    @Override
    public void encode(FriendlyByteBuf buffer) {}

    public static TransferSpellsPacket decode(FriendlyByteBuf buffer) {
        return new TransferSpellsPacket();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(ServerPlayer player) {
        if (player.containerMenu instanceof AltarOfAttunementMenu menu) {
            ItemStack topSlot = menu.blockEntity.itemHandler.getStackInSlot(0);
            ItemStack bottomSlot = menu.blockEntity.itemHandler.getStackInSlot(1);
            var botHolder = SpellHolderProvider.getSpellHolderUnwrap(bottomSlot);
            var topHolder = SpellHolderProvider.getSpellHolderUnwrap(topSlot);
            Spell spell = SpellUtils.getSpell(topSlot);
            if (spell != SpellInit.EMPTY.get() && botHolder != null && topHolder != null) {
                if (SpellUtils.canAddSpell(bottomSlot, spell)) {
                    if (player.isCreative() || player.experienceLevel >= 5 * (spell.rarityAsInt())) {
                        botHolder.addSpell(SpellInit.SPELLS_REGISTRY.get().getKey(spell).toString());
                        topHolder.removeSpell(SpellInit.SPELLS_REGISTRY.get().getKey(spell).toString());
                        player.level.playSound(null, player.blockPosition(), SoundInit.ALTAR_TRANSFER.get(), SoundSource.BLOCKS, 0.6F, 0.6F);
                        if (!player.isCreative()) {
                            player.giveExperiencePoints(-(5 * spell.rarityAsInt()));
                        }
                    }
                    else player.level.playSound(null, player.blockPosition(), SoundInit.MISCAST_SOUND.get(), SoundSource.BLOCKS, 0.6F, 0.6F);
                }
                else player.level.playSound(null, player.blockPosition(), SoundInit.MISCAST_SOUND.get(), SoundSource.BLOCKS, 0.6F, 0.6F);
            } else player.level.playSound(null, player.blockPosition(), SoundInit.MISCAST_SOUND.get(), SoundSource.BLOCKS, 0.6F, 0.6F);
        }
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_SERVER, TransferSpellsPacket.class, TransferSpellsPacket::decode);
    }
}
