package com.divinity.hlspells.mixin;

import com.divinity.hlspells.items.spellitems.SpellHoldingItem;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
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

@Mixin(GrindstoneMenu.class)
public class MixinGrindstoneContainer {

    @Shadow
    @Final
    Container repairSlots;

    /**
     * Modifies the local variable to allow wand item to not have empty result
     */
    @ModifyVariable(method = "createResult()V",
            at = @At(value = "STORE", target = "Lnet/minecraft/item/ItemStack;isEnchanted()Z"), ordinal = 2)
    private boolean canBeUsedInGrindstone(boolean original) {
        ItemStack stack = repairSlots.getItem(0);
        ItemStack stack1 = repairSlots.getItem(1);
        boolean condition = original;
        if ((!stack.isEmpty() && stack.getItem() instanceof SpellHoldingItem item && item.isWand())
                || (!stack1.isEmpty() && stack1.getItem() instanceof SpellHoldingItem item2 && item2.isWand())) {
            condition = false;
        }
        return condition;
    }


    @Inject(method = "removeNonCurses(Lnet/minecraft/world/item/ItemStack;II)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "RETURN"), cancellable = true)
    public void removeSpells(ItemStack stack, int pDamage, int pCount, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack output = cir.getReturnValue();
        if (output.getItem() instanceof SpellHoldingItem item && item.isWand()) {
            output.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP, null).ifPresent(iWandCap -> {
                //get all the spells present and remove them all
                if (!iWandCap.getSpells().isEmpty()) iWandCap.getSpells().clear();
            });
        }
        cir.setReturnValue(output);
    }
}

