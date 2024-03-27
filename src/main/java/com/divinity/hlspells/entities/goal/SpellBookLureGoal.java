package com.divinity.hlspells.entities.goal;

import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.items.spellitems.SpellHoldingItem;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.resources.ResourceLocation;

import java.util.EnumSet;

public class SpellBookLureGoal extends Goal {

    public static final double LURE_RANGE = 20.0D;
    private static final TargetingConditions TEMP_TARGETING = (new TargetingConditions(true)).range(LURE_RANGE).ignoreInvisibilityTesting().ignoreLineOfSight();
    private final Mob entity;
    private final double speedModifier;
    private Player player;
    private double pRotX;
    private double pRotY;
    private double px;
    private double py;
    private double pz;

    public SpellBookLureGoal(Mob entity, double speedModifier) {
        this.entity = entity;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (entity.getNavigation() instanceof GroundPathNavigation || entity.getNavigation() instanceof FlyingPathNavigation) {
            this.player = this.entity.level.getNearestPlayer(TEMP_TARGETING, this.entity);
            return player != null && player.isUsingItem() && this.canFollowItem(player, player.getItemInHand(player.getUsedItemHand())) && this.entity.distanceTo(player) >= 1;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (player != null && entity.distanceTo(this.player) <= LURE_RANGE) {
            if (this.player.distanceToSqr(this.px, this.py, this.pz) > 0.010000000000000002D) return false;
            if (Math.abs(this.player.xRot - this.pRotX) > 5.0D || Math.abs(this.player.yRot - this.pRotY) > 5.0D) return false;
        }
        else if (player != null) {
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
    }

    @Override
    public void stop() {
        this.player = null;
        this.entity.getNavigation().stop();
    }

    @Override
    public void tick() {
        this.entity.getLookControl().setLookAt(this.player, (this.entity.getMaxHeadYRot() + 20), this.entity.getMaxHeadXRot());
        if (this.entity.distanceTo(this.player) < 1) this.entity.getNavigation().stop();
        else this.entity.getNavigation().moveTo(this.player, this.speedModifier);
    }

    private boolean canFollowItem(Player player, ItemStack stack) {
        boolean[] canDo = new boolean[2];
        if (stack.getItem() instanceof SpellHoldingItem) {
            stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP, null).ifPresent(cap -> {
                ResourceLocation location = SpellInit.SPELLS_REGISTRY.get().getKey(SpellInit.LURE.get());
                // canDo[0] is true when the current spell is 'repel'.
                canDo[0] = location != null && cap.getCurrentSpell().equals(location.toString());
                // canDo[1] is true when xp requirements are met.
                canDo[1] = SpellUtils.checkXpReq(player, SpellInit.LURE.get());
            });
            return canDo[0] && canDo[1];
        }
        return false;
    }
}
