package com.divinity.hlspells.entities.goal;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.entities.Summonable;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

public class CopyOwnerTargetGoal extends TargetGoal {

    private final Mob mob;
    private final TargetingConditions copyOwnerTargeting = (new TargetingConditions(true)).ignoreInvisibilityTesting();

    public CopyOwnerTargetGoal(Mob pMob, boolean pMustSee) {
        super(pMob, pMustSee);
        this.mob = pMob;
    }

    @Override
    public boolean canUse() {
        if (this.mob instanceof Summonable summonable) {
            if (this.mob.getTarget() == null && summonable.getSummonedOwner() != null && summonable.getSummonedOwner().getLastHurtMob() != null && this.canAttack(summonable.getSummonedOwner().getLastHurtMob(), this.copyOwnerTargeting) && !(summonable.getSummonedOwner().getLastHurtMob() instanceof Summonable)) {
                if (summonable.getSummonedOwner().getLastHurtMob() instanceof Player) {
                    return HLSpells.CONFIG.summonsAttackPlayers.get();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void start() {
        if (this.mob instanceof Summonable summonable) {
            this.mob.setTarget(summonable.getSummonedOwner().getLastHurtMob());
            super.start();
        }
    }
}
