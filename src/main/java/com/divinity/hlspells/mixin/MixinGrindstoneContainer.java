package com.divinity.hlspells.mixin;

import com.divinity.hlspells.capabilities.spellholdercap.ISpellHolder;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.items.spellitems.SpellHoldingItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

/**
 * Mixin to remove spells in the wand item
 */
@Mixin(GrindstoneMenu.class)
public class MixinGrindstoneContainer {

    @Shadow
    @Final
    Container repairSlots;

    /**
     * Allows the wand item to produce a result even when it holds no enchantments
     */
    @Redirect(method = "computeResult(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;hasAnyEnchantments(Lnet/minecraft/world/item/ItemStack;)Z"))
    private boolean canBeUsedInGrindstone(ItemStack stack) {
        if (stack.getItem() instanceof SpellHoldingItem item && item.isWand()) {
            return true;
        }
        return EnchantmentHelper.hasAnyEnchantments(stack);
    }

    @Inject(method = "removeNonCursesFrom(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "RETURN"), cancellable = true)
    public void removeSpells(ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack output = cir.getReturnValue();
        if (output.getItem() instanceof SpellHoldingItem item && item.isWand()) {
            SpellHolderProvider.get(output).ifPresent(MixinGrindstoneContainer::clearSpells);
        }
        cir.setReturnValue(output);
    }

    private static void clearSpells(ISpellHolder holder) {
        new ArrayList<>(holder.getSpells()).forEach(holder::removeSpell);
    }
}
