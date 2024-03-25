package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.entities.projectile.*;
import com.divinity.hlspells.setup.init.EntityInit;
import com.divinity.hlspells.setup.init.SoundInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import com.divinity.hlspells.util.Util;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class PiercingBoltIISpell extends Spell {

    public PiercingBoltIISpell(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, boolean treasureOnly, int maxSpellLevel, RegistryObject<SimpleParticleType> rune) {
        super(type, rarity, tier, marker, displayName, xpCost, treasureOnly, maxSpellLevel, rune);
    }

    float viewVectorOffset = 1;
    float xOffset = 0;
    float yOffset = 1.35f;
    float zOffset = 0;
    float zRot = 1.2F;
    float velocity = 1.2F;
    float inaccuracy = 1.2F;
    boolean noVerticalMovement = false;

    @Override
    protected SpellConsumer<Player> getAction() {
        return p -> {
            Projectile projectile = new PiercingBoltEntity(EntityInit.PIERCING_BOLT_ENTITY.get(), p.level, true);
            BaseBoltEntity bolt = (BaseBoltEntity) projectile;
            bolt.setInitialPosition(p.position());
            this.velocity = 1.6F;
            Vec3 viewVector = p.getViewVector(1.0F);
            Vec3 positionVector = new Vec3(p.getX() + (viewVector.x * this.viewVectorOffset) + this.xOffset, p.getY() + this.yOffset, p.getZ() + (viewVector.z * this.viewVectorOffset) + this.zOffset);
            Util.shootSpellRelative(p, bolt, positionVector, this.zRot, this.velocity, this.inaccuracy, this.noVerticalMovement);
            playSound(bolt);
            return true;
        };
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
