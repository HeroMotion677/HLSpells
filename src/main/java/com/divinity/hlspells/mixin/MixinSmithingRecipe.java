package com.divinity.hlspells.mixin;

import com.divinity.hlspells.items.SpellHoldingItem;
import com.divinity.hlspells.items.capabilities.spellholdercap.SpellHolderProvider;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.SmithingRecipe;
import net.minecraft.nbt.CompoundNBT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SmithingRecipe.class)
public class MixinSmithingRecipe {

    @Inject(method = "assemble", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;setTag(Lnet/minecraft/nbt/CompoundNBT;)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    public void addCap(IInventory pInv, CallbackInfoReturnable<ItemStack> cir, ItemStack itemStack, CompoundNBT compoundNBT) {
        if (itemStack.getItem() instanceof SpellHoldingItem)
            pInv.getItem(0).getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(cap -> {
                cap.getSpells().forEach(spell -> {
                    itemStack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(outCap -> {
                        outCap.addSpell(spell);
                    });
                });
            });
    }
}
