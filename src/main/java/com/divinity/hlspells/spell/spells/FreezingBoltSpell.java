package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.entities.projectile.FlamingBoltEntity;
import com.divinity.hlspells.entities.projectile.FreezingBoltEntity;
import com.divinity.hlspells.setup.init.EntityInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.world.entity.player.Player;

public class FreezingBoltSpell extends Spell {

    public FreezingBoltSpell(String displayName, int xpCost, boolean treasureOnly) {
        super(SpellAttributes.Type.CAST, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.COMBAT, displayName, xpCost, treasureOnly);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            FreezingBoltEntity freezingBolt = new FreezingBoltEntity(EntityInit.FREEZING_BOLT_ENTITY.get(), p.level);
            freezingBolt.setOwner(p);
            freezingBolt.setPos(p.getX() + p.getViewVector(1.0F).x, p.getY() + 1.35, p.getZ() + p.getViewVector(1.0F).z);
            freezingBolt.shootFromRotation(p, p.xRot, p.yRot, 1.3F, 1.3F, 1.3F);
            p.level.addFreshEntity(freezingBolt);
            return true;
        };
    }
}
