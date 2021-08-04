package com.divinity.hlspells.goal;

import com.divinity.hlspells.init.SpellInit;
import com.divinity.hlspells.items.SpellBookItem;
import com.divinity.hlspells.items.WandItem;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.GroundPathNavigator;

import java.util.EnumSet;

public class SpellBookLureGoal extends Goal
{
    public static final double LURE_RANGE = 20.0D;
    private MobEntity entity;
    private PlayerEntity player;
    private final double speedModifier;
    private static final EntityPredicate TEMP_TARGETING = (new EntityPredicate()).range(LURE_RANGE).allowInvulnerable().allowSameTeam().allowNonAttackable().allowUnseeable();
    private double pRotX;
    private double pRotY;
    private double px;
    private double py;
    private double pz;
    private boolean isRunning;

    public SpellBookLureGoal(MobEntity entity, double speedModifier)
    {
        this.entity = entity;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse()
    {
       if (entity.getNavigation() instanceof GroundPathNavigator || entity.getNavigation() instanceof FlyingPathNavigator)
       {
            if (entity != null)
            {
                this.player = this.entity.level.getNearestPlayer(TEMP_TARGETING, this.entity);
                return player != null && player.isUsingItem() && this.canFollowItem(player.getItemInHand(player.getUsedItemHand())) && this.entity.distanceTo(player) >= 1;
            }
       }
       return false;
    }

    @Override
    public boolean canContinueToUse()
    {
        if (player != null && entity.distanceTo(this.player) <= LURE_RANGE)
        {
            if (this.player.distanceToSqr(this.px, this.py, this.pz) > 0.010000000000000002D)
            {
                return false;
            }

            if (Math.abs((double)this.player.xRot - this.pRotX) > 5.0D || Math.abs((double)this.player.yRot - this.pRotY) > 5.0D)
            {
                return false;
            }
        }

        else if (player != null)
        {
            this.px = this.player.getX();
            this.py = this.player.getY();
            this.pz = this.player.getZ();
            this.pRotX = this.player.xRot;
            this.pRotY = this.player.yRot;
        }
        return this.canUse();
    }

    @Override
    public void start()
    {
        this.px = this.player.getX();
        this.py = this.player.getY();
        this.pz = this.player.getZ();
        this.isRunning = true;
    }

    @Override
    public void stop()
    {
        this.player = null;
        this.entity.getNavigation().stop();
        this.isRunning = false;
    }

    @Override
    public void tick()
    {
        this.entity.getLookControl().setLookAt(this.player, (float)(this.entity.getMaxHeadYRot() + 20), (float)this.entity.getMaxHeadXRot());

        if (this.entity.distanceTo(this.player) < 1)
        {
            this.entity.getNavigation().stop();
        }

        else
        {
            this.entity.getNavigation().moveTo(this.player, this.speedModifier);
        }
    }

    // TODO: Update this to work with the wand
    private boolean canFollowItem(ItemStack stack)
    {
        return stack.getItem() instanceof WandItem || stack.getItem() instanceof SpellBookItem && SpellUtils.getSpellBook(stack).containsSpell(p -> p.getSpell() == SpellInit.LURE.get());
    }

    public boolean isRunning () { return this.isRunning; }
}
