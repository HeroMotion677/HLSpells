package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import com.divinity.hlspells.util.Util;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class SpeedSpell extends Spell {

    public SpeedSpell(String displayName, int xpCost, int tickDelay, boolean treasureOnly) {
        super(SpellAttributes.Type.HELD, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.TWO, SpellAttributes.Marker.UTILITY, displayName, xpCost, tickDelay, treasureOnly);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            AttributeInstance speedAttribute = p.getAttribute(Attributes.MOVEMENT_SPEED);
            if (speedAttribute != null && speedAttribute.getModifier(Util.speedUUID) == null) {
                speedAttribute.addPermanentModifier(Util.speedModifier);
            }
            return true;
        };
    }
}
