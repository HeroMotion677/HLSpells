package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.capabilities.playercap.PlayerCapProvider;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import com.divinity.hlspells.util.Util;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

public class FlamingCircleSpell extends Spell {

    public FlamingCircleSpell(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, int tickDelay, boolean treasureOnly, int maxSpellLevel) {
        super(type, rarity, tier, marker, displayName, xpCost, tickDelay, treasureOnly, maxSpellLevel);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            var livingEntities = Util.getEntitiesInRange(p, LivingEntity.class, 6, -1, 6);
            p.getCapability(PlayerCapProvider.PLAYER_CAP).ifPresent(cap -> {
                cap.setSpellTimer(cap.getSpellTimer() + 1);
                if (cap.getSpellTimer() % 10 == 0) {
                    doEnchantParticleInterior(p, p.level);
                    for (int i = 0; i < 2; i++) {
                        doOuterRingParticles(ParticleTypes.FLAME, p, p.level);
                    }
                    cap.setSpellTimer(0);
                }
                livingEntities.stream().filter(e -> e != null && e != p).forEach(e -> {
                    e.setLastHurtByPlayer(p);
                    e.setSecondsOnFire(1);
                });
            });
            return true;
        };
    }

    private static void doEnchantParticleInterior(Player player, Level world) {
        for (int x = -6; x <= 6; x++) {
            for (int z = -6; z <= 6; z++) {
                world.addParticle(ParticleTypes.ENCHANT, player.getX() + x + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + z + world.random.nextFloat(), 0, 0, 0);
            }
        }
    }

    private static void doOuterRingParticles(SimpleParticleType type, Player player, Level world) {
        world.addParticle(type, player.getX() - 1, player.getY() + 1.2, player.getZ() - 6, 0, 0, 0);
        world.addParticle(type, player.getX(), player.getY() + 1.2, player.getZ() - 6, 0, 0, 0);
        world.addParticle(type, player.getX() + 1, player.getY() + 1.2, player.getZ() - 6, 0, 0, 0);
        world.addParticle(type, player.getX() + 1.5, player.getY() + 1.2, player.getZ() - 5.5, 0, 0, 0);
        world.addParticle(type, player.getX() + 2, player.getY() + 1.2, player.getZ() - 5, 0, 0, 0);
        world.addParticle(type, player.getX() + 2.5, player.getY() + 1.2, player.getZ() - 4.5, 0, 0, 0);
        world.addParticle(type, player.getX() + 3, player.getY() + 1.2, player.getZ() - 4, 0, 0, 0);
        world.addParticle(type, player.getX() + 3.5, player.getY() + 1.2, player.getZ() - 3.5, 0, 0, 0);
        world.addParticle(type, player.getX() + 4, player.getY() + 1.2, player.getZ() - 3, 0, 0, 0);
        world.addParticle(type, player.getX() + 4.5, player.getY() + 1.2, player.getZ() - 2.5, 0, 0, 0);
        world.addParticle(type, player.getX() + 5, player.getY() + 1.2, player.getZ() - 2, 0, 0, 0);
        world.addParticle(type, player.getX() + 5.5, player.getY() + 1.2, player.getZ() - 1.5, 0, 0, 0);
        world.addParticle(type, player.getX() + 6, player.getY() + 1.2, player.getZ() - 1, 0, 0, 0);
        world.addParticle(type, player.getX() + 6, player.getY() + 1.2, player.getZ(), 0, 0, 0);
        world.addParticle(type, player.getX() + 6, player.getY() + 1.2, player.getZ() + 1, 0, 0, 0);
        world.addParticle(type, player.getX() + 5.5, player.getY() + 1.2, player.getZ() + 1.5, 0, 0, 0);
        world.addParticle(type, player.getX() + 5, player.getY() + 1.2, player.getZ() + 2, 0, 0, 0);
        world.addParticle(type, player.getX() + 4.5, player.getY() + 1.2, player.getZ() + 2.5, 0, 0, 0);
        world.addParticle(type, player.getX() + 4, player.getY() + 1.2, player.getZ() + 3, 0, 0, 0);
        world.addParticle(type, player.getX() + 3.5, player.getY() + 1.2, player.getZ() + 3.5, 0, 0, 0);
        world.addParticle(type, player.getX() + 3, player.getY() + 1.2, player.getZ() + 4, 0, 0, 0);
        world.addParticle(type, player.getX() + 2.5, player.getY() + 1.2, player.getZ() + 4.5, 0, 0, 0);
        world.addParticle(type, player.getX() + 2, player.getY() + 1.2, player.getZ() + 5, 0, 0, 0);
        world.addParticle(type, player.getX() + 1.5, player.getY() + 1.2, player.getZ() + 5.5, 0, 0, 0);
        world.addParticle(type, player.getX() + 1, player.getY() + 1.2, player.getZ() + 6, 0, 0, 0);
        world.addParticle(type, player.getX(), player.getY() + 1.2, player.getZ() + 6, 0, 0, 0);
        world.addParticle(type, player.getX() - 1, player.getY() + 1.2, player.getZ() + 6, 0, 0, 0);
        world.addParticle(type, player.getX() - 1.5, player.getY() + 1.2, player.getZ() + 5.5, 0, 0, 0);
        world.addParticle(type, player.getX() - 2, player.getY() + 1.2, player.getZ() + 5, 0, 0, 0);
        world.addParticle(type, player.getX() - 2.5, player.getY() + 1.2, player.getZ() + 4.5, 0, 0, 0);
        world.addParticle(type, player.getX() - 3, player.getY() + 1.2, player.getZ() + 4, 0, 0, 0);
        world.addParticle(type, player.getX() - 3.5, player.getY() + 1.2, player.getZ() + 3.5, 0, 0, 0);
        world.addParticle(type, player.getX() - 4, player.getY() + 1.2, player.getZ() + 3, 0, 0, 0);
        world.addParticle(type, player.getX() - 4.5, player.getY() + 1.2, player.getZ() + 2.5, 0, 0, 0);
        world.addParticle(type, player.getX() - 5, player.getY() + 1.2, player.getZ() + 2, 0, 0, 0);
        world.addParticle(type, player.getX() - 5.5, player.getY() + 1.2, player.getZ() + 1.5, 0, 0, 0);
        world.addParticle(type, player.getX() - 6, player.getY() + 1.2, player.getZ() + 1, 0, 0, 0);
        world.addParticle(type, player.getX() - 6, player.getY() + 1.2, player.getZ(), 0, 0, 0);
        world.addParticle(type, player.getX() - 6, player.getY() + 1.2, player.getZ() - 1, 0, 0, 0);
        world.addParticle(type, player.getX() - 5.5, player.getY() + 1.2, player.getZ() - 1.5, 0, 0, 0);
        world.addParticle(type, player.getX() - 5, player.getY() + 1.2, player.getZ() - 2, 0, 0, 0);
        world.addParticle(type, player.getX() - 4.5, player.getY() + 1.2, player.getZ() - 2.5, 0, 0, 0);
        world.addParticle(type, player.getX() - 4, player.getY() + 1.2, player.getZ() - 3, 0, 0, 0);
        world.addParticle(type, player.getX() - 3.5, player.getY() + 1.2, player.getZ() - 3.5, 0, 0, 0);
        world.addParticle(type, player.getX() - 3, player.getY() + 1.2, player.getZ() - 4, 0, 0, 0);
        world.addParticle(type, player.getX() - 2.5, player.getY() + 1.2, player.getZ() - 4.5, 0, 0, 0);
        world.addParticle(type, player.getX() - 2, player.getY() + 1.2, player.getZ() - 5, 0, 0, 0);
        world.addParticle(type, player.getX() - 1.5, player.getY() + 1.2, player.getZ() - 5.5, 0, 0, 0);
    }
}
