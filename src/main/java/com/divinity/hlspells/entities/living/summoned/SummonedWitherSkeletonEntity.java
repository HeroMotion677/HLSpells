package com.divinity.hlspells.entities.living.summoned;

import com.divinity.hlspells.entities.Summonable;
import com.divinity.hlspells.entities.goal.AttackedOwnerEnemyGoal;
import com.divinity.hlspells.entities.goal.CopyOwnerTargetGoal;
import com.divinity.hlspells.entities.goal.DecayGoal;
import com.divinity.hlspells.entities.goal.FollowOwnerGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class SummonedWitherSkeletonEntity extends WitherSkeleton implements Summonable {

    protected Player playerOwner;

    public SummonedWitherSkeletonEntity(EntityType<? extends WitherSkeleton> type, Level level) {
        super(type, level);
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

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new DecayGoal(this));
        this.goalSelector.addGoal(1, new FollowOwnerGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 3.0F, 0.7F));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, LivingEntity.class, 8.0F, 0.5F));
        this.targetSelector.addGoal(0, new CopyOwnerTargetGoal(this, true));
        this.targetSelector.addGoal(1, new AttackedOwnerEnemyGoal(this, true));
    }

    @Override protected SoundEvent getAmbientSound() { return SoundEvents.WITHER_SKELETON_AMBIENT; }

    @Override protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) { return SoundEvents.WITHER_SKELETON_AMBIENT;}

    @Override protected SoundEvent getDeathSound() { return SoundEvents.WITHER_SKELETON_DEATH; }

    @Override protected @NotNull SoundEvent getStepSound() { return SoundEvents.WITHER_SKELETON_STEP; }

    @Override
    @NotNull
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean isAlliedTo(@NotNull Entity entity) {
        return entity instanceof Summonable || entity == this.playerOwner;
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource pRandom, @NotNull DifficultyInstance instance) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.NETHERITE_HELMET));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        this.setDropChance(EquipmentSlot.HEAD, 0.0F);
    }

    @Override
    public Player getSummonedOwner() {
        return this.playerOwner;
    }

    @Override
    public void setSummonedOwner(Player owner) {
        this.playerOwner = owner;
    }
}
