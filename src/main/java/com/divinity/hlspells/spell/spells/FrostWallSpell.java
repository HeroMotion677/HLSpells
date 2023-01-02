package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.setup.init.BlockInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public class FrostWallSpell extends Spell {

    public FrostWallSpell(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, int tickDelay, boolean treasureOnly, int maxSpellLevel) {
        super(type, rarity, tier, marker, displayName, xpCost, tickDelay, treasureOnly, maxSpellLevel);
    }

    @Override
    protected SpellConsumer<Player> getAction() {
        return p -> {
            if (!p.level.isClientSide) {
                BlockPos pos = p.blockPosition().relative(p.getDirection(), 2);
                int xPos = pos.getX();
                int yPos = pos.getY() + 1;
                int zPos = pos.getZ();
                for (int x = xPos; x < xPos + 3; ++x) {
                    for (int y = yPos; y < yPos + 3; ++y) {
                        p.level.setBlockAndUpdate(new BlockPos(x, y, pos.getZ()), BlockInit.CUSTOM_FROSTED_ICE.get().defaultBlockState());
                    }
                }
            }
            return true;
        };
    }
}
