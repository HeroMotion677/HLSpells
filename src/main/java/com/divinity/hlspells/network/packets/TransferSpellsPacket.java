package com.divinity.hlspells.network.packets;

import com.divinity.hlspells.items.capabilities.spellholdercap.ISpellHolder;
import com.divinity.hlspells.items.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.setup.client.ClientAccess;
import com.divinity.hlspells.setup.client.inventory.AltarOfAttunementMenu;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
                    Spell spell = SpellUtils.getSpell(topSlot);
                    if (spell != SpellInit.EMPTY.get()) {
                        bottomSlot.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(botSlot -> {
                            if (SpellUtils.canAddSpell(bottomSlot.getItem(), botSlot.getSpells())) {
                                if (player.isCreative() || player.experienceLevel >= 5 * (spell.rarityAsInt())) {
                                    botSlot.addSpell(spell.getRegistryName().toString());
                                    topSlot.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(aboveSlot -> aboveSlot.removeSpell(spell.getRegistryName().toString()));
                                    if (!player.isCreative()) {
                                        player.giveExperiencePoints(-(5 * spell.rarityAsInt()));
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}
