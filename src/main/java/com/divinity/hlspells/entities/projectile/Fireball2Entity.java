package com.divinity.hlspells.entities.projectile;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.level.Explosion;
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
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Fireball2Entity extends BaseBoltEntity {

    public Fireball2Entity(EntityType<? extends BaseBoltEntity> type, Level world) {
        super(type, world, ParticleTypes.PORTAL, ParticleTypes.SMOKE,ParticleTypes.PORTAL, ParticleTypes.SMOKE,ParticleTypes.PORTAL, ParticleTypes.SMOKE);
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
                level.sendParticles(ParticleTypes.FLAME, this.getX() - this.random.nextInt(2),
                        this.getY(), this.getZ() - this.random.nextFloat(), 2, 0.2D, 0.2D, 0.2D, 0.0D);
            }
            entity.setSecondsOnFire(5);
            level.explode((Entity)null, this.getX(), this.getY(), this.getZ(),4, Explosion.BlockInteraction.DESTROY);
            level.sendParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 15, 0.2D, 0.2D, 0.2D, 0.0D);
            if (livingentity != null) this.doEnchantDamageEffects(livingentity, entity);
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        if (this.level instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(), 2, 0.2D, 0.2D, 0.2D, 0.0D);
            level.explode((Entity)null, this.getX(), this.getY(), this.getZ(),4, Explosion.BlockInteraction.DESTROY);
            BlockPos blockpos = result.getBlockPos().relative(result.getDirection());
            if (this.level.isEmptyBlock(blockpos)) this.level.setBlockAndUpdate(blockpos, BaseFireBlock.getState(this.level, blockpos));
            this.playSound(SoundEvents.SHULKER_BULLET_HIT, 1.0F, 1.0F);
            this.remove(RemovalReason.KILLED);
        }
    }

    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (pResult.getType() != HitResult.Type.ENTITY || !this.ownedBy(((EntityHitResult)pResult).getEntity())) {
            if (!this.level.isClientSide) {
                List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D));
                Entity entity = this.getOwner();
                if (!list.isEmpty()) {
                    for(LivingEntity livingentity : list) {
                        double d0 = this.distanceToSqr(livingentity);
                    }
                }

                this.level.levelEvent(2006, this.blockPosition(), this.isSilent() ? -1 : 1);
                this.discard();
            }

        }
    }
}
