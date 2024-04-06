package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.capabilities.playercap.PlayerCapProvider;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import com.divinity.hlspells.util.Util;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class HealingCircleSpell extends Spell {

    public HealingCircleSpell(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, int tickDelay, boolean treasureOnly, int maxSpellLevel, SimpleParticleType rune) {
        super(type, rarity, tier, marker, displayName, xpCost, tickDelay, treasureOnly, maxSpellLevel, rune);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            var livingEntities = Util.getEntitiesInRange(p, LivingEntity.class, 6, 6, 6);
            p.getCapability(PlayerCapProvider.PLAYER_CAP).ifPresent(cap -> {
                cap.setSpellTimer(cap.getSpellTimer() + 1);
                if (cap.getSpellTimer() % 10 == 0) {
                    doEnchantParticleInterior(p, p.level);
                }
                if (cap.getSpellTimer() % 20 == 0) {
                    for (LivingEntity livingEntity : livingEntities) {
                        doHealingCircleEntityParticle(livingEntity, p.level);
                        if (livingEntity.isInvertedHealAndHarm()) {
                            livingEntity.setLastHurtByPlayer(p);
                            livingEntity.hurt(DamageSource.MAGIC, 1.0F);
                        }
                        else if (livingEntity.getHealth() < livingEntity.getMaxHealth()) {
                            livingEntity.heal(2.5F);
                        }
                    }
                    cap.setSpellTimer(0);
                }
            });
            return true;
        };
    }

    private static void doHealingCircleEntityParticle(LivingEntity livingEntity, Level world) {
        double d0 = (livingEntity.getX() + world.random.nextFloat());
        double d1 = (livingEntity.getY() + world.random.nextFloat());
        double d2 = (livingEntity.getZ() + world.random.nextFloat());
        double d3 = (world.random.nextFloat() - 0.2D) * 0.5D;
        double d4 = (world.random.nextFloat() - 0.2D) * 0.5D;
        double d5 = (world.random.nextFloat() - 0.2D) * 0.5D;
        if (livingEntity.isInvertedHealAndHarm()) {
            for (int i = 0; i < 5; i++) {
                world.addParticle(ParticleTypes.SMOKE, d0, d1, d2, d3, d4, d5);
            }
        }
        else if (livingEntity.getHealth() < livingEntity.getMaxHealth()) {
            d0 -= 0.5;
            d1 -= 0.3;
            d2 -= 0.5;
            world.addParticle(ParticleTypes.HEART, d0, d1, d2, d3, d4, d5);
        }
    }

    private static void doEnchantParticleInterior(Player player, Level world) {
        for (int x = -6; x <= 6; x++) {
            for (int z = -6; z <= 6; z++) {
                world.addParticle(ParticleTypes.ENCHANT, player.getX() + x + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + z + world.random.nextFloat(), 0, 0, 0);
            }
        }
    }
}
