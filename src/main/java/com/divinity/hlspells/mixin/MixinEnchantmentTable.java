package com.divinity.hlspells.mixin;

import com.divinity.hlspells.enchantments.ISpell;
import com.divinity.hlspells.init.ItemInit;
import com.divinity.hlspells.items.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.util.SpellUtils;
import com.google.common.collect.Lists;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.EnchantmentContainer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;


@Mixin(EnchantmentContainer.class)
public class MixinEnchantmentTable {
    @Shadow
    @Final
    private IInventory enchantSlots;

    /**
     * Converts spell enchants to SpellBookObject NBT
     */
    @Inject(method = "clickMenuButton(Lnet/minecraft/entity/player/PlayerEntity;I)Z", at = @At(value = "TAIL"), cancellable = true)
    public void clickMenuButton(PlayerEntity player, int value, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = enchantSlots.getItem(0);
        if (stack.getItem() == ItemInit.SPELL_BOOK.get() && !player.level.isClientSide()) {
            Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(stack);
            for (Map.Entry<Enchantment, Integer> entry : enchantmentMap.entrySet()) {
                Enchantment enchantment = entry.getKey();
                if (enchantment instanceof ISpell) {
                    Spell spell = SpellUtils.getSpellByID(((ISpell) enchantment).getSpellRegistryName());
                    if (spell != null) {
                        stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(cap -> cap.addSpell(spell.getRegistryName().toString()));
                        stack.getEnchantmentTags().remove(!stack.getEnchantmentTags().isEmpty() ? stack.getEnchantmentTags().size() - 1 : 0);
                    }
                }
            }
        }
    }

    /**
     * Sets the possible enchantments to be only one for spell book
     */
    @Inject(method = "getEnchantmentList(Lnet/minecraft/item/ItemStack;II)Ljava/util/List;", at = @At(value = "RETURN"), cancellable = true)
    public void removeMultipleEnchants(ItemStack stack, int pEnchantSlot, int pLevel, CallbackInfoReturnable<List<EnchantmentData>> cir) {
        List<EnchantmentData> oldList = cir.getReturnValue();
        if (stack.getItem() == ItemInit.SPELL_BOOK.get() && !oldList.isEmpty()) {
            cir.setReturnValue(Lists.newArrayList(oldList.get(0)));
        }
    }
}
