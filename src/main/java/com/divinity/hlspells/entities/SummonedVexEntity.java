package com.divinity.hlspells.entities;

import com.divinity.hlspells.spells.SpellActions;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.Level;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;


import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraftforge.network.NetworkHooks;

public class SummonedVexEntity extends Vex {
    protected Player playerOwner;

    public SummonedVexEntity(EntityType<? extends Vex> entityType, Level world) {
        super(entityType, world);
        this.moveControl = new SummonedVexEntity.MoveHelperController(this);
        this.xpReward = 0;
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        return entity instanceof SummonedVexEntity || entity == this.playerOwner;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.playerOwner != null && this.isAlive()) {
            this.setBoundOrigin(this.playerOwner.blockPosition());
            if (this.distanceTo(this.playerOwner) > 12.0D && this.distanceTo(this.playerOwner) <= 50.0D) {
                BlockPos ownerPos = this.playerOwner.blockPosition().offset(-2 + this.playerOwner.level.random.nextInt(5),
                        1, -2 + this.playerOwner.level.random.nextInt(5));
                this.moveControl.setWantedPosition(ownerPos.getX(), ownerPos.getY(), ownerPos.getZ(), 0.75D);
            }
            else if (this.distanceTo(this.playerOwner) > 50D) {
                BlockPos ownerPos = this.playerOwner.blockPosition().offset(-2 + this.playerOwner.level.random.nextInt(5),
                        1, -2 + this.playerOwner.level.random.nextInt(5));
                this.teleportTo(ownerPos.getX(), ownerPos.getY(), ownerPos.getZ());
                this.moveControl.setWantedPosition(ownerPos.getX(), ownerPos.getY(), ownerPos.getZ(), 0.6D);
            }
        } else this.kill();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        if (nbt.contains("Owner")) {
            this.setSummonedOwner(this.level.getPlayerByUUID(nbt.getUUID("Owner")));
        }
        super.readAdditionalSaveData(nbt);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        if (this.getSummonedOwner() != null) {
            nbt.putUUID("Owner", this.getSummonedOwner().getUUID());
        }
        super.addAdditionalSaveData(nbt);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(4, new ChargeAttackGoal());
        this.goalSelector.addGoal(8, new MoveRandomGoal());
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(0, new CopyOwnerTargetGoal(this));
        this.targetSelector.addGoal(1, new AttackedOwnerEnemyGoal(this));
    }

    public Player getSummonedOwner() {
        return this.playerOwner;
    }

    public void setSummonedOwner(Player owner) {
        this.playerOwner = owner;
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance instance) {
        if (this.random.nextInt(2) == 0) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        } else {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
        }
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    class ChargeAttackGoal extends Goal {
        public ChargeAttackGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            if (SummonedVexEntity.this.getTarget() != null && !SummonedVexEntity.this.getMoveControl().hasWanted() && SummonedVexEntity.this.random.nextInt(2) == 0) {
                return SummonedVexEntity.this.distanceToSqr(SummonedVexEntity.this.getTarget()) > 2.0D;
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return SummonedVexEntity.this.getMoveControl().hasWanted() && SummonedVexEntity.this.isCharging() && SummonedVexEntity.this.getTarget() != null && SummonedVexEntity.this.getTarget().isAlive();
        }

        @Override
        public void start() {
            LivingEntity livingentity = SummonedVexEntity.this.getTarget();
            if (livingentity != null) {
                Vec3 vector3d = livingentity.getEyePosition(1.0F);
                SummonedVexEntity.this.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 1.0D);
                SummonedVexEntity.this.setIsCharging(true);
                SummonedVexEntity.this.playSound(SoundEvents.VEX_CHARGE, 1.0F, 1.0F);
            }
        }

        @Override
        public void stop() {
            SummonedVexEntity.this.setIsCharging(false);
        }

        @Override
        public void tick() {
            LivingEntity livingentity = SummonedVexEntity.this.getTarget();
            if (livingentity != null) {
                if (SummonedVexEntity.this.getBoundingBox().inflate(0.5).intersects(livingentity.getBoundingBox())) {
                    SummonedVexEntity.this.doHurtTarget(livingentity);
                    SummonedVexEntity.this.setIsCharging(false);
                }
                else {
                    double d0 = SummonedVexEntity.this.distanceToSqr(livingentity);
                    if (d0 < 9.0D) {
                        Vec3 vector3d = livingentity.getEyePosition(1.0F);
                        SummonedVexEntity.this.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 1.0D);
                    }
                }
            }
        }
    }

    class CopyOwnerTargetGoal extends TargetGoal {
        private final TargetingConditions copyOwnerTargeting = (new TargetingConditions(true)).ignoreInvisibilityTesting();

        public CopyOwnerTargetGoal(PathfinderMob mob) {
            super(mob, false);
        }

        @Override
        public boolean canUse() {
            return SummonedVexEntity.this.getTarget() == null && SummonedVexEntity.this.playerOwner != null && SummonedVexEntity.this.playerOwner.getLastHurtMob() != null && this.canAttack(SummonedVexEntity.this.playerOwner.getLastHurtMob(), this.copyOwnerTargeting) && !(SummonedVexEntity.this.playerOwner.getLastHurtMob() instanceof SummonedVexEntity) && !(SummonedVexEntity.this.playerOwner.getLastHurtMob() instanceof Player);
        }

        @Override
        public void start() {
            SummonedVexEntity.this.setTarget(SummonedVexEntity.this.playerOwner.getLastHurtMob());
            super.start();
        }
    }

    class AttackedOwnerEnemyGoal extends TargetGoal {
        private final TargetingConditions attackedOwnerTargeting = (new TargetingConditions(true)).ignoreInvisibilityTesting();
        private Mob entity;

        public AttackedOwnerEnemyGoal(PathfinderMob mob) {
            super(mob, false);
        }

        @Override
        public boolean canUse() {
            int scanRange = 15;
            List<Mob> mobEntities = SummonedVexEntity.this.level.getEntitiesOfClass(Mob.class,
                            new AABB(SummonedVexEntity.this.getX() - scanRange, SummonedVexEntity.this.getY() - scanRange, SummonedVexEntity.this.getZ() - scanRange,
                            SummonedVexEntity.this.getX() + scanRange, SummonedVexEntity.this.getY() + scanRange, SummonedVexEntity.this.getZ() + scanRange))
                    .stream().sorted(SpellActions.getEntityComparator(SummonedVexEntity.this.playerOwner)).collect(Collectors.toList());
            for (Mob mob : mobEntities) {
                if (mob != null && !(mob instanceof SummonedVexEntity)) {
                    if (mob.getTarget() == SummonedVexEntity.this.playerOwner) {
                        entity = mob;
                        return SummonedVexEntity.this.getTarget() == null && SummonedVexEntity.this.playerOwner != null && this.canAttack(mob, this.attackedOwnerTargeting);
                    }
                }
            }
            return false;
        }

        @Override
        public void start() {
            SummonedVexEntity.this.setTarget(entity);
            super.start();
        }
    }

    class MoveHelperController extends MoveControl {
        public MoveHelperController(Vex vexEntity) {
            super(vexEntity);
        }

        @Override
        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                Vec3 vector3d = new Vec3(this.wantedX - SummonedVexEntity.this.getX(), this.wantedY - SummonedVexEntity.this.getY(), this.wantedZ - SummonedVexEntity.this.getZ());
                double d0 = vector3d.length();
                if (d0 < SummonedVexEntity.this.getBoundingBox().getSize()) {
                    this.operation = MoveControl.Operation.WAIT;
                    SummonedVexEntity.this.setDeltaMovement(SummonedVexEntity.this.getDeltaMovement().scale(0.5D));
                } else {
                    SummonedVexEntity.this.setDeltaMovement(SummonedVexEntity.this.getDeltaMovement().add(vector3d.scale(this.speedModifier * 0.05D / d0)));

                    if (SummonedVexEntity.this.getTarget() == null) {
                        Vec3 vector3d1 = SummonedVexEntity.this.getDeltaMovement();
                        SummonedVexEntity.this.yRot = -((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180F / (float) Math.PI);
                    } else {
                        double d2 = SummonedVexEntity.this.getTarget().getX() - SummonedVexEntity.this.getX();
                        double d1 = SummonedVexEntity.this.getTarget().getZ() - SummonedVexEntity.this.getZ();
                        SummonedVexEntity.this.yRot = -((float) Mth.atan2(d2, d1)) * (180F / (float) Math.PI);
                    }
                    SummonedVexEntity.this.yBodyRot = SummonedVexEntity.this.yRot;
                }
            }
        }
    }

    class MoveRandomGoal extends Goal {
        public MoveRandomGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return !SummonedVexEntity.this.getMoveControl().hasWanted() && SummonedVexEntity.this.random.nextInt(7) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void tick() {
            BlockPos blockpos = SummonedVexEntity.this.getBoundOrigin();
            if (blockpos == null) {
                blockpos = SummonedVexEntity.this.playerOwner.blockPosition();
            }

            for (int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.offset(SummonedVexEntity.this.random.nextInt(15) - 7, SummonedVexEntity.this.random.nextInt(11) - 5, SummonedVexEntity.this.random.nextInt(15) - 7);
                if (SummonedVexEntity.this.level.isEmptyBlock(blockpos1)) {
                    SummonedVexEntity.this.moveControl.setWantedPosition(blockpos1.getX() + 0.5D, blockpos1.getY() + 0.5D, blockpos1.getZ() + 0.5D, 0.25D);
                    if (SummonedVexEntity.this.getTarget() == null) {
                        SummonedVexEntity.this.getLookControl().setLookAt(blockpos1.getX() + 0.5D, blockpos1.getY() + 0.5D, blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }
        }
    }
}
