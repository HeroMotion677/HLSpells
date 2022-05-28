package com.divinity.hlspells.mixin;

import com.divinity.hlspells.items.spellitems.SpellHoldingItem;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.UpgradeRecipe;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(UpgradeRecipe.class)
public class MixinSmithingRecipe {

    @Inject(method = "assemble", at = @At(value = "INVOKE", target = "net/minecraft/world/item/ItemStack.setTag(Lnet/minecraft/nbt/CompoundTag;)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    public void addCap(Container pInv, CallbackInfoReturnable<ItemStack> cir, ItemStack itemStack, CompoundTag compoundNBT) {
        if (itemStack.getItem() instanceof SpellHoldingItem)
            pInv.getItem(0).getCapability(SpellHolderProvider.SPELL_HOLDER_CAP)
                    .ifPresent(cap -> cap.getSpells()
                            .forEach(spell -> itemStack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP)
                                    .ifPresent(outCap -> outCap.addSpell(spell))));
    }
}
