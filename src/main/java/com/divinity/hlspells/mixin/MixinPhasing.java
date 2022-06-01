package com.divinity.hlspells.mixin;

import com.divinity.hlspells.capabilities.playercap.PlayerCapProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public abstract class MixinPhasing {

    @ModifyArg(method = "move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(DDD)V"), index = 0)
    public double injectedArgOne(double injected) {
        Entity entity = (Entity) (Object) this;
        double[] returnValue = new double[1];
        returnValue[0] = injected;
        if (entity instanceof Player player) {
            player.getCapability(PlayerCapProvider.PLAYER_CAP).ifPresent(cap -> {
                if (cap.getPhasingActive()) {
                    returnValue[0] = player.getDeltaMovement().x;
                }
            });
        }
        return returnValue[0];
    }

    @ModifyArg(method = "move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(DDD)V"), index = 1)
    public double injectedArgTwo(double injected) {
        Entity entity = (Entity) (Object) this;
        double[] returnValue = new double[1];
        returnValue[0] = injected;
        if (entity instanceof Player player) {
            player.getCapability(PlayerCapProvider.PLAYER_CAP).ifPresent(cap -> {
                if (cap.getPhasingActive()) {
                    returnValue[0] = player.getDeltaMovement().y;
                }
            });
        }
        return returnValue[0];
    }

    @ModifyArg(method = "move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(DDD)V"), index = 2)
    public double injectedArgThree(double injected) {
        Entity entity = (Entity) (Object) this;
        double[] returnValue = new double[1];
        returnValue[0] = injected;
        if (entity instanceof Player player) {
            player.getCapability(PlayerCapProvider.PLAYER_CAP).ifPresent(cap -> {
                if (cap.getPhasingActive()) {
                    returnValue[0] = player.getDeltaMovement().z();
                }
            });
        }
        return returnValue[0];
    }
}
