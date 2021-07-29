package com.divinity.hlspells.spells;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.entities.*;
import com.divinity.hlspells.init.EntityInit;
import com.divinity.hlspells.items.SpellBookItem;
import com.divinity.hlspells.util.Util;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraft.entity.passive.horse.ZombieHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class SpellActions {
    public static void doBlastSpell(World world, PlayerEntity playerEntity) {
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

    public static void doBoltSpell(PlayerEntity playerEntity) {
        if (Util.rayTrace(playerEntity.level, playerEntity, 25D) != null && playerEntity.isShiftKeyDown()) {
            Entity entity = Util.rayTrace(playerEntity.level, playerEntity, 25D);
            ShulkerBulletEntity smartBullet = new SmartShulkerBolt(playerEntity.level, playerEntity, entity, playerEntity.getDirection().getAxis());
            smartBullet.setPos(playerEntity.getX() + playerEntity.getViewVector(1.0F).x, playerEntity.getY() + 1.35, playerEntity.getZ() + playerEntity.getViewVector(1.0F).z);
            playerEntity.level.addFreshEntity(smartBullet);
            return;
        }

        ShulkerBulletEntity dumbBullet = new ShulkerBulletEntity(EntityType.SHULKER_BULLET, playerEntity.level) {
            @Override
            public void selectNextMoveDirection(@Nullable Direction.Axis axis) {
            }

            @Override
            public void onHit(RayTraceResult result) {
                RayTraceResult.Type raytraceresult$type = result.getType();
                if (raytraceresult$type == RayTraceResult.Type.ENTITY) {
                    this.onHitEntity((EntityRayTraceResult) result);
                } else if (raytraceresult$type == RayTraceResult.Type.BLOCK) {
                    this.onHitBlock((BlockRayTraceResult) result);
                }
            }

            @Override
            public void tick() {
                super.tick();
                if (this.getOwner() != null && this.distanceTo(this.getOwner()) >= 40) {
                    this.remove();
                }
            }

            @Override
            public IPacket<?> getAddEntityPacket() {
                return NetworkHooks.getEntitySpawningPacket(this);
            }

            @Override
            public void onHitEntity(EntityRayTraceResult result) {
                Entity entity = result.getEntity();
                Entity entity1 = this.getOwner();
                LivingEntity livingentity = entity1 instanceof LivingEntity ? (LivingEntity) entity1 : null;

                if (result.getEntity() == this.getOwner()) {
                    return;
                }

                boolean flag = entity.hurt(DamageSource.indirectMobAttack(this, livingentity).setProjectile(), 11.0F);
                if (flag) {
                    this.doEnchantDamageEffects(livingentity, entity);
                    this.remove();
                }
            }
        };

        dumbBullet.setNoGravity(true);
        dumbBullet.setOwner(playerEntity);
        dumbBullet.setPos(playerEntity.getX() + playerEntity.getViewVector(1.0F).x, playerEntity.getY() + 1.35, playerEntity.getZ() + playerEntity.getViewVector(1.0F).z);
        dumbBullet.shootFromRotation(playerEntity, playerEntity.xRot, playerEntity.yRot, 1.3F, 1.3F, 1.3F);
        playerEntity.level.addFreshEntity(dumbBullet);
    }

    public static void doAbsorbing(PlayerEntity playerEntity) {
        for (BlockPos blockpos1 : BlockPos.betweenClosed(MathHelper.floor(playerEntity.getX() - 8.0D), MathHelper.floor(playerEntity.getY() - 8.0D), MathHelper.floor(playerEntity.getZ() - 8.0D), MathHelper.floor(playerEntity.getX() + 8.0D), MathHelper.floor(playerEntity.getY() + 8.0D), MathHelper.floor(playerEntity.getZ() + 8.0D))) {
            BlockState blockState = playerEntity.level.getBlockState(blockpos1);
            FluidState fluidState = playerEntity.level.getFluidState(blockpos1);
            if (fluidState.is(FluidTags.WATER)) {
                if (blockState.getBlock() instanceof IWaterLoggable && ((IWaterLoggable) blockState.getBlock()).takeLiquid(playerEntity.level, blockpos1, blockState) != Fluids.EMPTY) {
                    playerEntity.level.setBlock(blockpos1, blockState.setValue(BlockStateProperties.WATERLOGGED, Boolean.FALSE), 3);
                } else {
                    playerEntity.level.setBlock(blockpos1, Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
    }

    public static void doFlamingBolt(PlayerEntity playerEntity) {
        FlamingBoltEntity flamingBolt = new FlamingBoltEntity(EntityInit.FLAMING_BOLT_ENTITY.get(), playerEntity.level);
        flamingBolt.setNoGravity(true);
        flamingBolt.setOwner(playerEntity);
        flamingBolt.setPos(playerEntity.getX() + playerEntity.getViewVector(1.0F).x, playerEntity.getY() + 1.35, playerEntity.getZ() + playerEntity.getViewVector(1.0F).z);
        flamingBolt.shootFromRotation(playerEntity, playerEntity.xRot, playerEntity.yRot, 1.3F, 1.3F, 1.3F);
        playerEntity.level.addFreshEntity(flamingBolt);
    }

    public static void doAquaBolt(PlayerEntity playerEntity) {
        AquaBoltEntity aquaBolt = new AquaBoltEntity(EntityInit.AQUA_BOLT_ENTITY.get(), playerEntity.level);
        aquaBolt.setNoGravity(true);
        aquaBolt.setOwner(playerEntity);
        aquaBolt.setPos(playerEntity.getX() + playerEntity.getViewVector(1.0F).x, playerEntity.getY() + 1.35, playerEntity.getZ() + playerEntity.getViewVector(1.0F).z);
        aquaBolt.shootFromRotation(playerEntity, playerEntity.xRot, playerEntity.yRot, 1.3F, 1.3F, 1.3F);
        playerEntity.level.addFreshEntity(aquaBolt);
    }

    public static void doPiercingBolt(PlayerEntity playerEntity) {
        PiercingBoltEntity piercingBullet = new PiercingBoltEntity(EntityInit.PIERCING_BOLT_ENTITY.get(), playerEntity.level);
        piercingBullet.setNoGravity(true);
        piercingBullet.setOwner(playerEntity);
        piercingBullet.setPos(playerEntity.getX() + playerEntity.getViewVector(1.0F).x, playerEntity.getY() + 1.35, playerEntity.getZ() + playerEntity.getViewVector(1.0F).z);
        piercingBullet.shootFromRotation(playerEntity, playerEntity.xRot, playerEntity.yRot, 1.3F, 1.3F, 1.3F);
        playerEntity.level.addFreshEntity(piercingBullet);
    }

    public static void doFireBallSpell(PlayerEntity playerEntity) {
        Vector3d vector3d = playerEntity.getViewVector(1.0F);
        FireballEntity fireballEntity = new FireballEntity(playerEntity.level, playerEntity, vector3d.x, vector3d.y, vector3d.z);
        fireballEntity.setPos(playerEntity.getX() + vector3d.x * 1.5D, playerEntity.getY() + 0.5, playerEntity.getZ() + vector3d.z * 1.5D);
        fireballEntity.setOwner(playerEntity);
        playerEntity.level.addFreshEntity(fireballEntity);
    }

    public static void doKnockBackBolt(PlayerEntity playerEntity) {
        ShulkerBulletEntity entity = new ShulkerBulletEntity(EntityType.SHULKER_BULLET, playerEntity.level) {
            @Override
            public void selectNextMoveDirection(@Nullable Direction.Axis axis) {
            }

            @Override
            public void onHit(RayTraceResult result) {
                RayTraceResult.Type raytraceresult$type = result.getType();
                if (raytraceresult$type == RayTraceResult.Type.ENTITY) {
                    this.onHitEntity((EntityRayTraceResult) result);
                } else if (raytraceresult$type == RayTraceResult.Type.BLOCK) {
                    this.onHitBlock((BlockRayTraceResult) result);
                }
            }

            @Override
            public void tick() {
                super.tick();
                if (this.getOwner() != null && this.distanceTo(this.getOwner()) >= 40) {
                    this.remove();
                }
            }

            @Override
            public void onHitEntity(EntityRayTraceResult result) {
                Entity entity = result.getEntity();
                Entity entity1 = this.getOwner();
                LivingEntity livingentity = entity1 instanceof LivingEntity ? (LivingEntity) entity1 : null;

                if (result.getEntity() == this.getOwner()) {
                    return;
                }

                boolean flag = entity.hurt(DamageSource.indirectMobAttack(this, livingentity).setProjectile(), 0.0F);
                if (flag) {
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

    public static void doLightningBolt(PlayerEntity playerEntity) {
        RayTraceResult rayTraceResult = Util.lookAt(playerEntity, 25D, 1F, false);
        Vector3d location = rayTraceResult.getLocation();
        int stepX = 0;
        int stepY = 0;
        int stepZ = 0;
        if (rayTraceResult instanceof BlockRayTraceResult) {
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
    public static void doTameSpell(PlayerEntity playerEntity) {
        if (Util.rayTrace(playerEntity.getCommandSenderWorld(), playerEntity, 20D) != null) {
            Entity targetEntity = Util.rayTrace(playerEntity.getCommandSenderWorld(), playerEntity, 20D);
            if (targetEntity instanceof TameableEntity) {
                TameableEntity entity = (TameableEntity) targetEntity;
                entity.tame(playerEntity);
            }
        }
    }

    // Pending change
    public static void doStormSpell(PlayerEntity playerEntity) {
        StormBoltEntity stormBullet = new StormBoltEntity(EntityInit.STORM_BULLET_ENTITY.get(), playerEntity.level);
        stormBullet.setHomePosition(playerEntity.position());
        stormBullet.setOwner(playerEntity);
        stormBullet.setPos(playerEntity.getX() + playerEntity.getViewVector(1.0F).x, playerEntity.getY() + 1.35, playerEntity.getZ() + playerEntity.getViewVector(1.0F).z);
        stormBullet.shootFromRotation(playerEntity, playerEntity.xRot, playerEntity.yRot, 1.3F, 1.3F, 1.3F);
        playerEntity.level.addFreshEntity(stormBullet);
    }

    public static void doPullSpell(PlayerEntity playerEntity) {
        if (Util.rayTrace(playerEntity.level, playerEntity, 35D) != null) {
            Entity targetEntity = Util.rayTrace(playerEntity.level, playerEntity, 35D);
            if (targetEntity != null && targetEntity.distanceTo(playerEntity) > 5)
                targetEntity.setDeltaMovement(playerEntity.getLookAngle().reverse().multiply(5, 5, 5));
        }
    }

    public static void doSoulSyphon(World world, PlayerEntity playerEntity) {
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

    public static void doSummonSpell(PlayerEntity playerEntity) {
        for (int i = 0; i < 4; ++i) {
            BlockPos blockpos = playerEntity.blockPosition().offset(-2 + playerEntity.level.random.nextInt(5), 1, -2 + playerEntity.level.random.nextInt(5));
            SummonedVexEntity vexEntity = new SummonedVexEntity(EntityType.VEX, playerEntity.level);
            vexEntity.moveTo(blockpos, 0.0F, 0.0F);
            vexEntity.setSummonedOwner(playerEntity);
            vexEntity.setLimitedLife(20 * (30 + playerEntity.level.random.nextInt(90)));

            if (playerEntity.level instanceof ServerWorld) {
                ServerWorld world = (ServerWorld) playerEntity.level;
                vexEntity.finalizeSpawn(world, world.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, null, null);
                world.addFreshEntityWithPassengers(vexEntity);
            }
        }
    }

    public static void doParticles(PlayerEntity playerEntity) {
        doBookParticles(playerEntity.getCommandSenderWorld(), new BlockPos(playerEntity.getX(), (playerEntity.getY() + 1), playerEntity.getZ()), 100);
        playerEntity.getCommandSenderWorld().playSound(null, new BlockPos(playerEntity.getX(), playerEntity.getY(), playerEntity.getZ()), SoundEvents.ENCHANTMENT_TABLE_USE,
                SoundCategory.AMBIENT, 0.6f, 1.0f);
    }

    public static void doBookParticles(World world, BlockPos pos, int number) {
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

    static int fangsSpellEvokerFangSpawnTimer = 0;
    static boolean fangsActiveFlag = false;
    static boolean fangsSpellStaggerBoolean = false;
    static PlayerEntity fangsSpellActivator;

    public static void doFangsSpell(PlayerEntity playerEntity) {
        fangsActiveFlag = true;
        fangsSpellActivator = playerEntity;
    }

    @SubscribeEvent
    public void fangsSpell(TickEvent.PlayerTickEvent event) {
        if (event.player != null && fangsSpellActivator != null) {
            if (fangsActiveFlag && !event.player.level.isClientSide() && fangsSpellActivator == event.player) {
                List<EvokerFangsEntity> entities = new ArrayList<>();

                for (int i = 0; i < 28; i++) {
                    entities.add(new EvokerFangsEntity(EntityType.EVOKER_FANGS, fangsSpellActivator.level));
                    entities.get(i).setOwner(fangsSpellActivator);
                }

                entities.get(0).setPosAndOldPos(fangsSpellActivator.getX() + 1, fangsSpellActivator.getY(), fangsSpellActivator.getZ());
                entities.get(1).setPosAndOldPos(fangsSpellActivator.getX() - 1, fangsSpellActivator.getY(), fangsSpellActivator.getZ());
                entities.get(2).setPosAndOldPos(fangsSpellActivator.getX(), fangsSpellActivator.getY(), fangsSpellActivator.getZ() + 1);
                entities.get(3).setPosAndOldPos(fangsSpellActivator.getX(), fangsSpellActivator.getY(), fangsSpellActivator.getZ() - 1);
                entities.get(4).setPosAndOldPos(fangsSpellActivator.getX() + 1, fangsSpellActivator.getY(), fangsSpellActivator.getZ() + 1);
                entities.get(5).setPosAndOldPos(fangsSpellActivator.getX() - 1, fangsSpellActivator.getY(), fangsSpellActivator.getZ() - 1);
                entities.get(6).setPosAndOldPos(fangsSpellActivator.getX() + 1, fangsSpellActivator.getY(), fangsSpellActivator.getZ() - 1);
                entities.get(7).setPosAndOldPos(fangsSpellActivator.getX() - 1, fangsSpellActivator.getY(), fangsSpellActivator.getZ() + 1);
                entities.get(8).setPosAndOldPos(fangsSpellActivator.getX() - 3, fangsSpellActivator.getY(), fangsSpellActivator.getZ() + 2);

                for (EvokerFangsEntity entity : entities) {
                    while (!(fangsSpellActivator.level.getBlockState(entity.blockPosition()).is(Blocks.AIR))) {
                        entity.setPos(entity.xOld, entity.yOld + 1, entity.zOld);
                    }
                }

                if (fangsSpellEvokerFangSpawnTimer == 0 && !fangsSpellStaggerBoolean) {
                    fangsSpellStaggerBoolean = true;

                    for (int i = 0; i < 8; i++) {
                        fangsSpellActivator.level.addFreshEntity(entities.get(i));
                    }
                }

                if (fangsSpellStaggerBoolean) {
                    fangsSpellEvokerFangSpawnTimer++;
                    if (fangsSpellEvokerFangSpawnTimer == 20) {
                        fangsSpellActivator.level.addFreshEntity(entities.get(8));
                        fangsSpellStaggerBoolean = false;
                        fangsActiveFlag = false;
                        fangsSpellEvokerFangSpawnTimer = 0;
                    }
                }
            }
        }
    }

    /*
    held spells
     */


    // For removing multiple enchants when the player hovers over the spell book after the enchanting is finished
    @SubscribeEvent
    public void tooltipEvent (ItemTooltipEvent event)
    {
        if (event.getPlayer() != null && event.getToolTip() != null)
        {
            if (event.getItemStack().getItem() instanceof SpellBookItem)
            {
                ItemStack stack = event.getItemStack();
                if (stack.getEnchantmentTags().size() > 1)
                {
                    for (int i = 0; i < stack.getEnchantmentTags().size() && stack.getEnchantmentTags().size() > 1; i++)
                    {
                        stack.getEnchantmentTags().remove(i);
                    }
                }
            }
        }
    }

    // Feather Falling
    public static void doFeatherFalling (PlayerEntity player)
    {
        if (player.getDeltaMovement().y <= 0)
        {
            player.addEffect(new EffectInstance(Effects.SLOW_FALLING, Integer.MAX_VALUE, 2, false, false));
            for (int a = 0; a < 9; a++)
            {
                player.getCommandSenderWorld().addParticle(ParticleTypes.CLOUD, player.getX(), player.getY() - 1,
                        player.getZ(), 0, player.getDeltaMovement().y, 0);
            }
        }
    }

    // Protection Circle
    public static void doProtectionCircle(PlayerEntity player)
    {
        List<LivingEntity> livingEntities = player.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class,
                new AxisAlignedBB(player.getX() - 6, player.getY() - 6, player.getZ() - 6,
                        player.getX() + 6, player.getY() + 6, player.getZ() + 6), null)
                .stream().sorted(new Object() {Comparator<Entity> compareDistOf(double x, double y, double z) {return Comparator.comparing(axis -> axis.distanceToSqr(x, y, z));}}
                        .compareDistOf(player.getX(), player.getY(), player.getZ())).collect(Collectors.toList());

        for (LivingEntity entity : livingEntities)
        {
            if (!(entity instanceof PlayerEntity)) entity.setDeltaMovement(entity.getLookAngle().reverse().multiply(0.3D,0,0.3D));
        }
    }

    // Levitation
    public static void doLevitation (PlayerEntity player)
    {
        if (player.getDeltaMovement().y >= 0)
        {
            player.addEffect(new EffectInstance(Effects.LEVITATION, Integer.MAX_VALUE, 2, false, false));

            for (int a = 0; a < 1; a++)
            {
                player.getCommandSenderWorld().addParticle(ParticleTypes.END_ROD, player.getX(), player.getY() - 1,
                        player.getZ(), 0, player.getDeltaMovement().y, 0);
            }
        }
    }


    // Arrow Rain

    static int arrowRainArrowSpawnTimer = 0;
    static int arrowRainCloudSpawnTimer = 0;
    static boolean arrowRainCloudSpawnBoolean = true;

    public static void doArrowRain(PlayerEntity player)
    {
        if (player.getCommandSenderWorld().isClientSide())
        {
            if (arrowRainCloudSpawnBoolean)
                doCloudParticles(player);
            arrowRainCloudSpawnBoolean = false;

            arrowRainCloudSpawnTimer++;
            if (arrowRainCloudSpawnTimer % 15 == 0)
            {
                arrowRainCloudSpawnBoolean = true;
                arrowRainCloudSpawnTimer = 0;
            }
        }

        else if (!player.getCommandSenderWorld().isClientSide())
        {
            arrowRainArrowSpawnTimer++;
            if (arrowRainArrowSpawnTimer % 15 == 0)
            {
                for (int i = 0; i < 5; i++)
                {
                    doArrowSpawn(player);
                }
                arrowRainArrowSpawnTimer = 0;
            }
        }
    }

    public static void doArrowSpawn(PlayerEntity player)
    {
        ArrowEntity arrowEntity = new ArrowEntity(player.getCommandSenderWorld(),
                player.getX() + (player.getCommandSenderWorld().random.nextDouble() - 0.5D) * (double) player.getBbWidth(),
                player.getY() + 4, player.getZ() + (player.getCommandSenderWorld().random.nextDouble() - 0.5D) * (double) player.getBbWidth());

        arrowEntity.shootFromRotation(player, player.xRot, player.yRot, 1.0F, 1.0F, 1.0F);
        arrowEntity.setDeltaMovement(MathHelper.cos((float) Math.toRadians(player.yRot + 90)) + (player.getCommandSenderWorld().random.nextFloat() - 0.5F) * player.getBbWidth(), -0.6, MathHelper.sin((float) Math.toRadians(player.yRot + 90)) + (player.getCommandSenderWorld().random.nextFloat() - 0.5F) * player.getBbWidth());
        player.getCommandSenderWorld().addFreshEntity(arrowEntity);
    }

    public static void doCloudParticles(PlayerEntity playerEntity)
    {
        for (int i = 0; i < 5; i++)
        {
            playerEntity.getCommandSenderWorld().addParticle(ParticleTypes.CLOUD, playerEntity.getX(), playerEntity.getY() + 5, playerEntity.getZ(), 0, 0, 0);
            playerEntity.getCommandSenderWorld().addParticle(ParticleTypes.CLOUD, playerEntity.getX() + 0.45, playerEntity.getY() + 5, playerEntity.getZ(), 0, 0, 0);
            playerEntity.getCommandSenderWorld().addParticle(ParticleTypes.CLOUD, playerEntity.getX() - 0.45, playerEntity.getY() + 5, playerEntity.getZ(), 0, 0, 0);
            playerEntity.getCommandSenderWorld().addParticle(ParticleTypes.CLOUD, playerEntity.getX(), playerEntity.getY() + 5, playerEntity.getZ() + 0.45, 0, 0, 0);
            playerEntity.getCommandSenderWorld().addParticle(ParticleTypes.CLOUD, playerEntity.getX(), playerEntity.getY() + 5, playerEntity.getZ() - 0.45, 0, 0, 0);
            playerEntity.getCommandSenderWorld().addParticle(ParticleTypes.CLOUD, playerEntity.getX() + 0.45, playerEntity.getY() + 5, playerEntity.getZ() + 0.45, 0, 0, 0);
            playerEntity.getCommandSenderWorld().addParticle(ParticleTypes.CLOUD, playerEntity.getX() - 0.45, playerEntity.getY() + 5, playerEntity.getZ() - 0.45, 0, 0, 0);
            playerEntity.getCommandSenderWorld().addParticle(ParticleTypes.CLOUD, playerEntity.getX() + 0.45, playerEntity.getY() + 5, playerEntity.getZ() - 0.45, 0, 0, 0);
            playerEntity.getCommandSenderWorld().addParticle(ParticleTypes.CLOUD, playerEntity.getX() - 0.45, playerEntity.getY() + 5, playerEntity.getZ() + 0.45, 0, 0, 0);
        }
    }

    // Healing Circle

    static int healingTimer = 0;

    public static void doHealingCircle (PlayerEntity player)
    {
        List<LivingEntity> livingEntities = player.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class,
                new AxisAlignedBB(player.getX() - 6, player.getY() - 6, player.getZ() - 6,
                        player.getX() + 6, player.getY() + 6, player.getZ() + 6), null)
                .stream().sorted(new Object() {Comparator<Entity> compareDistOf(double x, double y, double z) {return Comparator.comparing(axis -> axis.distanceToSqr(x, y, z));}}
                        .compareDistOf(player.getX(), player.getY(), player.getZ())).collect(Collectors.toList());

        World world = player.getCommandSenderWorld();

        healingTimer++;

        if (healingTimer % 10 == 0)
        {
            doHealingParticles(player, world);
        }

        if (healingTimer % 20 == 0)
        {
            for (LivingEntity entities : livingEntities)
            {
                doRadiusParticles(entities);

                if (entities instanceof PhantomEntity || entities instanceof SkeletonEntity || entities instanceof SkeletonHorseEntity
                        || entities instanceof WitherEntity || entities instanceof WitherSkeletonEntity || entities instanceof ZoglinEntity
                        || entities instanceof ZombieEntity || entities instanceof ZombieHorseEntity)
                {
                    entities.setLastHurtByPlayer(player);
                    entities.hurt(DamageSource.MAGIC, 1.0F);
                }

                else
                {
                    entities.heal(1.0F);
                }
            }
            healingTimer = 0;
        }
    }

    public static void doHealingParticles(PlayerEntity player, World world)
    {
        for (int i = 0; i < 2; i++)
        {
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 0 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 0 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 5 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 5 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 5 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 5 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 5 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 5 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 5 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 5 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 5 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 5 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 5 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 5 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 5 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 5 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 5 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 5 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 5 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 5 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 5 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 5 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 5 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 5 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 5 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 5 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 6 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 6 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 6 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 6 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 6 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 6 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 6 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 6 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 6 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 6 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 6 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 6 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 2 + world.random.nextFloat(), 0, 0, 0);
        }

        // Ring
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() - 1, player.getY() + 1.2, player.getZ() - 6, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX(), player.getY() + 1.2, player.getZ() - 6, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + 1, player.getY() + 1.2, player.getZ() - 6, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + 2, player.getY() + 1.2, player.getZ() - 5, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + 3, player.getY() + 1.2, player.getZ() - 4, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + 4, player.getY() + 1.2, player.getZ() - 3, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + 5, player.getY() + 1.2, player.getZ() - 2, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + 6, player.getY() + 1.2, player.getZ() - 1, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + 6, player.getY() + 1.2, player.getZ(), 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + 6, player.getY() + 1.2, player.getZ() + 1, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + 5, player.getY() + 1.2, player.getZ() + 2, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + 4, player.getY() + 1.2, player.getZ() + 3, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + 3, player.getY() + 1.2, player.getZ() + 4, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + 2, player.getY() + 1.2, player.getZ() + 5, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + 1, player.getY() + 1.2, player.getZ() + 6, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX(), player.getY() + 1.2, player.getZ() + 6, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() - 1, player.getY() + 1.2, player.getZ() + 6, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() - 2, player.getY() + 1.2, player.getZ() + 5, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() - 3, player.getY() + 1.2, player.getZ() + 4, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() - 4, player.getY() + 1.2, player.getZ() + 3, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() - 5, player.getY() + 1.2, player.getZ() + 2, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() - 6, player.getY() + 1.2, player.getZ() + 1, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() - 6, player.getY() + 1.2, player.getZ(), 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() - 6, player.getY() + 1.2, player.getZ() - 1, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() - 5, player.getY() + 1.2, player.getZ() - 2, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() - 4, player.getY() + 1.2, player.getZ() - 3, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() - 3, player.getY() + 1.2, player.getZ() - 4, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() - 2, player.getY() + 1.2, player.getZ() - 5, 0, 0, 0);
    }

    public static void doRadiusParticles(Entity entities)
    {
        if (Minecraft.getInstance().player != null)
        {
            World clientWorld = Minecraft.getInstance().player.getCommandSenderWorld();

            if (entities instanceof PhantomEntity || entities instanceof SkeletonEntity || entities instanceof SkeletonHorseEntity
                    || entities instanceof WitherEntity || entities instanceof WitherSkeletonEntity || entities instanceof ZoglinEntity
                    || entities instanceof ZombieEntity || entities instanceof ZombieHorseEntity)
            {
                for (int i = 0; i < 5; i++)
                {
                    double d0 = (entities.getX() + clientWorld.random.nextFloat());
                    double d1 = (entities.getY() + clientWorld.random.nextFloat());
                    double d2 = (entities.getZ() + clientWorld.random.nextFloat());
                    double d3 = (clientWorld.random.nextFloat() - 0.2D) * 0.5D;
                    double d4 = (clientWorld.random.nextFloat() - 0.2D) * 0.5D;
                    double d5 = (clientWorld.random.nextFloat() - 0.2D) * 0.5D;
                    clientWorld.addParticle(ParticleTypes.SMOKE, d0, d1, d2, d3, d4, d5);
                }
            }

            else
            {
                double d0 = (entities.getX() + (clientWorld.random.nextFloat() - 0.5D));
                double d1 = (entities.getY() + (clientWorld.random.nextFloat() - 0.3D));
                double d2 = (entities.getZ() + (clientWorld.random.nextFloat() - 0.5D));
                double d3 = (clientWorld.random.nextFloat() - 0.2D) * 0.5D;
                double d4 = (clientWorld.random.nextFloat() - 0.2D) * 0.5D;
                double d5 = (clientWorld.random.nextFloat() - 0.2D) * 0.5D;
                clientWorld.addParticle(ParticleTypes.HEART, d0, d1, d2, d3,d4, d5);
            }
        }
    }

    // Speed
    public static void doSpeed (PlayerEntity player)
    {
        ModifiableAttributeInstance attribute = player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
        if (attribute != null)
        {
            attribute.setBaseValue(2.2F);
        }
    }

    // Respiration

    static int airTimer = 0;

    public static void doRespiration (PlayerEntity player)
    {
        List<PlayerEntity> players = player.getCommandSenderWorld().getEntitiesOfClass(PlayerEntity.class,
                new AxisAlignedBB(player.getX() - 10, player.getY() - 4, player.getZ() - 10,
                        player.getX() + 10, player.getY() + 4, player.getZ() + 10), null)
                .stream().sorted(new Object() {Comparator<Entity> compareDistOf(double x, double y, double z) {return Comparator.comparing(axis -> axis.distanceToSqr(x, y, z));}}
                        .compareDistOf(player.getX(), player.getY(), player.getZ())).collect(Collectors.toList());

        airTimer++;
        for (PlayerEntity p : players)
        {
            if (p.isUnderWater() && airTimer == 10)
            {
                p.setAirSupply(p.getAirSupply() + 15);
                if (p.getAirSupply() > p.getMaxAirSupply())
                {
                    p.setAirSupply(p.getMaxAirSupply());
                }
                airTimer = 0;
            }
        }
    }

    public static void resetEffects (PlayerEntity playerEntity)
    {
        arrowRainArrowSpawnTimer = 0;
        arrowRainCloudSpawnTimer = 0;
        healingTimer = 0;
        airTimer = 0;
        ModifiableAttributeInstance instance = playerEntity.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
        if (instance != null)
        {
            instance.setBaseValue(0.10000000149011612F);
        }
        playerEntity.removeEffect(Effects.SLOW_FALLING);
        playerEntity.removeEffect(Effects.LEVITATION);
    }
}
