package com.divinity.hlspells.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class Util {

    /**
     * Teleports an entity to a location
     * @param world
     * @param pos
     * @param teleportPos
     * @param entity
     */

    public static void teleportToLocation(Level world, BlockPos pos, BlockPos teleportPos, Entity entity) {
        entity.teleportTo(teleportPos.getX(), teleportPos.getY(), teleportPos.getZ());
        doTeleportParticles(world, pos, 150);
        doTeleportParticles(world, teleportPos, 150);
        world.playSound(null, pos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 0.6F, 1.0F);
    }

    /**
     * Spawns teleportToLocation particles at the given location
     */
    public static void doTeleportParticles(Level world, BlockPos pos, int number) {
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
    public static HitResult lookAt(Entity entity, double range, float height, boolean includeFluids) {
        Vec3 vector3d = entity.getEyePosition(height);
        Vec3 vector3d1 = entity.getViewVector(height);
        Vec3 vector3d2 = vector3d.add(vector3d1.x * range, vector3d1.y * range, vector3d1.z * range);
        return entity.level.clip(new ClipContext(vector3d, vector3d2, ClipContext.Block.OUTLINE, includeFluids ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, entity));
    }

    /**
     * Raytrace the player look vector to return what entity is looked at. Returns null if not found
     */
    public static Entity rayTrace(Level world, Player player, double range) {
        Vec3 pos = player.position();
        Vec3 cam1 = player.getLookAngle();
        Vec3 cam2 = cam1.add(cam1.x * range, cam1.y * range, cam1.z * range);
        AABB aabb = player.getBoundingBox().expandTowards(cam1.scale(range)).inflate(1.0F, 1.0F, 1.0F);
        EntityHitResult ray = findEntity(world, player, pos, cam2, aabb, range);

        if (ray != null && ray.getType() == HitResult.Type.ENTITY) {
            return ray.getEntity() instanceof LivingEntity && !(ray.getEntity() instanceof Player) ? ray.getEntity() : null;
        }
        return null;
    }

    /**
     * Raytrace the player look vector to return EntityRayTraceResult
     */
    private static EntityHitResult findEntity(Level world, Player player, Vec3 pos, Vec3 look, AABB aabb, double range) {
        for (Entity entity1 : world.getEntities(player, aabb)) {
            AABB mob = entity1.getBoundingBox().inflate(1.0F);
            if (intersect(pos, look, mob, range)) {
                return new EntityHitResult(entity1);
            }
        }
        return null;
    }

    private static boolean intersect(Vec3 pos, Vec3 look, AABB mob, double range) {
        Vec3 invDir = new Vec3(1f / look.x, 1f / look.y, 1f / look.z);

        boolean signDirX = invDir.x < 0;
        boolean signDirY = invDir.y < 0;
        boolean signDirZ = invDir.z < 0;

        Vec3 max = new Vec3(mob.maxX, mob.maxY, mob.maxZ);
        Vec3 min = new Vec3(mob.minX, mob.minY, mob.minZ);

        Vec3 bbox = signDirX ? max : min;
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
