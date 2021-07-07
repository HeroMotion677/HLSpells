package com.heromotion.hlspells.misc;

import com.heromotion.hlspells.entities.CustomShulkerBullet;
import com.heromotion.hlspells.init.SpellBookInit;
import com.heromotion.hlspells.util.SpellUtils;
import com.heromotion.hlspells.util.Util;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.lwjgl.system.CallbackI;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CastSpells
{
    public static void doCastSpell(PlayerEntity playerEntity, World world, ItemStack itemStack)
    {
        if (SpellUtils.getSpellBook(itemStack) == SpellBookInit.BLAST_PROTECTION.get())
        {
            doBlastSpell(world, playerEntity);
        }

        else if (SpellUtils.getSpellBook(itemStack) == SpellBookInit.SOUL_SYPHON.get())
        {
            doSoulSyphon(world, playerEntity);
        }

        else if (SpellUtils.getSpellBook(itemStack) == SpellBookInit.BOLT.get())
        {
            doBoltSpell(playerEntity);
        }
    }

    public static void doBlastSpell(World world, PlayerEntity playerEntity)
    {
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
            for (Entity entity : _entfound)
            {
                if ((entity instanceof LivingEntity) && (entity != playerEntity))
                {
                    LivingEntity livingEntity = (LivingEntity) entity;
                    livingEntity.knockback(5F * 0.5F, MathHelper.sin(playerEntity.yRot * ((float) Math.PI / 180F)), -MathHelper.cos(playerEntity.yRot * ((float) Math.PI / 180F)));
                    livingEntity.hurt(DamageSource.explosion((livingEntity)), 4.0F);
                    playerEntity.setDeltaMovement(playerEntity.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                }
            }
        }
        world.playSound(null, new BlockPos(x, y, z), SoundEvents.GENERIC_EXPLODE,
                SoundCategory.WEATHER, 0.6f, 1.0f);
        world.addParticle(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 1.0D, 0.0D, 0.0D);
    }

    public static void doBoltSpell(PlayerEntity playerEntity)
    {
        if (Util.rayTrace(playerEntity.getCommandSenderWorld(), playerEntity, 25D) != null)
        {
            CustomShulkerBullet bullet = new CustomShulkerBullet(playerEntity.getCommandSenderWorld(), playerEntity, Util.rayTrace(playerEntity.getCommandSenderWorld(), playerEntity, 25D), playerEntity.getDirection().getAxis());

            playerEntity.getCommandSenderWorld().addFreshEntity(bullet);
        }
    }

    public static void doSoulSyphon(World world, PlayerEntity playerEntity)
    {
        RayTraceResult rayTraceResult = Util.lookAt(playerEntity, 20D, 1F, false);
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

    public static void doParticles(PlayerEntity playerEntity)
    {
        doBookParticles(playerEntity.getCommandSenderWorld(), new BlockPos(playerEntity.getX(), (playerEntity.getY() + 1), playerEntity.getZ()), 100);
        playerEntity.getCommandSenderWorld().playSound(null, new BlockPos(playerEntity.getX(), playerEntity.getY(), playerEntity.getZ()), SoundEvents.ENCHANTMENT_TABLE_USE,
                SoundCategory.AMBIENT, 0.6f, 1.0f);
    }

    private static void doBookParticles(World world, BlockPos pos, int number)
    {
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
