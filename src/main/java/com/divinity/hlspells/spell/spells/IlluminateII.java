package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.entities.Summonable;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import com.divinity.hlspells.util.Util;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;

public class IlluminateII extends Spell {

    public IlluminateII(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, int tickDelay, boolean treasureOnly, int maxSpellLevel) {
        super(type, rarity, tier, marker, displayName, xpCost, tickDelay, true, maxSpellLevel);
    }

    @Override
    protected SpellConsumer<Player> getAction() {
        return p -> {
            var mobList = Util.getEntitiesInRange(p, Mob.class, 15, 15, 15, m -> !(m instanceof Summonable));
            mobList.stream().filter(m -> p != null && m != null).forEach(m -> {
                if (m.getMobType() == MobType.UNDEAD) {
                    m.setLastHurtByPlayer(p);
                    m.setSecondsOnFire(1);
                }
                else if (m instanceof Spider spider) {
                    spider.setTarget(null);
                }
            });
            return true;
        };
    }
}
