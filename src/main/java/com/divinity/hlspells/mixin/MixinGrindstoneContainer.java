package com.divinity.hlspells.mixin;

import com.divinity.hlspells.items.SpellBookItem;
import com.divinity.hlspells.items.capabilities.WandItemProvider;
import net.minecraft.inventory.container.GrindstoneContainer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(GrindstoneContainer.class)
public class MixinGrindstoneContainer
{

    @Inject(method = "removeNonCurses", at = @At(value = "RETURN"), cancellable = true)
    public void removeSpells(ItemStack p_217007_1_, int p_217007_2_, int p_217007_3_, CallbackInfoReturnable<ItemStack> cir)
    {
        ItemStack output = cir.getReturnValue();
        if (output.getItem() instanceof SpellBookItem)
        {
            output.getCapability(WandItemProvider.WAND_CAP, null).ifPresent(iWandCap ->
            {
                //get all the spells present and remove them all
                List<String> spells = iWandCap.getSpells();
                spells.forEach(iWandCap::removeSpell);
            });
        }
        cir.setReturnValue(output);
    }
}
