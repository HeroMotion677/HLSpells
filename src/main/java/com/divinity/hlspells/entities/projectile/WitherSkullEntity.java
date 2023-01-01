package com.divinity.hlspells.entities.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class WitherSkullEntity extends WitherSkull {

    public WitherSkullEntity(EntityType<? extends WitherSkull> p_37598_, Level p_37599_) {
        super(p_37598_, p_37599_);
    }

    @Override
    public void tick() {
        super.tick();
        // Remove if it's more than 40 block away from entity pos
        Entity owner = this.getOwner();
        if (owner != null && Math.sqrt(this.distanceToSqr(owner.position())) > 40) this.discard();
        else if (owner == null) this.discard();
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
}
