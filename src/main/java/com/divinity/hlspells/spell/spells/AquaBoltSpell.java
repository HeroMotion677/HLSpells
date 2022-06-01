package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.entities.projectile.AquaBoltEntity;
import com.divinity.hlspells.setup.init.EntityInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.world.entity.player.Player;

public class AquaBoltSpell extends Spell {

    public AquaBoltSpell(String displayName, int xpCost, boolean treasureOnly) {
        super(SpellAttributes.Type.CAST, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.COMBAT, displayName, xpCost, treasureOnly);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            AquaBoltEntity aquaBolt = new AquaBoltEntity(EntityInit.AQUA_BOLT_ENTITY.get(), p.level);
            aquaBolt.setOwner(p);
            aquaBolt.setPos(p.getX() + p.getViewVector(1.0F).x, p.getY() + 1.35, p.getZ() + p.getViewVector(1.0F).z);
            aquaBolt.shootFromRotation(p, p.xRot, p.yRot, 1.3F, 1.3F, 1.3F);
            p.level.addFreshEntity(aquaBolt);
            return true;
        };
    }
}
