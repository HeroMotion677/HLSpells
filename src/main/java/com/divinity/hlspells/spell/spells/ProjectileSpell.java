package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.entities.projectile.BaseBoltEntity;
import com.divinity.hlspells.entities.projectile.FlamingBoltEntity;
import com.divinity.hlspells.entities.projectile.FreezingBoltEntity;
import com.divinity.hlspells.entities.projectile.InvisibleTargetingEntity;
import com.divinity.hlspells.setup.init.SoundInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import com.divinity.hlspells.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.phys.Vec3;

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

    public ProjectileSpell(EntityType<T> projectile, SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, boolean treasureOnly, int maxSpellLevel) {
        super(type, rarity, tier, marker, displayName, xpCost, treasureOnly, maxSpellLevel);
        this.projectile = projectile;
        this.viewVectorOffset = 1;
        this.xOffset = 0;
        this.yOffset = 0;
        this.zOffset = 0;
        this.zRot = 1.2F;
        this.velocity = 1.2F;
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
                    this.velocity = 1.6F;
                }
                Vec3 viewVector = p.getViewVector(1.0F);
                Vec3 positionVector = new Vec3(p.getX() + (viewVector.x * this.viewVectorOffset) + this.xOffset, p.getY() + this.yOffset, p.getZ() + (viewVector.z * this.viewVectorOffset) + this.zOffset);
                Util.shootSpellRelative(p, trueProjectile, positionVector, this.zRot, this.velocity, this.inaccuracy, this.noVerticalMovement);
                playSound(trueProjectile);
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
        if (projectile instanceof BaseBoltEntity e && !(e instanceof FreezingBoltEntity || e instanceof FlamingBoltEntity || e instanceof InvisibleTargetingEntity)) {
            projectile.playSound(SoundInit.CAST_BOLT.get(), 0.7F, 0.7F);
        }
        else if (projectile instanceof FreezingBoltEntity) {
            projectile.playSound(SoundInit.CAST_ICE.get(), 0.7F, 0.7F);
        }
        else if (projectile instanceof FlamingBoltEntity) {
            projectile.playSound(SoundInit.CAST_FLAME.get(), 0.7F, 0.7F);
        }
        else if (projectile instanceof WitherSkull) {
            projectile.playSound(SoundInit.CAST_NECROMANCY.get(), 0.7F, 0.7F);
        }
    }
}
