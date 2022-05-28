package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.entities.FlamingBoltEntity;
import com.divinity.hlspells.setup.init.EntityInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.world.entity.player.Player;

public class FlamingBoltSpell extends Spell {

    public FlamingBoltSpell(String displayName, int xpCost, boolean treasureOnly) {
        super(SpellAttributes.Type.CAST, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.TWO, SpellAttributes.Marker.COMBAT, displayName, xpCost, treasureOnly);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            FlamingBoltEntity flamingBolt = new FlamingBoltEntity(EntityInit.FLAMING_BOLT_ENTITY.get(), p.level);
            flamingBolt.setOwner(p);
            flamingBolt.setPos(p.getX() + p.getViewVector(1.0F).x, p.getY() + 1.35, p.getZ() + p.getViewVector(1.0F).z);
            flamingBolt.shootFromRotation(p, p.xRot, p.yRot, 1.3F, 1.3F, 1.3F);
            p.level.addFreshEntity(flamingBolt);
            return true;
        };
    }
}
