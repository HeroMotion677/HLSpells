package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import com.divinity.hlspells.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class TeleportSpell extends Spell {

    public TeleportSpell(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, boolean treasureOnly, int maxSpellLevel, SimpleParticleType rune) {
        super(type, rarity, tier, marker, displayName, xpCost, treasureOnly, maxSpellLevel, rune);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            HitResult rayTraceResult = Util.lookAt(p, 100, 1F, false);
            Vec3 location = rayTraceResult.getLocation();
            Vec3i loc = new Vec3i((int)location.x, (int)location.y, (int)location.z);
            int stepX = 0;
            int stepY = 1;
            int stepZ = 0;
            if ((rayTraceResult instanceof BlockHitResult result) && !p.level().getBlockState(new BlockPos(loc).above()).isAir()) {
                Direction rayTraceDirection = result.getDirection();
                stepX = rayTraceDirection.getStepX();
                stepY = rayTraceDirection.getStepY();
                stepZ = rayTraceDirection.getStepZ();
            }
            int tx = loc.getX() + stepX;
            int ty = loc.getY() + stepY;
            int tz = loc.getZ() + stepZ;
            BlockPos teleportPos = new BlockPos(tx, ty, tz);
            p.fallDistance = 0;
            Util.teleportToLocation(p.level(), p.blockPosition(), teleportPos, p);
            return true;
        };
    }
}
