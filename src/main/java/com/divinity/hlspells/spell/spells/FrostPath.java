package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;

public class FrostPath extends Spell {

    public FrostPath(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, int tickDelay, boolean treasureOnly, int maxSpellLevel) {
        super(type, rarity, tier, marker, displayName, xpCost, tickDelay, treasureOnly, maxSpellLevel);
    }

    @Override
    protected SpellConsumer<Player> getAction() {
        return p -> {
            if (p.isOnGround()) {
                BlockState blockstate = Blocks.FROSTED_ICE.defaultBlockState();
                float f = (float) Math.min(16, 3);
                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                BlockPos pPos = p.blockPosition();
                boolean used = false;
                for (BlockPos blockpos : BlockPos.betweenClosed(pPos.offset(-f, -1.0D, -f), pPos.offset(f, -1.0D, f))) {
                    if (blockpos.closerToCenterThan(p.position(), f)) {
                        blockpos$mutableblockpos.set(blockpos.getX(), blockpos.getY() + 1, blockpos.getZ());
                        BlockState blockstate1 = p.level.getBlockState(blockpos$mutableblockpos);
                        if (blockstate1.isAir()) {
                            BlockState blockstate2 = p.level.getBlockState(blockpos);
                            boolean isFull = blockstate2.getBlock() == Blocks.WATER && blockstate2.getValue(LiquidBlock.LEVEL) == 0;
                            if (blockstate2.getMaterial() == Material.WATER && isFull && blockstate.canSurvive(p.level, blockpos) && p.level.isUnobstructed(blockstate, blockpos, CollisionContext.empty()) && !ForgeEventFactory.onBlockPlace(p, BlockSnapshot.create(p.level.dimension(), p.level, blockpos), Direction.UP)) {
                                used = true;
                                p.level.setBlockAndUpdate(blockpos, blockstate);
                                p.level.scheduleTick(blockpos, Blocks.FROSTED_ICE, Mth.nextInt(p.getRandom(), 60, 120));
                            }
                        }
                    }
                }
                return used;
            }
            return false;
        };
    }

    @Nullable
    @Override
    public Spell getUpgrade() {
        return SpellInit.FROST_PATH_II.get();
    }

    @Nullable
    @Override
    public Spell getUpgradeableSpellPath() {
        return SpellInit.FROST_PATH.get();
    }
}
