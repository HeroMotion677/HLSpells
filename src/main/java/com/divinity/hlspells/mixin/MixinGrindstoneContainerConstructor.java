package com.divinity.hlspells.mixin;

import com.divinity.hlspells.items.SpellHoldingItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to allow wand to be inserted into grindstone
 */
@Mixin(targets = {"net/minecraft/world/inventory/GrindstoneMenu$2", "net/minecraft/world/inventory/GrindstoneMenu$3"})
public class MixinGrindstoneContainerConstructor {
    @Inject(method = "mayPlace(Lnet/minecraft/world/item/ItemStack;)Z", at = @At(value = "HEAD"), cancellable = true)
    private void mayPlace(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.isDamageableItem() || stack.getItem() == Items.ENCHANTED_BOOK
                || stack.isEnchanted() || (stack.getItem() instanceof SpellHoldingItem && ((SpellHoldingItem) stack.getItem()).isWand()))
            cir.setReturnValue(true);
    }
}
