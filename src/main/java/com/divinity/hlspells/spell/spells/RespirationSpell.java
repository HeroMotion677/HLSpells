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

public class RespirationSpell extends Spell {

    public RespirationSpell(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, int tickDelay, boolean treasureOnly, int maxSpellLevel) {
        super(type, rarity, tier, marker, displayName, xpCost, tickDelay, treasureOnly, maxSpellLevel);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            var players = Util.getEntitiesInRange(p, LivingEntity.class, 15, 15, 15);
            PlayerCapProvider.get(p).ifPresent(cap -> {
                cap.setSpellTimer(cap.getSpellTimer() + 1);
                for (LivingEntity player : players) {

                    if (player.isUnderWater()) {
                        player.setAirSupply(player.getAirSupply() + 10);
                        doRespirationEntityParticle(player, p.level());
                        if (player.getAirSupply() > player.getMaxAirSupply()) {
                            player.setAirSupply(player.getMaxAirSupply());
                        }
                        cap.setSpellTimer(0);
                    }
                }
            });
            return true;
        };
    }
    private static void doRespirationEntityParticle(LivingEntity livingEntity, Level world) {
        double d0 = (livingEntity.getX() + world.random.nextFloat());
        double d1 = (livingEntity.getY() + world.random.nextFloat());
        double d2 = (livingEntity.getZ() + world.random.nextFloat());
        double d3 = (world.random.nextFloat() - 0.2D) * 0.5D;
        double d4 = (world.random.nextFloat() - 0.2D) * 0.5D;
        double d5 = (world.random.nextFloat() - 0.2D) * 0.5D;

            d0 -= 0.5;
            d1 -= 0.3;
            d2 -= 0.5;
            world.addParticle(ParticleTypes.BUBBLE, d0, d1, d2, d3, d4, d5);
        }
    }

