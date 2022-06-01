package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class KnockbackBoltSpell extends Spell {

    public KnockbackBoltSpell(String displayName, int xpCost, boolean treasureOnly) {
        super(SpellAttributes.Type.CAST, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.COMBAT, displayName, xpCost, treasureOnly);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            ShulkerBullet entity = new ShulkerBullet(EntityType.SHULKER_BULLET, p.level) {

                @Override
                public void selectNextMoveDirection(@Nullable Direction.Axis axis) {}

                @Override
                public void tick() {
                    super.tick();
                    if (this.getOwner() != null && this.distanceTo(this.getOwner()) >= 40) {
                        this.remove(RemovalReason.KILLED);
                    }
                }

                @Override
                @NotNull
                public Packet<?> getAddEntityPacket() {
                    return NetworkHooks.getEntitySpawningPacket(this);
                }

                @Override
                public void onHitEntity(EntityHitResult result) {
                    Entity entity = result.getEntity();
                    Entity entity1 = this.getOwner();
                    LivingEntity livingentity = entity1 instanceof LivingEntity ? (LivingEntity) entity1 : null;

                    if (result.getEntity() == this.getOwner()) {
                        return;
                    }

                    boolean flag = entity.hurt(DamageSource.indirectMobAttack(this, livingentity).setProjectile(), 0.0F);
                    if (flag) {
                        entity.setDeltaMovement(this.getLookAngle().reverse().multiply(5.0D, 0, 5.0D));
                        this.remove(RemovalReason.KILLED);
                    }
                }
            };
            entity.setNoGravity(true);
            entity.setPos(p.getX() + p.getViewVector(1.0F).x, p.getY() + 1.35, p.getZ() + p.getViewVector(1.0F).z);
            entity.shootFromRotation(p, p.xRot, p.yRot, 1.3F, 1.3F, 1.3F);
            p.level.addFreshEntity(entity);
            return true;
        };
    }
}
