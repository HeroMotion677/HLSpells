package com.divinity.hlspells.entities.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class FreezingBreathEntity extends BaseBreathEntity {

    public FreezingBreathEntity(EntityType<? extends FreezingBreathEntity> type, Level world) {
        super(type, world, ParticleTypes.CLOUD);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        Entity entity1 = this.getOwner();
        LivingEntity livingentity = entity1 instanceof LivingEntity livingEntity ? livingEntity : null;
        if (result.getEntity() == this.getOwner()) return;
        boolean hasHurt = entity.hurt(new DamageSources(this.level().registryAccess()).mobProjectile(this, livingentity), 4F);
       {

                if (hasHurt && level() instanceof ServerLevel level) {
                    for (int i = 0; i < 3; i++) {
                        level.sendParticles(ParticleTypes.SNOWFLAKE, this.getX() - this.random.nextInt(2),
                                this.getY(), this.getZ() - this.random.nextFloat(), 2, 0.2D, 0.2D, 0.2D, 0.1D);
                    }
                    entity.setTicksFrozen(350);
                    level.sendParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), 3, 0.2D, 0.2D, 0.2D, 0.0D);
                    if (livingentity != null) EnchantmentHelper.doPostAttackEffects(level, entity, this.damageSources().mobProjectile(this, livingentity));
                    this.remove(RemovalReason.KILLED);
                }
            }

            }



        @Override
        protected void onHitBlock (@NotNull BlockHitResult result){
            if (this.level() instanceof ServerLevel level) {
                level.sendParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), 2, 0.2D, 0.2D, 0.2D, 0.0D);
                BlockPos blockpos = result.getBlockPos().relative(result.getDirection());
                if (this.level().isEmptyBlock(blockpos))
                    this.level().setBlockAndUpdate(blockpos, Blocks.SNOW.defaultBlockState());
                this.remove(RemovalReason.KILLED);
            }
        }

}
