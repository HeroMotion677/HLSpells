package com.divinity.hlspells.goal;

import com.divinity.hlspells.init.SpellInit;
import com.divinity.hlspells.items.SpellBookItem;
import com.divinity.hlspells.items.WandItem;
import com.divinity.hlspells.items.capabilities.wandcap.WandItemProvider;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.*;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;


public class SpellBookRepelGoal extends Goal {
    private static final EntityPredicate TEMP_TARGETING = (new EntityPredicate()).range(15.0D).allowInvulnerable().allowSameTeam().allowNonAttackable().allowUnseeable();
    protected final PathNavigator pathNav;
    private final MobEntity entity;
    private final double speedModifier;
    protected Path path;
    private PlayerEntity toAvoid;

    public SpellBookRepelGoal(MobEntity entity, double speedModifier) {
        this.entity = entity;
        this.speedModifier = speedModifier;
        this.pathNav = entity.getNavigation();
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public static Vector3d getPosAvoid(MobEntity p_75461_0_, int p_75461_1_, int p_75461_2_, Vector3d p_75461_3_) {
        Vector3d vector3d = p_75461_0_.position().subtract(p_75461_3_);
        return generateRandomPos(p_75461_0_, p_75461_1_, p_75461_2_, 0, vector3d, true, (float) Math.PI / 2F, value -> 0, false, 0, 0, true);
    }

    @Nullable
    private static BlockPos getRandomDelta(Random p_226343_0_, int p_226343_1_, int p_226343_2_, int p_226343_3_, @Nullable Vector3d p_226343_4_, double p_226343_5_) {
        if (p_226343_4_ != null && p_226343_5_ < Math.PI) {
            double d3 = MathHelper.atan2(p_226343_4_.z, p_226343_4_.x) - ((float) Math.PI / 2F);
            double d4 = d3 + (2.0F * p_226343_0_.nextFloat() - 1.0F) * p_226343_5_;
            double d0 = Math.sqrt(p_226343_0_.nextDouble()) * MathHelper.SQRT_OF_TWO * p_226343_1_;
            double d1 = -d0 * Math.sin(d4);
            double d2 = d0 * Math.cos(d4);
            if (Math.abs(d1) <= p_226343_1_ && Math.abs(d2) <= p_226343_1_) {
                int l = p_226343_0_.nextInt(2 * p_226343_2_ + 1) - p_226343_2_ + p_226343_3_;
                return new BlockPos(d1, l, d2);
            } else {
                return null;
            }
        } else {
            int i = p_226343_0_.nextInt(2 * p_226343_1_ + 1) - p_226343_1_;
            int j = p_226343_0_.nextInt(2 * p_226343_2_ + 1) - p_226343_2_ + p_226343_3_;
            int k = p_226343_0_.nextInt(2 * p_226343_1_ + 1) - p_226343_1_;
            return new BlockPos(i, j, k);
        }
    }

    static BlockPos moveUpToAboveSolid(BlockPos p_226342_0_, int p_226342_1_, int p_226342_2_, Predicate<BlockPos> p_226342_3_) {
        if (p_226342_1_ < 0) {
            throw new IllegalArgumentException("aboveSolidAmount was " + p_226342_1_ + ", expected >= 0");
        } else if (!p_226342_3_.test(p_226342_0_)) {
            return p_226342_0_;
        } else {
            BlockPos blockpos;
            for (blockpos = p_226342_0_.above(); blockpos.getY() < p_226342_2_ && p_226342_3_.test(blockpos); blockpos = blockpos.above()) {
            }

            BlockPos blockpos1;
            BlockPos blockpos2;
            for (blockpos1 = blockpos; blockpos1.getY() < p_226342_2_ && blockpos1.getY() - blockpos.getY() < p_226342_1_; blockpos1 = blockpos2) {
                blockpos2 = blockpos1.above();
                if (p_226342_3_.test(blockpos2)) {
                    break;
                }
            }

            return blockpos1;
        }
    }

    @Nullable
    private static Vector3d generateRandomPos(MobEntity p_226339_0_, int p_226339_1_, int p_226339_2_, int p_226339_3_, @Nullable Vector3d p_226339_4_, boolean p_226339_5_, double p_226339_6_, ToDoubleFunction<BlockPos> p_226339_8_, boolean p_226339_9_, int p_226339_10_, int p_226339_11_, boolean p_226339_12_) {
        PathNavigator pathnavigator = p_226339_0_.getNavigation();
        Random random = p_226339_0_.getRandom();
        boolean flag;
        if (p_226339_0_.hasRestriction()) {
            flag = p_226339_0_.getRestrictCenter().closerThan(p_226339_0_.position(), (p_226339_0_.getRestrictRadius() + p_226339_1_) + 1.0D);
        } else {
            flag = false;
        }

        boolean flag1 = false;
        double d0 = Double.NEGATIVE_INFINITY;
        BlockPos blockpos = p_226339_0_.blockPosition();

        for (int i = 0; i < 10; ++i) {
            BlockPos blockpos1 = getRandomDelta(random, p_226339_1_, p_226339_2_, p_226339_3_, p_226339_4_, p_226339_6_);
            if (blockpos1 != null) {
                int j = blockpos1.getX();
                int k = blockpos1.getY();
                int l = blockpos1.getZ();
                if (p_226339_0_.hasRestriction() && p_226339_1_ > 1) {
                    BlockPos blockpos2 = p_226339_0_.getRestrictCenter();
                    if (p_226339_0_.getX() > blockpos2.getX()) {
                        j -= random.nextInt(p_226339_1_ / 2);
                    } else {
                        j += random.nextInt(p_226339_1_ / 2);
                    }

                    if (p_226339_0_.getZ() > blockpos2.getZ()) {
                        l -= random.nextInt(p_226339_1_ / 2);
                    } else {
                        l += random.nextInt(p_226339_1_ / 2);
                    }
                }

                BlockPos blockpos3 = new BlockPos(j + p_226339_0_.getX(), k + p_226339_0_.getY(), l + p_226339_0_.getZ());
                if (blockpos3.getY() >= 0 && blockpos3.getY() <= p_226339_0_.level.getMaxBuildHeight() && (!flag || p_226339_0_.isWithinRestriction(blockpos3)) && (!p_226339_12_ || pathnavigator.isStableDestination(blockpos3))) {
                    if (p_226339_9_) {
                        blockpos3 = moveUpToAboveSolid(blockpos3, random.nextInt(p_226339_10_ + 1) + p_226339_11_, p_226339_0_.level.getMaxBuildHeight(), (p_226341_1_) -> {
                            return p_226339_0_.level.getBlockState(p_226341_1_).getMaterial().isSolid();
                        });
                    }

                    if (p_226339_5_ || !p_226339_0_.level.getFluidState(blockpos3).is(FluidTags.WATER)) {
                        PathNodeType pathnodetype = WalkNodeProcessor.getBlockPathTypeStatic(p_226339_0_.level, blockpos3.mutable());
                        if (p_226339_0_.getPathfindingMalus(pathnodetype) == 0.0F) {
                            double d1 = p_226339_8_.applyAsDouble(blockpos3);
                            if (d1 > d0) {
                                d0 = d1;
                                blockpos = blockpos3;
                                flag1 = true;
                            }
                        }
                    }
                }
            }
        }

        return flag1 ? Vector3d.atBottomCenterOf(blockpos) : null;
    }

    @Override
    public boolean canUse() {
        if (this.entity.getNavigation() instanceof GroundPathNavigator || this.entity.getNavigation() instanceof FlyingPathNavigator) {
            this.toAvoid = this.entity.level.getNearestPlayer(TEMP_TARGETING, this.entity);

            if (this.toAvoid == null) {
                return false;
            } else if (this.toAvoid.isUsingItem() && canRepelItem(toAvoid, toAvoid.getItemInHand(toAvoid.getUsedItemHand())) && this.entity.distanceTo(this.toAvoid) <= 15) {
                Vector3d vector3d = getPosAvoid(this.entity, 16, 7, this.toAvoid.position());
                if (vector3d == null) {
                    return false;
                } else if (this.toAvoid.distanceToSqr(vector3d.x, vector3d.y, vector3d.z) < this.toAvoid.distanceToSqr(this.entity)) {
                    return false;
                } else {
                    this.path = this.pathNav.createPath(vector3d.x, vector3d.y, vector3d.z, 0);
                    return this.path != null;
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.pathNav.isDone();
    }

    @Override
    public void start() {
        this.pathNav.moveTo(this.path, this.speedModifier);
    }

    @Override
    public void stop() {
        this.toAvoid = null;
    }

    @Override
    public void tick() {
        if (this.entity.distanceTo(this.toAvoid) <= 15.0D) {
            this.entity.getNavigation().setSpeedModifier(this.speedModifier);
        }
    }

    private boolean canRepelItem(PlayerEntity player, ItemStack stack) {
        boolean[] canDo = new boolean[2];
        if (stack.getItem() instanceof WandItem) {
            stack.getCapability(WandItemProvider.WAND_CAP, null).ifPresent(cap ->
            {
                ResourceLocation location = SpellInit.REPEL.get().getRegistryName();
                // canDo[0] is true when the current spell is repel.
                canDo[0] = location != null && cap.getSpells().get(cap.getCurrentSpellCycle()).equals(location.toString());
                // canDo[1] is true when xp requirements are met.
                canDo[1] = player.totalExperience >= SpellInit.REPEL.get().getXpCost() && SpellInit.REPEL.get().hasCost();

            });
            if (canDo[0] && canDo[1]) {
                return true;
            }
        }
        return stack.getItem() instanceof SpellBookItem && SpellUtils.getSpellBook(stack).containsSpell(p -> p.getSpell() == SpellInit.REPEL.get());
    }
}
