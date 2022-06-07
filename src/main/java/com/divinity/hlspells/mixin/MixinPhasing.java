package com.divinity.hlspells.mixin;

import com.divinity.hlspells.capabilities.playercap.PlayerCapProvider;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.profiling.jfr.event.PacketReceivedEvent;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(LivingEntity.class)
public abstract class MixinPhasing {

    @Inject(method = "jumpFromGround", at = @At("HEAD"), cancellable = true)
    public void jumpFromGround(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof Player player) {
            if (player.jumpTriggerTime > 0 && player.isUsingItem() && player.noPhysics)
                ci.cancel();
        }
    }

/*    @Shadow protected abstract Vec3 collide(Vec3 pVec);

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    public void move(MoverType pType, Vec3 pPos, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof Player player) {
            if (player.isUsingItem()) {
                Vec3 vec3 = this.collide(pPos);
                player.setPos(player.getX() + pPos.x, player.getY() + vec3.y, player.getZ() + pPos.z);
                ci.cancel();
            }
        }
    }*/

/*    @Inject(method = "collide(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;", at = @At("HEAD"), cancellable = true)
    private void collide(Vec3 pVec, CallbackInfoReturnable<Vec3> cir) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof Player player && player.isUsingItem())
            cir.setReturnValue(pVec);
    }

    boolean isInWall(Player player) {
        float f = player.dimensions.width * 0.8F;
        AABB aabb = AABB.ofSize(player.getEyePosition(), f, 1.0E-6D, f);
        return BlockPos.betweenClosedStream(aabb).anyMatch((p_201942_) -> {
            BlockState blockstate = player.level.getBlockState(p_201942_);
            return !blockstate.isAir() && blockstate.isSuffocating(player.level, p_201942_) && Shapes.joinIsNotEmpty(blockstate.getCollisionShape(player.level, p_201942_).move((double)p_201942_.getX(), (double)p_201942_.getY(), (double)p_201942_.getZ()), Shapes.create(aabb), BooleanOp.AND);
        });
    }*/

/*    @Inject(method = "collideBoundingBox(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Lnet/minecraft/world/level/Level;Ljava/util/List;)Lnet/minecraft/world/phys/Vec3;",
    at = @At("HEAD"), cancellable = true)
    private static void collideBoundingBox(@Nullable Entity pEntity, Vec3 pVec, AABB pCollisionBox, Level pLevel, List<VoxelShape> pPotentialHits, CallbackInfoReturnable<Vec3> cir) {
        if (pEntity instanceof Player player) {
            if (player.isUsingItem()) {
                cir.setReturnValue(collideOnlyYAxis(pVec, pCollisionBox, pPotentialHits));
            }
        }
    }

    private static Vec3 collideOnlyYAxis(Vec3 vec, AABB aabb, List<VoxelShape> shapes) {
        if (!shapes.isEmpty()) {
            var y = vec.y;
            if (y != 0.0D) {
                var newY = Shapes.collide(Direction.Axis.Y, aabb, shapes, y);
                if (newY != 0.0D)
                    aabb.move(0.0D, newY, 0.0D);
                    return new Vec3(vec.x, newY, vec.z);
            }
        }
        return vec;
    }*/
}
