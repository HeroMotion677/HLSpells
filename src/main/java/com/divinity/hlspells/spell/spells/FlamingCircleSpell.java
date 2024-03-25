package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.capabilities.playercap.PlayerCapProvider;
import com.divinity.hlspells.setup.init.EntityInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import com.divinity.hlspells.util.Util;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class FlamingCircleSpell extends Spell {


    public FlamingCircleSpell(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, int tickDelay, boolean treasureOnly, int maxSpellLevel, RegistryObject<SimpleParticleType> rune) {
        super(type, rarity, tier, marker, displayName, xpCost, tickDelay, treasureOnly, maxSpellLevel, rune);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            setTime();
            if(time.getTime() <= currentTime+1000) {
                var livingEntities = Util.getEntitiesInRange(p, LivingEntity.class, 6, -1, 6);
                p.getCapability(PlayerCapProvider.PLAYER_CAP).ifPresent(cap -> {
                    cap.setSpellTimer(cap.getSpellTimer() + 1);
                    if (cap.getSpellTimer() % 10 == 0) {
                        doEnchantParticleInterior(p, p.level);
                        cap.setSpellTimer(0);
                    }
                    livingEntities.stream().filter(e -> e != null && e != p).forEach(e -> {
                        e.setLastHurtByPlayer(p);
                        e.setSecondsOnFire(1);
                    });

                });
            }
            return true;
        };
    }

    private static void doEnchantParticleInterior(Player player, Level world) {
        for (int x = -6; x <= 6; x++) {
            for (int z = -6; z <= 6; z++) {
                world.addParticle(ParticleTypes.ENCHANT, player.getX() + x + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + z + world.random.nextFloat(), 0, 0, 0);
                world.addParticle(ParticleTypes.SMOKE, player.getX() + x + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + z + world.random.nextFloat(), 0, 0, 0);
            }
        }
    }

}
