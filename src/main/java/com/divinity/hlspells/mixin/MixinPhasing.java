package com.divinity.hlspells.mixin;

import com.divinity.hlspells.spell.spells.Phasing;
import com.divinity.hlspells.spell.spells.PhasingII;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class MixinPhasing {

    @Shadow protected abstract BlockState asState();

    @Shadow public abstract Block getBlock();

    /**
     * Makes the 3rd person camera not be super zoomed-in when half inside a smoothed block.
     * Credits to Cadiboo (author of NoCubes)
     */
    @SuppressWarnings("deprecation")
    @Inject(method = "getVisualShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("HEAD"), cancellable = true)
    public void getVisualShape(BlockGetter pLevel, BlockPos pPos, CollisionContext pContext, CallbackInfoReturnable<VoxelShape> cir) {
        BlockState state = this.asState();
        VoxelShape visualShape = this.getBlock().getVisualShape(state, pLevel, pPos, pContext);
        cir.setReturnValue(visualShape.isEmpty() ? visualShape : state.getCollisionShape(pLevel, pPos, pContext));
    }

    /**
     * Allows the player to phase through blocks
     */
    @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("HEAD"), cancellable = true)
    public void getCollisionShape(BlockGetter pLevel, BlockPos pPos, CollisionContext pContext, CallbackInfoReturnable<VoxelShape> cir) {
        if (pContext instanceof EntityCollisionContext collisionContext) {
            if (collisionContext.getEntity() instanceof Player player) {
                if (SpellUtils.getSpell(player.getUseItem()) instanceof Phasing spell && spell.canUseSpell() || SpellUtils.getSpell(player.getUseItem()) instanceof PhasingII spell2 && spell2.canUseSpell()) {
                    if (pPos.getY() >= player.getY()) { // Prevents the player from falling through the ground
                        if (player.level.getBlockState(pPos).getBlock().defaultDestroyTime() >= 0.0F) { // Check if the block is not unbreakable (aka, not bedrock)
                            cir.setReturnValue(Shapes.empty());
                        }
                    }
                }
            }
        }
    }
}
