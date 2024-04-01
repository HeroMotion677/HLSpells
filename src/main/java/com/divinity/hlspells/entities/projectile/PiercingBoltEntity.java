package com.divinity.hlspells.entities.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.entity.Entity.RemovalReason;

public class PiercingBoltEntity extends BaseBoltEntity {

    private static final EntityDataAccessor<Boolean> IS_SPECIAL = SynchedEntityData.defineId(PiercingBoltEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> PIERCED_ENEMIES = SynchedEntityData.defineId(PiercingBoltEntity.class, EntityDataSerializers.INT);
    boolean isSpecial;

    public PiercingBoltEntity(EntityType<? extends PiercingBoltEntity> type, Level world) {
        super(type, world, ParticleTypes.ENCHANTED_HIT, ParticleTypes.ENCHANTED_HIT);
    }

    public PiercingBoltEntity(EntityType<? extends PiercingBoltEntity> type, Level world, boolean isSpecial) {
        super(type, world, ParticleTypes.ENCHANTED_HIT, ParticleTypes.ENCHANTED_HIT);
        this.isSpecial = isSpecial;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(IS_SPECIAL, false);
        entityData.define(PIERCED_ENEMIES, 0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        this.entityData.set(IS_SPECIAL, pCompound.getBoolean("special"));
        this.entityData.set(PIERCED_ENEMIES, pCompound.getInt("enemies"));
        super.readAdditionalSaveData(pCompound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putBoolean("special", this.entityData.get(IS_SPECIAL));
        pCompound.putInt("enemies", this.entityData.get(PIERCED_ENEMIES));
        super.addAdditionalSaveData(pCompound);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        Entity entity1 = this.getOwner();
        LivingEntity livingentity = entity1 instanceof LivingEntity ? (LivingEntity) entity1 : null;
        if (result.getEntity() == this.getOwner()) return;
        boolean flag = entity.hurt(DamageSource.indirectMobAttack(this, livingentity).setProjectile().bypassArmor(), 4.0F);
        if (flag && this.level instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 2, 0.2D, 0.2D, 0.2D, 0.0D);
            if (livingentity != null) this.doEnchantDamageEffects(livingentity, entity);
            if (this.isSpecial) {
                if (entityData.get(PIERCED_ENEMIES) != 5) {
                    entityData.set(PIERCED_ENEMIES, entityData.get(PIERCED_ENEMIES) + 1);
                }
                else this.remove(RemovalReason.KILLED);
            }
            else {
                this.remove(RemovalReason.KILLED);
            }
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult blockRayTraceResult) {
        if (this.level instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 2, 0.2D, 0.2D, 0.2D, 0.0D);
            this.playSound(SoundEvents.SHULKER_BULLET_HIT, 1.0F, 1.0F);
            this.remove(RemovalReason.KILLED);
        }
    }
}
