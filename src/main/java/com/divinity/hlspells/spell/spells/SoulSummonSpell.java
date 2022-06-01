package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.entities.living.SummonedVexEntity;
import com.divinity.hlspells.setup.init.EntityInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;

public class SoulSummonSpell extends Spell {

    public SoulSummonSpell(String displayName, int xpCost, boolean treasureOnly) {
        super(SpellAttributes.Type.CAST, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.THREE, SpellAttributes.Marker.COMBAT, displayName, xpCost, treasureOnly);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            for (int i = 0; i < 4; ++i) {
                BlockPos blockpos = p.blockPosition().offset(-2 + p.level.random.nextInt(5), 1, -2 + p.level.random.nextInt(5));
                SummonedVexEntity vexEntity = new SummonedVexEntity(EntityInit.SUMMONED_VEX_ENTITY.get(), p.level);
                vexEntity.moveTo(blockpos, 0.0F, 0.0F);
                vexEntity.setSummonedOwner(p);
                vexEntity.setLimitedLife(20 * (30 + p.level.random.nextInt(50)));
                if (p.level instanceof ServerLevel serverWorld) {
                    vexEntity.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(blockpos), MobSpawnType.MOB_SUMMONED, null, null);
                    serverWorld.addFreshEntityWithPassengers(vexEntity);
                }
            }
            return true;
        };
    }
}
