package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.entities.projectile.SmartShulkerBolt;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import com.divinity.hlspells.util.Util;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BoltSpell extends Spell {

    public BoltSpell(String displayName, int xpCost, boolean treasureOnly) {
        super(SpellAttributes.Type.CAST, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.COMBAT, displayName, xpCost, treasureOnly);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            if (Util.rayTrace(p.level, p, 25D) != null && p.isShiftKeyDown()) {
                Entity entity = Util.rayTrace(p.level, p, 25D);
                if (entity != null) {
                    ShulkerBullet smartBullet = new SmartShulkerBolt(p.level, p, entity, p.getDirection().getAxis());
                    smartBullet.setPos(p.getX() + p.getViewVector(1.0F).x, p.getY() + 1.35, p.getZ() + p.getViewVector(1.0F).z);
                    p.level.addFreshEntity(smartBullet);
                }
            }
            else {
                ShulkerBullet dumbBullet = new ShulkerBullet(EntityType.SHULKER_BULLET, p.level) {
                    @Override
                    public void selectNextMoveDirection(@Nullable Direction.Axis axis) {}

                    @Override
                    public void onHit(@NotNull HitResult result) {
                        if (result instanceof EntityHitResult entityHitResult) {
                            this.onHitEntity(entityHitResult);
                        }
                        if (result instanceof BlockHitResult blockHitResult) {
                            this.onHitBlock(blockHitResult);
                        }
                    }

                    @Override
                    public void tick() {
                        super.tick();
                        if (this.getOwner() != null && this.distanceTo(this.getOwner()) >= 40) {
                            this.remove(RemovalReason.KILLED);
                        }
                    }

                    @Override
                    @NotNull
                    public Packet<?> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }

                    @Override
                    public void onHitEntity(EntityHitResult result) {
                        Entity entity = result.getEntity();
                        Entity entity1 = this.getOwner();
                        LivingEntity livingentity = entity1 instanceof LivingEntity entity2 ? entity2 : null;
                        if (result.getEntity() == this.getOwner()) return;
                        boolean flag = entity.hurt(DamageSource.indirectMobAttack(this, livingentity).setProjectile(), 8.0F);
                        if (flag) {
                            if (livingentity != null) this.doEnchantDamageEffects(livingentity, entity);
                            this.remove(RemovalReason.KILLED);
                        }
                    }
                };
                dumbBullet.setNoGravity(true);
                dumbBullet.setOwner(p);
                dumbBullet.setPos(p.getX() + p.getViewVector(1.0F).x, p.getY() + 1.35, p.getZ() + p.getViewVector(1.0F).z);
                dumbBullet.shootFromRotation(p, p.xRot, p.yRot, 1.3F, 1.3F, 1.3F);
                p.level.addFreshEntity(dumbBullet);
            }
            return true;
        };
    }
}
