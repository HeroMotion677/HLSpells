package com.divinity.hlspells.entities;

import com.divinity.hlspells.spell.spells.FangsSpell;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class InvisibleTargetingEntity extends BaseBoltEntity {

    private Vec3 home;
    // True if lighting, false if evoker fangs
    private boolean isLightning = true;

    public InvisibleTargetingEntity(EntityType<? extends InvisibleTargetingEntity> type, Level world) {
        super(type, world, true);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.home != null) {
            // If the entity is more than specified blocks away from home position if so then remove it
            float distance = Mth.sqrt((float) distanceToSqr(this.home));
            if ((isLightning && distance >= 25) || (!isLightning && (distance >= 10))) {
                this.remove(RemovalReason.KILLED);
            }
        }
        if (this.level.getGameTime() % 2 == 0) {
            if (this.isLightning) {
                LightningBolt lightning = new LightningBolt(EntityType.LIGHTNING_BOLT, this.level);
                lightning.moveTo(this.getX(), this.getY(), this.getZ());
                this.level.addFreshEntity(lightning);
            }
            else if (this.getOwner() instanceof Player) {
                FangsSpell.createFangsEntity((LivingEntity) this.getOwner(), this.level, this.xOld, this.zOld, getY(), 0, 0);
            }
        }
    }

    @Override public boolean isInvulnerable() {
        return true;
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        this.level.addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 0.2D, 0.2D, 0.2D);
        this.playSound(SoundEvents.SHULKER_BULLET_HIT, 1.0F, 1.0F);
        this.remove(RemovalReason.KILLED);
    }

    @Override protected void onHitEntity(@NotNull EntityHitResult pResult) {}

    public void setIsLightning(boolean isLightning) {
        this.isLightning = isLightning;
    }

    public void setHomePosition(Vec3 position3d) {
        this.home = position3d;
    }
}