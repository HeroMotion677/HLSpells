package com.divinity.hlspells.goal;

import com.divinity.hlspells.init.SpellInit;
import com.divinity.hlspells.items.SpellHoldingItem;
import com.divinity.hlspells.items.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.util.ResourceLocation;

import java.util.EnumSet;

public class SpellBookLureGoal extends Goal {
    public static final double LURE_RANGE = 20.0D;
    private static final EntityPredicate TEMP_TARGETING = (new EntityPredicate()).range(LURE_RANGE).allowInvulnerable().allowSameTeam().allowNonAttackable().allowUnseeable();
    private final MobEntity entity;
    private final double speedModifier;
    private PlayerEntity player;
    private double pRotX;
    private double pRotY;
    private double px;
    private double py;
    private double pz;
    private boolean isRunning;

    public SpellBookLureGoal(MobEntity entity, double speedModifier) {
        this.entity = entity;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (entity.getNavigation() instanceof GroundPathNavigator || entity.getNavigation() instanceof FlyingPathNavigator) {
            this.player = this.entity.level.getNearestPlayer(TEMP_TARGETING, this.entity);
            return player != null && player.isUsingItem() && this.canFollowItem(player, player.getItemInHand(player.getUsedItemHand())) && this.entity.distanceTo(player) >= 1;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (player != null && entity.distanceTo(this.player) <= LURE_RANGE) {
            if (this.player.distanceToSqr(this.px, this.py, this.pz) > 0.010000000000000002D) {
                return false;
            }

            if (Math.abs(this.player.xRot - this.pRotX) > 5.0D || Math.abs(this.player.yRot - this.pRotY) > 5.0D) {
                return false;
            }
        } else if (player != null) {
            this.px = this.player.getX();
            this.py = this.player.getY();
            this.pz = this.player.getZ();
            this.pRotX = this.player.xRot;
            this.pRotY = this.player.yRot;
        }
        return this.canUse();
    }

    @Override
    public void start() {
        this.px = this.player.getX();
        this.py = this.player.getY();
        this.pz = this.player.getZ();
        this.isRunning = true;
    }

    @Override
    public void stop() {
        this.player = null;
        this.entity.getNavigation().stop();
        this.isRunning = false;
    }

    @Override
    public void tick() {
        this.entity.getLookControl().setLookAt(this.player, (this.entity.getMaxHeadYRot() + 20), this.entity.getMaxHeadXRot());

        if (this.entity.distanceTo(this.player) < 1) {
            this.entity.getNavigation().stop();
        } else {
            this.entity.getNavigation().moveTo(this.player, this.speedModifier);
        }
    }

    private boolean canFollowItem(PlayerEntity player, ItemStack stack) {
        boolean[] canDo = new boolean[2];
        if (stack.getItem() instanceof SpellHoldingItem) {
            stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP, null).ifPresent(cap -> {
                ResourceLocation location = SpellInit.LURE.get().getRegistryName();
                // canDo[0] is true when the current spell is repel.
                canDo[0] = location != null && cap.getCurrentSpell().equals(location.toString());
                // canDo[1] is true when xp requirements are met.
                canDo[1] = SpellUtils.checkXpReq(player, SpellInit.LURE.get());
            });
            return canDo[0] && canDo[1];
        }
        return false;
    }

    public boolean isRunning() {
        return this.isRunning;
    }
}
