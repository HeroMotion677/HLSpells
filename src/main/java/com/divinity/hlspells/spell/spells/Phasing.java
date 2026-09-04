package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public class Phasing extends Spell {

    public Phasing(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, int tickDelay, boolean treasureOnly, int maxSpellLevel, SimpleParticleType rune) {
        super(type, rarity, tier, marker, displayName, xpCost, tickDelay, treasureOnly, maxSpellLevel, rune);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            this.canUse = !p.noPhysics && !p.onClimbable() && !p.isPassenger();
            p.setInvulnerable(true);
            p.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 3, 5, false, false, false));
            return this.canUse;
        };
    }
    @Nullable
    @Override
    public Spell getUpgrade() {
        return SpellInit.PHASING_II.get();
    }
}
