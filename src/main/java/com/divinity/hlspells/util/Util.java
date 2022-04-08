package com.divinity.hlspells.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class Util {

    /**
     * Teleports an entity to a location
     * @param world
     * @param pos
     * @param teleportPos
     * @param entity
     */

    public static void teleportToLocation(World world, BlockPos pos, BlockPos teleportPos, Entity entity) {
        entity.teleportTo(teleportPos.getX(), teleportPos.getY(), teleportPos.getZ());
        doTeleportParticles(world, pos, 150);
        doTeleportParticles(world, teleportPos, 150);
        world.playSound(null, pos, SoundEvents.ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 0.6F, 1.0F);
    }

    /**
     * Spawns teleportToLocation particles at the given location
     */
    public static void doTeleportParticles(World world, BlockPos pos, int number) {
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
    public static RayTraceResult lookAt(Entity entity, double range, float height, boolean includeFluids) {
        Vector3d vector3d = entity.getEyePosition(height);
        Vector3d vector3d1 = entity.getViewVector(height);
        Vector3d vector3d2 = vector3d.add(vector3d1.x * range, vector3d1.y * range, vector3d1.z * range);
        return entity.level.clip(new RayTraceContext(vector3d, vector3d2, RayTraceContext.BlockMode.OUTLINE, includeFluids ? RayTraceContext.FluidMode.ANY : RayTraceContext.FluidMode.NONE, entity));
    }

    /**
     * Raytrace the player look vector to return what entity is looked at. Returns null if not found
     */
    public static Entity rayTrace(World world, PlayerEntity player, double range) {
        Vector3d pos = player.position();
        Vector3d cam1 = player.getLookAngle();
        Vector3d cam2 = cam1.add(cam1.x * range, cam1.y * range, cam1.z * range);
        AxisAlignedBB aabb = player.getBoundingBox().expandTowards(cam1.scale(range)).inflate(1.0F, 1.0F, 1.0F);
        EntityRayTraceResult ray = findEntity(world, player, pos, cam2, aabb, null, range);

        if (ray != null && ray.getType() == RayTraceResult.Type.ENTITY) {
            return ray.getEntity() instanceof LivingEntity && !(ray.getEntity() instanceof PlayerEntity) ? ray.getEntity() : null;
        }
        return null;
    }

    /**
     * Raytrace the player look vector to return EntityRayTraceResult
     */
    private static EntityRayTraceResult findEntity(World world, PlayerEntity player, Vector3d pos, Vector3d look, AxisAlignedBB aabb, Predicate<Entity> filter, double range) {
        for (Entity entity1 : world.getEntities(player, aabb, filter)) {
            AxisAlignedBB mob = entity1.getBoundingBox().inflate(1.0F);
            if (intersect(pos, look, mob, range)) {
                return new EntityRayTraceResult(entity1);
            }
        }
        return null;
    }

    private static boolean intersect(Vector3d pos, Vector3d look, AxisAlignedBB mob, double range) {
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
        return (tmin < range) && (tmax > 0);
    }
}
