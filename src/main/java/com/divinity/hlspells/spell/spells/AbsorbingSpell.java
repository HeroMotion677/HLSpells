package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AbsorbingSpell extends Spell {

    public AbsorbingSpell(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, boolean treasureOnly, int maxSpellLevel, SimpleParticleType rune) {
        super(type, rarity, tier, marker, displayName, xpCost, treasureOnly, maxSpellLevel, rune);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            boolean used = false;
            for (BlockPos blockPos : BlockPos.betweenClosed(Mth.floor(p.getX() - 2.0D), Mth.floor(p.getY() - 2.0D), Mth.floor(p.getZ() - 2.0D), Mth.floor(p.getX() + 2.0D), Mth.floor(p.getY() + 2.0D), Mth.floor(p.getZ() + 2.0D))) {
                BlockState blockState = p.level.getBlockState(blockPos);
                FluidState fluidState = p.level.getFluidState(blockPos);
                if (fluidState.is(FluidTags.WATER)) {
                    used = true;
                    if (blockState.getBlock() instanceof SimpleWaterloggedBlock block && !block.canPlaceLiquid(p.level, blockPos, blockState, Fluids.WATER)) {
                        p.level.setBlock(blockPos, blockState.setValue(BlockStateProperties.WATERLOGGED, Boolean.FALSE), 3);
                    }
                    else p.level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                }
            }
            return used;
        };
    }

    @Override
    public int getMaxSpellLevel() {
        return 1;
    }
}
