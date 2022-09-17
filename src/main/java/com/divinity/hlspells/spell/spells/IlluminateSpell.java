package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.compat.LucentCompat;
import com.divinity.hlspells.entities.Summonable;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import com.divinity.hlspells.util.Util;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class IlluminateSpell extends Spell {

    public IlluminateSpell(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, int tickDelay, boolean treasureOnly, int maxSpellLevel) {
        super(type, rarity, tier, marker, displayName, xpCost, tickDelay, treasureOnly, maxSpellLevel);
    }

    /**
     * Implementation handled by Lucent
     * {@link LucentCompat}
     */
    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            this.canUse = true;
            if (this.getSpellLevel() == 2) {
                var mobList = Util.getEntitiesInRange(p, Mob.class, 15, 15, 15, m -> !(m instanceof Summonable));
                mobList.stream().filter(m -> p != null && m != null).forEach(m -> {
                    m.setLastHurtByPlayer(p);
                    m.setSecondsOnFire(1);
                });
            }
            return this.canUse;
        };
    }
}
