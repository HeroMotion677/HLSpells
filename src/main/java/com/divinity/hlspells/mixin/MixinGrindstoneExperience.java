package com.divinity.hlspells.mixin;

import com.divinity.hlspells.items.SpellHoldingItem;
import com.divinity.hlspells.items.capabilities.spellholdercap.SpellHolderProvider;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Adds experience when wand is used in grindstone
 */
@Mixin(targets = "net/minecraft/inventory/container/GrindstoneContainer$4")
public class MixinGrindstoneExperience {
    @Inject(method = "getExperienceFromItem(Lnet/minecraft/item/ItemStack;)I", at = @At(value = "RETURN"), cancellable = true)
    public void getExperienceFromItem(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (stack.getItem() instanceof SpellHoldingItem && ((SpellHoldingItem) stack.getItem()).isWand()) {
            int spellSize = stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).map(m -> m.getSpells().size()).orElse(0);
            cir.setReturnValue(5 * spellSize);
        }
    }
}
