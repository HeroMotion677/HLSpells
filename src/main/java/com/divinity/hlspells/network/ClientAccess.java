package com.divinity.hlspells.network;

import com.divinity.hlspells.world.blocks.blockentities.inventory.AltarOfAttunementMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class ClientAccess {

    // SERVER -> CLIENT Packet methods (So a dedicated server doesn't load client-side classes and crash)
    public static void updateSpellClues(UUID player, String[] spellClues) {
        Minecraft mc = Minecraft.getInstance();
        Player spellCluePlayer = mc.level != null ? mc.level.getPlayerByUUID(player) : null;
        if (spellCluePlayer != null) {
            if (spellCluePlayer.containerMenu instanceof AltarOfAttunementMenu menu) {
                menu.spellClues = spellClues;
            }
        }
    }

    public static void syncTotemActivation(UUID player, ItemStack stack) {
        Minecraft mc = Minecraft.getInstance();
        Player totemActivatedPlayer = mc.level != null ? mc.level.getPlayerByUUID(player) : null;
        if (totemActivatedPlayer != null) {
            mc.particleEngine.createTrackingEmitter(totemActivatedPlayer, ParticleTypes.TOTEM_OF_UNDYING, 30);
            if (mc.player != null && mc.player.getUUID().equals(player)) {
                mc.gameRenderer.displayItemActivation(stack);
            }
        }
    }
}
