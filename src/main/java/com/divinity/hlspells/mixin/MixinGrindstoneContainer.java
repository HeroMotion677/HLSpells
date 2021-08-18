package com.divinity.hlspells.mixin;

import com.divinity.hlspells.items.WandItem;
import com.divinity.hlspells.items.capabilities.wandcap.WandItemProvider;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.GrindstoneContainer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to remove spells in the wand item
 */

@Mixin(GrindstoneContainer.class)
public class MixinGrindstoneContainer {
    @Shadow
    @Final
    private IInventory repairSlots;

    /**
     * Modifies the local variable to allow wand item to not have empty result
     */
    @ModifyVariable(method = "createResult()V",
            at = @At(value = "STORE", target = "Lnet/minecraft/item/ItemStack;isEnchanted()Z"), ordinal = 2)
    private boolean canBeUsedInGrindstone(boolean original) {
        ItemStack stack = repairSlots.getItem(0);
        ItemStack stack1 = repairSlots.getItem(1);
        boolean condition = original;
        if ((!stack.isEmpty() && stack.getItem() instanceof WandItem) || (!stack1.isEmpty() && stack1.getItem() instanceof WandItem)) {
            condition = false;
        }
        return condition;
    }


    @Inject(method = "removeNonCurses(Lnet/minecraft/item/ItemStack;II)Lnet/minecraft/item/ItemStack;", at = @At(value = "RETURN"), cancellable = true)
    public void removeSpells(ItemStack stack, int pDamage, int pCount, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack output = cir.getReturnValue();
        if (output.getItem() instanceof WandItem) {
            output.getCapability(WandItemProvider.WAND_CAP, null).ifPresent(iWandCap ->
            {
                //get all the spells present and remove them all
                if (!iWandCap.getSpells().isEmpty())
                    iWandCap.getSpells().clear();
            });
        }
        cir.setReturnValue(output);
    }
}

