package com.divinity.hlspells.mixin;

import com.divinity.hlspells.items.WandItem;
import com.divinity.hlspells.items.capabilities.WandItemProvider;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.GrindstoneContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/inventory/container/GrindstoneContainer$4")
public class MixinGrindstoneExperience
{
    @Inject(method = "getExperienceFromItem(Lnet/minecraft/item/ItemStack;)I", at = @At(value = "RETURN"), cancellable = true)
    public void getExperienceFromItem(ItemStack stack, CallbackInfoReturnable<Integer> cir)
    {
       if (stack.getItem() instanceof WandItem)
       {

           int spellSize = stack.getCapability(WandItemProvider.WAND_CAP, null).map(m -> m.getSpells().size()).orElse(0);
           cir.setReturnValue(5 * spellSize); //
       }
    }
}
