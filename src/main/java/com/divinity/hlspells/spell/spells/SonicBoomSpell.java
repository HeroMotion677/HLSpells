package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.entities.projectile.*;
import com.divinity.hlspells.setup.init.EntityInit;
import com.divinity.hlspells.setup.init.SoundInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import com.divinity.hlspells.util.Util;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.warden.SonicBoom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

public class SonicBoomSpell extends Spell {

    float viewVectorOffset = 1;
    float xOffset = 0;
    float yOffset = 1.35f;
    float zOffset = 0;
    float zRot = 1.2F;
    float velocity = 1.2F;
    float inaccuracy = 1.2F;
    boolean noVerticalMovement = false;

    public SonicBoomSpell(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, boolean treasureOnly, int maxSpellLevel, SimpleParticleType rune) {
        super(type, rarity, tier, marker, displayName, xpCost, treasureOnly, maxSpellLevel, rune);
    }


    @Override
    protected SpellConsumer<Player> getAction() {
        return p -> {
            var entity = Util.rayTrace(p.level, p, 150D);
            if(entity !=null){
                Vec3 vec3 = p.position().add(0.0D, (double)1.6F, 0.0D);
                Vec3 vec31 = entity.getEyePosition().subtract(vec3);
                Vec3 vec32 = vec31.normalize();

                for(int i = 1; i < Mth.floor(vec31.length()) + 7; ++i) {
                    Vec3 vec33 = vec3.add(vec32.scale((double)i));
                    p.getLevel().addParticle(ParticleTypes.SONIC_BOOM, vec33.x, vec33.y, vec33.z, 0.0D, 0.0D, 0.0D);
                }

                p.playSound(SoundEvents.WARDEN_SONIC_BOOM, 2.0F, 1.0F);


                entity.hurt(DamageSource.sonicBoom(p), 15.0F);

                double d1 = 0.5D;
                double d0 = 2.5D;
                entity.push(vec32.x() * d0, vec32.y() * d1, vec32.z() * d0);
            }else{
                var block = Util.lookAt(p, 150, 10, true);

                Vec3 vec3 = p.position().add(0.0D, (double)1.6F, 0.0D);
                Vec3 vec31 = block.getLocation().subtract(vec3);
                Vec3 vec32 = vec31.normalize();

                for(int i = 1; i < Mth.floor(vec31.length()) + 7; ++i) {
                    Vec3 vec33 = vec3.add(vec32.scale((double)i));
                    p.getLevel().addParticle(ParticleTypes.SONIC_BOOM, vec33.x, vec33.y, vec33.z, 0.0D, 0.0D, 0.0D);
                }

                p.playSound(SoundEvents.WARDEN_SONIC_BOOM, 2.0F, 1.0F);
            }

            return true;
        };
    }

}
