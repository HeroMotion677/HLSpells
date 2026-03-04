package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class Fortify extends Spell {

    public Fortify(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, int tickDelay, boolean treasureOnly, int maxSpellLevel, SimpleParticleType rune) {
        super(type, rarity, tier, marker, displayName, xpCost, tickDelay, treasureOnly, maxSpellLevel, rune);
    }
    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            if (canUse) {
                p.setSpeed(-1);
                p.setInvulnerable(true);

                p.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 3, 255, false, false, false));
            }
            return this.canUse;
        };
    }
}
