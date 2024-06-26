package com.divinity.hlspells.entities.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.entity.Entity.RemovalReason;

public class FreezingBoltEntity extends BaseBoltEntity {

    public FreezingBoltEntity(EntityType<? extends BaseBoltEntity> entityType, Level level) {
        super(entityType, level, ParticleTypes.SNOWFLAKE, ParticleTypes.SNOWFLAKE);
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        Entity entity = result.getEntity();
        Entity entity1 = this.getOwner();
        LivingEntity livingentity = entity1 instanceof LivingEntity livingEntity ? livingEntity : null;
        if (entity == this.getOwner()) return;
        boolean flag = entity.hurt(DamageSource.indirectMobAttack(this, livingentity).setProjectile(), 6.5F);
        if (flag && this.level instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 2, 0.2D, 0.2D, 0.2D, 0.0D);
            if (livingentity != null) {
                this.doEnchantDamageEffects(livingentity, entity);
                entity.setTicksFrozen(350);
            }
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        if (this.level instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 2, 0.2D, 0.2D, 0.2D, 0.0D);
            this.playSound(SoundEvents.SHULKER_BULLET_HIT, 1.0F, 1.0F);
            BlockPos blockpos = result.getBlockPos().relative(result.getDirection());
            if (this.level.isEmptyBlock(blockpos)) {
                this.level.setBlockAndUpdate(blockpos, Blocks.SNOW.defaultBlockState());
            }
            else if (this.level.getBlockState(blockpos).getFluidState() == Fluids.WATER.defaultFluidState()) {
                this.level.setBlockAndUpdate(blockpos, Blocks.ICE.defaultBlockState());
            }
            this.remove(RemovalReason.KILLED);
        }
    }
}
