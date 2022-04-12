package com.divinity.hlspells.entities;

import com.divinity.hlspells.spells.SpellActions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.network.protocol.Packet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class InvisibleTargetingEntity extends Arrow {
    private Vec3 home;
    // True if lighting, false if evoker fangs
    private boolean isLightning = true;

    public InvisibleTargetingEntity(EntityType<? extends Arrow> type, Level world) {
        super(type, world);
        this.setNoGravity(true);
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
            float distance = Mth.sqrt((float) distanceToSqr(this.home));
            if ((isLightning && distance >= 50) || (!isLightning && (distance >= 10))) {
                this.remove(RemovalReason.KILLED);
            }
        }

        if (this.level.getGameTime() % 2 == 0) {
            if (isLightning) {
                LightningBolt lightning = new LightningBolt(EntityType.LIGHTNING_BOLT, this.getCommandSenderWorld());
                lightning.moveTo(this.getX(), this.getY(), this.getZ());
                this.level.addFreshEntity(lightning);
            } else if (this.getOwner() instanceof Player) {
                SpellActions.createFangsEntity((LivingEntity) this.getOwner(), level, this.xOld, this.zOld, getY(), 0, 0);
            }
        }
    }

    public void setIsLightning(boolean isLightning) {
        this.isLightning = isLightning;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        this.level.addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 0.2D, 0.2D, 0.2D);
        this.playSound(SoundEvents.SHULKER_BULLET_HIT, 1.0F, 1.0F);
        this.remove(RemovalReason.KILLED);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
    }

    public void setHomePosition(Vec3 position3d) {
        this.home = position3d;
    }
}