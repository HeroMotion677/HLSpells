package com.divinity.hlspells.spells;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.entities.*;
import com.divinity.hlspells.goal.SpellBookLureGoal;
import com.divinity.hlspells.goal.SpellBookRepelGoal;
import com.divinity.hlspells.init.BlockInit;
import com.divinity.hlspells.init.EntityInit;
import com.divinity.hlspells.player.capability.PlayerCapProvider;
import com.divinity.hlspells.util.Util;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.network.IPacket;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.divinity.hlspells.goal.SpellBookLureGoal.LURE_RANGE;

/**
 * This class is responsible for doing specific spell actions
 */
@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class SpellActions {
    static final UUID speedUUID = UUID.fromString("05b61a62-ae84-492e-8536-f365b7143296");
    static final AttributeModifier speedModifier = new AttributeModifier(speedUUID, "Speed", 2, AttributeModifier.Operation.MULTIPLY_TOTAL);
    public static EffectInstance GLOWING = new EffectInstance(Effects.GLOWING, Integer.MAX_VALUE, 5, false, false);
    public static EffectInstance LEVITATION = new EffectInstance(Effects.LEVITATION, Integer.MAX_VALUE, 5, false, false);
    public static EffectInstance SLOW_FALLING = new EffectInstance(Effects.SLOW_FALLING, Integer.MAX_VALUE, 5, false, false);
    static int flameTimer = 0;
    static int arrowRainArrowSpawnTimer = 0;
    static int arrowRainCloudSpawnTimer = 0;
    static int protectionCircleTimer = 0;
    static boolean arrowRainCloudSpawnBoolean = true;
    static int healingTimer = 0;
    static int airTimer = 0;

    /**
     * Returns a comparator which compares entities' distances to given player
     */
    public static Comparator<Entity> getEntityComparator(PlayerEntity player) {
        return new Object() {
            Comparator<Entity> compareDistOf(double x, double y, double z) {
                return Comparator.comparing(entity -> entity.distanceToSqr(x, y, z));
            }
        }.compareDistOf(player.getX(), player.getY(), player.getZ());
    }

    public static boolean doBlastSpell(PlayerEntity player, World world) {
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();
        List<Entity> entities = world.getEntitiesOfClass(Entity.class,
                new AxisAlignedBB(x - 6, y - 6, z - 6,
                        x + 6, y + 6, z + 6),
                null).stream().sorted(getEntityComparator(player)).collect(Collectors.toList());
        for (Entity entity : entities) {
            if ((entity instanceof LivingEntity) && (entity != player)) {
                LivingEntity livingEntity = (LivingEntity) entity;
                livingEntity.knockback(5F * 0.5F, MathHelper.sin(player.yRot * ((float) Math.PI / 180F)), -MathHelper.cos(player.yRot * ((float) Math.PI / 180F)));
                livingEntity.hurt(DamageSource.explosion((livingEntity)), 4.0F);
                player.setDeltaMovement(player.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
            }
        }
        world.playSound(null, new BlockPos(x, y, z), SoundEvents.GENERIC_EXPLODE,
                SoundCategory.WEATHER, 0.6f, 1.0f);
        world.addParticle(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 1.0D, 0.0D, 0.0D);
        return true;
    }

    public static boolean doBoltSpell(PlayerEntity player, World world) {
        if (Util.rayTrace(world, player, 25D) != null && player.isShiftKeyDown()) {
            Entity entity = Util.rayTrace(world, player, 25D);
            ShulkerBulletEntity smartBullet = new SmartShulkerBolt(world, player, entity, player.getDirection().getAxis());
            smartBullet.setPos(player.getX() + player.getViewVector(1.0F).x, player.getY() + 1.35, player.getZ() + player.getViewVector(1.0F).z);
            world.addFreshEntity(smartBullet);
        } else {
            ShulkerBulletEntity dumbBullet = new ShulkerBulletEntity(EntityType.SHULKER_BULLET, world) {
                @Override
                public void selectNextMoveDirection(@Nullable Direction.Axis axis) {
                }

                @Override
                public void onHit(RayTraceResult result) {
                    RayTraceResult.Type type = result.getType();
                    if (type == RayTraceResult.Type.ENTITY) {
                        this.onHitEntity((EntityRayTraceResult) result);
                    } else if (type == RayTraceResult.Type.BLOCK) {
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

                    boolean flag = entity.hurt(DamageSource.indirectMobAttack(this, livingentity).setProjectile(), 8.0F);
                    if (flag) {
                        this.doEnchantDamageEffects(livingentity, entity);
                        this.remove();
                    }
                }
            };

            dumbBullet.setNoGravity(true);
            dumbBullet.setOwner(player);
            dumbBullet.setPos(player.getX() + player.getViewVector(1.0F).x, player.getY() + 1.35, player.getZ() + player.getViewVector(1.0F).z);
            dumbBullet.shootFromRotation(player, player.xRot, player.yRot, 1.3F, 1.3F, 1.3F);
            world.addFreshEntity(dumbBullet);
        }
        return true;
    }

    public static boolean doAbsorbing(PlayerEntity player, World world) {
        boolean used = false;
        for (BlockPos blockPos : BlockPos.betweenClosed(MathHelper.floor(player.getX() - 8.0D), MathHelper.floor(player.getY() - 8.0D), MathHelper.floor(player.getZ() - 8.0D), MathHelper.floor(player.getX() + 8.0D), MathHelper.floor(player.getY() + 8.0D), MathHelper.floor(player.getZ() + 8.0D))) {
            BlockState blockState = world.getBlockState(blockPos);
            FluidState fluidState = world.getFluidState(blockPos);
            if (fluidState.is(FluidTags.WATER)) {
                used = true;
                if (blockState.getBlock() instanceof IWaterLoggable && ((IWaterLoggable) blockState.getBlock()).takeLiquid(world, blockPos, blockState) != Fluids.EMPTY) {
                    world.setBlock(blockPos, blockState.setValue(BlockStateProperties.WATERLOGGED, Boolean.FALSE), 3);
                } else {
                    world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
        return used;
    }

    public static boolean doFlamingBolt(PlayerEntity player, World world) {
        FlamingBoltEntity flamingBolt = new FlamingBoltEntity(EntityInit.FLAMING_BOLT_ENTITY.get(), world);
        flamingBolt.setOwner(player);
        flamingBolt.setPos(player.getX() + player.getViewVector(1.0F).x, player.getY() + 1.35, player.getZ() + player.getViewVector(1.0F).z);
        flamingBolt.shootFromRotation(player, player.xRot, player.yRot, 1.3F, 1.3F, 1.3F);
        world.addFreshEntity(flamingBolt);
        return true;
    }

    public static boolean doAquaBolt(PlayerEntity player, World world) {
        AquaBoltEntity aquaBolt = new AquaBoltEntity(EntityInit.AQUA_BOLT_ENTITY.get(), world);
        aquaBolt.setOwner(player);
        aquaBolt.setPos(player.getX() + player.getViewVector(1.0F).x, player.getY() + 1.35, player.getZ() + player.getViewVector(1.0F).z);
        aquaBolt.shootFromRotation(player, player.xRot, player.yRot, 1.3F, 1.3F, 1.3F);
        world.addFreshEntity(aquaBolt);
        return true;
    }

    public static boolean doPiercingBolt(PlayerEntity player, World world) {
        PiercingBoltEntity piercingBullet = new PiercingBoltEntity(EntityInit.PIERCING_BOLT_ENTITY.get(), world);
        piercingBullet.setOwner(player);
        piercingBullet.setPos(player.getX() + player.getViewVector(1.0F).x, player.getY() + 1.35, player.getZ() + player.getViewVector(1.0F).z);
        piercingBullet.shootFromRotation(player, player.xRot, player.yRot, 1.3F, 1.3F, 1.3F);
        world.addFreshEntity(piercingBullet);
        return true;
    }

    public static boolean doFireBallSpell(PlayerEntity player, World world) {
        Vector3d vector3d = player.getViewVector(1.0F);
        FireballEntity fireballEntity = new FireballEntity(world, player, vector3d.x, vector3d.y, vector3d.z);
        fireballEntity.setPos(player.getX() + vector3d.x * 1.5D, player.getY() + 0.5, player.getZ() + vector3d.z * 1.5D);
        fireballEntity.setOwner(player);
        world.addFreshEntity(fireballEntity);
        return true;
    }

    public static boolean doKnockBackBolt(PlayerEntity player, World world) {
        ShulkerBulletEntity entity = new ShulkerBulletEntity(EntityType.SHULKER_BULLET, world) {
            @Override
            public void selectNextMoveDirection(@Nullable Direction.Axis axis) {
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
        entity.setPos(player.getX() + player.getViewVector(1.0F).x, player.getY() + 1.35, player.getZ() + player.getViewVector(1.0F).z);
        entity.shootFromRotation(player, player.xRot, player.yRot, 1.3F, 1.3F, 1.3F);
        world.addFreshEntity(entity);
        return true;
    }

    public static boolean doLightningBolt(PlayerEntity player, World world) {
        RayTraceResult rayTraceResult = Util.lookAt(player, 25D, 1F, false);
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

        LightningBoltEntity lightning = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, world);
        lightning.moveTo(dx, dy, dz);
        world.addFreshEntity(lightning);
        return true;
    }

    // Add config option for this
    public static boolean doBondSpell(PlayerEntity player, World world) {
        Entity targetEntity = Util.rayTrace(world, player, 20D);
        if (targetEntity instanceof TameableEntity) {
            TameableEntity entity = (TameableEntity) targetEntity;
            entity.tame(player);
            return true;
        }
        return false;
    }

    public static boolean doLightingChain(PlayerEntity player, World world) {
        InvisibleTargetingEntity stormBullet = new InvisibleTargetingEntity(EntityInit.INVISIBLE_TARGETING_ENTITY.get(), world);
        stormBullet.setHomePosition(player.position());
        stormBullet.setOwner(player);
        stormBullet.setPos(player.getX() + player.getViewVector(1.0F).x, player.getY() + 1.35, player.getZ() + player.getViewVector(1.0F).z);
        stormBullet.shootFromRotation(player, player.xRot, player.yRot, 1.3F, 1.3F, 1.3F);
        world.addFreshEntity(stormBullet);
        return true;
    }

    public static boolean doPullSpell(PlayerEntity player, World world) {
        if (Util.rayTrace(world, player, 35D) != null) {
            Entity targetEntity = Util.rayTrace(world, player, 35D);
            if (targetEntity != null && targetEntity.distanceTo(player) > 5) {
                targetEntity.setDeltaMovement(player.getLookAngle().reverse().multiply(5, 5, 5));
                return true;
            }
        }
        return false;
    }

    public static boolean doTeleport(PlayerEntity player, World world) {
        RayTraceResult rayTraceResult = Util.lookAt(player, 50D, 1F, false);
        Vector3d location = rayTraceResult.getLocation();
        int stepX = 0;
        int stepY = 1;
        int stepZ = 0;
        if ((rayTraceResult instanceof BlockRayTraceResult)
                && world.getBlockState(new BlockPos(location).above()).getMaterial() != Material.AIR) {
            Direction rayTraceDirection = ((BlockRayTraceResult) rayTraceResult).getDirection();
            stepX = rayTraceDirection.getStepX();
            stepY = rayTraceDirection.getStepY();
            stepZ = rayTraceDirection.getStepZ();
        }
        double tx = location.x() + stepX;
        double ty = location.y() + stepY;
        double tz = location.z() + stepZ;
        BlockPos teleportPos = new BlockPos(tx, ty, tz);
        player.fallDistance = 0;
        Util.teleport(world, player.blockPosition(), teleportPos, player);
        return true;
    }

    public static boolean doSummonSpell(PlayerEntity player, World world) {
        for (int i = 0; i < 4; ++i) {
            BlockPos blockpos = player.blockPosition().offset(-2 + world.random.nextInt(5), 1, -2 + world.random.nextInt(5));
            SummonedVexEntity vexEntity = new SummonedVexEntity(EntityInit.SUMMONED_VEX_ENTITY.get(), world);
            vexEntity.moveTo(blockpos, 0.0F, 0.0F);
            vexEntity.setSummonedOwner(player);
            vexEntity.setLimitedLife(20 * (30 + world.random.nextInt(50)));

            if (world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) world;
                vexEntity.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, null, null);
                serverWorld.addFreshEntityWithPassengers(vexEntity);
            }
        }
        return true;
    }

    public static void doParticles(PlayerEntity player) {
        doBookParticles(player.level, new BlockPos(player.getX(), (player.getY() + 1), player.getZ()), 100);
        player.level.playSound(null, new BlockPos(player.getX(), player.getY(), player.getZ()), SoundEvents.ENCHANTMENT_TABLE_USE,
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

    public static boolean doFangsSpell(PlayerEntity player, World world) {
        float f = (float) MathHelper.atan2(player.getZ(), player.getX());
        if (!player.isShiftKeyDown()) {
            InvisibleTargetingEntity stormBullet = new InvisibleTargetingEntity(EntityInit.INVISIBLE_TARGETING_ENTITY.get(), world);
            stormBullet.setHomePosition(player.position());
            stormBullet.setIsLightning(false);
            stormBullet.setOwner(player);
            stormBullet.setPos(player.getX(), player.getY(), player.getZ());
            stormBullet.shootFromRotation(player, player.xRot, player.yRot, 1.2F, 1.2F, 1.2F);
            stormBullet.setDeltaMovement(MathHelper.cos((float) Math.toRadians(player.yRot + 90)), 0, MathHelper.sin((float) Math.toRadians(player.yRot + 90)));
            world.addFreshEntity(stormBullet);
        } else {
            for (int i = 0; i < 5; ++i) {
                float f1 = f + i * (float) Math.PI * 0.4F;
                createFangsEntity(player, world, player.getX() + MathHelper.cos(f1) * 1.5D, player.getZ() + MathHelper.sin(f1) * 1.5D, player.getY(), f1, 0);
            }
            for (int k = 0; k < 8; ++k) {
                float f2 = f + k * (float) Math.PI * 2.0F / 8.0F + 1.2566371F;
                createFangsEntity(player, world, player.getX() + MathHelper.cos(f2) * 2.5D, player.getZ() + MathHelper.sin(f2) * 2.5D, player.getY(), f2, 3);
            }
        }
        return true;
    }

    // Slow Fall
    public static boolean doSlowFall(PlayerEntity player, World world) {
        if (player.getDeltaMovement().y <= 0) {
            EffectInstance effect = player.getEffect(Effects.SLOW_FALLING);
            if (effect != null && effect.isVisible()) {
                player.getCapability(PlayerCapProvider.PLAYER_CAP).ifPresent(cap -> {
                    if (cap.getEffect() == null) {
                        cap.setEffect(effect.getEffect());
                        cap.setEffectDuration(effect.getDuration());
                        cap.setEffectAmplifier(effect.getAmplifier());
                    }
                });
            }
            player.addEffect(SLOW_FALLING);
            for (int i = 0; i < 3; i++) {
                world.addParticle(ParticleTypes.CLOUD, player.getX(), player.getY() - 1,
                        player.getZ(), 0, player.getDeltaMovement().y, 0);
            }
            return true;
        }
        return false;
    }

    // Frost Path (Code is from FrostWalkerEnchantment onEntityMoved())
    public static boolean doFrostPath(PlayerEntity player, World world) {
        BlockPos pos = player.blockPosition();
        BlockState blockstate = BlockInit.CUSTOM_FROSTED_ICE.get().defaultBlockState();
        float f = 3;
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();
        boolean used = false;
        for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset((-f), -1.0D, (-f)), pos.offset(f, -1.0D, f))) {
            if (blockpos.closerThan(player.position(), f)) {
                mutablePos.set(blockpos.getX(), blockpos.getY() + 1, blockpos.getZ());
                BlockState mutableState = world.getBlockState(mutablePos);
                if (mutableState.isAir(world, mutablePos)) {
                    BlockState state = world.getBlockState(blockpos);
                    if (state.getMaterial().isReplaceable() && blockstate.canSurvive(world, blockpos) && world.isUnobstructed(blockstate, blockpos, ISelectionContext.empty()) && !net.minecraftforge.event.ForgeEventFactory.onBlockPlace(player, net.minecraftforge.common.util.BlockSnapshot.create(world.dimension(), world, blockpos), net.minecraft.util.Direction.UP)) {
                        used = true;
                        world.setBlockAndUpdate(blockpos, blockstate);
                        world.getBlockTicks().scheduleTick(blockpos, BlockInit.CUSTOM_FROSTED_ICE.get(), MathHelper.nextInt(player.getRandom(), 60, 120));
                    }
                }
            }
        }
        return used;
    }


    // Lure
    public static boolean doLure(PlayerEntity player, World world) {
        EffectInstance effect = player.getEffect(Effects.GLOWING);
        if (effect != null && effect.isVisible()) {
            player.getCapability(PlayerCapProvider.PLAYER_CAP).ifPresent(cap -> {
                if (cap.getEffect() == null) {
                    cap.setEffect(effect.getEffect());
                    cap.setEffectDuration(effect.getDuration());
                    cap.setEffectAmplifier(effect.getAmplifier());
                }
            });
        }
        player.addEffect(GLOWING);

        List<MobEntity> mobEntities = world.getEntitiesOfClass(MobEntity.class,
                new AxisAlignedBB(player.getX() - LURE_RANGE, player.getY() - LURE_RANGE, player.getZ() - LURE_RANGE,
                        player.getX() + LURE_RANGE, player.getY() + LURE_RANGE, player.getZ() + LURE_RANGE), null)
                .stream().sorted(getEntityComparator(player)).collect(Collectors.toList());
        boolean used = false;
        for (MobEntity mob : mobEntities) {
            List<? extends String> blacklistedMobs = HLSpells.CONFIG.sapientMobsList.get();
            boolean predicate = false;
            for (String id : blacklistedMobs) {
                if (id.equals(mob.getType().getRegistryName().toString())) {
                    predicate = true;
                }
            }
            if (!predicate && mob.goalSelector.getRunningGoals().noneMatch(p -> p.getGoal() instanceof SpellBookLureGoal)) {
                mob.goalSelector.addGoal(0, new SpellBookLureGoal(mob, 1.0D));
                used = true;
            }
        }
        return used;
    }


    // Repel
    public static boolean doRepel(PlayerEntity player, World world) {
        List<MobEntity> mobEntities = world.getEntitiesOfClass(MobEntity.class,
                new AxisAlignedBB(player.getX() - 15, player.getY() - 15, player.getZ() - 15,
                        player.getX() + 15, player.getY() + 15, player.getZ() + 15), null)
                .stream().sorted(getEntityComparator(player)).collect(Collectors.toList());
        boolean used = false;
        for (MobEntity mob : mobEntities) {
            List<? extends String> blacklistedMobs = HLSpells.CONFIG.sapientMobsList.get();
            boolean predicate = false;
            for (String id : blacklistedMobs) {
                if (id.equals(mob.getType().getRegistryName().toString())) {
                    predicate = true;
                }
            }
            if (!predicate && mob.goalSelector.getRunningGoals().noneMatch(p -> p.getGoal() instanceof SpellBookRepelGoal)) {
                mob.goalSelector.addGoal(0, new SpellBookRepelGoal(mob, 1.2D));
                used = true;
            }
        }
        return used;
    }

    // Flaming Circle
    public static boolean doFlamingCircle(PlayerEntity player, World world) {
        List<LivingEntity> livingEntities = world.getEntitiesOfClass(LivingEntity.class,
                new AxisAlignedBB(player.getX() - 6, player.getY() + 1, player.getZ() - 6,
                        player.getX() + 6, player.getY() - 1, player.getZ() + 6), null)
                .stream().sorted(getEntityComparator(player)).collect(Collectors.toList());

        flameTimer++;
        boolean used = false;
        if (flameTimer % 10 == 0) {
            doEnchantParticleInterior(player, world);
            doOuterRingParticles(ParticleTypes.FLAME, player, world);
            flameTimer = 0;
        }
        for (LivingEntity entity : livingEntities) {
            if (entity != null && entity != player) {
                used = true;
                entity.setLastHurtByPlayer(player);
                entity.setSecondsOnFire(1);
            }
        }
        return used;
    }

    private static void doEnchantParticleInterior(PlayerEntity player, World world) {
        for (int x = -6; x <= 6; x++) {
            for (int z = -6; z <= 6; z++) {
                world.addParticle(ParticleTypes.ENCHANT, player.getX() + x + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + z + world.random.nextFloat(), 0, 0, 0);
            }
        }
    }

    // Adds the given particle in a circle around the player
    private static void doOuterRingParticles(BasicParticleType type, PlayerEntity player, World world) {
        world.addParticle(type, player.getX() - 1, player.getY() + 1.2, player.getZ() - 6, 0, 0, 0);
        world.addParticle(type, player.getX(), player.getY() + 1.2, player.getZ() - 6, 0, 0, 0);
        world.addParticle(type, player.getX() + 1, player.getY() + 1.2, player.getZ() - 6, 0, 0, 0);
        world.addParticle(type, player.getX() + 1.5, player.getY() + 1.2, player.getZ() - 5.5, 0, 0, 0);
        world.addParticle(type, player.getX() + 2, player.getY() + 1.2, player.getZ() - 5, 0, 0, 0);
        world.addParticle(type, player.getX() + 2.5, player.getY() + 1.2, player.getZ() - 4.5, 0, 0, 0);
        world.addParticle(type, player.getX() + 3, player.getY() + 1.2, player.getZ() - 4, 0, 0, 0);
        world.addParticle(type, player.getX() + 3.5, player.getY() + 1.2, player.getZ() - 3.5, 0, 0, 0);
        world.addParticle(type, player.getX() + 4, player.getY() + 1.2, player.getZ() - 3, 0, 0, 0);
        world.addParticle(type, player.getX() + 4.5, player.getY() + 1.2, player.getZ() - 2.5, 0, 0, 0);
        world.addParticle(type, player.getX() + 5, player.getY() + 1.2, player.getZ() - 2, 0, 0, 0);
        world.addParticle(type, player.getX() + 5.5, player.getY() + 1.2, player.getZ() - 1.5, 0, 0, 0);
        world.addParticle(type, player.getX() + 6, player.getY() + 1.2, player.getZ() - 1, 0, 0, 0);
        world.addParticle(type, player.getX() + 6, player.getY() + 1.2, player.getZ(), 0, 0, 0);
        world.addParticle(type, player.getX() + 6, player.getY() + 1.2, player.getZ() + 1, 0, 0, 0);
        world.addParticle(type, player.getX() + 5.5, player.getY() + 1.2, player.getZ() + 1.5, 0, 0, 0);
        world.addParticle(type, player.getX() + 5, player.getY() + 1.2, player.getZ() + 2, 0, 0, 0);
        world.addParticle(type, player.getX() + 4.5, player.getY() + 1.2, player.getZ() + 2.5, 0, 0, 0);
        world.addParticle(type, player.getX() + 4, player.getY() + 1.2, player.getZ() + 3, 0, 0, 0);
        world.addParticle(type, player.getX() + 3.5, player.getY() + 1.2, player.getZ() + 3.5, 0, 0, 0);
        world.addParticle(type, player.getX() + 3, player.getY() + 1.2, player.getZ() + 4, 0, 0, 0);
        world.addParticle(type, player.getX() + 2.5, player.getY() + 1.2, player.getZ() + 4.5, 0, 0, 0);
        world.addParticle(type, player.getX() + 2, player.getY() + 1.2, player.getZ() + 5, 0, 0, 0);
        world.addParticle(type, player.getX() + 1.5, player.getY() + 1.2, player.getZ() + 5.5, 0, 0, 0);
        world.addParticle(type, player.getX() + 1, player.getY() + 1.2, player.getZ() + 6, 0, 0, 0);
        world.addParticle(type, player.getX(), player.getY() + 1.2, player.getZ() + 6, 0, 0, 0);
        world.addParticle(type, player.getX() - 1, player.getY() + 1.2, player.getZ() + 6, 0, 0, 0);
        world.addParticle(type, player.getX() - 1.5, player.getY() + 1.2, player.getZ() + 5.5, 0, 0, 0);
        world.addParticle(type, player.getX() - 2, player.getY() + 1.2, player.getZ() + 5, 0, 0, 0);
        world.addParticle(type, player.getX() - 2.5, player.getY() + 1.2, player.getZ() + 4.5, 0, 0, 0);
        world.addParticle(type, player.getX() - 3, player.getY() + 1.2, player.getZ() + 4, 0, 0, 0);
        world.addParticle(type, player.getX() - 3.5, player.getY() + 1.2, player.getZ() + 3.5, 0, 0, 0);
        world.addParticle(type, player.getX() - 4, player.getY() + 1.2, player.getZ() + 3, 0, 0, 0);
        world.addParticle(type, player.getX() - 4.5, player.getY() + 1.2, player.getZ() + 2.5, 0, 0, 0);
        world.addParticle(type, player.getX() - 5, player.getY() + 1.2, player.getZ() + 2, 0, 0, 0);
        world.addParticle(type, player.getX() - 5.5, player.getY() + 1.2, player.getZ() + 1.5, 0, 0, 0);
        world.addParticle(type, player.getX() - 6, player.getY() + 1.2, player.getZ() + 1, 0, 0, 0);
        world.addParticle(type, player.getX() - 6, player.getY() + 1.2, player.getZ(), 0, 0, 0);
        world.addParticle(type, player.getX() - 6, player.getY() + 1.2, player.getZ() - 1, 0, 0, 0);
        world.addParticle(type, player.getX() - 5.5, player.getY() + 1.2, player.getZ() - 1.5, 0, 0, 0);
        world.addParticle(type, player.getX() - 5, player.getY() + 1.2, player.getZ() - 2, 0, 0, 0);
        world.addParticle(type, player.getX() - 4.5, player.getY() + 1.2, player.getZ() + -2.5, 0, 0, 0);
        world.addParticle(type, player.getX() - 4, player.getY() + 1.2, player.getZ() - 3, 0, 0, 0);
        world.addParticle(type, player.getX() - 3.5, player.getY() + 1.2, player.getZ() + -3.5, 0, 0, 0);
        world.addParticle(type, player.getX() - 3, player.getY() + 1.2, player.getZ() - 4, 0, 0, 0);
        world.addParticle(type, player.getX() - 2.5, player.getY() + 1.2, player.getZ() - 4.5, 0, 0, 0);
        world.addParticle(type, player.getX() - 2, player.getY() + 1.2, player.getZ() - 5, 0, 0, 0);
        world.addParticle(type, player.getX() - 1.5, player.getY() + 1.2, player.getZ() - 5.5, 0, 0, 0);
    }

    // Protection Circle
    public static boolean doProtectionCircle(PlayerEntity player, World world) {
        List<Entity> entities = world.getEntitiesOfClass(Entity.class,
                new AxisAlignedBB(player.getX() - 6, player.getY() - 6, player.getZ() - 6,
                        player.getX() + 6, player.getY() + 6, player.getZ() + 6), null)
                .stream().sorted(getEntityComparator(player)).collect(Collectors.toList());
        boolean used = false;
        for (Entity entity : entities) {
            if (!(entity instanceof PlayerEntity)) {
                entity.setDeltaMovement(entity.getLookAngle().reverse().multiply(0.3D, 0D, 0.3D));
                used = true;
            }
        }
        protectionCircleTimer++;
        if (protectionCircleTimer % 10 == 0) {
            doOuterRingParticles(ParticleTypes.HAPPY_VILLAGER, player, world);
        }
        return used;
    }

    // Levitation
    public static boolean doLevitation(PlayerEntity player, World world) {
        if (player.getDeltaMovement().y >= 0) {
            EffectInstance effect = player.getEffect(Effects.LEVITATION);
            if (effect != null && effect.isVisible()) {
                player.getCapability(PlayerCapProvider.PLAYER_CAP).ifPresent(cap -> {
                    if (cap.getEffect() == null) {
                        cap.setEffect(effect.getEffect());
                        cap.setEffectDuration(effect.getDuration());
                        cap.setEffectAmplifier(effect.getAmplifier());
                    }
                });
            }

            player.addEffect(LEVITATION);

            for (int a = 0; a < 1; a++) {
                world.addParticle(ParticleTypes.END_ROD, player.getX(), player.getY() - 1,
                        player.getZ(), 0, player.getDeltaMovement().y, 0);
            }
            return true;
        }
        return false;
    }

    // Arrow Rain
    public static boolean doArrowRain(PlayerEntity player, World world) {
        if (world.isClientSide()) {
            if (arrowRainCloudSpawnBoolean)
                doCloudParticles(player, world);
            arrowRainCloudSpawnBoolean = false;

            arrowRainCloudSpawnTimer++;
            if (arrowRainCloudSpawnTimer % 15 == 0) {
                arrowRainCloudSpawnBoolean = true;
                arrowRainCloudSpawnTimer = 0;
            }
        } else {
            arrowRainArrowSpawnTimer++;
            if (arrowRainArrowSpawnTimer % 15 == 0) {
                for (int i = 0; i < 5; i++) {
                    doArrowSpawn(player, world);
                }
                arrowRainArrowSpawnTimer = 0;
            }
        }
        return true;
    }

    public static boolean doArrowSpawn(PlayerEntity player, World world) {
        ArrowEntity arrowEntity = new ArrowEntity(world,
                player.getX() + (world.random.nextDouble() - 0.5D) * player.getBbWidth(),
                player.getY() + 4, player.getZ() + (world.random.nextDouble() - 0.5D) * player.getBbWidth());

        arrowEntity.shootFromRotation(player, player.xRot, player.yRot, 1.0F, 1.0F, 1.0F);
        arrowEntity.setDeltaMovement(MathHelper.cos((float) Math.toRadians(player.yRot + 90)) + (world.random.nextFloat() - 0.5F) * player.getBbWidth(), -0.6, MathHelper.sin((float) Math.toRadians(player.yRot + 90)) + (world.random.nextFloat() - 0.5F) * player.getBbWidth());
        world.addFreshEntity(arrowEntity);
        return true;
    }

    public static void doCloudParticles(PlayerEntity player, World world) {
        for (int i = 0; i < 5; i++) {
            world.addParticle(ParticleTypes.CLOUD, player.getX(), player.getY() + 5, player.getZ(), 0, 0, 0);
            world.addParticle(ParticleTypes.CLOUD, player.getX() + 0.45, player.getY() + 5, player.getZ(), 0, 0, 0);
            world.addParticle(ParticleTypes.CLOUD, player.getX() - 0.45, player.getY() + 5, player.getZ(), 0, 0, 0);
            world.addParticle(ParticleTypes.CLOUD, player.getX(), player.getY() + 5, player.getZ() + 0.45, 0, 0, 0);
            world.addParticle(ParticleTypes.CLOUD, player.getX(), player.getY() + 5, player.getZ() - 0.45, 0, 0, 0);
            world.addParticle(ParticleTypes.CLOUD, player.getX() + 0.45, player.getY() + 5, player.getZ() + 0.45, 0, 0, 0);
            world.addParticle(ParticleTypes.CLOUD, player.getX() - 0.45, player.getY() + 5, player.getZ() - 0.45, 0, 0, 0);
            world.addParticle(ParticleTypes.CLOUD, player.getX() + 0.45, player.getY() + 5, player.getZ() - 0.45, 0, 0, 0);
            world.addParticle(ParticleTypes.CLOUD, player.getX() - 0.45, player.getY() + 5, player.getZ() + 0.45, 0, 0, 0);
        }
    }

    // Healing Circle
    public static boolean doHealingCircle(PlayerEntity player, World world) {
        List<LivingEntity> livingEntities = world.getEntitiesOfClass(LivingEntity.class,
                new AxisAlignedBB(player.getX() - 6, player.getY() - 6, player.getZ() - 6,
                        player.getX() + 6, player.getY() + 6, player.getZ() + 6), null)
                .stream().sorted(getEntityComparator(player)).collect(Collectors.toList());
        healingTimer++;
        boolean used = false;
        if (healingTimer % 10 == 0) {
            doEnchantParticleInterior(player, world);
            doOuterRingParticles(ParticleTypes.HAPPY_VILLAGER, player, world);
        }

        if (healingTimer % 20 == 0) {
            for (LivingEntity livingEntity : livingEntities) {
                doHealingCircleEntityParticle(livingEntity, world);
                used = true;
                if (livingEntity.isInvertedHealAndHarm()) {
                    livingEntity.setLastHurtByPlayer(player);
                    livingEntity.hurt(DamageSource.MAGIC, 1.0F);
                } else if (livingEntity.getHealth() < livingEntity.getMaxHealth()) {
                    livingEntity.heal(1.0F);
                }
            }
            healingTimer = 0;
        }
        return used;
    }

    public static void doHealingCircleEntityParticle(LivingEntity livingEntity, World world) {
        double d0 = (livingEntity.getX() + world.random.nextFloat());
        double d1 = (livingEntity.getY() + world.random.nextFloat());
        double d2 = (livingEntity.getZ() + world.random.nextFloat());
        double d3 = (world.random.nextFloat() - 0.2D) * 0.5D;
        double d4 = (world.random.nextFloat() - 0.2D) * 0.5D;
        double d5 = (world.random.nextFloat() - 0.2D) * 0.5D;
        if (livingEntity.isInvertedHealAndHarm()) {
            for (int i = 0; i < 5; i++) {
                world.addParticle(ParticleTypes.SMOKE, d0, d1, d2, d3, d4, d5);
            }
        } else if (livingEntity.getHealth() < livingEntity.getMaxHealth()) {
            d0 -= 0.5;
            d1 -= 0.3;
            d2 -= 0.5;
            world.addParticle(ParticleTypes.HEART, d0, d1, d2, d3, d4, d5);
        }
    }

    // Speed
    public static boolean doSpeed(PlayerEntity player, World world) {
        ModifiableAttributeInstance speedAttribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speedAttribute != null && speedAttribute.getModifier(speedUUID) == null) {
            speedAttribute.addPermanentModifier(speedModifier);
        }
        return true;
    }

    // Respiration
    public static boolean doRespiration(PlayerEntity player, World world) {
        List<PlayerEntity> players = world.getEntitiesOfClass(PlayerEntity.class,
                new AxisAlignedBB(player.getX() - 10, player.getY() - 4, player.getZ() - 10,
                        player.getX() + 10, player.getY() + 4, player.getZ() + 10), null)
                .stream().sorted(getEntityComparator(player)).collect(Collectors.toList());
        airTimer++;
        boolean used = false;
        for (PlayerEntity p : players) {
            if (p.isUnderWater() && airTimer == 10) {
                p.setAirSupply(p.getAirSupply() + 15);
                if (p.getAirSupply() > p.getMaxAirSupply()) {
                    p.setAirSupply(p.getMaxAirSupply());
                    used = true;
                }
                airTimer = 0;
            }
        }
        return used;
    }

    //(Code is from EvokerEntity$AttackSpellGoal createSpellEntity)
    public static void createFangsEntity(LivingEntity entity, World world, double x, double z, double y, float yaw, int warmup) {
        BlockPos blockpos = new BlockPos(x, y, z);
        boolean flag = false;
        double d0 = 0.0D;
        do {
            BlockPos below = blockpos.below();
            BlockState blockstate = world.getBlockState(below);
            if (blockstate.isFaceSturdy(world, below, Direction.UP)) {
                if (!world.isEmptyBlock(blockpos)) {
                    BlockState state = world.getBlockState(blockpos);
                    VoxelShape voxelshape = state.getCollisionShape(world, blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.below();
        } while (blockpos.getY() >= MathHelper.floor(y + 1) - 1);

        if (flag) {
            world.addFreshEntity(new EvokerFangsEntity(world, x, blockpos.getY() + d0, z, yaw, warmup, entity));
        }
    }

    public static void resetEffects(PlayerEntity playerEntity) {
        arrowRainArrowSpawnTimer = 0;
        arrowRainCloudSpawnTimer = 0;
        healingTimer = 0;
        protectionCircleTimer = 0;
        airTimer = 0;
        ModifiableAttributeInstance speedAttribute = playerEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speedAttribute != null && speedAttribute.getModifier(speedUUID) != null) {
            speedAttribute.removeModifier(speedModifier);
        }
        EffectInstance instance = playerEntity.getEffect(Effects.GLOWING);
        EffectInstance instance2 = playerEntity.getEffect(Effects.LEVITATION);
        EffectInstance instance3 = playerEntity.getEffect(Effects.SLOW_FALLING);

        /* Check if the player has any one of these effects while they're not holding down a spell item, should prevent
        a bug where if you quickly tap the spell item and switch to a different one the infinite effect is applied to the player
        MIGHT have repercussions on other mods that have these effects and are not visible. */

        if (instance != null && !instance.isVisible() && instance.getAmplifier() >= 5) {
            playerEntity.removeEffect(instance.getEffect());
        }
        if (instance2 != null && !instance2.isVisible() && instance2.getAmplifier() >= 5) {
            playerEntity.removeEffect(instance2.getEffect());
        }
        if (instance3 != null && !instance3.isVisible() && instance3.getAmplifier() >= 5) {
            playerEntity.removeEffect(instance3.getEffect());
        }

        // Reapplies the old effect to the player (if applicable)
        playerEntity.getCapability(PlayerCapProvider.PLAYER_CAP).ifPresent(c -> {
            Effect effect = c.getEffect();
            if (effect != null) {
                playerEntity.addEffect(new EffectInstance(effect, c.getEffectDuration(), c.getEffectAmplifier()));
                c.resetEffect();
            }
        });
    }
}
