package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.setup.init.BlockInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;

public class FrostPathSpell extends Spell {

    public FrostPathSpell(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, int tickDelay, boolean treasureOnly, int maxSpellLevel) {
        super(type, rarity, tier, marker, displayName, xpCost, tickDelay, treasureOnly, maxSpellLevel);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            BlockPos pos = p.blockPosition();
            BlockState blockstate = BlockInit.CUSTOM_FROSTED_ICE.get().defaultBlockState();
            float f = 3;
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
            boolean used = false;
            for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset((-f), -1.0D, (-f)), pos.offset(f, -1.0D, f))) {
                if (blockpos.closerToCenterThan(p.position(), f)) {
                    mutablePos.set(blockpos.getX(), blockpos.getY() + 1, blockpos.getZ());
                    BlockState mutableState = p.level.getBlockState(mutablePos);
                    if (mutableState.isAir()) {
                        BlockState state = p.level.getBlockState(blockpos);
                        if (state.getMaterial().isReplaceable() && blockstate.canSurvive(p.level, blockpos) && p.level.isUnobstructed(blockstate, blockpos, CollisionContext.empty()) && !ForgeEventFactory.onBlockPlace(p, BlockSnapshot.create(p.level.dimension(), p.level, blockpos), Direction.UP)) {
                            used = true;
                            p.level.setBlockAndUpdate(blockpos, blockstate);
                            p.level.scheduleTick(blockpos, BlockInit.CUSTOM_FROSTED_ICE.get(), Mth.nextInt(p.getRandom(), 60, 120));
                        }
                    }
                }
            }
            return used;
        };
    }
}
