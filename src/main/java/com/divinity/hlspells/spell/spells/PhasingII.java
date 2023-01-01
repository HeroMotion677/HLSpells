package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.capabilities.playercap.PlayerCapProvider;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class PhasingII extends Spell {

    public PhasingII(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, int tickDelay, boolean treasureOnly, int maxSpellLevel) {
        super(type, rarity, tier, marker, displayName, xpCost, tickDelay, treasureOnly, maxSpellLevel);
    }

    private static final MobEffectInstance INVIS = new MobEffectInstance(MobEffects.INVISIBILITY, 60, 5, false, false, false);

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            this.canUse = !p.noPhysics && !p.onClimbable() && !p.isPassenger();
            if (canUse) {
                p.addEffect(INVIS);
                p.getCapability(PlayerCapProvider.PLAYER_CAP).ifPresent(cap -> {
                    if (cap.getEffect() == null) {
                        cap.setEffect(INVIS.getEffect());
                        cap.setEffectDuration(INVIS.getDuration());
                        cap.setEffectAmplifier(INVIS.getAmplifier());
                    }
                });
            }
            return this.canUse;
        };
    }
}
