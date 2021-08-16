package com.divinity.hlspells.mixin;

import com.divinity.hlspells.enchantments.ISpell;
import com.divinity.hlspells.init.EnchantmentInit;
import com.divinity.hlspells.init.SpellBookInit;
import com.divinity.hlspells.init.SpellInit;
import com.divinity.hlspells.items.SpellBookItem;
import com.divinity.hlspells.items.WandItem;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellBookObject;
import com.divinity.hlspells.util.SpellUtils;
import com.google.common.collect.Lists;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.EnchantmentContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.fixes.ItemSpawnEggSplit;
import net.minecraftforge.fml.RegistryObject;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(EnchantmentContainer.class)
public class MixinEnchantmentTable
{
    @Shadow
    @Final
    private IInventory enchantSlots;

    @Inject(method = "clickMenuButton(Lnet/minecraft/entity/player/PlayerEntity;I)Z", at= @At(value = "TAIL"), cancellable = true)
    public void clickMenuButton(PlayerEntity player, int value, CallbackInfoReturnable<Boolean> cir)
    {
        ItemStack stack = enchantSlots.getItem(0);
        if (stack.getItem() instanceof SpellBookItem)
        {
            if (!player.level.isClientSide())
            {
                outerLoop:
                for (RegistryObject<Enchantment> ench : EnchantmentInit.ENCHANTMENTS.getEntries())
                {
                    if (EnchantmentHelper.getItemEnchantmentLevel(ench.get(), stack) > 0 && ench.get() instanceof ISpell)
                    {
                       ISpell spell = (ISpell) ench.get();
                       for (RegistryObject<Spell> spellObj : SpellInit.SPELLS_DEFERRED_REGISTER.getEntries())
                       {
                           ResourceLocation location = spellObj.get().getRegistryName();
                           if (location != null  && location.toString().equals(spell.getSpellRegistryName()))
                           {
                               String spellName = spellObj.get().getTrueDisplayName();
                               for (RegistryObject<SpellBookObject> spellBook : SpellBookInit.SPELL_BOOK_DEFERRED_REGISTER.getEntries())
                               {
                                   if (spellBook.get().getName().equals(spellName))
                                   {
                                       SpellUtils.setSpellBook(stack, spellBook.get());
                                       stack.getEnchantmentTags().remove(stack.getEnchantmentTags().size() > 0 ? stack.getEnchantmentTags().size() - 1 : 0);
                                       break outerLoop;
                                   }
                               }
                           }
                       }
                    }
                }
            }
        }
    }

    @Inject(method = "getEnchantmentList(Lnet/minecraft/item/ItemStack;II)Ljava/util/List;", at= @At(value = "RETURN"), cancellable = true)
    public void removeMultipleEnchants(ItemStack stack, int p_178148_2_, int p_178148_3_, CallbackInfoReturnable<List<EnchantmentData>> cir)
    {
        List<EnchantmentData> oldList = cir.getReturnValue();
        if (stack.getItem() instanceof SpellBookItem && oldList.size() > 1)
        {
            cir.setReturnValue(Lists.newArrayList(oldList.get(0)));
        }
    }
}
