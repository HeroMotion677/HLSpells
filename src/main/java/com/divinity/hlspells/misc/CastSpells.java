package com.divinity.hlspells.misc;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.entities.*;
import com.divinity.hlspells.init.EntityInit;
import com.divinity.hlspells.util.Util;

import com.divinity.hlspells.init.SpellBookInit;
import com.divinity.hlspells.util.SpellUtils;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.SnowyDirtBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.entity.model.ShulkerBulletModel;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = HLSpells.MODID)
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

        else if (SpellUtils.getSpellBook(itemStack) == SpellBookInit.SOUL_SUMMON.get())
        {
            doSummonSpell(playerEntity);
        }

        else if (SpellUtils.getSpellBook(itemStack) == SpellBookInit.PULL.get())
        {
            doPullSpell(playerEntity);
        }

        else if (SpellUtils.getSpellBook(itemStack) ==  SpellBookInit.STORM.get())
        {
            doStormSpell(playerEntity);
        }

        else if (SpellUtils.getSpellBook(itemStack) == SpellBookInit.BOND.get())
        {
            doTameSpell(playerEntity);
        }

        else if (SpellUtils.getSpellBook(itemStack) == SpellBookInit.FIRE_BALL.get())
        {
            doFireBallSpell(playerEntity);
        }

        else if (SpellUtils.getSpellBook(itemStack) == SpellBookInit.LIGHTNING_BOLT.get())
        {
            doLightningBolt(playerEntity);
        }

        else if (SpellUtils.getSpellBook(itemStack) == SpellBookInit.KNOCKBACK_BOLT.get())
        {
            doKnockBackBolt(playerEntity);
        }

        else if (SpellUtils.getSpellBook(itemStack) == SpellBookInit.PIERCING_BOLT.get())
        {
            doPiercingBolt(playerEntity);
        }

        else if (SpellUtils.getSpellBook(itemStack) == SpellBookInit.FANGS.get())
        {
            doFangsSpell();
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
        if (Util.rayTrace(playerEntity.level, playerEntity, 25D) != null && playerEntity.isShiftKeyDown())
        {
            Entity entity = Util.rayTrace(playerEntity.level, playerEntity, 25D);
            ShulkerBulletEntity smartBullet = new SmartShulkerBullet(playerEntity.level, playerEntity, entity , playerEntity.getDirection().getAxis());
            smartBullet.setPos(playerEntity.getX() + playerEntity.getViewVector(1.0F).x, playerEntity.getY() + 1.35, playerEntity.getZ() + playerEntity.getViewVector(1.0F).z);
            playerEntity.level.addFreshEntity(smartBullet);
            return;
        }

        ShulkerBulletEntity dumbBullet = new ShulkerBulletEntity(EntityType.SHULKER_BULLET, playerEntity.level)
        {
            @Override
            public void selectNextMoveDirection(@Nullable Direction.Axis axis) {}

            @Override
            protected void onHit(RayTraceResult result)
            {
                RayTraceResult.Type raytraceresult$type = result.getType();
                if (raytraceresult$type == RayTraceResult.Type.ENTITY)
                {
                    this.onHitEntity((EntityRayTraceResult) result);
                }

                else if (raytraceresult$type == RayTraceResult.Type.BLOCK)
                {
                    this.onHitBlock((BlockRayTraceResult) result);
                }
            }

            @Override
            public void tick()
            {
                super.tick();
                if (this.getOwner() != null && this.distanceTo(this.getOwner()) >= 40)
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
            protected void onHitEntity(EntityRayTraceResult result)
            {
                Entity entity = result.getEntity();
                Entity entity1 = this.getOwner();
                LivingEntity livingentity = entity1 instanceof LivingEntity ? (LivingEntity) entity1 : null;

                if (result.getEntity() == this.getOwner())
                {
                    return;
                }

                boolean flag = entity.hurt(DamageSource.indirectMobAttack(this, livingentity).setProjectile(), 11.0F);
                if (flag)
                {
                    this.doEnchantDamageEffects(livingentity, entity);
                    this.remove();
                }
            }
        };

        dumbBullet.setNoGravity(true);
        dumbBullet.setOwner(playerEntity);
        dumbBullet.setPos(playerEntity.getX() + playerEntity.getViewVector(1.0F).x, playerEntity.getY() + 1.35, playerEntity.getZ() + playerEntity.getViewVector(1.0F).z);
        dumbBullet.shootFromRotation(playerEntity, playerEntity.xRot, playerEntity.yRot, 1.3F, 1.3F, 1.3F);;
        playerEntity.level.addFreshEntity(dumbBullet);
    }

    private static void doPiercingBolt (PlayerEntity playerEntity)
    {
        ShulkerBulletEntity piercingBullet = new ShulkerBulletEntity(EntityType.SHULKER_BULLET, playerEntity.level)
        {
            @Override
            public void selectNextMoveDirection(@Nullable Direction.Axis axis) {}

            @Override
            protected void onHit(RayTraceResult result)
            {
                RayTraceResult.Type raytraceresult$type = result.getType();
                if (raytraceresult$type == RayTraceResult.Type.ENTITY)
                {
                    this.onHitEntity((EntityRayTraceResult) result);
                }

                else if (raytraceresult$type == RayTraceResult.Type.BLOCK)
                {
                    this.onHitBlock((BlockRayTraceResult) result);
                }
            }

            @Override
            public void tick()
            {
                super.tick();
                if (this.getOwner() != null && this.distanceTo(this.getOwner()) >= 40)
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
            protected void onHitEntity(EntityRayTraceResult result)
            {
                Entity entity = result.getEntity();
                Entity entity1 = this.getOwner();
                LivingEntity livingentity = entity1 instanceof LivingEntity ? (LivingEntity) entity1 : null;

                if (result.getEntity() == this.getOwner())
                {
                    return;
                }

                boolean flag = entity.hurt(DamageSource.indirectMobAttack(this, livingentity).setProjectile().bypassArmor(), 8.0F);
                if (flag)
                {
                    this.doEnchantDamageEffects(livingentity, entity);
                    this.remove();
                }
            }
        };


        piercingBullet.setNoGravity(true);
        piercingBullet.setOwner(playerEntity);
        piercingBullet.setPos(playerEntity.getX() + playerEntity.getViewVector(1.0F).x, playerEntity.getY() + 1.35, playerEntity.getZ() + playerEntity.getViewVector(1.0F).z);
        piercingBullet.shootFromRotation(playerEntity, playerEntity.xRot, playerEntity.yRot, 1.3F, 1.3F, 1.3F);
        playerEntity.level.addFreshEntity(piercingBullet);
    }

    static int timer = 0;
    static boolean flag = false;
    static boolean flag2 = false;

    private static void doFangsSpell ()
    {
        flag = true;
    }

    @SubscribeEvent
    public void fangsSpell (TickEvent.PlayerTickEvent event)
    {
        if (event.player != null)
        {
            if (flag && !event.player.level.isClientSide())
            {
                List<EvokerFangsEntity> entities = new ArrayList<>();

                for (int i = 0; i < 28; i++)
                {
                    entities.add(new EvokerFangsEntity(EntityType.EVOKER_FANGS, event.player.level));
                    entities.get(i).setOwner(event.player);
                }

                entities.get(0).moveTo(event.player.getX() + 1, event.player.getY(), event.player.getZ());
                entities.get(1).moveTo(event.player.getX() - 1, event.player.getY(), event.player.getZ());
                entities.get(2).moveTo(event.player.getX(), event.player.getY(), event.player.getZ() + 1);
                entities.get(3).moveTo(event.player.getX(), event.player.getY(), event.player.getZ() - 1);
                entities.get(4).moveTo(event.player.getX() + 1, event.player.getY(), event.player.getZ() + 1);
                entities.get(5).moveTo(event.player.getX() - 1, event.player.getY(), event.player.getZ() - 1);
                entities.get(6).moveTo(event.player.getX() + 1, event.player.getY(), event.player.getZ() - 1);
                entities.get(7).moveTo(event.player.getX() - 1, event.player.getY(), event.player.getZ() + 1);
                entities.get(8).moveTo(event.player.getX() - 3, event.player.getY(), event.player.getZ() + 2);



                if (timer == 0 && !flag2)
                {
                    flag2 = true;

                    for (int i = 0;  i < 8; i++)
                    {
                        event.player.level.addFreshEntity(entities.get(i));
                    }
                }

                if (flag2)
                {
                    timer++;
                    if (timer == 20)
                    {
                        event.player.level.addFreshEntity(entities.get(8));
                        flag2 = false;
                        flag = false;
                        timer = 0;
                    }
                }
            }
        }
    }

    public static void doFireBallSpell (PlayerEntity playerEntity)
    {
        Vector3d vector3d = playerEntity.getViewVector(1.0F);
        FireballEntity fireballEntity = new FireballEntity(playerEntity.level, playerEntity, vector3d.x, vector3d.y, vector3d.z);
        fireballEntity.setPos(playerEntity.getX() + vector3d.x * 1.5D, playerEntity.getY() + 0.5, playerEntity.getZ() + vector3d.z * 1.5D);
        fireballEntity.setOwner(playerEntity);
        playerEntity.level.addFreshEntity(fireballEntity);
    }

    public static void doKnockBackBolt(PlayerEntity playerEntity)
    {
        ShulkerBulletEntity entity = new ShulkerBulletEntity(EntityType.SHULKER_BULLET, playerEntity.level)
        {
            @Override
            public void selectNextMoveDirection(@Nullable Direction.Axis axis) {}

            @Override
            protected void onHit(RayTraceResult result)
            {
                RayTraceResult.Type raytraceresult$type = result.getType();
                if (raytraceresult$type == RayTraceResult.Type.ENTITY)
                {
                    this.onHitEntity((EntityRayTraceResult) result);
                }

                else if (raytraceresult$type == RayTraceResult.Type.BLOCK)
                {
                    this.onHitBlock((BlockRayTraceResult) result);
                }
            }

            @Override
            public void tick()
            {
                super.tick();
                if (this.getOwner() != null && this.distanceTo(this.getOwner()) >= 40)
                {
                    this.remove();
                }
            }

            @Override
            protected void onHitEntity(EntityRayTraceResult result)
            {
                Entity entity = result.getEntity();
                Entity entity1 = this.getOwner();
                LivingEntity livingentity = entity1 instanceof LivingEntity ? (LivingEntity) entity1 : null;

                if (result.getEntity() == this.getOwner())
                {
                    return;
                }

                boolean flag = entity.hurt(DamageSource.indirectMobAttack(this, livingentity).setProjectile(), 0.0F);
                if (flag)
                {
                    entity.setDeltaMovement(this.getLookAngle().reverse().multiply(5.0D, 0, 5.0D));
                    this.remove();
                }
            }
        };

        entity.setNoGravity(true);
        entity.setPos(playerEntity.getX() + playerEntity.getViewVector(1.0F).x, playerEntity.getY() + 1.35, playerEntity.getZ() + playerEntity.getViewVector(1.0F).z);
        entity.shootFromRotation(playerEntity, playerEntity.xRot, playerEntity.yRot, 1.3F, 1.3F, 1.3F);
        playerEntity.level.addFreshEntity(entity);
    }

    public static void doLightningBolt (PlayerEntity playerEntity)
    {
        RayTraceResult rayTraceResult = Util.lookAt(playerEntity, 25D, 1F, false);
        Vector3d location = rayTraceResult.getLocation();
        int stepX = 0;
        int stepY = 0;
        int stepZ = 0;
        if (rayTraceResult instanceof BlockRayTraceResult)
        {
            Direction rayTraceDirection = ((BlockRayTraceResult) rayTraceResult).getDirection();
            stepX = rayTraceDirection.getStepX();
            stepY = rayTraceDirection.getStepY();
            stepZ = rayTraceDirection.getStepZ();
        }

        double dx = location.x() + stepX;
        double dy = location.y() + stepY - 1;
        double dz = location.z() + stepZ;

        LightningBoltEntity lightning = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, playerEntity.getCommandSenderWorld());
        lightning.moveTo(dx, dy, dz);
        playerEntity.level.addFreshEntity(lightning);
    }

    // Add config option for this
    public static void doTameSpell (PlayerEntity playerEntity)
    {
        if (Util.rayTrace(playerEntity.getCommandSenderWorld(), playerEntity, 20D) != null)
        {
            Entity targetEntity = Util.rayTrace(playerEntity.getCommandSenderWorld(), playerEntity, 20D);
            if (targetEntity instanceof TameableEntity)
            {
                TameableEntity entity = (TameableEntity) targetEntity;
                entity.tame(playerEntity);
            }
        }
    }

    // Pending change
    public static void doStormSpell (PlayerEntity playerEntity)
    {
        StormBulletEntity stormBullet = new StormBulletEntity(EntityInit.STORM_BULLET_ENTITY.get(), playerEntity.level);
        stormBullet.setHomePosition(playerEntity.position());
        stormBullet.setOwner(playerEntity);
        stormBullet.setPos(playerEntity.getX() + playerEntity.getViewVector(1.0F).x, playerEntity.getY() + 1.35, playerEntity.getZ() + playerEntity.getViewVector(1.0F).z);
        stormBullet.shootFromRotation(playerEntity, playerEntity.xRot, playerEntity.yRot, 1.3F, 1.3F, 1.3F);
        playerEntity.level.addFreshEntity(stormBullet);
    }

    public static void doPullSpell(PlayerEntity playerEntity)
    {
        if (Util.rayTrace(playerEntity.level, playerEntity, 35D) != null)
        {
            Entity targetEntity = Util.rayTrace(playerEntity.level, playerEntity, 35D);
            if (targetEntity != null && targetEntity.distanceTo(playerEntity) > 5) targetEntity.setDeltaMovement(playerEntity.getLookAngle().reverse().multiply(5, 0, 5));
        }
    }

    public static void doSoulSyphon(World world, PlayerEntity playerEntity)
    {
        RayTraceResult rayTraceResult = Util.lookAt(playerEntity, 150D, 1F, false);
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

    public static void doSummonSpell (PlayerEntity playerEntity)
    {
        for (int i = 0; i < 4; ++i)
        {
            BlockPos blockpos = playerEntity.blockPosition().offset(-2 + playerEntity.level.random.nextInt(5), 1, -2 + playerEntity.level.random.nextInt(5));
            SummonedVexEntity vexEntity = new SummonedVexEntity(EntityType.VEX, playerEntity.level);
            vexEntity.moveTo(blockpos, 0.0F, 0.0F);
            vexEntity.setSummonedOwner(playerEntity);
            vexEntity.setLimitedLife(20 * (30 + playerEntity.level.random.nextInt(90)));

            if (playerEntity.level instanceof ServerWorld)
            {
                ServerWorld world = (ServerWorld) playerEntity.level;
                vexEntity.finalizeSpawn(world, world.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                world.addFreshEntityWithPassengers(vexEntity);
            }
        }
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
