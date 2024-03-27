package com.divinity.hlspells.entities.living.summoned;

import com.divinity.hlspells.entities.Summonable;
import com.divinity.hlspells.entities.goal.AttackedOwnerEnemyGoal;
import com.divinity.hlspells.entities.goal.CopyOwnerTargetGoal;
import com.divinity.hlspells.entities.goal.FollowOwnerGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class SummonedVexEntity extends Vex implements Summonable {

    protected Player playerOwner;

    public SummonedVexEntity(EntityType<? extends Vex> entityType, Level world) {
        super(entityType, world);
        this.moveControl = new SummonedVexEntity.MoveHelperController(this);
        this.xpReward = 0;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        if (nbt.contains("Owner")) this.setSummonedOwner(this.level.getPlayerByUUID(nbt.getUUID("Owner")));
        super.readAdditionalSaveData(nbt);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        if (this.playerOwner != null) nbt.putUUID("Owner", this.playerOwner.getUUID());
        super.addAdditionalSaveData(nbt);
    }

    @Override @NotNull public Packet<?> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }

    @Override
    public boolean isAlliedTo(@NotNull Entity entity) {
        return entity instanceof Summonable || entity == this.playerOwner;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new FollowOwnerGoal(this));
        this.goalSelector.addGoal(2, new ChargeAttackGoal());
        this.goalSelector.addGoal(3, new MoveRandomGoal());
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 3.0F, 0.7F));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, LivingEntity.class, 8.0F, 0.5F));
        this.targetSelector.addGoal(0, new CopyOwnerTargetGoal(this, false));
        this.targetSelector.addGoal(1, new AttackedOwnerEnemyGoal(this, false));
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource pRandom, @NotNull DifficultyInstance instance) {
        if (this.random.nextInt(2) == 0) this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        else this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
    }

    @Override
    public Player getSummonedOwner() {
        return this.playerOwner;
    }

    @Override
    public void setSummonedOwner(Player owner) {
        this.playerOwner = owner;
    }

    class ChargeAttackGoal extends Goal {

        public ChargeAttackGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            if (SummonedVexEntity.this.getTarget() != null && !SummonedVexEntity.this.getMoveControl().hasWanted() && SummonedVexEntity.this.random.nextInt(2) == 0) {
                return SummonedVexEntity.this.distanceToSqr(SummonedVexEntity.this.getTarget()) > 2.0D;
            }
            return false;
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

    class MoveHelperController extends MoveControl {

        public MoveHelperController(Vex vexEntity) {
            super(vexEntity);
        }

        @Override
        @SuppressWarnings("all")
        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                Vec3 vector3d = new Vec3(this.wantedX - SummonedVexEntity.this.getX(), this.wantedY - SummonedVexEntity.this.getY(), this.wantedZ - SummonedVexEntity.this.getZ());
                double d0 = vector3d.length();
                if (d0 < SummonedVexEntity.this.getBoundingBox().getSize()) {
                    this.operation = MoveControl.Operation.WAIT;
                    SummonedVexEntity.this.setDeltaMovement(SummonedVexEntity.this.getDeltaMovement().scale(0.5D));
                }
                else {
                    SummonedVexEntity.this.setDeltaMovement(SummonedVexEntity.this.getDeltaMovement().add(vector3d.scale(this.speedModifier * 0.05D / d0)));
                    if (SummonedVexEntity.this.getTarget() == null) {
                        Vec3 vector3d1 = SummonedVexEntity.this.getDeltaMovement();
                        SummonedVexEntity.this.yRot = -((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180F / (float) Math.PI);
                    }
                    else {
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
