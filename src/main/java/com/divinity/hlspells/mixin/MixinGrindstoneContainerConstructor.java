package com.divinity.hlspells.mixin;

import com.divinity.hlspells.items.WandItem;
import com.divinity.hlspells.items.capabilities.IWandCap;
import com.divinity.hlspells.items.capabilities.WandItemProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.GrindstoneContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Map;
/**
 * Mixin to allow wand to be inserted into grindstone
 */
@Mixin(targets = {"net/minecraft/inventory/container/GrindstoneContainer$2",
        "net/minecraft/inventory/container/GrindstoneContainer$3"
})
public class MixinGrindstoneContainerConstructor {
    @Inject(method = "mayPlace(Lnet/minecraft/item/ItemStack;)Z", at = @At(value = "HEAD"), cancellable = true)
    private void mayPlace(ItemStack p_75214_1_, CallbackInfoReturnable<Boolean> cir) {
        if (p_75214_1_.isDamageableItem() || p_75214_1_.getItem() == Items.ENCHANTED_BOOK
                || p_75214_1_.isEnchanted() || p_75214_1_.getItem() instanceof WandItem)
            cir.setReturnValue(true);
    }
}
