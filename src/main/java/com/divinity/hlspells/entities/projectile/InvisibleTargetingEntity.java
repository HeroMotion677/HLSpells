package com.divinity.hlspells.entities.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class InvisibleTargetingEntity extends BaseBoltEntity {

    public InvisibleTargetingEntity(EntityType<? extends InvisibleTargetingEntity> type, Level world) {
        super(type, world);
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        this.level.addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 0.2D, 0.2D, 0.2D);
        this.playSound(SoundEvents.SHULKER_BULLET_HIT, 1.0F, 1.0F);
        this.remove(RemovalReason.KILLED);
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult pResult) {}
}