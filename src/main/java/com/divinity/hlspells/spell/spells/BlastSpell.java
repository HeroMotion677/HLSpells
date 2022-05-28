package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import com.divinity.hlspells.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class BlastSpell extends Spell {

    public BlastSpell(String displayName, int xpCost, boolean treasureOnly) {
        super(SpellAttributes.Type.CAST, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.COMBAT, displayName, xpCost, treasureOnly);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            double x = p.getX();
            double y = p.getY();
            double z = p.getZ();
            List<LivingEntity> entities = Util.getEntitiesInRange(p, LivingEntity.class, 6, 6, 6);
            for (LivingEntity entity : entities) {
                if (entity != p) {
                    entity.knockback(5F * 0.5F, Mth.sin(p.yRot * ((float) Math.PI / 180F)), -Mth.cos(p.yRot * ((float) Math.PI / 180F)));
                    entity.hurt(DamageSource.explosion((entity)), 4.0F);
                    p.setDeltaMovement(p.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                }
            }
            p.level.playSound(null, new BlockPos(x, y, z), SoundEvents.GENERIC_EXPLODE,
                    SoundSource.WEATHER, 0.6f, 1.0f);
            p.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 1.0D, 0.0D, 0.0D);
            return true;
        };
    }
}
