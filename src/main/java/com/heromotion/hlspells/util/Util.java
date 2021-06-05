package com.heromotion.hlspells.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;

public class Util {

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
    public static void teleportParticles(World world, BlockPos pos, int number) {
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
}
