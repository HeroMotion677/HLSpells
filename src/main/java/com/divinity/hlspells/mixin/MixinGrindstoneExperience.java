package com.divinity.hlspells.mixin;

import com.divinity.hlspells.items.WandItem;
import com.divinity.hlspells.items.capabilities.wandcap.SpellHolderProvider;
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
        if (stack.getItem() instanceof WandItem) {
            int spellSize = stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP, null).map(m -> m.getSpells().size()).orElse(0);
            cir.setReturnValue(5 * spellSize);
        }
    }
}
