package com.divinity.hlspells.mixin;

import com.divinity.hlspells.items.SpellBookItem;
import com.google.common.collect.Lists;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.inventory.container.EnchantmentContainer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EnchantmentContainer.class)
public class MixinEnchantmentContainer
{

    @Inject(method = "getEnchantmentList", at = @At("TAIL"), cancellable = true)
    public void onEnchantmentAdding(ItemStack p_178148_1_, int p_178148_2_, int p_178148_3_, CallbackInfoReturnable<List<EnchantmentData>> cir)
    {
        if (p_178148_1_.getItem() instanceof SpellBookItem)
        {
            List<EnchantmentData> enchantmentData = cir.getReturnValue();
            if (!enchantmentData.isEmpty())
            {
                cir.setReturnValue(Lists.newArrayList(enchantmentData.get(0)));
            }
        }
    }
}
