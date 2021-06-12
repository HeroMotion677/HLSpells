package com.heromotion.hlspells.misc;

import com.heromotion.hlspells.init.SpellBookInit;
import com.heromotion.hlspells.util.SpellUtils;
import com.heromotion.hlspells.util.Util;

import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.*;
import java.util.stream.Collectors;

public class Spells {

    public static void doSpell(World world, PlayerEntity playerEntity, ItemStack itemStack) {
        boolean flag = false;
        if (SpellUtils.getSpellBook(itemStack) == SpellBookInit.SLOW_FALLING.get()) {
            slowFalling(world, playerEntity);
            flag = true;
        } else if (SpellUtils.getSpellBook(itemStack) == SpellBookInit.BLAST_PROTECTION.get()) {
            blastProtection(world, playerEntity);
            flag = true;
        } else if (SpellUtils.getSpellBook(itemStack) == SpellBookInit.SOUL_SYPHON.get()) {
            soulSyphon(world, playerEntity);
            flag = true;
        }

        if (flag) {
            double x = playerEntity.getX();
            double y = playerEntity.getY();
            double z = playerEntity.getZ();
            bookParticles(world, new BlockPos(x, (y + 1), z), 100);
            world.playSound(null, new BlockPos(x, y, z), SoundEvents.ENCHANTMENT_TABLE_USE,
                    SoundCategory.AMBIENT, 0.6f, 1.0f);
        }
    }

    public static void slowFalling(World world, PlayerEntity playerEntity) {
        boolean flag = playerEntity.isSwimming();
        boolean flag1 = playerEntity.isInWater();
        boolean flag2 = playerEntity.isInLava();
        boolean flag3 = playerEntity.getDeltaMovement().y < -0.2;
        if ((!flag) && (!flag1) && (!flag2) && (flag3)) {
            playerEntity.setDeltaMovement(playerEntity.getDeltaMovement().x,
                    -0.1, playerEntity.getDeltaMovement().z);
            playerEntity.fallDistance = 0;
            for (int i = 0; i < 9; i++) {
                world.addParticle(ParticleTypes.CLOUD, playerEntity.getX(), playerEntity.getY(),
                        playerEntity.getZ(), 0, -0.1, 0);
            }
        }
    }

    public static void blastProtection(World world, PlayerEntity playerEntity) {
        double x = playerEntity.getX();
        double y = playerEntity.getY();
        double z = playerEntity.getZ();
        {
            List<Entity> _entfound = world.getEntitiesOfClass(Entity.class,
                    new AxisAlignedBB(x - 6, y - 6, z - 6,
                            x + 6, y + 6, z + 6),
                    null).stream().sorted(new Object() {
                Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                    return Comparator.comparing(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                }
            }.compareDistOf(x, y, z)).collect(Collectors.toList());
            for (Entity entity : _entfound) {
                if ((entity instanceof LivingEntity) && (entity != playerEntity)) {
                    ((LivingEntity) entity).knockback(5F * 0.5F, MathHelper.sin(playerEntity.yRot * ((float) Math.PI / 180F)), -MathHelper.cos(playerEntity.yRot * ((float) Math.PI / 180F)));
                    playerEntity.setDeltaMovement(playerEntity.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                }
            }
        }
        world.playSound(null, new BlockPos(x, y, z), SoundEvents.GENERIC_EXPLODE,
                SoundCategory.WEATHER, 0.6f, 1.0f);
        world.addParticle(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 1.0D, 0.0D, 0.0D);
    }

    public static void soulSyphon(World world, PlayerEntity playerEntity) {
        RayTraceResult rayTraceResult = Util.lookAt(playerEntity, 100D, 1F, false);
        Vector3d location = rayTraceResult.getLocation();
        int stepX = 0;
        int stepY = 1;
        int stepZ = 0;
        if ((rayTraceResult instanceof BlockRayTraceResult)
                && (!(world.getBlockState(new BlockPos(location).above()).getMaterial() == Material.AIR))) {
            Direction rayTraceDirection = ((BlockRayTraceResult) rayTraceResult).getDirection();
            stepX = rayTraceDirection.getStepX();
            stepY = rayTraceDirection.getStepY();
            stepZ = rayTraceDirection.getStepZ();
        }
        double tx = location.x() + stepX;
        double ty = location.y() + stepY;
        double tz = location.z() + stepZ;
        BlockPos teleportPos = new BlockPos(tx, ty, tz);
        playerEntity.fallDistance = 0;
        Util.teleport(world, playerEntity.blockPosition(), teleportPos, playerEntity);
    }

    public static void bookParticles(World world, BlockPos pos, int number) {
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
