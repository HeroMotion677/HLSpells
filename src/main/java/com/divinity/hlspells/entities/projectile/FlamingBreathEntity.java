package com.divinity.hlspells.entities.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class FlamingBreathEntity extends BaseBreathEntity {

    public FlamingBreathEntity(EntityType<? extends FlamingBreathEntity> type, Level world) {
        super(type, world, ParticleTypes.FLAME, ParticleTypes.SMOKE);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        Entity entity1 = this.getOwner();
        LivingEntity livingentity = entity1 instanceof LivingEntity livingEntity ? livingEntity : null;
        if (result.getEntity() == this.getOwner()) return;
        boolean hasHurt = entity.hurt(new DamageSources(this.level().registryAccess()).mobProjectile(this, livingentity), 7F);
       {

                if (hasHurt && level() instanceof ServerLevel level) {
                    for (int i = 0; i < 3; i++) {
                        level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX() - this.random.nextInt(2),
                                this.getY(), this.getZ() - this.random.nextFloat(), 2, 0.2D, 0.2D, 0.2D, 0.1D);
                    }
                    entity.setSecondsOnFire(5);
                    level.sendParticles(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 15, 0.2D, 0.2D, 0.2D, 0.0D);
                    if (livingentity != null) this.doEnchantDamageEffects(livingentity, entity);
                    this.remove(RemovalReason.KILLED);
                }
            }

            }



        @Override
        protected void onHitBlock (@NotNull BlockHitResult result){
            if (this.level() instanceof ServerLevel level) {
                level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(), 2, 0.2D, 0.2D, 0.2D, 0.0D);
                BlockPos blockpos = result.getBlockPos().relative(result.getDirection());
                if (this.level().isEmptyBlock(blockpos))
                    this.level().setBlockAndUpdate(blockpos, BaseFireBlock.getState(this.level(), blockpos));
                this.remove(RemovalReason.KILLED);
            }
        }

}
