package com.divinity.hlspells.network.packets.serverbound;

import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.network.packets.clientbound.UpdateDimensionsPacket;
import com.divinity.hlspells.network.util.IPacket;
import com.divinity.hlspells.setup.init.SoundInit;
import com.divinity.hlspells.world.blocks.blockentities.inventory.AltarOfAttunementMenu;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.client.resources.sounds.MinecartSoundInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class TransferSpellsPacket implements IPacket {

    public TransferSpellsPacket() {}

    public void encode(FriendlyByteBuf buffer) {}

    public static TransferSpellsPacket decode(FriendlyByteBuf buffer) {
        return new TransferSpellsPacket();
    }

    @SuppressWarnings("ConstantConditions")
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                if (player.containerMenu instanceof AltarOfAttunementMenu menu) {
                    ItemStack topSlot = menu.blockEntity.itemHandler.getStackInSlot(0);
                    ItemStack bottomSlot = menu.blockEntity.itemHandler.getStackInSlot(1);
                    var botHolder = SpellHolderProvider.getSpellHolderUnwrap(bottomSlot);
                    var topHolder = SpellHolderProvider.getSpellHolderUnwrap(topSlot);
                    Spell spell = SpellUtils.getSpell(topSlot);
                    if (spell != SpellInit.EMPTY.get() && botHolder != null && topHolder != null) {
                        if (SpellUtils.canAddSpell(bottomSlot, spell)) {
                            if (player.isCreative() || player.experienceLevel >= 5 * (spell.rarityAsInt())) {
                                botHolder.addSpell(spell.getRegistryName().toString());
                                topHolder.removeSpell(spell.getRegistryName().toString());
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
        });
        context.setPacketHandled(true);
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_SERVER, TransferSpellsPacket.class, TransferSpellsPacket::decode);
    }
}
