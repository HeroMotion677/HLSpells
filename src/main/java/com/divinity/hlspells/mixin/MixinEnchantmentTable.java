package com.divinity.hlspells.mixin;

import com.divinity.hlspells.enchantments.ISpell;
import com.divinity.hlspells.init.ItemInit;
import com.divinity.hlspells.items.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.enchantment.Enchantment;
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

import java.util.Map;


@Mixin(EnchantmentContainer.class)
public class MixinEnchantmentTable {
    @Shadow
    @Final
    private IInventory enchantSlots;

    /**
     * Converts spell enchants to SpellBookObject NBT
     */
    @Inject(method = "clickMenuButton(Lnet/minecraft/entity/player/PlayerEntity;I)Z", at = @At("TAIL"), cancellable = true)
    public void clickMenuButton(PlayerEntity player, int value, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = enchantSlots.getItem(0);
        if (stack.getItem() == ItemInit.SPELL_BOOK.get() && !player.level.isClientSide()) {
            Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(stack);
            for (Map.Entry<Enchantment, Integer> entry : enchantmentMap.entrySet()) {
                Enchantment enchantment = entry.getKey();
                if (enchantment instanceof ISpell) {
                    Spell spell = SpellUtils.getSpellByID(((ISpell) enchantment).getSpellRegistryName());
                    stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(cap -> {
                        cap.addSpell(spell.getRegistryName().toString());
                        // By default empty spell is added to the empty spell book in creative inventory
                    });
                    stack.getEnchantmentTags().remove(!stack.getEnchantmentTags().isEmpty() ? stack.getEnchantmentTags().size() - 1 : 0);
                }
            }
        }
    }
}
