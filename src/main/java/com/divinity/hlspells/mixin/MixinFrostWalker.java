package com.divinity.hlspells.mixin;

import com.divinity.hlspells.items.SpellHoldingItem;
import com.divinity.hlspells.setup.client.ClientSetup;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/**
 *  To allow the player to sprint while holding down a Spell Book with the Frost Path spell (WIP)
 */
@Mixin(ClientPlayerEntity.class)
public class MixinFrostWalker
{
    @Shadow
    public int sprintTriggerTime;

    @Inject(method = "aiStep()V", at= @At(value = "FIELD", target = "net/minecraft/client/entity/player/ClientPlayerEntity.sprintTriggerTime:I", ordinal = 2, shift = At.Shift.AFTER), cancellable = true)
    public void aiStep(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        if (player.isUsingItem() && !player.isPassenger() && player.getUseItem().getItem() instanceof SpellHoldingItem) {
            sprintTriggerTime = ClientSetup.sprintTriggerTime;
            System.out.println(sprintTriggerTime);
        }
    }
}
