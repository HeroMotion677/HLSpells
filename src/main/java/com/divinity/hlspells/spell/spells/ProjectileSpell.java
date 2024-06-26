package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.entities.projectile.*;
import com.divinity.hlspells.setup.init.ParticlesInit;
import com.divinity.hlspells.setup.init.SoundInit;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import com.divinity.hlspells.util.Util;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;

public class ProjectileSpell<T extends Projectile> extends Spell {
    private final EntityType<T> projectile;
    private double viewVectorOffset;
    private double xOffset;
    private double yOffset;
    private double zOffset;
    private float zRot;
    private float velocity;
    private float inaccuracy;
    private boolean noVerticalMovement;

    public ProjectileSpell(EntityType<T> projectile, SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, boolean treasureOnly, int maxSpellLevel, SimpleParticleType rune) {
        super(type, rarity, tier, marker, displayName, xpCost, treasureOnly, maxSpellLevel, rune);
        this.projectile = projectile;
        this.viewVectorOffset = 1;
        this.xOffset = 0;
        this.yOffset = 2;
        this.zOffset = 0;
        this.zRot = 1.2F;
        this.velocity = 2.5F;
        this.inaccuracy = 1.2F;
        this.noVerticalMovement = false;
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            Entity projectile = this.projectile.create(p.level);

            if (projectile instanceof Projectile trueProjectile) {
                if (trueProjectile instanceof BaseBoltEntity bolt) {
                    bolt.setInitialPosition(p.position());
                    this.velocity = 2.5F;
                }
                if(trueProjectile instanceof WitherSkullEntity bolt){
                    bolt.setInitialPosition(p.position());
                    this.velocity = 2.7F;
                }
                Vec3 viewVector = p.getViewVector(1.0F);
                Vec3 positionVector = new Vec3(p.getX() + (viewVector.x * this.viewVectorOffset) + this.xOffset, p.getY() + this.yOffset, p.getZ() + (viewVector.z * this.viewVectorOffset) + this.zOffset);
                Util.shootSpellRelative(p, trueProjectile, positionVector, this.zRot, this.velocity, this.inaccuracy, this.noVerticalMovement);
                playSound(trueProjectile);
                if(projectile instanceof FlamingBoltEntity){
                    Level world = p.getLevel();
                    double d0 = (projectile.getX());
                    double d1 = (projectile.getY() - 0.2);
                    double d2 = (projectile.getZ());
                    world.addParticle(ParticlesInit.ORANGE_BOLT_BOOM.get(), d0, d1, d2, 0, 0, 0);
                }
                if(projectile instanceof FreezingBoltEntity){
                    Level world = p.getLevel();
                    double d0 = (projectile.getX());
                    double d1 = (projectile.getY() - 0.2);
                    double d2 = (projectile.getZ());
                    world.addParticle(ParticlesInit.BLUE_BOLT_BOOM.get(), d0, d1, d2, 0, 0, 0);
                }
                if(projectile instanceof BaseBoltEntity e && !(e instanceof FreezingBoltEntity || e instanceof FlamingBoltEntity || e instanceof InvisibleTargetingEntity)) {
                    Level world = p.getLevel();
                    double d0 = (projectile.getX());
                    double d1 = (projectile.getY() - 0.2);
                    double d2 = (projectile.getZ());
                    world.addParticle(ParticlesInit.WHITE_BOLT_BOOM.get(), d0, d1, d2, 0, 0, 0);
                }
                if(projectile instanceof PiercingBoltEntity){
                    Level world = p.getLevel();
                    double d0 = (projectile.getX());
                    double d1 = (projectile.getY() - 0.2);
                    double d2 = (projectile.getZ());
                    world.addParticle(ParticlesInit.GREEN_BOLT_BOOM.get(), d0, d1, d2, 0, 0, 0);
                }
                if(projectile instanceof ChorusBoltEntity){
                    Level world = p.getLevel();
                    double d0 = (projectile.getX());
                    double d1 = (projectile.getY() - 0.2);
                    double d2 = (projectile.getZ());
                    world.addParticle(ParticlesInit.PURPLE_BOLT_BOOM.get(), d0, d1, d2, 0, 0, 0);
                }
                if(projectile instanceof WitherSkullEntity){
                    Level world = p.getLevel();
                    double d0 = (projectile.getX());
                    double d1 = (projectile.getY());
                    double d2 = (projectile.getZ());
                    world.addParticle(ParticlesInit.BLACK_BOLT_BOOM.get(), d0, d1, d2, 0, 0, 0);
                }
                if(projectile instanceof FireballEntity){
                    Level world = p.getLevel();
                    double d0 = (projectile.getX());
                    double d1 = (projectile.getY() - 0.2);
                    double d2 = (projectile.getZ());
                    world.addParticle(ParticlesInit.ORANGE_BOLT_BOOM.get(), d0, d1, d2, 0, 0, 0);
                }
                if(projectile instanceof Fireball2Entity){
                    Level world = p.getLevel();
                    double d0 = (projectile.getX());
                    double d1 = (projectile.getY() - 0.2);
                    double d2 = (projectile.getZ());
                    world.addParticle(ParticlesInit.PURPLE_BOLT_BOOM.get(), d0, d1, d2, 0, 0, 0);
                }
                if(projectile instanceof SmartShulkerBolt){
                    Level world = p.getLevel();
                    double d0 = (projectile.getX());
                    double d1 = (projectile.getY() - 0.2);
                    double d2 = (projectile.getZ());
                    world.addParticle(ParticlesInit.WHITE_BOLT_BOOM.get(), d0, d1, d2, 0, 0, 0);
                }
                if(projectile instanceof AquaBoltEntity){
                    Level world = p.getLevel();
                    double d0 = (projectile.getX());
                    double d1 = (projectile.getY() - 0.2);
                    double d2 = (projectile.getZ());
                    world.addParticle(ParticlesInit.BLUE_BOLT_BOOM.get(), d0, d1, d2, 0, 0, 0);
                }
                return true;
            }
            return false;
        };
    }

    public ProjectileSpell<T> viewVectorOffset(double viewVectorOffset) {
        this.viewVectorOffset = viewVectorOffset;
        return this;
    }

    public ProjectileSpell<T> xPosOffset(double xOffset) {
        this.xOffset = xOffset;
        return this;
    }

    public ProjectileSpell<T> yPosOffset(double yOffset) {
        this.yOffset = yOffset;
        return this;
    }

    public ProjectileSpell<T> zPosOffset(double zOffset) {
        this.zOffset = zOffset;
        return this;
    }

    public ProjectileSpell<T> fromZRot(float zRot) {
        this.zRot = zRot;
        return this;
    }

    public ProjectileSpell<T> velocity(float velocity) {
        this.velocity = velocity;
        return this;
    }

    public ProjectileSpell<T> inaccuracy(float inaccuracy) {
        this.inaccuracy = inaccuracy;
        return this;
    }

    public ProjectileSpell<T> verticalMovement(boolean noVerticalMovement) {
        this.noVerticalMovement = noVerticalMovement;
        return this;
    }

    private static void playSound(Projectile projectile) {
        if (projectile instanceof BaseBoltEntity e && !(e instanceof FreezingBoltEntity || e instanceof FlamingBoltEntity || e instanceof InvisibleTargetingEntity || e instanceof FireballEntity || e instanceof Fireball2Entity)) {
            projectile.playSound(SoundInit.CAST_BOLT.get(), 0.3F, 0.7F);
        }
        else if (projectile instanceof FreezingBoltEntity) {
            projectile.playSound(SoundInit.CAST_ICE.get(), 0.4F, 0.7F);
        }
        else if (projectile instanceof FlamingBoltEntity) {
            projectile.playSound(SoundInit.CAST_FLAME.get(), 0.5F, 0.7F);
        }
        else if (projectile instanceof FireballEntity) {
            projectile.playSound(SoundInit.CAST_FLAME.get(), 0.5F, 0.7F);
        }
        else if (projectile instanceof Fireball2Entity) {
            projectile.playSound(SoundInit.CAST_FLAME.get(), 0.5F, 0.7F);
        }
        else if (projectile instanceof WitherSkull) {
            projectile.playSound(SoundInit.CAST_NECROMANCY.get(), 0.6F, 0.7F);
        }else{
            projectile.playSound(SoundInit.CAST_BOLT.get(), 0.3F, 0.7F);
        }
    }

    @Nullable
    @Override
    public Spell getUpgrade() {
        if(this.getTrueDisplayName().equals("Fire Ball")){
            return SpellInit.FIRE_BALL_II.get();
        }
        else if(this.getTrueDisplayName().equals("Piercing Bolt")){
            return SpellInit.PIERCING_BOLT_II.get();
        } else{
            return null;
        }
    }
}
