package com.divinity.hlspells.network.packets.serverbound;

import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.world.blocks.blockentities.inventory.AltarOfAttunementMenu;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.client.resources.sounds.MinecartSoundInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class TransferSpellsPacket {

    public TransferSpellsPacket() {}

    public static void encode(TransferSpellsPacket msg, FriendlyByteBuf buffer) {}

    public static TransferSpellsPacket decode(FriendlyByteBuf buffer) {
        return new TransferSpellsPacket();
    }

    @SuppressWarnings("ConstantConditions")
    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
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
                                if (!player.isCreative()) {
                                    player.giveExperiencePoints(-(5 * spell.rarityAsInt()));
                                }
                            }
                        }
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}
