package com.divinity.hlspells.util;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.function.Predicate;

public class Util
{
    /**
     * Method that teleports an entity
     *
     * @param world       The departure world
     * @param pos         The departure position
     * @param teleportPos The arrival position
     * @param entity      The entity that is teleported
     */

    public static void teleport(World world, BlockPos pos, BlockPos teleportPos, Entity entity) {
        double teleportXCo = teleportPos.getX();
        double teleportYCo = teleportPos.getY();
        double teleportZCo = teleportPos.getZ();
        {
            entity.teleportTo(teleportXCo, teleportYCo, teleportZCo);
            if (entity instanceof ServerPlayerEntity) {
                ((ServerPlayerEntity) entity).connection.teleport(teleportXCo, teleportYCo, teleportZCo, entity.yRot,
                        entity.xRot, Collections.emptySet());
            }
        }
        SoundEvent teleportSound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.enderman.teleport"));
        teleportParticles(world, pos, 300);
        teleportParticles(world, teleportPos, 300);
        if (teleportSound == null) return;
        world.playSound(null, pos, teleportSound, SoundCategory.NEUTRAL,
                0.6F, 1.0F);
        world.playSound(null, teleportPos, teleportSound, SoundCategory.NEUTRAL,
                0.6F, 1.0F);
    }


    /**
     * Method that handles particle spawning
     *
     * @param world  The world of the teleport
     * @param pos    The position where to spawn particles
     * @param number The number of particles to spawn
     */

    public static void teleportParticles(World world, BlockPos pos, int number)
    {
        for (int l = 0; l < number; l++) {
            double d0 = (pos.getX() + world.random.nextFloat());
            double d1 = (pos.getY() + world.random.nextFloat());
            double d2 = (pos.getZ() + world.random.nextFloat());
            double d3 = (world.random.nextFloat() - 0.2D) * 0.5D;
            double d4 = (world.random.nextFloat() - 0.2D) * 0.5D;
            double d5 = (world.random.nextFloat() - 0.2D) * 0.5D;
            world.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }
    }

    /**
     * Method that gets the RayTraceResult of where an entity looks at
     *
     * @param entity        The entity
     * @param range         The range
     * @param height        The speed
     * @param includeFluids Defines if fluids count
     * @return The RayTraceResult
     */


    public static RayTraceResult lookAt(Entity entity, double range, float height, boolean includeFluids)
    {
        Vector3d vector3d = entity.getEyePosition(height);
        Vector3d vector3d1 = entity.getViewVector(height);
        Vector3d vector3d2 = vector3d.add(vector3d1.x * range, vector3d1.y * range, vector3d1.z * range);
        return entity.level.clip(new RayTraceContext(vector3d, vector3d2, RayTraceContext.BlockMode.OUTLINE, includeFluids ? RayTraceContext.FluidMode.ANY : RayTraceContext.FluidMode.NONE, entity));
    }

    public static Entity rayTrace(World world, PlayerEntity player, double range)
    {
        Vector3d pos = player.getPosition(0f);
        Vector3d cam1 = player.getLookAngle();
        Vector3d cam2 = cam1.add(cam1.x * range, cam1.y * range, cam1.z * range);
        AxisAlignedBB aabb = player.getBoundingBox().expandTowards(cam1.scale(range)).inflate(1.0F, 1.0F, 1.0F);
        RayTraceResult ray = findEntity(world, player, pos, cam2, aabb, null, range);

        if (ray != null)
        {
            if (ray.getType() == RayTraceResult.Type.ENTITY)
            {
                EntityRayTraceResult ray2 = (EntityRayTraceResult) ray;
                return ray2.getEntity() instanceof LivingEntity && !(ray2.getEntity() instanceof PlayerEntity) ? ray2.getEntity() : null;
            }
        }
        return null;
    }

    private static EntityRayTraceResult findEntity(World world, PlayerEntity player, Vector3d pos, Vector3d look, AxisAlignedBB aabb, Predicate<Entity> filter, double range)
    {
        for (Entity entity1 : world.getEntities(player, aabb, filter))
        {
            AxisAlignedBB mob = entity1.getBoundingBox().inflate(1.0F);
            if (intersect(pos, look, mob, range))
            {
                return new EntityRayTraceResult(entity1);
            }
        }
        return null;
    }

    private static boolean intersect(Vector3d pos, Vector3d look, AxisAlignedBB mob, double range)
    {
        Vector3d invDir = new Vector3d(1f / look.x, 1f / look.y, 1f / look.z);

        boolean signDirX = invDir.x < 0;
        boolean signDirY = invDir.y < 0;
        boolean signDirZ = invDir.z < 0;

        Vector3d max = new Vector3d(mob.maxX, mob.maxY, mob.maxZ);
        Vector3d min = new Vector3d(mob.minX, mob.minY, mob.minZ);

        Vector3d bbox = signDirX ? max : min;
        double tmin = (bbox.x - pos.x) * invDir.x;
        bbox = signDirX ? min : max;
        double tmax = (bbox.x - pos.x) * invDir.x;
        bbox = signDirY ? max : min;
        double tymin = (bbox.y - pos.y) * invDir.y;
        bbox = signDirY ? min : max;
        double tymax = (bbox.y - pos.y) * invDir.y;

        if ((tmin > tymax) || (tymin > tmax)) {
            return false;
        }

        if (tymin > tmin) {
            tmin = tymin;
        }

        if (tymax < tmax) {
            tmax = tymax;
        }

        bbox = signDirZ ? max : min;
        double tzmin = (bbox.z - pos.z) * invDir.z;
        bbox = signDirZ ? min : max;
        double tzmax = (bbox.z - pos.z) * invDir.z;

        if ((tmin > tzmax) || (tzmin > tmax)) {
            return false;
        }
        if (tzmin > tmin) {
            tmin = tzmin;
        }
        if (tzmax < tmax) {
            tmax = tzmax;
        }
        if ((tmin < range) && (tmax > 0)) {
            return true;
        }
        return false;
    }
}
