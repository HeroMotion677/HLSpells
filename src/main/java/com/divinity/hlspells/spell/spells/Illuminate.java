package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.compat.LucentCompat;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;

public class Illuminate extends Spell {

    public Illuminate(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, int tickDelay, boolean treasureOnly, int maxSpellLevel, RegistryObject<SimpleParticleType> rune) {
        super(type, rarity, tier, marker, displayName, xpCost, tickDelay, treasureOnly, maxSpellLevel, rune);
    }

    /**
     * Implementation handled by Lucent
     * {@link LucentCompat}
     */
    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            this.canUse = true;
            return true;
        };
    }
    @Nullable
    @Override
    public Spell getUpgrade() {
        return SpellInit.ILLUMINATE_II.get();
    }
}
