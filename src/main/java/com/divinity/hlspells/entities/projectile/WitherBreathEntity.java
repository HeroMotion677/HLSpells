package com.divinity.hlspells.entities.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class WitherBreathEntity extends BaseBreathEntity {

    public WitherBreathEntity(EntityType<? extends WitherBreathEntity> type, Level world) {
        super(type, world, ParticleTypes.SMOKE, ParticleTypes.SMOKE);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        Entity entity1 = this.getOwner();
        LivingEntity livingentity = entity1 instanceof LivingEntity livingEntity ? livingEntity : null;

        LivingEntity livingentity1 = entity instanceof LivingEntity livingEntity ? livingEntity : null;

        if (result.getEntity() == this.getOwner()) return;
        boolean hasHurt = entity.hurt(new DamageSources(this.level().registryAccess()).mobProjectile(this, livingentity), 5F);


                if (hasHurt && level() instanceof ServerLevel level) {

                    for (int i = 0; i < 3; i++) {
                        level.sendParticles(ParticleTypes.SMOKE, this.getX() - this.random.nextInt(2),
                                this.getY(), this.getZ() - this.random.nextFloat(), 2, 0.2D, 0.2D, 0.2D, 0.1D);
                    }
                    livingentity1.addEffect(new MobEffectInstance(MobEffects.WITHER, 20 * 10, 1), this.getEffectSource());
                    level.sendParticles(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 3, 0.2D, 0.2D, 0.2D, 0.0D);
                    if (livingentity != null) this.doEnchantDamageEffects(livingentity, entity);
                    this.remove(RemovalReason.KILLED);
                }
            }





        @Override
        protected void onHitBlock (@NotNull BlockHitResult result){
            if (this.level() instanceof ServerLevel level) {
                level.sendParticles(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 2, 0.2D, 0.2D, 0.2D, 0.0D);

                this.remove(RemovalReason.KILLED);
            }
        }

}
