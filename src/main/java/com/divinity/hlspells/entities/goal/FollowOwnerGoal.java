package com.divinity.hlspells.entities.goal;

import com.divinity.hlspells.entities.Summonable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;

public class FollowOwnerGoal extends Goal {

    private final Mob mob;

    public FollowOwnerGoal(Mob mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        if (this.mob instanceof Summonable summonable) {
            return summonable.getSummonedOwner() != null && this.mob.isAlive() && (this.mob.distanceTo(summonable.getSummonedOwner()) > 12.0D && this.mob.distanceTo(summonable.getSummonedOwner()) <= 50.0D || this.mob.distanceTo(summonable.getSummonedOwner()) > 50D);
        }
        return false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.mob instanceof Summonable summonable) {
            Player owner = summonable.getSummonedOwner();
            if (!owner.isAlive() && this.mob.isAlive()) this.mob.kill();
            else {
                BlockPos ownerPos = owner.blockPosition().offset(-2 + owner.level.random.nextInt(5), 1, -2 + owner.level.random.nextInt(5));
                if (this.mob instanceof Vex vex) vex.setBoundOrigin(ownerPos);
                if (this.mob.distanceTo(owner) > 12.0D && this.mob.distanceTo(owner) <= 50.0D) {
                    this.mob.moveControl.setWantedPosition(ownerPos.getX(), ownerPos.getY(), ownerPos.getZ(), 0.75D);
                }
                else if (this.mob.distanceTo(owner) > 50D) {
                    this.mob.teleportTo(ownerPos.getX(), ownerPos.getY(), ownerPos.getZ());
                    this.mob.moveControl.setWantedPosition(ownerPos.getX(), ownerPos.getY(), ownerPos.getZ(), 0.6D);
                }
            }
        }
    }
}
