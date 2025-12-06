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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class BreathSpell<T extends Projectile> extends Spell {
    private final EntityType<T> projectile;
    private double viewVectorOffset;
    private double xOffset;
    private double yOffset;
    private double zOffset;
    private float zRot;
    private float velocity;
    private float inaccuracy;
    private boolean noVerticalMovement;

    public BreathSpell(EntityType<T> projectile, SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, boolean treasureOnly, int tickDelay, int maxSpellLevel, SimpleParticleType rune) {
        super(type, rarity, tier, marker, displayName, xpCost, tickDelay, treasureOnly, maxSpellLevel, rune);
        this.projectile = projectile;
        this.viewVectorOffset = 0;
        this.xOffset = 0;
        this.yOffset = 0;
        this.zOffset = 0;
        this.zRot = 1.2F;
        this.velocity = 2.5F;
        this.inaccuracy = 1.1F;
        this.noVerticalMovement = false;
        Vec3 initialPosition;
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            Entity projectile = this.projectile.create(p.level());

            if (projectile instanceof Projectile trueProjectile) {
                if (trueProjectile instanceof BaseBreathEntity bolt) {
                    bolt.setInitialPosition(p.position());
                    this.velocity = 1.7F;
                }

                Vec3 viewVector = p.getViewVector(1.0F);
                Vec3 positionVector = new Vec3(p.getX() + (viewVector.x * this.viewVectorOffset) + this.xOffset, p.getEyeY() - 0.1 + this.viewVectorOffset + this.yOffset, p.getZ() + (viewVector.z * this.viewVectorOffset) + this.zOffset);
                Util.shootSpellRelative(p, trueProjectile, positionVector, this.zRot, this.velocity, this.inaccuracy, this.noVerticalMovement);

                if(projectile instanceof FlamingBreathEntity){
                    Level world = p.level();
                    double d0 = (projectile.getX() + (viewVector.x * 1F));
                    double d1 = (projectile.getY() + (viewVector.y * 1F));
                    double d2 = (projectile.getZ() + (viewVector.z * 1F));
                    world.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0, 0, 0);
                }

                return true;
            }
            return false;
        };
    }

    public BreathSpell<T> viewVectorOffset(double viewVectorOffset) {
        this.viewVectorOffset = viewVectorOffset;
        return this;
    }

    public BreathSpell<T> xPosOffset(double xOffset) {
        this.xOffset = xOffset;
        return this;
    }

    public BreathSpell<T> yPosOffset(double yOffset) {
        this.yOffset = yOffset;
        return this;
    }

    public BreathSpell<T> zPosOffset(double zOffset) {
        this.zOffset = zOffset;
        return this;
    }

    public BreathSpell<T> fromZRot(float zRot) {
        this.zRot = zRot;
        return this;
    }

    public BreathSpell<T> velocity(float velocity) {
        this.velocity = velocity;
        return this;
    }

    public BreathSpell<T> inaccuracy(float inaccuracy) {
        this.inaccuracy = inaccuracy;
        return this;
    }

    public BreathSpell<T> verticalMovement(boolean noVerticalMovement) {
        this.noVerticalMovement = noVerticalMovement;
        return this;
    }

  /*  private static void playSound(Projectile projectile) {
        if (projectile instanceof FlamingBreathEntity) {
            projectile.playSound(SoundEvents.FIRE_AMBIENT, 0.5F, 0.7F);
            cap.setSpellSoundBuffer(23);
        }

    }*/

}
