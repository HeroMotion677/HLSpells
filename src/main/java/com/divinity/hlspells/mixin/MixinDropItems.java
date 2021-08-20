package com.divinity.hlspells.mixin;

import com.divinity.hlspells.init.ItemInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tags.ITag;
import net.minecraft.util.HandSide;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class MixinDropItems {

    @Shadow
    @Final
    public PlayerInventory inventory;

    @Inject(method = "dropEquipment()V", at= @At(value = "INVOKE", target = "net/minecraft/entity/LivingEntity.dropEquipment()V", shift = At.Shift.AFTER), cancellable = true)
    public void dropEquipment(CallbackInfo ci) {
        boolean mainCondition = this.inventory.getItem(this.inventory.selected).getItem() == ItemInit.TOTEM_OF_KEEPING.get();
        boolean offCondition = this.inventory.offhand.get(0).getItem() == ItemInit.TOTEM_OF_KEEPING.get();
        if (mainCondition || offCondition) {
            ci.cancel();
        }
    }
}
