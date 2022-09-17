package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import com.divinity.hlspells.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class PullSpell extends Spell {

    public PullSpell(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, boolean treasureOnly, int maxSpellLevel) {
        super(type, rarity, tier, marker, displayName, xpCost, treasureOnly, maxSpellLevel);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            var entity = Util.rayTrace(p.level, p, 35D);
            if (entity != null) {
                if (entity.distanceTo(p) > 5) {
                    entity.setDeltaMovement(p.getLookAngle().reverse().multiply(5, 5, 5));
                    return true;
                }
            }
            return false;
        };
    }
}
