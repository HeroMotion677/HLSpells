package com.divinity.hlspells.capabilities.playercap;

import com.divinity.hlspells.HLSpells;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Optional;
import java.util.function.Supplier;

public class PlayerCapProvider {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, HLSpells.MODID);

    public static final Supplier<AttachmentType<PlayerCap>> PLAYER_CAP = ATTACHMENTS.register("player_cap",
            () -> AttachmentType.serializable(PlayerCap::new).build());

    public static Optional<IPlayerCap> get(Entity entity) {
        return entity instanceof Player player ? Optional.of(player.getData(PLAYER_CAP)) : Optional.empty();
    }
}
