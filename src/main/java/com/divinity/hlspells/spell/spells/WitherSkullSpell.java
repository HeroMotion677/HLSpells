package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.phys.Vec3;

public class WitherSkullSpell extends Spell {

    public WitherSkullSpell(String displayName, int xpCost, boolean treasureOnly) {
        super(SpellAttributes.Type.CAST, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.THREE, SpellAttributes.Marker.COMBAT, displayName, xpCost, treasureOnly);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            Vec3 vector3d = p.getViewVector(1.0F);
            WitherSkull witherSkullEntity = new WitherSkull(p.level, p, vector3d.x, vector3d.y, vector3d.z);
            witherSkullEntity.setPos(p.getX() + vector3d.x * 1.5D, p.getY() + 1, p.getZ() + vector3d.z * 1.5D);
            witherSkullEntity.setOwner(p);
            p.level.addFreshEntity(witherSkullEntity);
            return true;
        };
    }
}
