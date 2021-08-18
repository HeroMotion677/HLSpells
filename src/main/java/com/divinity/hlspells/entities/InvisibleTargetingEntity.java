package com.divinity.hlspells.entities;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

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
            if ((isLightning && (distanceToSqr(this.home) >= 2500)) || (!isLightning && (distanceToSqr(this.home) >= 1000))) {
                this.remove();
            }
        }

        if (isLightning && this.level.getGameTime() % 2 == 0) {
            LightningBoltEntity lightning = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, this.getCommandSenderWorld()) {
                @Override
                public void tick() {
                    if (this.life == 2) {
                        Difficulty difficulty = this.level.getDifficulty();
                        if (difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD) {
                            this.spawnFire(4);
                        }

                        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, 100.0F, 0.6F + this.random.nextFloat() * 0.2F);
                        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundCategory.WEATHER, 2.0F, 0.3F + this.random.nextFloat() * 0.2F);
                    }
                    --this.life;
                    if (this.life < 0) {
                        if (this.flashes == 0) {
                            this.remove();
                        } else if (this.life < -this.random.nextInt(10)) {
                            --this.flashes;
                            this.life = 1;
                            this.seed = this.random.nextLong();
                            this.spawnFire(0);
                        }
                    }

                    if (this.life >= 0) {
                        if (!(this.level instanceof ServerWorld)) {
                            this.level.setSkyFlashTime(2);
                        } else if (!this.visualOnly) {
                            List<Entity> list = this.level.getEntities(this, new AxisAlignedBB(this.getX() - 3.0D, this.getY() - 3.0D, this.getZ() - 3.0D, this.getX() + 3.0D, this.getY() + 6.0D + 3.0D, this.getZ() + 3.0D), Entity::isAlive);

                            for (Entity entity : list) {
                                if (!net.minecraftforge.event.ForgeEventFactory.onEntityStruckByLightning(entity, this) && !(entity instanceof PlayerEntity))
                                    entity.thunderHit((ServerWorld) this.level, this);
                            }

                            if (this.cause != null) {
                                CriteriaTriggers.CHANNELED_LIGHTNING.trigger(this.cause, list);
                            }
                        }
                    }
                }

                @Override
                public IPacket<?> getAddEntityPacket() {
                    return new SSpawnObjectPacket(this);
                }
            };
            lightning.moveTo(this.getX(), this.getY(), this.getZ());
            this.level.addFreshEntity(lightning);
        } else if (!isLightning && this.level.getGameTime() % 2 == 0) {
            EvokerFangsEntity entity = new EvokerFangsEntity(EntityType.EVOKER_FANGS, this.level);
            if (this.getOwner() instanceof PlayerEntity) {
                entity.moveTo(this.getX(), this.getY(), this.getZ());
                entity.setOwner((LivingEntity) this.getOwner());
                if (entity.getOwner() != null) {
                    while (!(entity.getOwner().level.getBlockState(entity.blockPosition()).is(Blocks.AIR))) {
                        entity.moveTo(entity.xOld, entity.blockPosition().getY() + 1D, entity.zOld);
                    }
                    while (entity.getOwner().level.getBlockState(entity.blockPosition().below()).is(Blocks.AIR)) {
                        entity.moveTo(entity.xOld, entity.blockPosition().getY() - 1D, entity.zOld);
                    }
                }
                this.level.addFreshEntity(entity);
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

    public void setHomePosition(Vector3d position3d) {
        this.home = position3d;
    }
}