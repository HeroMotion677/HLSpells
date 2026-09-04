package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.entities.projectile.*;
import com.divinity.hlspells.setup.init.EntityInit;
import com.divinity.hlspells.setup.init.ParticlesInit;
import com.divinity.hlspells.setup.init.SoundInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import com.divinity.hlspells.util.Util;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PiercingBoltIISpell extends Spell {

    public PiercingBoltIISpell(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, boolean treasureOnly, int maxSpellLevel, SimpleParticleType rune) {
        super(type, rarity, tier, marker, displayName, xpCost, treasureOnly, maxSpellLevel, rune);
    }

    float viewVectorOffset = 0F;
    float xOffset = 0;
    float yOffset = 0f;
    float zOffset = 0;
    float zRot = 1.2F;
    float velocity = 3.7F;
    float inaccuracy = 1.1F;
    boolean noVerticalMovement = false;

    @Override
    protected SpellConsumer<Player> getAction() {
        return p -> {
            Projectile projectile = new PiercingBoltEntity(EntityInit.PIERCING_BOLT_ENTITY.get(), p.level(), true);
            BaseBoltEntity bolt = (BaseBoltEntity) projectile;
            bolt.setInitialPosition(p.position());
            this.velocity = 3.7F;
            Vec3 viewVector = p.getViewVector(1F);
            Vec3 positionVector = new Vec3(p.getX() + (viewVector.x * this.viewVectorOffset) + this.xOffset, p.getEyeY() - 0.1 + this.viewVectorOffset + this.yOffset, p.getZ() + (viewVector.z * this.viewVectorOffset) + this.zOffset);
            Util.shootSpellRelative(p, bolt, positionVector, this.zRot, this.velocity, this.inaccuracy, this.noVerticalMovement);
            playSound(bolt);
            if(projectile instanceof PiercingBoltEntity){
                Level world = p.level();
                double d0 = (projectile.getX() + (viewVector.x * 1F));
                double d1 = (projectile.getY() + (viewVector.y * 1F));
                double d2 = (projectile.getZ() + (viewVector.z * 1F));
                world.addParticle(ParticlesInit.GREEN_BOLT_BOOM.get(), d0, d1, d2, 0, 0, 0);
            }
            return true;
        };
    }

    private static void playSound(Projectile projectile) {
        if (projectile instanceof BaseBoltEntity e && !(e instanceof FreezingBoltEntity || e instanceof FlamingBoltEntity || e instanceof InvisibleTargetingEntity || e instanceof FireballEntity || e instanceof Fireball2Entity)) {
            projectile.playSound(SoundInit.CAST_SOUND.get(), 0.6F, 0.9F);

        }
        else if (projectile instanceof FreezingBoltEntity) {
            projectile.playSound(SoundInit.CAST_ICE.get(), 0.4F, 0.7F);
            projectile.playSound(SoundInit.CAST_SOUND.get(), 0.6F, 0.9F);

        }
        else if (projectile instanceof FlamingBoltEntity) {
            projectile.playSound(SoundInit.CAST_FLAME.get(), 0.5F, 0.7F);
            projectile.playSound(SoundInit.CAST_SOUND.get(), 0.6F, 0.9F);

        }
        else if (projectile instanceof FireballEntity) {
            projectile.playSound(SoundInit.CAST_FLAME.get(), 0.5F, 0.7F);
        }
        else if (projectile instanceof Fireball2Entity) {
            projectile.playSound(SoundInit.CAST_FLAME.get(), 0.5F, 0.7F);
        }
        else if (projectile instanceof WitherSkull) {
            projectile.playSound(SoundInit.CAST_NECROMANCY.get(), 0.5F, 0.7F);
            projectile.playSound(SoundInit.CAST_SOUND.get(), 0.4F, 0.9F);

        }else{
            projectile.playSound(SoundInit.CAST_SOUND.get(), 0.6F, 0.9F);
        }
    }
}
