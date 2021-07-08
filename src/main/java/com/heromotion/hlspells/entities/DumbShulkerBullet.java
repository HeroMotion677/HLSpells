package com.heromotion.hlspells.entities;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class DumbShulkerBullet extends ShulkerBulletEntity
{
    private World world;
    private Entity entity;
    private Vector3d pos;

    public DumbShulkerBullet(EntityType<? extends ShulkerBulletEntity> type, World world, Entity entity)
    {
        super(type, world);
        this.entity = entity;
        this.pos = entity.position();
        this.setNoGravity(true);
        this.moveTo(entity.getX(), entity.getY() + 1, entity.getZ());
    }

    @Override
    public void selectNextMoveDirection(@Nullable Direction.Axis axis) {}

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
    public void tick()
    {
        super.tick();
        if (distanceTo(entity) >= 30)
        {
            this.remove();
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

        if (result.getEntity() == this.entity)
        {
            return;
        }

        boolean flag = entity.hurt(DamageSource.indirectMobAttack(this, livingentity).setProjectile(), 6.0F);
        if (flag)
        {
            this.doEnchantDamageEffects(livingentity, entity);
            this.remove();
        }
    }
}
