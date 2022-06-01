package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.entities.projectile.ChorusBoltEntity;
import com.divinity.hlspells.entities.projectile.FlamingBoltEntity;
import com.divinity.hlspells.setup.init.EntityInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockCollisions;

public class ChorusBoltSpell extends Spell {

    public ChorusBoltSpell(String displayName, int xpCost, boolean treasureOnly) {
        super(SpellAttributes.Type.CAST, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.TWO, SpellAttributes.Marker.UTILITY, displayName, xpCost, treasureOnly);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            ChorusBoltEntity chorusBolt = new ChorusBoltEntity(EntityInit.CHORUS_BOLT_ENTITY.get(), p.level);
            chorusBolt.setOwner(p);
            chorusBolt.setPos(p.getX() + p.getViewVector(1.0F).x, p.getY() + 1.35, p.getZ() + p.getViewVector(1.0F).z);
            chorusBolt.shootFromRotation(p, p.xRot, p.yRot, 1.3F, 1.3F, 1.3F);
            p.level.addFreshEntity(chorusBolt);
            return true;
        };
    }
}
