package com.divinity.hlspells.entities.projectile;

import com.divinity.hlspells.HLSpells;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.entity.Entity.RemovalReason;

public class AquaBoltEntity extends BaseBoltEntity {

    public AquaBoltEntity(EntityType<? extends AquaBoltEntity> type, Level world) {
        super(type, world, ParticleTypes.RAIN, ParticleTypes.BUBBLE);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        Entity entity1 = this.getOwner();
        LivingEntity livingentity = entity1 instanceof LivingEntity livingEntity ? livingEntity : null;
        if (result.getEntity() == this.getOwner()) return;
        var fireMobsList = HLSpells.CONFIG.fireMobsList.get();
        boolean predicate = false;
        for (String id : fireMobsList) {
            if (id.equals(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()) != null ? BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString() : "")) {
                predicate = true;
                break;
            }
        }
        boolean hasHurt = entity.hurt(new DamageSources(this.level().registryAccess()).mobProjectile(this, livingentity), (this.isUnderWater() || predicate) ? 25.0F : 5.0F);
        if (hasHurt && this.level() instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.BUBBLE_POP, this.getX() - this.random.nextInt(2), this.getY(), this.getZ() - this.random.nextFloat(), 12, 0.2D, 0.2D, 0.2D, 0.0D);
            entity.clearFire();
            if (livingentity != null) EnchantmentHelper.doPostAttackEffects(level, entity, this.damageSources().mobProjectile(this, livingentity));
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        if (this.level() instanceof ServerLevel level) {
            BlockState airState = Blocks.AIR.defaultBlockState();
            for (BlockPos blockPos : BlockPos.withinManhattan(this.blockPosition(), 2, 0, 2)) {
                if (this.level().getBlockState(blockPos).getBlock() == Blocks.FIRE) {
                    this.level().setBlockAndUpdate(blockPos, airState);
                }
            }
            level.sendParticles(ParticleTypes.BUBBLE_POP, this.getX(), this.getY(), this.getZ(), 12, 0.25D, 0.25D, 0.25D, 0D);
            this.playSound(SoundEvents.SHULKER_BULLET_HIT, 1.0F, 1.0F);
            this.remove(RemovalReason.KILLED);
        }
    }
}
