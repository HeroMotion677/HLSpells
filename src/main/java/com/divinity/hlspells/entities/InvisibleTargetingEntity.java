package com.divinity.hlspells.entities;

import com.divinity.hlspells.spells.SpellActions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class InvisibleTargetingEntity extends ArrowEntity {
    private Vector3d home;
    // True if lighting, false if evoker fangs
    private boolean isLightning = true;

    public InvisibleTargetingEntity(EntityType<? extends ArrowEntity> type, World world) {
        super(type, world);
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected float getWaterInertia() {
        return 1F;
    }

    @Override
    public void tick() {
        super.tick();
        if (home != null) {
            // If the entity is more than specified blocks away from home position if so then remove it
            float distance = MathHelper.sqrt(distanceToSqr(this.home));
            if ((isLightning && distance >= 50) || (!isLightning && (distance >= 10))) {
                this.remove();
            }
        }

        if (this.level.getGameTime() % 2 == 0) {
            if (isLightning) {
                LightningBoltEntity lightning = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, this.getCommandSenderWorld());
                lightning.moveTo(this.getX(), this.getY(), this.getZ());
                this.level.addFreshEntity(lightning);
            } else if (this.getOwner() instanceof PlayerEntity) {
                SpellActions.createFangsEntity((LivingEntity) this.getOwner(), level, this.xOld, this.zOld, getY(), 0, 0);
            }
        }
    }

    public void setIsLightning(boolean isLightning) {
        this.isLightning = isLightning;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult result) {
        this.level.addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 0.2D, 0.2D, 0.2D);
        this.playSound(SoundEvents.SHULKER_BULLET_HIT, 1.0F, 1.0F);
        this.remove();
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult pResult) {
    }

    public void setHomePosition(Vector3d position3d) {
        this.home = position3d;
    }
}