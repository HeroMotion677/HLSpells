package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.entities.living.summoned.SummonedSkeletonEntity;
import com.divinity.hlspells.entities.living.summoned.SummonedVexEntity;
import com.divinity.hlspells.setup.init.EntityInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;

public class NecromancySpell extends Spell {

    public NecromancySpell(String displayName, int xpCost, boolean treasureOnly) {
        super(SpellAttributes.Type.CAST, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.THREE, SpellAttributes.Marker.COMBAT, displayName, xpCost, treasureOnly);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            for (int i = 0; i < 4; ++i) {
                BlockPos blockpos = p.blockPosition().offset(-2 + p.level.random.nextInt(5), 1, -2 + p.level.random.nextInt(5));
                SummonedSkeletonEntity skeletonEntity = new SummonedSkeletonEntity(EntityInit.SUMMONED_SKELETON_ENTITY.get(), p.level);
                skeletonEntity.moveTo(blockpos, 0.0F, 0.0F);
                skeletonEntity.setSummonedOwner(p);
                if (p.level instanceof ServerLevel serverWorld) {
                    skeletonEntity.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(blockpos), MobSpawnType.MOB_SUMMONED, null, null);
                    serverWorld.addFreshEntityWithPassengers(skeletonEntity);
                }
            }
            return true;
        };
    }
}
