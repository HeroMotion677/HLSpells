package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.capabilities.playercap.PlayerCapProvider;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import com.divinity.hlspells.util.Util;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class RespirationSpell extends Spell {

    public RespirationSpell(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, int tickDelay, boolean treasureOnly, int maxSpellLevel) {
        super(type, rarity, tier, marker, displayName, xpCost, tickDelay, treasureOnly, maxSpellLevel);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            var players = Util.getEntitiesInRange(p, LivingEntity.class, 15, 15, 15);
            p.getCapability(PlayerCapProvider.PLAYER_CAP).ifPresent(cap -> {
                cap.setSpellTimer(cap.getSpellTimer() + 1);
                for (LivingEntity player : players) {
                    if (player.isUnderWater()) {
                        player.setAirSupply(player.getAirSupply() + 15);
                        if (player.getAirSupply() > player.getMaxAirSupply()) {
                            player.setAirSupply(player.getMaxAirSupply());
                        }
                        cap.setSpellTimer(0);
                    }
                }
            });
            return true;
        };
    }
}
