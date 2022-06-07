package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.capabilities.playercap.PlayerCapProvider;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;

public class PhasingSpell extends Spell {

    public PhasingSpell(String displayName, int xpCost, int tickDelay, boolean treasureOnly) {
        super(SpellAttributes.Type.HELD, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.THREE, SpellAttributes.Marker.UTILITY, displayName, xpCost, tickDelay, treasureOnly);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            p.getCapability(PlayerCapProvider.PLAYER_CAP).ifPresent(cap -> cap.setPhasingActive(true));
            p.noPhysics = true;
            if (p instanceof LocalPlayer localPlayer) {
                if (!localPlayer.level.getBlockState(localPlayer.getOnPos()).isAir()) {
                    if (!localPlayer.input.jumping)
                        localPlayer.setDeltaMovement(localPlayer.getDeltaMovement().x, 0, localPlayer.getDeltaMovement().z);
                        localPlayer.resetFallDistance();
                }
            }
            return true;
        };
    }

    private boolean isInWall(Player player) {
        float f = player.dimensions.width * 0.8F;
        AABB aabb = AABB.ofSize(player.getEyePosition(), f, 1.0E-6D, f);
        return BlockPos.betweenClosedStream(aabb).anyMatch((p_201942_) -> {
            BlockState blockstate = player.level.getBlockState(p_201942_);
            return !blockstate.isAir() && blockstate.isSuffocating(player.level, p_201942_) && Shapes.joinIsNotEmpty(blockstate.getCollisionShape(player.level, p_201942_).move(p_201942_.getX(), p_201942_.getY(), p_201942_.getZ()), Shapes.create(aabb), BooleanOp.AND);
        });
    }
}
