package com.divinity.hlspells.entities.goal;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;

public class DecayGoal extends Goal {

    private final Mob mob;
    private int decayAmount;

    public DecayGoal(Mob mob) {
        this.mob = mob;
        this.decayAmount = 20 * (30 + this.mob.level.random.nextInt(50));
    }

    @Override
    public boolean canUse() {
        return this.mob.isAlive();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (--this.decayAmount <= 0) {
            this.decayAmount = 20;
            this.mob.hurt(DamageSource.STARVE, 3.0F);
        }
    }
}
