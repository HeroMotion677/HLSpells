package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.capabilities.playercap.PlayerCapProvider;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class LevitationSpell extends Spell {

    private static final MobEffectInstance LEVITATION = new MobEffectInstance(MobEffects.LEVITATION, Integer.MAX_VALUE, 5, false, false);

    public LevitationSpell(String displayName, int xpCost, int tickDelay, boolean treasureOnly) {
        super(SpellAttributes.Type.HELD, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.TWO, SpellAttributes.Marker.UTILITY, displayName, xpCost, tickDelay, treasureOnly);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            MobEffectInstance effect = p.getEffect(MobEffects.LEVITATION);
            if (effect != null && effect.isVisible()) {
                p.getCapability(PlayerCapProvider.PLAYER_CAP).ifPresent(cap -> {
                    if (cap.getEffect() == null) {
                        cap.setEffect(effect.getEffect());
                        cap.setEffectDuration(effect.getDuration());
                        cap.setEffectAmplifier(effect.getAmplifier());
                    }
                });
            }
            p.addEffect(LEVITATION);
            for (int a = 0; a < 1; a++) {
                p.level.addParticle(ParticleTypes.END_ROD, p.getX(), p.getY() - 1, p.getZ(), 0, p.getDeltaMovement().y, 0);
            }
            return true;
        };
    }
}
