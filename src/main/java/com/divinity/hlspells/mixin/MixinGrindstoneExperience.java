package com.divinity.hlspells.mixin;

import com.divinity.hlspells.items.spellitems.SpellHoldingItem;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.StairBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/world/inventory/GrindstoneMenu$4")
public class MixinGrindstoneExperience {

    @Inject(method = "getExperienceFromItem(Lnet/minecraft/world/item/ItemStack;)I", at = @At(value = "RETURN"), cancellable = true)
    public void getExperienceFromItem(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (stack.getItem() instanceof SpellHoldingItem item && !item.isSpellBook()) {
            int spellSize = stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).map(m -> m.getSpells().size()).orElse(0);
            cir.setReturnValue(5 * spellSize);
        }
    }
}
