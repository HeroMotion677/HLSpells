package com.divinity.hlspells.entities;

import com.divinity.hlspells.HLSpells;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public class AquaBoltEntity extends ArrowEntity {
    public AquaBoltEntity(EntityType<? extends AquaBoltEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected float getWaterInertia() {
        return 1F;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        //Remove if its more than 40 block away from the owner
        if (this.getOwner() != null && this.distanceTo(this.getOwner()) > 40) {
            this.remove();
        }

        Vector3d vector3d1 = this.getDeltaMovement();
        if (this.level.isClientSide) {
            this.level.addParticle(ParticleTypes.RAIN, this.getX() - vector3d1.x, this.getY() - vector3d1.y + 0.15D, this.getZ() - vector3d1.z, 0.0D, 0.0D, 0.0D);
            this.level.addParticle(ParticleTypes.BUBBLE, this.getX() - vector3d1.x, this.getY() - vector3d1.y + 0.16D, this.getZ() - vector3d1.z, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult result) {
        Entity entity = result.getEntity();
        Entity entity1 = this.getOwner();
        LivingEntity livingentity = entity1 instanceof LivingEntity ? (LivingEntity) entity1 : null;
        if (result.getEntity() == this.getOwner()) {
            return;
        }
        List<? extends String> fireMobsList = HLSpells.CONFIG.fireMobsList.get();
        boolean predicate = false;
        for (String id : fireMobsList) {
            if (id.equals(entity.getType().getRegistryName().toString())) {
                predicate = true;
            }
        }
        if (this.isUnderWater() || predicate) {
            boolean flag = entity.hurt(DamageSource.indirectMobAttack(this, livingentity).setProjectile(), 7.0F);
            if (flag && this.level instanceof ServerWorld) {
                ((ServerWorld) this.level).sendParticles(ParticleTypes.BUBBLE_POP, this.getX() - this.random.nextInt(2), this.getY(), this.getZ() - this.random.nextFloat(), 12, 0.2D, 0.2D, 0.2D, 0.0D);
                entity.clearFire();
                this.doEnchantDamageEffects(livingentity, entity);
                this.remove();
            }
        } else {
            boolean flag = entity.hurt(DamageSource.indirectMobAttack(this, livingentity).setProjectile(), 3.0F);
            if (flag && this.level instanceof ServerWorld) {
                System.out.println("Hurt 3");
                ((ServerWorld) this.level).sendParticles(ParticleTypes.BUBBLE_POP, this.getX() - this.random.nextInt(2), this.getY(), this.getZ() - this.random.nextFloat(), 12, 0.2D, 0.2D, 0.2D, 0.0D);
                entity.clearFire();
                this.doEnchantDamageEffects(livingentity, entity);
                this.remove();
            }
        }
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult result) {
        if (this.level instanceof ServerWorld) {
            BlockPos blockpos = result.getBlockPos().relative(result.getDirection());
            BlockPos defaultPos = result.getBlockPos();
            BlockState blockState = this.level.getBlockState(blockpos);
            BlockState defaultState = this.level.getBlockState(defaultPos);
            BlockState airState = Blocks.AIR.defaultBlockState();

            if (blockState.getBlock() == Blocks.FIRE) {
                this.level.setBlockAndUpdate(blockpos, airState);
            } else if (defaultState.getBlock() == Blocks.FIRE) {
                this.level.setBlockAndUpdate(defaultPos, airState);
            } else if (this.level.getBlockState(defaultPos.above()).getBlock() == Blocks.FIRE) {
                this.level.setBlockAndUpdate(defaultPos.above(), airState);
            } else if (this.level.getBlockState(blockpos.below()).getBlock() == Blocks.FIRE) {
                this.level.setBlockAndUpdate(blockpos.below(), airState);
            }
            ((ServerWorld) this.level).sendParticles(ParticleTypes.BUBBLE_POP, this.getX(), this.getY(), this.getZ(), 12, 0.25D, 0.25D, 0.25D, 0D);
            this.playSound(SoundEvents.SHULKER_BULLET_HIT, 1.0F, 1.0F);
            this.remove();
        }
    }

    @Override
    public void checkDespawn() {
        super.checkDespawn();
        if (this.level.getDifficulty() == Difficulty.PEACEFUL) {
            this.remove();
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
            ((ServerWorld) this.level).sendParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 15, 0.2D, 0.2D, 0.2D, 0.0D);
            this.remove();
            return true;
        }
        return false;
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return super.canHitEntity(entity) && !entity.noPhysics;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }
}
