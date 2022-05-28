package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.phys.Vec3;

public class FireballSpell extends Spell {

    public FireballSpell(String displayName, int xpCost, boolean treasureOnly) {
        super(SpellAttributes.Type.CAST, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.THREE, SpellAttributes.Marker.COMBAT, displayName, xpCost, treasureOnly);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            Vec3 vector3d = p.getViewVector(1.0F);
            LargeFireball fireballEntity = new LargeFireball(p.level, p, vector3d.x, vector3d.y, vector3d.z, 0);
            fireballEntity.setPos(p.getX() + vector3d.x * 1.5D, p.getY() + 0.5, p.getZ() + vector3d.z * 1.5D);
            fireballEntity.setOwner(p);
            p.level.addFreshEntity(fireballEntity);
            return true;
        };
    }
}
