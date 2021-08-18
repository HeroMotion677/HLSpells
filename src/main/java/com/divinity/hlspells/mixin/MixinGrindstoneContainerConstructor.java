package com.divinity.hlspells.mixin;

import com.divinity.hlspells.items.WandItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to allow wand to be inserted into grindstone
 */
@Mixin(targets = {"net/minecraft/inventory/container/GrindstoneContainer$2",
        "net/minecraft/inventory/container/GrindstoneContainer$3"
})
public class MixinGrindstoneContainerConstructor {
    @Inject(method = "mayPlace(Lnet/minecraft/item/ItemStack;)Z", at = @At(value = "HEAD"), cancellable = true)
    private void mayPlace(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.isDamageableItem() || stack.getItem() == Items.ENCHANTED_BOOK
                || stack.isEnchanted() || stack.getItem() instanceof WandItem)
            cir.setReturnValue(true);
    }
}
