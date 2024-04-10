package com.divinity.hlspells.mixin;

import com.divinity.hlspells.items.spellitems.SpellHoldingItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FriendlyByteBuf.class)
public abstract class FriendlyByteBufMixin {
	@Redirect(method = "writeItemStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getTag()Lnet/minecraft/nbt/CompoundTag;"))
	private CompoundTag writeItem(ItemStack instance) {
		return instance.getItem() instanceof SpellHoldingItem ? instance.getShareTag() : instance.getTag();
	}
}