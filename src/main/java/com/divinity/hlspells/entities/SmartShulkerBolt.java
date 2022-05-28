package com.divinity.hlspells.entities;

import com.google.common.collect.Lists;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.network.protocol.Packet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SmartShulkerBolt extends ShulkerBullet {

    public SmartShulkerBolt(Level world, LivingEntity entity, Entity targetEntity, Direction.Axis direction) {
        super(world, entity, targetEntity, direction);
    }

    @Override
    public void selectNextMoveDirection(@Nullable Direction.Axis axis) {
        double d0 = 0.5D;
        BlockPos blockpos;
        if (this.finalTarget == null) blockpos = this.blockPosition().below();
        else {
            d0 = this.finalTarget.getBbHeight() * 0.5D;
            blockpos = new BlockPos(this.finalTarget.getX(), this.finalTarget.getY() + d0, this.finalTarget.getZ());
        }
        double d1 = blockpos.getX() + 0.5D;
        double d2 = blockpos.getY() + d0;
        double d3 = blockpos.getZ() + 0.5D;
        Direction direction = null;
        if (!blockpos.closerToCenterThan(this.position(), 2.0D)) {
            BlockPos blockpos1 = this.blockPosition();
            List<Direction> list = Lists.newArrayList();
            if (axis != Direction.Axis.X) {
                if (blockpos1.getX() < blockpos.getX() && this.level.isEmptyBlock(blockpos1.east())) {
                    list.add(Direction.EAST);
                }
                else if (blockpos1.getX() > blockpos.getX() && this.level.isEmptyBlock(blockpos1.west())) {
                    list.add(Direction.WEST);
                }
            }
            if (axis != Direction.Axis.Y) {
                if (blockpos1.getY() < blockpos.getY() && this.level.isEmptyBlock(blockpos1.above())) list.add(Direction.UP);
                else if (blockpos1.getY() > blockpos.getY() && this.level.isEmptyBlock(blockpos1.below())) list.add(Direction.DOWN);
            }
            if (axis != Direction.Axis.Z) {
                if (blockpos1.getZ() < blockpos.getZ() && this.level.isEmptyBlock(blockpos1.south())) list.add(Direction.SOUTH);
                else if (blockpos1.getZ() > blockpos.getZ() && this.level.isEmptyBlock(blockpos1.north())) list.add(Direction.NORTH);
            }
            direction = Direction.getRandom(this.random);
            if (list.isEmpty()) {
                for (int i = 1; !this.level.isEmptyBlock(blockpos1.relative(direction)) && i > 0; --i) {
                    direction = Direction.getRandom(this.random);
                }
            }
            else direction = list.get(this.random.nextInt(list.size()));
            d1 = this.getX() + direction.getStepX();
            d2 = this.getY() + direction.getStepY();
            d3 = this.getZ() + direction.getStepZ();
        }
        this.setMoveDirection(direction);
        double d6 = d1 - this.getX();
        double d7 = d2 - this.getY();
        double d4 = d3 - this.getZ();
        double d5 = Math.sqrt(d6 * d6 + d7 * d7 + d4 * d4);
        if (d5 == 0.0D) {
            this.targetDeltaX = 0.0D;
            this.targetDeltaY = 0.0D;
            this.targetDeltaZ = 0.0D;
        }
        else {
            this.targetDeltaX = d6 / d5 * 0.38D;
            this.targetDeltaY = d7 / d5 * 0.38D;
            this.targetDeltaZ = d4 / d5 * 0.38D;
        }
        this.hasImpulse = true;
        this.flightSteps = 10;
    }

    @Override @Nonnull public Packet<?> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        Entity entity1 = this.getOwner();
        LivingEntity livingentity = entity1 instanceof LivingEntity livingEntity ? livingEntity : null;
        if (result.getEntity() == this.getOwner()) return;
        boolean flag = entity.hurt(DamageSource.indirectMobAttack(this, livingentity).setProjectile(), 8.0F);
        if (flag) {
            if (livingentity != null) this.doEnchantDamageEffects(livingentity, entity);
            this.remove(RemovalReason.KILLED);
        }
    }
}
