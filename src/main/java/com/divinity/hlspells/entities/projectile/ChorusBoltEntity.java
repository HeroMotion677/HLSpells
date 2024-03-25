package com.divinity.hlspells.entities.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class ChorusBoltEntity extends BaseBoltEntity {

    public ChorusBoltEntity(EntityType<? extends BaseBoltEntity> entityType, Level level) {
        super(entityType, level, ParticleTypes.PORTAL, ParticleTypes.PORTAL);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        Entity entity1 = this.getOwner();
        LivingEntity livingentity = entity1 instanceof LivingEntity livingEntity ? livingEntity : null;
        if (entity == this.getOwner()) return;
        boolean flag = entity.hurt(DamageSource.indirectMobAttack(this, livingentity).setProjectile(), 4.0F);
        if (flag && this.level instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 2, 0.2D, 0.2D, 0.2D, 0.0D);
            if (livingentity != null) {
                if (entity instanceof LivingEntity livingEntity) {
                    Level livingLevel = livingEntity.level;
                    if (!livingLevel.isClientSide) {
                        this.doChorusTeleport(livingEntity, livingLevel);
                    }
                    this.doEnchantDamageEffects(livingentity, entity);
                }
            }
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        if (this.level instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 2, 0.2D, 0.2D, 0.2D, 0.0D);
            this.playSound(SoundEvents.SHULKER_BULLET_HIT, 1.0F, 1.0F);
            this.remove(RemovalReason.KILLED);
        }
    }

    private void doChorusTeleport(LivingEntity livingEntity, Level livingLevel) {
        double d0 = livingEntity.getX();
        double d1 = livingEntity.getY();
        double d2 = livingEntity.getZ();
        for (int i = 0; i < 64; ++i) {
            double d3 = livingEntity.getX() + (livingEntity.getRandom().nextDouble() - 0.5D) * 32.0D;
            double d4 = Mth.clamp(livingEntity.getY() + (double) (livingEntity.getRandom().nextInt(32) - 8), livingLevel.getMinBuildHeight(), livingLevel.getMinBuildHeight() + ((ServerLevel) livingLevel).getLogicalHeight() - 1);
            double d5 = livingEntity.getZ() + (livingEntity.getRandom().nextDouble() - 0.5D) * 32.0D;
            if (livingEntity.isPassenger()) livingEntity.stopRiding();
            if (livingEntity.randomTeleport(d3, d4, d5, true)) {
                SoundEvent soundevent = livingEntity instanceof Fox ? SoundEvents.FOX_TELEPORT : SoundEvents.CHORUS_FRUIT_TELEPORT;
                livingEntity.level.playSound(null, d0, d1, d2, soundevent, SoundSource.PLAYERS, 1.0F, 1.0F);
                livingEntity.playSound(soundevent, 1.0F, 1.0F);
                break;
            }
        }
    }
}
