package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import com.divinity.hlspells.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;

public class BondSpell extends Spell {

    public BondSpell(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, boolean treasureOnly, int maxSpellLevel) {
        super(type, rarity, tier, marker, displayName, xpCost, treasureOnly, maxSpellLevel);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            Entity targetEntity = Util.rayTrace(p.level, p, 20D);
            if (targetEntity instanceof TamableAnimal entity) {
                entity.tame(p);
                return true;
            }
            return false;
        };
    }
}
