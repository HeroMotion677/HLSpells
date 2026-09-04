package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.entities.projectile.MysticBoltEntity;
import com.divinity.hlspells.entities.projectile.SmartBoltEntity;
import com.divinity.hlspells.entities.projectile.SmartShulkerBolt;
import com.divinity.hlspells.setup.init.EntityInit;
import com.divinity.hlspells.setup.init.ParticlesInit;
import com.divinity.hlspells.setup.init.SoundInit;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import com.divinity.hlspells.util.Util;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BoltII extends Spell {

    public BoltII(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, boolean treasureOnly, int maxSpellLevel, SimpleParticleType rune) {
        super(type, rarity, tier, marker, displayName, xpCost, true, maxSpellLevel, rune);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            if (Util.rayTrace(p.level(), p, 100D) != null && p.isShiftKeyDown()) {
                Entity entity = Util.rayTrace(p.level(), p, 100D);
                if (entity != null) {
                    Vec3 viewVector = p.getViewVector(1.0F);
                    SmartBoltEntity smartBullet = new SmartBoltEntity(EntityInit.SMART_BOLT_ENTITY.get(), p.level());
                    smartBullet.setPos(p.getX() + p.getViewVector(1.0F).x, p.getEyeY() - 0.1 + p.getViewVector(1.0F).y, p.getZ() + p.getViewVector(1.0F).z);
                    smartBullet.shootFromRotation(p, p.getXRot(), p.getYRot(), 0.0F, 2.7F, 1.2F);
                    p.level().addFreshEntity(smartBullet);

                    Level world = p.level();
                    double d0 = (p.getX() + (viewVector.x));
                    double d1 = (p.getEyeY() + (viewVector.y));
                    double d2 = (p.getZ() + (viewVector.z));
                    world.addParticle(ParticlesInit.GREEN_BOLT_BOOM.get(), d0, d1, d2, 0, 0, 0);
                }
            }
            else {
                MysticBoltEntity dumbBullet = new MysticBoltEntity(EntityInit.MYSTIC_BOLT_ENTITY.get(), p.level()) {

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
                        if (this.getOwner() != null && this.distanceTo(this.getOwner()) >= 100) {
                            this.remove(RemovalReason.KILLED);
                        }
                    }

                    @Override
                    public void onHitEntity(EntityHitResult result) {
                        Entity entity = result.getEntity();
                        if (!(entity instanceof MysticBoltEntity)) {
                            Entity entity1 = this.getOwner();
                            LivingEntity livingentity = entity1 instanceof LivingEntity entity2 ? entity2 : null;
                            if (result.getEntity() == this.getOwner()) return;
                            boolean flag = entity.hurt(new DamageSources(this.level().registryAccess()).mobProjectile(this, livingentity), 15F);
                            if (flag && this.level() instanceof ServerLevel serverLevel) {
                                if (livingentity != null) EnchantmentHelper.doPostAttackEffects(serverLevel, entity, this.damageSources().mobProjectile(this, livingentity));
                                this.remove(RemovalReason.KILLED);
                            }
                        }
                    }
                };

                Vec3 viewVector = p.getViewVector(1.0F);

                dumbBullet.setNoGravity(true);
                dumbBullet.setOwner(p);
                dumbBullet.setPos(p.getX() + p.getViewVector(1.0F).x, p.getEyeY() - 0.1 + p.getViewVector(1.0F).y, p.getZ() + p.getViewVector(1.0F).z);
                dumbBullet.shootFromRotation(p, p.xRot, p.yRot, 1.3F, 2.7F, 1.3F);
                p.level().addFreshEntity(dumbBullet);

                Level world = p.level();
                double d0 = (p.getX() + (viewVector.x));
                double d1 = (p.getEyeY() + (viewVector.y));
                double d2 = (p.getZ() + (viewVector.z));
                world.addParticle(ParticlesInit.GREEN_BOLT_BOOM.get(), d0, d1, d2, 0, 0, 0);


            }
            return true;
        };
    }

    @Override
    public SoundEvent getSpellSound() {
        return SoundInit.CAST_BOLT.get();
    }

 /*   @Nullable
    @Override
    public Spell getUpgradeableSpellPath() {
        return SpellInit.BOLT.get();
    }*/
}
