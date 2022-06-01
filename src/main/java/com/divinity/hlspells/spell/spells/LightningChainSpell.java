package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.entities.projectile.InvisibleTargetingEntity;
import com.divinity.hlspells.setup.init.EntityInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.world.entity.player.Player;

public class LightningChainSpell extends Spell {

    public LightningChainSpell(String displayName, int xpCost, boolean treasureOnly) {
        super(SpellAttributes.Type.CAST, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.THREE, SpellAttributes.Marker.COMBAT, displayName, xpCost, treasureOnly);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            InvisibleTargetingEntity stormBullet = new InvisibleTargetingEntity(EntityInit.INVISIBLE_TARGETING_ENTITY.get(), p.level);
            stormBullet.setHomePosition(p.position());
            stormBullet.setOwner(p);
            stormBullet.setPos(p.getX() + p.getViewVector(1.0F).x, p.getY() + 1.35, p.getZ() + p.getViewVector(1.0F).z);
            stormBullet.shootFromRotation(p, p.xRot, p.yRot, 1.3F, 1.3F, 1.3F);
            p.level.addFreshEntity(stormBullet);
            return true;
        };
    }
}
