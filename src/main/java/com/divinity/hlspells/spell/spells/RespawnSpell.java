package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.LodestoneTracker;

import java.util.Optional;

public class RespawnSpell extends Spell {

    public RespawnSpell(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, boolean treasureOnly, int maxSpellLevel, SimpleParticleType rune) {
        super(type, rarity, tier, marker, displayName, xpCost, treasureOnly, maxSpellLevel, rune);
    }

    @Override
    protected SpellConsumer<Player> getAction() {
        return p -> {
            if (p instanceof ServerPlayer player && player.getServer() != null) {
                Optional<GlobalPos> lodestone = getLodestonePosition(p.getUseItem());
                if (lodestone.isEmpty()) {
                    ServerLevel dimLevel = player.getServer().getLevel(player.getRespawnDimension());
                    BlockPos pos = player.getRespawnPosition();
                    if (dimLevel != null && pos != null) {
                        player.teleportTo(dimLevel, pos.getX(), pos.getY(), pos.getZ(), player.getYRot(), player.getXRot());
                        return true;
                    }
                    return false;
                } else {
                    BlockPos pos = lodestone.get().pos();
                    ServerLevel dimLevel = player.getServer().getLevel(lodestone.get().dimension());
                    if (dimLevel != null) {
                        player.teleportTo(dimLevel, pos.getX(), pos.getY(), pos.getZ(), player.getYRot(), player.getXRot());
                        return true;
                    }
                }
            }
            return false;
        };
    }

    public static Optional<GlobalPos> getLodestonePosition(ItemStack stack) {
        LodestoneTracker tracker = stack.get(DataComponents.LODESTONE_TRACKER);
        return tracker != null ? tracker.target() : Optional.empty();
    }
}
