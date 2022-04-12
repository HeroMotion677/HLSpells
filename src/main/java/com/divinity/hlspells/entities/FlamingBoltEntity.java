package com.divinity.hlspells.entities;

import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.network.protocol.Packet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkHooks;

public class FlamingBoltEntity extends Arrow {
    public FlamingBoltEntity(EntityType<? extends FlamingBoltEntity> type, Level world) {
        super(type, world);
        this.setNoGravity(true);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getOwner() != null && this.distanceTo(this.getOwner()) > 40) {
            this.remove(RemovalReason.KILLED);
        }
        Vec3 vector3d1 = this.getDeltaMovement();
        if (this.level.isClientSide) {
            this.level.addParticle(ParticleTypes.FLAME, this.getX() - vector3d1.x, this.getY() - vector3d1.y + 0.15D, this.getZ() - vector3d1.z, 0.0D, 0.0D, 0.0D);
            this.level.addParticle(ParticleTypes.SMOKE, this.getX() - vector3d1.x, this.getY() - vector3d1.y + 0.16D, this.getZ() - vector3d1.z, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        Entity entity1 = this.getOwner();
        LivingEntity livingentity = entity1 instanceof LivingEntity ? (LivingEntity) entity1 : null;

        if (result.getEntity() == this.getOwner()) {
            return;
        }

        boolean flag = entity.hurt(DamageSource.indirectMobAttack(this, livingentity).setProjectile(), 8.0F);
        if (flag && level instanceof ServerLevel) {
            for (int i = 0; i < 3; i++) {
                ((ServerLevel) this.level).sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX() - this.random.nextInt(2),
                        this.getY(), this.getZ() - this.random.nextFloat(), 2, 0.2D, 0.2D, 0.2D, 0.0D);
            }
            entity.setSecondsOnFire(5);
            ((ServerLevel) this.level).sendParticles(ParticleTypes.CRIT, this.getX(),
                    this.getY(), this.getZ(), 15, 0.2D, 0.2D, 0.2D, 0.0D);
            if (livingentity != null)
                this.doEnchantDamageEffects(livingentity, entity);
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    protected float getWaterInertia() {
        return 1F;
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (this.level instanceof ServerLevel) {
            ((ServerLevel) this.level).sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(), 2, 0.2D, 0.2D, 0.2D, 0.0D);
            BlockPos blockpos = result.getBlockPos().relative(result.getDirection());
            if (this.level.isEmptyBlock(blockpos)) {
                this.level.setBlockAndUpdate(blockpos, BaseFireBlock.getState(this.level, blockpos));
            }
            this.playSound(SoundEvents.SHULKER_BULLET_HIT, 1.0F, 1.0F);
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    public void checkDespawn() {
        super.checkDespawn();
        if (this.level.getDifficulty() == Difficulty.PEACEFUL) {
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.SHULKER_BULLET_HIT;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.level.isClientSide && source.isProjectile() && this.isAlive()) {
            this.playSound(SoundEvents.SHULKER_BULLET_HURT, 1.0F, 1.0F);
            ((ServerLevel) this.level).sendParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 15, 0.2D, 0.2D, 0.2D, 0.0D);
            this.remove(RemovalReason.KILLED);
            return true;
        }
        return false;
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return super.canHitEntity(entity) && !entity.noPhysics;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }
}
