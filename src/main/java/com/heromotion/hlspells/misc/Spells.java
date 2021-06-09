package com.heromotion.hlspells.misc;

import com.heromotion.hlspells.init.SpellBookInit;
import com.heromotion.hlspells.util.SpellUtils;

import com.heromotion.hlspells.util.Util;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.*;
import java.util.stream.Collectors;

public class Spells {

    public static void doSpell(World world, PlayerEntity playerEntity, ItemStack itemStack) {
        if (SpellUtils.getSpellBook(itemStack) == SpellBookInit.SLOW_FALLING.get()) {
            slowFalling(playerEntity);
        } else if (SpellUtils.getSpellBook(itemStack) == SpellBookInit.BLAST_PROTECTION.get()) {
            blastProtection(world, playerEntity);
        } else if (SpellUtils.getSpellBook(itemStack) == SpellBookInit.SOUL_SYPHON.get()) {
            soulSyphon(world, playerEntity);
        }
    }

    public static void slowFalling(PlayerEntity playerEntity) {
        boolean flag = playerEntity.isCreative();
        boolean flag1 = playerEntity.isSpectator();
        boolean flag2 = playerEntity.isSwimming();
        boolean flag3 = playerEntity.getDeltaMovement().y < -0.2;
        if ((!flag) && (!flag1) && (!flag2) && (flag3)) {
            playerEntity.setDeltaMovement(playerEntity.getDeltaMovement().x,
                    -0.1, playerEntity.getDeltaMovement().z);
            playerEntity.fallDistance = 0;
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
}
