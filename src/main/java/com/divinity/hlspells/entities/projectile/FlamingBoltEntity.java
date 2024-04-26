package com.divinity.hlspells.entities.projectile;

import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.entity.Entity.RemovalReason;

public class FlamingBoltEntity extends BaseBoltEntity {

    public FlamingBoltEntity(EntityType<? extends FlamingBoltEntity> type, Level world) {
        super(type, world, ParticleTypes.FLAME, ParticleTypes.SMOKE);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        Entity entity1 = this.getOwner();
        LivingEntity livingentity = entity1 instanceof LivingEntity livingEntity ? livingEntity : null;
        if (result.getEntity() == this.getOwner()) return;
        boolean hasHurt = entity.hurt(DamageSource.indirectMobAttack(this, livingentity).setProjectile(), 6.5F);
        if (hasHurt && level instanceof ServerLevel level) {
            for (int i = 0; i < 3; i++) {
                level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX() - this.random.nextInt(2),
                        this.getY(), this.getZ() - this.random.nextFloat(), 2, 0.2D, 0.2D, 0.2D, 0.1D);
            }
            entity.setSecondsOnFire(5);
            level.sendParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 15, 0.2D, 0.2D, 0.2D, 0.0D);
            if (livingentity != null) this.doEnchantDamageEffects(livingentity, entity);
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        if (this.level instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(), 2, 0.2D, 0.2D, 0.2D, 0.0D);
            BlockPos blockpos = result.getBlockPos().relative(result.getDirection());
            if (this.level.isEmptyBlock(blockpos)) this.level.setBlockAndUpdate(blockpos, BaseFireBlock.getState(this.level, blockpos));
            this.playSound(SoundEvents.SHULKER_BULLET_HIT, 1.0F, 1.0F);
            this.remove(RemovalReason.KILLED);
        }
    }
}
