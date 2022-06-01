package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.entities.projectile.PiercingBoltEntity;
import com.divinity.hlspells.setup.init.EntityInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.world.entity.player.Player;

public class PiercingBoltSpell extends Spell {

    public PiercingBoltSpell(String displayName, int xpCost, boolean treasureOnly) {
        super(SpellAttributes.Type.CAST, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.TWO, SpellAttributes.Marker.COMBAT, displayName, xpCost, treasureOnly);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            PiercingBoltEntity piercingBullet = new PiercingBoltEntity(EntityInit.PIERCING_BOLT_ENTITY.get(), p.level);
            piercingBullet.setOwner(p);
            piercingBullet.setPos(p.getX() + p.getViewVector(1.0F).x, p.getY() + 1.35, p.getZ() + p.getViewVector(1.0F).z);
            piercingBullet.shootFromRotation(p, p.xRot, p.yRot, 1.3F, 1.3F, 1.3F);
            p.level.addFreshEntity(piercingBullet);
            return true;
        };
    }
}
