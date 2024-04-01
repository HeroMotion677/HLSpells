package com.divinity.hlspells.entities.projectile;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.entity.EnchantmentTableBlockEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import net.minecraft.world.entity.Entity.RemovalReason;

public class KnockbackBoltEntity extends ShulkerBullet {

    public KnockbackBoltEntity(EntityType<? extends ShulkerBullet> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void selectNextMoveDirection(@Nullable Direction.Axis axis) {}

    @Override
    public void tick() {
        super.tick();
        if (this.getOwner() != null && this.distanceTo(this.getOwner()) >= 100) {
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    @NotNull
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        Entity entity1 = this.getOwner();
        LivingEntity livingentity = entity1 instanceof LivingEntity ? (LivingEntity) entity1 : null;

        if (result.getEntity() == this.getOwner()) {
            return;
        }

        boolean flag = entity.hurt(DamageSource.indirectMobAttack(this, livingentity).setProjectile(), 0.0F);
        if (flag) {
            entity.setDeltaMovement(this.getLookAngle().reverse().multiply(5.0D, 0, 5.0D));
            this.remove(RemovalReason.KILLED);
        }
    }
}
