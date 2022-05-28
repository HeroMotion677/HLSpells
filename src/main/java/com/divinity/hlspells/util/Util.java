package com.divinity.hlspells.util;

import com.divinity.hlspells.network.NetworkManager;
import com.divinity.hlspells.network.packets.clientbound.TotemActivatedPacket;
import com.divinity.hlspells.capabilities.playercap.PlayerCapProvider;
import com.google.common.collect.Lists;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.PacketDistributor;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class Util {

    public static final UUID speedUUID = UUID.fromString("05b61a62-ae84-492e-8536-f365b7143296");
    public static final AttributeModifier speedModifier = new AttributeModifier(speedUUID, "Speed", 2, AttributeModifier.Operation.MULTIPLY_TOTAL);

    private Util() {} // No instances of this class should be created

    /**
     * Teleports an entity to a location
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
     * Gets the RayTraceResult of where an entity looks at
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
     * Returns a list of entities (targets) from a relative entity within the specified x, y, and z bounds.
     */
    public static <T extends LivingEntity> List<T> getEntitiesInRange(LivingEntity relativeEntity, Class<T> targets, double xBound, double yBound, double zBound) {
        return relativeEntity.level.getEntitiesOfClass(targets,
                        new AABB(relativeEntity.getX() - xBound, relativeEntity.getY() - yBound, relativeEntity.getZ() - zBound,
                                relativeEntity.getX() + xBound, relativeEntity.getY() + yBound, relativeEntity.getZ() + zBound))
                .stream().sorted(getEntityComparator(relativeEntity)).collect(Collectors.toList());
    }
    // For totem
    public static void randomTeleport(LivingEntity entity) {
        double d0 = entity.getX();
        double d1 = entity.getY();
        double d2 = entity.getZ();
        for (int i = 0; i < 16; ++i) {
            double d3 = entity.getX() + (entity.getRandom().nextDouble() - 0.5D) * 16.0D;
            double d4 = Mth.clamp(entity.getY() + (entity.getRandom().nextInt(16) - 8), 0.0D, (entity.level.getHeight() - 1));
            double d5 = entity.getZ() + (entity.getRandom().nextDouble() - 0.5D) * 16.0D;
            if (entity.isPassenger()) {
                entity.stopRiding();
            }
            if (entity.randomTeleport(d3, d4, d5, true)) {
                SoundEvent soundevent = SoundEvents.CHORUS_FRUIT_TELEPORT;
                entity.level.playSound(null, d0, d1, d2, soundevent, SoundSource.PLAYERS, 1.0F, 1.0F);
                entity.playSound(soundevent, 1.0F, 1.0F);
                break;
            }
        }
    }
    /**
     * Method to hide the client side call to show totem activation and/or particles
     */
    public static void displayActivation(Player player, Item item) {
        NetworkManager.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new TotemActivatedPacket(player.getUUID(), new ItemStack(item)));
    }

    public static void clearEffects(Player playerEntity) {
        AttributeInstance speedAttribute = playerEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speedAttribute != null && speedAttribute.getModifier(speedUUID) != null) {
            speedAttribute.removeModifier(speedModifier);
        }

        /* Check if the player has any one of these effects while they're not holding down a spell item, should prevent
        a bug where if you quickly tap the spell item and switch to a different one the infinite effect is applied to the player
        MIGHT have repercussions on other mods that have these effects and are not visible. */

        Lists.newArrayList(MobEffects.GLOWING, MobEffects.LEVITATION, MobEffects.SLOW_FALLING)
                .stream().map(playerEntity::getEffect)
                .filter(p -> p != null && !p.isVisible() && p.getAmplifier() >= 5)
                .forEach(p -> playerEntity.removeEffect(p.getEffect()));

        // Reapplies the old effect to the player (if applicable)
        playerEntity.getCapability(PlayerCapProvider.PLAYER_CAP).ifPresent(cap -> {
            MobEffect effect = cap.getEffect();
            if (effect != null) {
                playerEntity.addEffect(new MobEffectInstance(effect, cap.getEffectDuration(), cap.getEffectAmplifier()));
                cap.resetEffect();
            }
            cap.setSpellTimer(0);
            cap.setDurabilityTickCounter(0);
            cap.setSpellXpTickCounter(0);
        });
    }

    public static void doParticles(Player player) {
        doBookParticles(player.level, new BlockPos(player.getX(), (player.getY() + 1), player.getZ()), 100);
        player.level.playSound(null, new BlockPos(player.getX(), player.getY(), player.getZ()), SoundEvents.ENCHANTMENT_TABLE_USE,
                SoundSource.AMBIENT, 0.6f, 1.0f);
    }

    public static void vanillaTotemBehavior(Player entity, ItemStack heldItem, Item animationItem) {
        heldItem.shrink(1);
        entity.setHealth(1.0F);
        entity.removeAllEffects();
        entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
        entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
        Util.displayActivation(entity, animationItem);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
        entity.playSound(SoundEvents.TOTEM_USE, 1.0F, 1.0F);
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

        Vec3 bBox = signDirX ? max : min;
        double tMin = (bBox.x - pos.x) * invDir.x;
        bBox = signDirX ? min : max;
        double tMax = (bBox.x - pos.x) * invDir.x;
        bBox = signDirY ? max : min;
        double tYMin = (bBox.y - pos.y) * invDir.y;
        bBox = signDirY ? min : max;
        double tYMax = (bBox.y - pos.y) * invDir.y;

        if ((tMin > tYMax) || (tYMin > tMax)) {
            return false;
        }

        if (tYMin > tMin) {
            tMin = tYMin;
        }

        if (tYMax < tMax) {
            tMax = tYMax;
        }

        bBox = signDirZ ? max : min;
        double tZMin = (bBox.z - pos.z) * invDir.z;
        bBox = signDirZ ? min : max;
        double tZMax = (bBox.z - pos.z) * invDir.z;

        if ((tMin > tZMax) || (tZMin > tMax)) {
            return false;
        }
        if (tZMin > tMin) {
            tMin = tZMin;
        }
        if (tZMax < tMax) {
            tMax = tZMax;
        }
        return (tMin < range) && (tMax > 0);
    }

    /**
     * Returns a comparator which compares entities' distances to a given LivingEntity
     */
    private static Comparator<Entity> getEntityComparator(LivingEntity other) {
        return new Object() {
            Comparator<Entity> compareDistOf(double x, double y, double z) {
                return Comparator.comparing(entity -> entity.distanceToSqr(x, y, z));
            }
        }.compareDistOf(other.getX(), other.getY(), other.getZ());
    }

    @SuppressWarnings("all")
    private static void doBookParticles(Level world, BlockPos pos, int number) {
        for (int l = 0; l < number; l++) {
            double d0 = (pos.getX() + world.random.nextFloat());
            double d1 = (pos.getY() + world.random.nextFloat());
            double d2 = (pos.getZ() + world.random.nextFloat());
            double d3 = (world.random.nextFloat() - 0.2D) * 0.5D;
            double d4 = (world.random.nextFloat() - 0.2D) * 0.5D;
            double d5 = (world.random.nextFloat() - 0.2D) * 0.5D;
            world.addParticle(ParticleTypes.ENCHANT, d0, d1, d2, d3, d4, d5);
        }
    }
}
