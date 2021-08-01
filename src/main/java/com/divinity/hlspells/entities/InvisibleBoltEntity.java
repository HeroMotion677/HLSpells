package com.divinity.hlspells.entities;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class InvisibleBoltEntity extends ArrowEntity
{
    private Vector3d position3d;
    private boolean isLightning = true;

    public InvisibleBoltEntity(EntityType<? extends ArrowEntity> type, World world)
    {
        super(type, world);
    }

    @Override
    public boolean fireImmune()
    {
        return true;
    }

    @Override
    public boolean isInvulnerable()
    {
        return true;
    }

    @Override
    public boolean isNoGravity()
    {
        return true;
    }

    @Override
    protected void onHit(RayTraceResult result)
    {
        RayTraceResult.Type raytraceresult$type = result.getType();

        if (raytraceresult$type == RayTraceResult.Type.ENTITY)
        {
            this.onHitEntity((EntityRayTraceResult) result);
        }

        else if (raytraceresult$type == RayTraceResult.Type.BLOCK)
        {
            this.onHitBlock((BlockRayTraceResult) result);
        }
    }

    @Override
    public void tick()
    {
        super.tick();

        if (isLightning && position3d != null && distanceToSqr(this.position3d) >= 2500)
        {
            this.remove();
        }

        else if (!isLightning && position3d != null && distanceToSqr(this.position3d) >= 1000)
        {
            this.remove();
        }

        if (isLightning && this.level.getGameTime() % 2 == 0)
        {
            LightningBoltEntity lightning = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, this.getCommandSenderWorld())
            {
                @Override
                public void tick()
                {
                    if (this.life == 2)
                    {
                        Difficulty difficulty = this.level.getDifficulty();
                        if (difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD)
                        {
                            this.spawnFire(4);
                        }

                        this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, 100.0F, 0.6F + this.random.nextFloat() * 0.2F);
                        this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundCategory.WEATHER, 2.0F, 0.3F + this.random.nextFloat() * 0.2F);
                    }
                    --this.life;
                    if (this.life < 0)
                    {
                        if (this.flashes == 0)
                        {
                            this.remove();
                        }

                        else if (this.life < -this.random.nextInt(10))
                        {
                            --this.flashes;
                            this.life = 1;
                            this.seed = this.random.nextLong();
                            this.spawnFire(0);
                        }
                    }

                    if (this.life >= 0)
                    {
                        if (!(this.level instanceof ServerWorld))
                        {
                            this.level.setSkyFlashTime(2);
                        }

                        else if (!this.visualOnly)
                        {
                            List<Entity> list = this.level.getEntities(this, new AxisAlignedBB(this.getX() - 3.0D, this.getY() - 3.0D, this.getZ() - 3.0D, this.getX() + 3.0D, this.getY() + 6.0D + 3.0D, this.getZ() + 3.0D), Entity::isAlive);

                            for(Entity entity : list)
                            {
                                if (!net.minecraftforge.event.ForgeEventFactory.onEntityStruckByLightning(entity, this)  && !(entity instanceof PlayerEntity))
                                    entity.thunderHit((ServerWorld)this.level, this);
                            }

                            if (this.cause != null)
                            {
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
        }

        else if (!isLightning && this.level.getGameTime() % 2 == 0)
        {
            EvokerFangsEntity entity = new EvokerFangsEntity(EntityType.EVOKER_FANGS, this.level);
            if (this.getOwner() instanceof PlayerEntity)
            {
                entity.setPosAndOldPos(this.getX(), this.getY(), this.getZ());
                entity.setPos(this.getX(), this.getY(), this.getZ());
                entity.setOwner((LivingEntity) this.getOwner());
                if (entity.getOwner() != null)
                {
                    while (!(entity.getOwner().level.getBlockState(entity.blockPosition()).is(Blocks.AIR)))
                    {
                        entity.moveTo(entity.xOld, entity.blockPosition().getY() + 1, entity.zOld);
                    }

                    while (entity.getOwner().level.getBlockState(entity.blockPosition().below()).is(Blocks.AIR))
                    {
                        entity.moveTo(entity.xOld, entity.blockPosition().getY() - 1, entity.zOld);
                    }
                }
                this.level.addFreshEntity(entity);
            }
        }
    }

    public void setIsLightning (boolean isLightning)
    {
        this.isLightning = isLightning;
    }

    @Override
    public IPacket<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult result) {}

    @Override
    protected void onHitBlock(BlockRayTraceResult result)
    {
        this.level.addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 0.2D, 0.2D, 0.2D);
        this.playSound(SoundEvents.SHULKER_BULLET_HIT, 1.0F, 1.0F);
        this.remove();
    }

    public void setHomePosition (Vector3d position3d)
    {
        this.position3d = position3d;
    }
}

