package com.divinity.hlspells.entities.goal;

import com.divinity.hlspells.entities.Summonable;
import com.divinity.hlspells.util.Util;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class AttackedOwnerEnemyGoal extends TargetGoal {

    private final Mob mob;
    private LivingEntity entityToAttack;
    private final TargetingConditions attackedOwnerTargeting = (new TargetingConditions(true)).ignoreInvisibilityTesting();

    public AttackedOwnerEnemyGoal(Mob pMob, boolean pMustSee) {
        super(pMob, pMustSee);
        this.mob = pMob;
    }

    @Override
    public boolean canUse() {
        int scanRange = 15;
        if (this.mob instanceof Summonable summonable) {
            var mobEntities = Util.getEntitiesInRange(this.mob, LivingEntity.class, scanRange, scanRange, scanRange);
            for (LivingEntity mob : mobEntities) {
                if (mob != null && !(mob instanceof Summonable)) {
                    if (mob instanceof NeutralMob neutralMob) {
                        if (neutralMob.getTarget() == summonable.getSummonedOwner()) {
                            this.entityToAttack = mob;
                            return this.mob.getTarget() == null && summonable.getSummonedOwner() != null && this.canAttack(mob, this.attackedOwnerTargeting);
                        }
                    }
                    else if (mob instanceof Mob regularMob) {
                        if (regularMob.getTarget() == summonable.getSummonedOwner()) {
                            this.entityToAttack = mob;
                            return this.mob.getTarget() == null && summonable.getSummonedOwner() != null && this.canAttack(mob, this.attackedOwnerTargeting);
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void start() {
        this.mob.setTarget(this.entityToAttack);
        super.start();
    }
}
