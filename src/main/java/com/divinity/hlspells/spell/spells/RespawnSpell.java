package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RespawnSpell extends Spell {

    public RespawnSpell(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, boolean treasureOnly, int maxSpellLevel, SimpleParticleType rune) {
        super(type, rarity, tier, marker, displayName, xpCost, treasureOnly, maxSpellLevel, rune);
    }

    @Override
    protected SpellConsumer<Player> getAction() {
        return p -> {
            if (p instanceof ServerPlayer player && player.getServer() != null) {
                ServerLevel dimLevel = player.getServer().getLevel(player.getRespawnDimension());
                BlockPos pos = player.getRespawnPosition();
                if (dimLevel != null && pos != null) {
                    player.teleportTo(dimLevel, pos.getX(), pos.getY(), pos.getZ(), player.getYRot(), player.getXRot());
                    return true;
                }
                return false;
            }
           return false;
        };
    }
}
