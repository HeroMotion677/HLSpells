package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import com.divinity.hlspells.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class PullSpell extends Spell {

    public PullSpell(String displayName, int xpCost, boolean treasureOnly) {
        super(SpellAttributes.Type.CAST, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.UTILITY, displayName, xpCost, treasureOnly);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            if (Util.rayTrace(p.level, p, 35D) != null) {
                Entity targetEntity = Util.rayTrace(p.level, p, 35D);
                if (targetEntity != null && targetEntity.distanceTo(p) > 5) {
                    targetEntity.setDeltaMovement(p.getLookAngle().reverse().multiply(5, 5, 5));
                    return true;
                }
            }
            return false;
        };
    }
}
