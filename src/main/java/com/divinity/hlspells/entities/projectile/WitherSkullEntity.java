package com.divinity.hlspells.entities.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class WitherSkullEntity extends WitherSkull {
    private Vec3 initialPosition;

    public WitherSkullEntity(EntityType<? extends WitherSkull> p_37598_, Level p_37599_) {
        super(p_37598_, p_37599_);
    }

    @Override
    public void tick() {
        super.tick();
       // Remove if it's more than 40 block away from entity pos
        if(this.tickCount == 50){
            if(this.level instanceof ServerLevel level){
                level.sendParticles(ParticleTypes.SMOKE, this.getX() - this.random.nextInt(2),
                        this.getY(), this.getZ() - this.random.nextFloat(), 70, 0.2D, 0.2D, 0.2D, 0.0D);
            }
        }
        if(this.tickCount >= 52){
            this.discard();
        }
    }
    @Override
    public void checkDespawn() {
        super.checkDespawn();
        if (this.level.getDifficulty() == Difficulty.PEACEFUL) this.discard();
    }

    @Override @NotNull public Packet<?> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }

    @Override public boolean isNoGravity() { return true; }

    @Override public boolean isPickable() { return false; }

    @Override protected boolean canHitEntity(@NotNull Entity entity) { return super.canHitEntity(entity) && !entity.noPhysics; }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public float getBlockExplosionResistance(Explosion pExplosion, BlockGetter pLevel, BlockPos pPos, BlockState pBlockState, FluidState pFluidState, float pExplosionPower) {
        return pBlockState.canEntityDestroy(pLevel, pPos, this) ? Math.min(0.8F, pExplosionPower) : pExplosionPower;
    }
    public Vec3 getInitialPosition() {
        return this.initialPosition;
    }

    public void setInitialPosition(Vec3 position) {
        this.initialPosition = position;
    }
}
