package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class Phasing extends Spell {

    public Phasing(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, int tickDelay, boolean treasureOnly, int maxSpellLevel, RegistryObject<SimpleParticleType> rune) {
        super(type, rarity, tier, marker, displayName, xpCost, tickDelay, treasureOnly, maxSpellLevel, rune);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            this.canUse = !p.noPhysics && !p.onClimbable() && !p.isPassenger();
            return this.canUse;
        };
    }
}
