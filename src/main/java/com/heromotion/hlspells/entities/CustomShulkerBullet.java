package com.heromotion.hlspells.entities;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class CustomShulkerBullet extends ShulkerBulletEntity
{
    private World world;
    private LivingEntity entity;
    private Entity targetEntity;
    private Direction.Axis direction;

    public CustomShulkerBullet(World world, LivingEntity entity, Entity targetEntity, Direction.Axis direction)
    {
        super(world, entity,targetEntity, direction);
        BlockPos blockpos = entity.blockPosition();
        double d0 = (double)blockpos.getX() + 0.5D;
        double d1 = (double)blockpos.getY() + 5D;
        double d2 = (double)blockpos.getZ() + 0.5D;
        this.moveTo(d0, d1, d2, this.yRot, this.xRot);
        this.noPhysics = true;
        this.setDeltaMovement(1,1,1);
        this.world = world;
        this.entity = entity;
        this.targetEntity = targetEntity;
        this.direction = direction;
    }




    @Override
    protected void onHit(RayTraceResult result)
    {
        RayTraceResult.Type raytraceresult$type = result.getType();
        if (raytraceresult$type == RayTraceResult.Type.ENTITY)
        {
            this.onHitEntity((EntityRayTraceResult)result);
        }

        else if (raytraceresult$type == RayTraceResult.Type.BLOCK)
        {
            this.onHitBlock((BlockRayTraceResult)result);
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult result) {}

    @Override
    protected void onHitEntity(EntityRayTraceResult result)
    {
        Entity entity = result.getEntity();
        Entity entity1 = this.getOwner();
        LivingEntity livingentity = entity1 instanceof LivingEntity ? (LivingEntity)entity1 : null;

        boolean flag = entity.hurt(DamageSource.indirectMobAttack(this, livingentity).setProjectile(), 6.0F);
        if (flag)
        {
            this.doEnchantDamageEffects(livingentity, entity);
            this.remove();
        }
    }
}
