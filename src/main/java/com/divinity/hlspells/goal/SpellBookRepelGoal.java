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

    public static Vector3d getPosAvoid(MobEntity entity, int horizontalRange, int verticalRange, Vector3d direction) {
        Vector3d vector3d = entity.position().subtract(direction);
        return generateRandomPos(entity, horizontalRange, verticalRange, 0, vector3d, true, (float) Math.PI / 2F, value -> 0, false, 0, 0, true);
    }

    @Nullable
    private static BlockPos getRandomDelta(Random random, int horizontalRange, int verticalRange, int startHeight, @Nullable Vector3d direction, double angleRange) {
        if (direction != null && angleRange < Math.PI) {
            double d3 = MathHelper.atan2(direction.z, direction.x) - ((float) Math.PI / 2F);
            double d4 = d3 + (2.0F * random.nextFloat() - 1.0F) * angleRange;
            double d0 = Math.sqrt(random.nextDouble()) * MathHelper.SQRT_OF_TWO * horizontalRange;
            double d1 = -d0 * Math.sin(d4);
            double d2 = d0 * Math.cos(d4);
            if (Math.abs(d1) <= horizontalRange && Math.abs(d2) <= horizontalRange) {
                int l = random.nextInt(2 * verticalRange + 1) - verticalRange + startHeight;
                return new BlockPos(d1, l, d2);
            } else {
                return null;
            }
        } else {
            int i = random.nextInt(2 * horizontalRange + 1) - horizontalRange;
            int j = random.nextInt(2 * verticalRange + 1) - verticalRange + startHeight;
            int k = random.nextInt(2 * horizontalRange + 1) - horizontalRange;
            return new BlockPos(i, j, k);
        }
    }

    static BlockPos moveUpToAboveSolid(BlockPos pos, int extraAbove, int max, Predicate<BlockPos> condition) {
        if (extraAbove < 0) {
            throw new IllegalArgumentException("aboveSolidAmount was " + extraAbove + ", expected >= 0");
        } else if (!condition.test(pos)) {
            return pos;
        } else {
            BlockPos blockpos;
            for (blockpos = pos.above(); blockpos.getY() < max && condition.test(blockpos); blockpos = blockpos.above()) {
            }

            BlockPos blockpos1;
            BlockPos blockpos2;
            for (blockpos1 = blockpos; blockpos1.getY() < max && blockpos1.getY() - blockpos.getY() < extraAbove; blockpos1 = blockpos2) {
                blockpos2 = blockpos1.above();
                if (condition.test(blockpos2)) {
                    break;
                }
            }

            return blockpos1;
        }
    }

    @Nullable
    private static Vector3d generateRandomPos(MobEntity entity, int horizontalRange, int maxVerticalDistance, int preferredYDifference, @Nullable Vector3d preferredAngle, boolean notInWater, double angleRange, ToDoubleFunction<BlockPos> scorer, boolean aboveGround, int distanceAboveGroundRange, int minDistanceAboveGround, boolean validPositionsOnly) {
        PathNavigator pathnavigator = entity.getNavigation();
        Random random = entity.getRandom();
        boolean flag;
        if (entity.hasRestriction()) {
            flag = entity.getRestrictCenter().closerThan(entity.position(), (entity.getRestrictRadius() + horizontalRange) + 1.0D);
        } else {
            flag = false;
        }

        boolean flag1 = false;
        double d0 = Double.NEGATIVE_INFINITY;
        BlockPos blockpos = entity.blockPosition();

        for (int i = 0; i < 10; ++i) {
            BlockPos blockpos1 = getRandomDelta(random, horizontalRange, maxVerticalDistance, preferredYDifference, preferredAngle, angleRange);
            if (blockpos1 != null) {
                int j = blockpos1.getX();
                int k = blockpos1.getY();
                int l = blockpos1.getZ();
                if (entity.hasRestriction() && horizontalRange > 1) {
                    BlockPos blockpos2 = entity.getRestrictCenter();
                    if (entity.getX() > blockpos2.getX()) {
                        j -= random.nextInt(horizontalRange / 2);
                    } else {
                        j += random.nextInt(horizontalRange / 2);
                    }

                    if (entity.getZ() > blockpos2.getZ()) {
                        l -= random.nextInt(horizontalRange / 2);
                    } else {
                        l += random.nextInt(horizontalRange / 2);
                    }
                }

                BlockPos blockpos3 = new BlockPos(j + entity.getX(), k + entity.getY(), l + entity.getZ());
                if (blockpos3.getY() >= 0 && blockpos3.getY() <= entity.level.getMaxBuildHeight() && (!flag || entity.isWithinRestriction(blockpos3)) && (!validPositionsOnly || pathnavigator.isStableDestination(blockpos3))) {
                    if (aboveGround) {
                        blockpos3 = moveUpToAboveSolid(blockpos3, random.nextInt(distanceAboveGroundRange + 1) + minDistanceAboveGround, entity.level.getMaxBuildHeight(), blockPosx -> entity.level.getBlockState(blockPosx).getMaterial().isSolid());
                    }

                    if (notInWater || !entity.level.getFluidState(blockpos3).is(FluidTags.WATER)) {
                        PathNodeType pathnodetype = WalkNodeProcessor.getBlockPathTypeStatic(entity.level, blockpos3.mutable());
                        if (entity.getPathfindingMalus(pathnodetype) == 0.0F) {
                            double d1 = scorer.applyAsDouble(blockpos3);
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
        return stack.getItem() instanceof SpellBookItem && SpellUtils.getSpell(stack).test(spell -> spell == SpellInit.REPEL.get());
    }
}
