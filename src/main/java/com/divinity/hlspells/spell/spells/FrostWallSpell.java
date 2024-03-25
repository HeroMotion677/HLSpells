package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.setup.init.BlockInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class FrostWallSpell extends Spell {

    public FrostWallSpell(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, int tickDelay, boolean treasureOnly, int maxSpellLevel, RegistryObject<SimpleParticleType> rune) {
        super(type, rarity, tier, marker, displayName, xpCost, tickDelay, treasureOnly, maxSpellLevel, rune);
    }

    @Override
    protected SpellConsumer<Player> getAction() {
        return p -> {
            if (!p.level.isClientSide) {
                BlockPos pos = p.blockPosition().relative(p.getDirection(), 2);
                boolean orient_x = p.getDirection().getAxis() == Direction.Axis.Z;
                int yPos = pos.getY();
                int wPos = orient_x? pos.getX():pos.getZ();

                BlockState ice = BlockInit.CUSTOM_FROSTED_ICE.get().defaultBlockState();
                BlockPos.MutableBlockPos _pos = pos.mutable();
                p.level.setBlockAndUpdate(_pos,ice);

                for (int y = yPos; y < yPos + 3; ++y)
                    for (int w = wPos-1; w <= wPos+1; ++w)
                        p.level.setBlockAndUpdate(_pos.set(orient_x? w:pos.getX(),y,orient_x? pos.getZ():w),
                         BlockInit.CUSTOM_FROSTED_ICE.get().defaultBlockState());
            } return true;
        };
    }
}
